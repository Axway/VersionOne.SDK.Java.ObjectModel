package com.versionone.om.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.apiclient.FilterTerm;
import com.versionone.om.Conversation;
import com.versionone.om.Member;
import com.versionone.om.Story;
import com.versionone.om.filters.ConversationFilter;

public class ConversationFilterTester extends BaseSDKTester {
    private Stack<Conversation> conversationsForDelete = new Stack<Conversation>();

    @Test
    public void getConversationByFilter() {
        final String content = "testing conversation";

        Member member = getInstance().getLoggedInMember();
        Conversation conversation = createConversation(member, content);
        ConversationFilter filter = new ConversationFilter();
        filter.author.add(member);
        filter.authoredAt.addTerm(FilterTerm.Operator.Equal, conversation.getAuthoredAt());

        List<Conversation> conversations = new ArrayList<Conversation>(getInstance().get().conversations(filter));
        Assert.assertEquals(1, conversations.size());
        Conversation newConv = conversations.get(0);
        Assert.assertEquals(conversation.getID(), newConv.getID());
        Assert.assertEquals(conversation.getContent(), newConv.getContent());
        Assert.assertEquals(member, newConv.getAuthor());
    }

    @Test
    public void getConversationByParent() {
        Conversation parent = createConversation(getInstance().getLoggedInMember(), "parent");
        Conversation child = createConversation(getInstance().getLoggedInMember(), "child");
        child.setParentConversation(parent);
        child.save();
        Conversation unrelated = createConversation(getInstance().getLoggedInMember(), "something else");
        unrelated.setParentConversation(child);
        unrelated.save();

        ConversationFilter filter = new ConversationFilter();
        filter.conversation.add(parent);
        List<Conversation> conversations = new ArrayList<Conversation>(getInstance().get().conversations(filter));

        Assert.assertEquals(2, conversations.size());
        ListAssert.contains(child, conversations);
        ListAssert.contains(parent, conversations);
        ListAssert.notcontains(unrelated, conversations);
    }

    @Test
    public void getConversationByReplyReferences() {
        Conversation base = createConversation(getInstance().getLoggedInMember(), "base");
        Conversation reply = createConversation(getInstance().getLoggedInMember(), "a reply");
        reply.setInReplyTo(base);
        reply.save();
        Conversation unrelated = createConversation(getInstance().getLoggedInMember(), "something else");

        ConversationFilter filter = new ConversationFilter();
        filter.inReplyTo.add(base);
        Collection<Conversation> conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(1, conversations.size());
        ListAssert.contains(reply, conversations);
        ListAssert.notcontains(unrelated, conversations);

        filter = new ConversationFilter();
        filter.replies.add(reply);
        conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(1, conversations.size());
        ListAssert.contains(base, conversations);
        ListAssert.notcontains(unrelated, conversations);
    }

    @Test
    public void getConversationByMentions() {
        Member firstMember = getEntityFactory().createMember("test1");
        Conversation firstConversation = createConversation(getInstance().getLoggedInMember(), "testing - #1");
        firstConversation.getMentions().add(firstMember);
        firstConversation.save();

        Member secondMember = getEntityFactory().createMember("test2");
        Conversation secondConversation = createConversation(getInstance().getLoggedInMember(), "testing - #2");
        secondConversation.getMentions().add(secondMember);
        secondConversation.save();

        ConversationFilter filter = new ConversationFilter();
        filter.mentions.add(firstMember);
        Collection<Conversation> conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(1, conversations.size());
        ListAssert.contains(firstConversation, conversations);
        ListAssert.notcontains(secondConversation, conversations);

        filter = new ConversationFilter();
        filter.mentions.add(secondMember);
        conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(1, conversations.size());
        ListAssert.contains(secondConversation, conversations);
        ListAssert.notcontains(firstConversation, conversations);

        filter = new ConversationFilter();
        filter.mentions.add(firstMember);
        filter.mentions.add(secondMember);
        conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(2, conversations.size());
        ListAssert.contains(secondConversation, conversations);
        ListAssert.contains(firstConversation, conversations);
    }

    @Test
    public void GetConversationByBaseAssets() {
        Story story = getEntityFactory().createStory("fly to the Moon using a magnet and will power", getSandboxProject());
        Conversation firstConversation = createConversation(getInstance().getLoggedInMember(), "testing - #1");
        firstConversation.getMentions().add(story);
        firstConversation.save();

        com.versionone.om.Test test = getEntityFactory().createTest("check the direction", story);
        Conversation secondConversation = createConversation(getInstance().getLoggedInMember(), "testing - #2");
        secondConversation.getMentions().add(test);
        secondConversation.save();

        ConversationFilter filter = new ConversationFilter();
        filter.mentions.add(story);
        Collection<Conversation> conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(1, conversations.size());
        ListAssert.contains(firstConversation, conversations);
        ListAssert.notcontains(secondConversation, conversations);

        filter = new ConversationFilter();
        filter.mentions.add(test);
        conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(1, conversations.size());
        ListAssert.contains(secondConversation, conversations);
        ListAssert.notcontains(firstConversation, conversations);

        filter = new ConversationFilter();
        filter.mentions.add(story);
        filter.mentions.add(test);
        conversations = getInstance().get().conversations(filter);

        Assert.assertEquals(2, conversations.size());
        ListAssert.contains(secondConversation, conversations);
        ListAssert.contains(firstConversation, conversations);
    }

    private Conversation createConversation(Member member, String content) {
        Conversation conversation = getInstance().create().conversation(member, content);
        conversationsForDelete.push(conversation);
        return conversation;
    }

    @After
    public void tearDownTest() {
        while (conversationsForDelete.size() > 0) {
            Conversation item = conversationsForDelete.pop();

            if (item.canDelete()) {
                item.delete();
            }
        }
    }
}
