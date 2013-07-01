package com.versionone.om.tests;

import com.versionone.om.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;


public class ExpressionTester extends BaseSDKTester {

	private EntityFactory entityFactory = getEntityFactory();
	private List<Conversation> disposableConversations = new ArrayList<Conversation>();

    @After
    public void tearDown() {
         for(Conversation item : disposableConversations) {
             for (Expression expression : item.getContainedExpressions()) {
                 expression.setAuthor(getInstance().getLoggedInMember());
                 expression.save();

                 if (expression.canDelete()) {
                     expression.delete();
                 }
             }
         }
    }

	@Test
	public void createConversation() {
		String conversationContent = "testing conversation creation";
		Member author = entityFactory.createMember("test");
		Conversation conversation = createDisposableConversation(author, conversationContent);
		conversation.save();
		resetInstance();

		Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
        Iterator<Expression> expressionIterator = newConversation.getContainedExpressions().iterator();
        Expression expression = expressionIterator.next();
        Assert.assertEquals(author, expression.getAuthor());
		Assert.assertEquals(conversationContent, expression.getContent());
	}

     @Test
     public void createConversationWithCurrentLoggedUser() {
         String conversationContent = "testing conversation creation with logged member";
         Conversation conversation = getInstance().create().conversation(conversationContent);
         conversation.save();
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Iterator<Expression> expressionIterator = newConversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();
         Assert.assertEquals(getInstance().getLoggedInMember(), expression.getAuthor());
         Assert.assertEquals(conversationContent, expression.getContent());
     }

     @Test
     public void deleteConversation() {
         Member currentUser = getInstance().getLoggedInMember();
         Conversation conversation = getInstance().create().conversation(currentUser, "A disposable conversation");
         conversation.save();

         AssetID id = conversation.getID();

         Conversation persistedConversation = getInstance().get().conversationByID(id);
         Assert.assertNotNull(persistedConversation);

         conversation.delete();

         persistedConversation = getInstance().get().conversationByID(id);
         Assert.assertNull(persistedConversation);
     }

     @Test(expected=UnsupportedOperationException.class)
     public void deleteConversationAuthoredByAnotherUser(){
         Member author = getEntityFactory().createMember("ConversationAuthor");
         Conversation conversation = createDisposableConversation(author, "A disposable conversation");

         AssetID id = conversation.getID();

         Conversation persistedConversation = getInstance().get().conversationByID(id);
         Assert.assertNotNull(persistedConversation);

         conversation.delete();
     }

     @Test
     public void mentionsReference() {
         Conversation conversation = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation");
         Iterator<Expression> expressionIterator = conversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();
         expression.getMentions().add(getInstance().getLoggedInMember());
         expression.save();

         Conversation persistedConversation = getInstance().get().conversationByID(conversation.getID());
         expressionIterator = persistedConversation.getContainedExpressions().iterator();
         expression = expressionIterator.next();
         ListAssert.contains(getInstance().getLoggedInMember(), expression.getMentions());
         ListAssert.contains(expression, getInstance().getLoggedInMember().getMentionedInExpressions());
     }

     @Test
     public void baseAssetsReference() {
         Story story = getEntityFactory().createStory("Conversations related", getSandboxProject());
         Conversation conversation = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation");
         Iterator<Expression> expressionIterator = conversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();
         expression.getMentions().add(story);
         expression.save();

         Conversation persistedConversation = getInstance().get().conversationByID(conversation.getID());
         expressionIterator = persistedConversation.getContainedExpressions().iterator();
         expression = expressionIterator.next();
         Story persistedStory = getInstance().get().storyByID(story.getID());
         ListAssert.contains(persistedStory, expression.getMentions());
         ListAssert.contains(expression, persistedStory.getMentionedInExpressions());
     }

     @Test
     public void authorReference() {
         Conversation conversation = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation");

         Conversation persistedConversation = getInstance().get().conversationByID(conversation.getID());
         Iterator<Expression> expressionIterator = persistedConversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();
         Assert.assertEquals(getInstance().getLoggedInMember(), expression.getAuthor());
         ListAssert.contains(expression, getInstance().getLoggedInMember().getExpressions());
     }

     @Test
     public void repliesReference() {
         Conversation conversation = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation - parent");
         Expression parent = conversation.getContainedExpressions().iterator().next();
         Expression child = createDisposableExpression(getInstance().getLoggedInMember(), "A disposable conversation - child", parent);

         Expression persistedParentExpression = getInstance().get().expressionByID(parent.getID());
         Expression persistedChildExpression = getInstance().get().expressionByID(child.getID());
         Assert.assertEquals(persistedChildExpression.getInReplyTo(), persistedParentExpression);
         ListAssert.contains(persistedChildExpression, persistedParentExpression.getReplies());
     }

