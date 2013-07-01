package com.versionone.om.tests;

import com.versionone.apiclient.FilterTerm;
import com.versionone.om.Conversation;
import com.versionone.om.Expression;
import com.versionone.om.Member;
import com.versionone.om.Story;
import com.versionone.om.filters.ExpressionFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ExpressionFilterTester extends BaseSDKTester {
    private Stack<Conversation> conversationsForDelete = new Stack<Conversation>();

    @Test
    public void getConversationByFilter() {
        final String content = "testing conversation";

        Member member = getInstance().getLoggedInMember();
        Conversation conversation = createConversation(member, content);
        ExpressionFilter filter = new ExpressionFilter();
        Expression expression = conversation.getContainedExpressions().iterator().next();
        filter.author.add(member);
        filter.authoredAt.addTerm(FilterTerm.Operator.Equal, expression.getAuthoredAt());

        List<Expression> expressions = new ArrayList<Expression>(getInstance().get().expressions(filter));
        Assert.assertEquals(1, expressions.size());
        Expression newExpression = expressions.get(0);
        Assert.assertEquals(expression.getID(), newExpression.getID());
        Assert.assertEquals(expression.getContent(), newExpression.getContent());
        Assert.assertEquals(member, newExpression.getAuthor());
    }

    @Test
    public void getConversationByReplyReferences() {
        Conversation conversation = createConversation(getInstance().getLoggedInMember(), "base");
        Expression base = conversation.getContainedExpressions().iterator().next();
        Expression reply = createExpression(getInstance().getLoggedInMember(), "a reply", base);
        Conversation anotherConversation = createConversation(getInstance().getLoggedInMember(), "unrelated");
        Expression unrelated = anotherConversation.getContainedExpressions().iterator().next();

        ExpressionFilter filter = new ExpressionFilter();
        filter.inReplyTo.add(base);
        Collection<Expression> expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(1, expressions.size());
        ListAssert.contains(reply, expressions);
        ListAssert.notcontains(unrelated, expressions);

        filter = new ExpressionFilter();
        filter.replies.add(reply);
        expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(1, expressions.size());
        ListAssert.contains(base, expressions);
        ListAssert.notcontains(unrelated, expressions);
    }

    @Test
    public void getConversationByMentions() {
        Member firstMember = getEntityFactory().createMember("test1");
        Conversation firstConversation = createConversation(getInstance().getLoggedInMember(), "testing - #1");
        Expression firstExpression = firstConversation.getContainedExpressions().iterator().next();
        firstExpression.getMentions().add(firstMember);
        firstExpression.save();

        Member secondMember = getEntityFactory().createMember("test2");
        Conversation secondConversation = createConversation(getInstance().getLoggedInMember(), "testing - #2");
        Expression secondExpression = firstConversation.getContainedExpressions().iterator().next();
        secondExpression.getMentions().add(secondMember);
        secondExpression.save();

        ExpressionFilter filter = new ExpressionFilter();
        filter.mentions.add(firstMember);
        Collection<Expression> expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(1, expressions.size());
        ListAssert.contains(firstExpression, expressions);
        ListAssert.notcontains(secondExpression, expressions);

        filter = new ExpressionFilter();
        filter.mentions.add(secondMember);
        expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(1, expressions.size());
        ListAssert.contains(secondExpression, expressions);
        ListAssert.notcontains(firstExpression, expressions);

        filter = new ExpressionFilter();
        filter.mentions.add(firstMember);
        filter.mentions.add(secondMember);
        expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(2, expressions.size());
        ListAssert.contains(secondExpression, expressions);
        ListAssert.contains(firstExpression, expressions);
    }

    @Test
    public void GetConversationByBaseAssets() {
        Story story = getEntityFactory().createStory("fly to the Moon using a magnet and will power", getSandboxProject());
        Conversation firstConversation = createConversation(getInstance().getLoggedInMember(), "testing - #1");
        Expression firstExpression = firstConversation.getContainedExpressions().iterator().next();
        firstExpression.getMentions().add(story);
        firstExpression.save();

        com.versionone.om.Test test = getEntityFactory().createTest("check the direction", story);
        Conversation secondConversation = createConversation(getInstance().getLoggedInMember(), "testing - #2");
        Expression secondExpression = firstConversation.getContainedExpressions().iterator().next();
        secondExpression .getMentions().add(test);
        secondExpression .save();

        ExpressionFilter filter = new ExpressionFilter();
        filter.mentions.add(story);
        Collection<Expression> expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(1, expressions.size());
        ListAssert.contains(firstExpression, expressions);
        ListAssert.notcontains(secondExpression, expressions);

        filter = new ExpressionFilter();
        filter.mentions.add(test);
        expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(1, expressions.size());
        ListAssert.contains(secondExpression, expressions);
        ListAssert.notcontains(firstExpression, expressions);

        filter = new ExpressionFilter();
        filter.mentions.add(story);
        filter.mentions.add(test);
        expressions = getInstance().get().expressions(filter);

        Assert.assertEquals(2, expressions.size());
        ListAssert.contains(secondExpression, expressions);
        ListAssert.contains(firstExpression, expressions);
    }

    private Conversation createConversation(Member member, String content) {
        Conversation conversation = getInstance().create().conversation(member, content);
        conversationsForDelete.push(conversation);
        return conversation;
    }

    private Expression createExpression(Member member, String content, Expression inReplyTo) {
        return getInstance().create().expression(member, content, inReplyTo);
    }

    @After
    public void tearDownTest() {
        while (conversationsForDelete.size() > 0) {
            Conversation item = conversationsForDelete.pop();
            for (Expression expression : item.getContainedExpressions()) {
                if (expression.canDelete()) {
                    expression.delete();
                }
            }
        }
    }
}
