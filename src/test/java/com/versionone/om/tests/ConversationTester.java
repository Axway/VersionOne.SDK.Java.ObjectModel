package com.versionone.om.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.AssetID;
import com.versionone.om.Conversation;
import com.versionone.om.Member;
import com.versionone.om.Story;


public class ConversationTester extends BaseSDKTester {

	private EntityFactory entityFactory = getEntityFactory();
	private List<Conversation> disposableConversations = new ArrayList<Conversation>();

    @After
    public void tearDown() {
         for(Conversation item : disposableConversations) {
             item.setAuthor(getInstance().getLoggedInMember());
             item.save();

             if(item.canDelete()){
                 item.delete();
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
		Assert.assertEquals(author, newConversation.getAuthor());
		Assert.assertEquals(conversationContent, newConversation.getContent());
	}

     @Test
     public void createConversationWithCurrentLoggedUser() {
         String conversationContent = "testing conversation creation with logged member";
         Conversation conversation = getInstance().create().conversation(conversationContent);
         conversation.save();
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Assert.assertEquals(getInstance().getLoggedInMember(), newConversation.getAuthor());
         Assert.assertEquals(conversationContent, newConversation.getContent());
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
         conversation.getMentions().add(getInstance().getLoggedInMember());
         conversation.save();

         Conversation persistedConversation = getInstance().get().conversationByID(conversation.getID());
         ListAssert.contains(getInstance().getLoggedInMember(), persistedConversation.getMentions());
         ListAssert.contains(persistedConversation, getInstance().getLoggedInMember().getMentionedInExpressions());
     }

     @Test
     public void baseAssetsReference() {
         Story story = getEntityFactory().createStory("Conversations related", getSandboxProject());
         Conversation conversation = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation");
         conversation.getMentions().add(story);
         conversation.save();

         Conversation persistedConversation = getInstance().get().conversationByID(conversation.getID());
         Story persistedStory = getInstance().get().storyByID(story.getID());
         ListAssert.contains(persistedStory, persistedConversation.getMentions());
         ListAssert.contains(persistedConversation, persistedStory.getMentionedInExpressions());
     }

     @Test
     public void authorReference() {
         Conversation conversation = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation");

         Conversation persistedConversation = getInstance().get().conversationByID(conversation.getID());
         Assert.assertEquals(getInstance().getLoggedInMember(), persistedConversation.getAuthor());
         ListAssert.contains(persistedConversation, getInstance().getLoggedInMember().getExpressions());
     }

     @Test
     public void parentConversationReference() {
         Conversation parent = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation - parent");
         Conversation child = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation - child");
         child.setParentConversation(parent);
         child.save();

         Conversation persistedParentConversation = getInstance().get().conversationByID(parent.getID());
         Conversation persistedChildConversation = getInstance().get().conversationByID(child.getID());
         Assert.assertEquals(persistedChildConversation.getParentConversation(), persistedParentConversation);
         ListAssert.contains(persistedChildConversation, persistedParentConversation.getExpressionsInConversation());
     }

     @Test
     public void repliesReference() {
         Conversation parent = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation - parent");
         parent.save();
         Conversation child = createDisposableConversation(getInstance().getLoggedInMember(), "A disposable conversation - child");
         child.setInReplyTo(parent);
         child.save();

         Conversation persistedParentConversation = getInstance().get().conversationByID(parent.getID());
         Conversation persistedChildConversation = getInstance().get().conversationByID(child.getID());
         Assert.assertEquals(persistedChildConversation.getInReplyTo(), persistedParentConversation);
         ListAssert.contains(persistedChildConversation, persistedParentConversation.getReplies());
     }

     @Test
     public void createConversationThroughStory() {
         String conversationText = "Created through story";
         Story story = getEntityFactory().createStory("Conversation Story", getInstance().get().projectByID(AssetID.fromToken("Scope:0")));
         Conversation conversation = story.createConversation(getInstance().getLoggedInMember(), conversationText);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Assert.assertEquals(conversationText, newConversation.getContent());
         Assert.assertEquals(1, newConversation.getMentions().size());
         ListAssert.contains(story, newConversation.getMentions());
         Story newStory = getInstance().get().storyByID(story.getID());
         Assert.assertEquals(1, newStory.getMentionedInExpressions().size());
         ListAssert.contains(newConversation, newStory.getMentionedInExpressions());
     }

     @Test
     public void createConversationThroughMember() {
         String conversationText = "Created through member";
         Member member = getInstance().getLoggedInMember();
         Conversation conversation = member.createConversation(getInstance().getLoggedInMember(), conversationText);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Assert.assertEquals(conversationText, newConversation.getContent());
         Assert.assertEquals(1, newConversation.getMentions().size());
         ListAssert.contains(member, newConversation.getMentions());
         Member newMember = getInstance().getLoggedInMember();
         ListAssert.contains(newConversation, newMember.getMentionedInExpressions());
     }

     @Test
     public void createConversationThroughMemberWithCurrentAuthor() {
         String conversationText = "Created through member with this author";
         Member member = getInstance().getLoggedInMember();
         Conversation conversation = member.createConversation(conversationText);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Assert.assertEquals(conversationText, newConversation.getContent());
         Assert.assertEquals(0, newConversation.getMentions().size());
         Assert.assertEquals(member, newConversation.getAuthor());
     }

     @Test
     public void createConversationThroughMemberWithAttributes() {
         String conversationText = "Created through member with attributes";
         String newConversationContent = "test from the attribute";
         Member member = getInstance().getLoggedInMember();
         // as expression doesn't have specific attribute,
         // we are going just re-write content attribute.
         Map<String, Object> attributes = new HashMap<String, Object>();
         attributes.put("Content", newConversationContent);
         Conversation conversation = member.createConversation(member, conversationText, attributes);
         disposableConversations.add(conversation);
         resetInstance();

         Conversation newConversation = getInstance().get().conversationByID(conversation.getID());
         Assert.assertEquals(newConversationContent, newConversation.getContent());
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
         Assert.assertEquals(conversationText + "2", newConversation.getContent());
         Assert.assertEquals(conversationParent, newConversation.getInReplyTo());
         Assert.assertEquals(1, newConversation.getMentions().size());
         ListAssert.contains(story, newConversation.getMentions());
         Story newStory = getInstance().get().storyByID(story.getID());
         Assert.assertEquals(2, newStory.getMentionedInExpressions().size());
         ListAssert.contains(newConversation, newStory.getMentionedInExpressions());
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
         conversation.save();
         disposableConversations.add(conversation);

         return conversation;
     }
}