     @Test
     public void createConversationThroughStory() {
         String expressionText = "Created through story";
         Story story = getEntityFactory().createStory("Conversation Story", getInstance().get().projectByID(AssetID.fromToken("Scope:0")));
         Conversation conversation = story.createConversation(getInstance().getLoggedInMember(), expressionText);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Iterator<Expression> expressionIterator = newConversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();

         Assert.assertEquals(expressionText, expression.getContent());
         Assert.assertEquals(1, expression.getMentions().size());
         ListAssert.contains(story, expression.getMentions());
         Story newStory = getInstance().get().storyByID(story.getID());
         Assert.assertEquals(1, newStory.getMentionedInExpressions().size());
         ListAssert.contains(expression, newStory.getMentionedInExpressions());
     }

     @Test
     public void createConversationThroughMember() {
         String expressionText = "Created through member";
         Member member = getInstance().getLoggedInMember();
         Conversation conversation = member.createConversation(getInstance().getLoggedInMember(), expressionText);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Iterator<Expression> expressionIterator = newConversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();

         Assert.assertEquals(expressionText, expression.getContent());
         Assert.assertEquals(1, expression.getMentions().size());
         ListAssert.contains(member, expression.getMentions());
         Member newMember = getInstance().getLoggedInMember();
         ListAssert.contains(expression, newMember.getMentionedInExpressions());
     }

     @Test
     public void createConversationThroughMemberWithCurrentAuthor() {
         String expressionText = "Created through member with this author";
         Member member = getInstance().getLoggedInMember();
         Conversation conversation = member.createConversation(expressionText);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Iterator<Expression> expressionIterator = newConversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();

         Assert.assertEquals(expressionText, expression.getContent());
         Assert.assertEquals(0, expression.getMentions().size());
         Assert.assertEquals(member, expression.getAuthor());
     }

     @Test
     public void createConversationThroughMemberWithAttributes() {
         String conversationText = "Created through member with attributes";
         String newContent = "test from the attribute";
         Member member = getInstance().getLoggedInMember();
         // as expression doesn't have specific attribute,
         // we are going just re-write content attribute.
         Map<String, Object> attributes = new HashMap<String, Object>();
         attributes.put("Content", newContent);
         Conversation conversation = member.createConversation(member, conversationText, attributes);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Iterator<Expression> expressionIterator = newConversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();
         Assert.assertEquals(newContent, expression.getContent());
     }

     @Test
     public void createConversationThroughStoryWithAttributes() {
         String conversationText = "Created through story";
         Story story = getEntityFactory().createStory("Conversation Story", getInstance().get().projectByID(AssetID.fromToken("Scope:0")));
         Conversation conversationParent = story.createConversation(getInstance().getLoggedInMember(), conversationText + "1");
         Map<String, Object> attributes = new HashMap<String, Object>();
         attributes.put("InReplyTo", conversationParent.getID().getToken());
         Conversation conversation = story.createConversation(getInstance().getLoggedInMember(), conversationText + "2", attributes);
         disposableConversations.add(conversationParent);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Iterator<Expression> expressionIterator = newConversation.getContainedExpressions().iterator();
         Expression expression = expressionIterator.next();
         Assert.assertEquals(conversationText + "2", expression.getContent());
         Assert.assertEquals(conversationParent, expression.getInReplyTo());
         Assert.assertEquals(1, expression.getMentions().size());
         ListAssert.contains(story, expression.getMentions());
         Story newStory = getInstance().get().storyByID(story.getID());
         Assert.assertEquals(2, newStory.getMentionedInExpressions().size());
         ListAssert.contains(expression, newStory.getMentionedInExpressions());
     }

     @Test
     public void getConversationById() {
         Conversation conversation = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation");
         conversation.save();

         Conversation persistedConversation = getInstance().get().conversationByID(conversation.getID());
         Assert.assertEquals(conversation, persistedConversation);
     }

     private Conversation createDisposableConversation(Member author, String content) {
         Conversation conversation = getInstance().create().conversation(author, content);
         disposableConversations.add(conversation);

         return conversation;
     }

    private Expression createDisposableExpression(Member author, String content, Expression inReplyTo) {
        return getInstance().create().expression(author, content, inReplyTo);
    }
}
