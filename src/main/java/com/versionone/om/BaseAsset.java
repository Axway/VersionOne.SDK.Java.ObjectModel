/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.filters.AttachmentFilter;
import com.versionone.om.filters.LinkFilter;
import com.versionone.om.filters.NoteFilter;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * Represents the base type of members, projects, teams, etc in the VersionOne
 * system.
 */
@MetaDataAttribute(value = "BaseAsset", defaultAttributeSelectionNames = "Name")
public abstract class BaseAsset extends Entity {
    protected static final int STATE_CLOSED = 128;
    protected static final int STATE_ACTIVE = 64;

    private ICustomAttributeDictionary customField;
    private ICustomDropdownDictionary customDropdown;

    /**
     * Constructor used to represent an BaseAsset entity that does NOT yet exist
     * in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    BaseAsset(V1Instance instance) {
        super(instance);
    }

    /**
     * Constructor used to represent an BaseAsset entity that DOES exist in the
     * VersionOne System.
     *
     * @param id       Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    BaseAsset(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * @return URL to VersionOne Detail Page for this BaseAsset.
     */
    public String getURL() {
        return getEntityURL();
    }

    /**
     * @return Name of the base asset.
     */
    public String getName() {
        return (String) get(NAME_VALUE);
    }

    /**
     * @param name Name of the base asset.
     */
    public void setName(String name) {
        set(NAME_VALUE, name);
    }

    /**
     * @return description of the base asset.
     */
    public String getDescription() {
        return (String) get(DESCRIPTION_VALUE);
    }

    /**
     * @param value description of the base asset.
     */
    public void setDescription(String value) {
        set(DESCRIPTION_VALUE, value);
    }

    /**
     * @return Expressions that referenced to current item.
     */
    public Collection<Conversation> getMentionedInExpressions() {
    	return getMultiRelation("MentionedInExpressions");
    }

    /**
     * @param filter filter for getting a collection of attachments (If null,
     *               then all items returned).
     * @return A collection attachments that belong to this base asset filtered
     *         by the passed in filter.
     */
    public Collection<Attachment> getAttachments(AttachmentFilter filter) {
        filter = (filter != null) ? filter : new AttachmentFilter();

        filter.asset.clear();
        filter.asset.add(this);
        return getInstance().get().attachments(filter);
    }

    /**
     * @param filter filter for getting a collection of notes (If null, then all
     *               items returned).
     * @return A collection notes that belong to this base asset filtered by the
     *         passed in filter.
     */
    public Collection<Note> getNotes(NoteFilter filter) {
        filter = (filter != null) ? filter : new NoteFilter();

        filter.asset.clear();
        filter.asset.add(this);
        return getInstance().get().notes(filter);
    }

    /**
     * @param filter Limit the link returned. If null, then all items returned.
     * @return A collection links that belong to this base asset filtered by the
     *         passed in filter.
     */
    public Collection<Link> getLinks(LinkFilter filter) {
        filter = (filter == null) ? new LinkFilter() : filter;

        filter.asset.clear();
        filter.asset.add(this);
        return getInstance().get().links(filter);
    }

    /**
     * @return Indicates if the entity is open or active.
     */
    public boolean isActive() {
        return isActiveImpl();
    }

    /**
     * @return Indicates if the entity is closed or inactive.
     */
    public boolean isClosed() {
        return isClosedImpl();
    }

    /**
     * @return Internal worker function to determine active state.
     */
    boolean isActiveImpl() {
        return get(ASSET_STATE_VALUE, true).equals(STATE_ACTIVE);
    }

    /**
     * @return Internal worker function to determine closed state.
     */
    boolean isClosedImpl() {
        return get(ASSET_STATE_VALUE, true).equals(STATE_CLOSED);
    }

    /**
     * @return True if the item can be deleted
     */
    public boolean canDelete() {
        return canDeleteImpl();
    }

    /**
     * @return Internal worker function to determine ability to delete item.
     */
    boolean canDeleteImpl() {
        return getInstance().canExecuteOperation(this, "Delete");
    }

    /**
     * Deletes the Item
     *
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                                       the Operation.
     */
    public void delete() throws UnsupportedOperationException {
        save();
        getInstance().executeOperation(this, DELETE_OPERATION);
    }

    /**
     * @return Simple Custom Fields.
     */
    public ICustomAttributeDictionary getCustomField() {

        if (customField == null) {
            customField = new SimpleCustomAttributeDictionary();
        }
        return customField;
    }

    /**
     * @return Custom List-Type Fields.
     */
    public ICustomDropdownDictionary getCustomDropdown() {

        if (customDropdown == null) {
            customDropdown = new ListCustomAttributeDictionary();
        }
        return customDropdown;
    }

    /**
     * Create a link that belongs to this asset.
     *
     * @param name   The name of the link.
     * @param url    The url of the link.
     * @param onMenu True if the link is visible on this asset's detail page
     *               menu.
     * @return created Link.
     */
    public Link createLink(String name, String url, boolean onMenu) {
        return getInstance().create().link(name, this, url, onMenu);
    }

    /**
     * Create a link that belongs to this asset.
     *
     * @param name       The name of the link.
     * @param url        The url of the link.
     * @param onMenu     True if the link is visible on this asset's detail page
     *                   menu.
     * @param attributes additional attributes for the Link.
     * @return created Link.
     */
    public Link createLink(String name, String url, boolean onMenu, Map<String, Object> attributes) {
        return getInstance().create().link(name, this, url, onMenu, attributes);
    }

    /**
     * Create a note that belongs to this asset.
     *
     * @param name     The name of the note.
     * @param content  The content of the note.
     * @param personal True if the note is only visible to the currently logged
     *                 in user.
     * @return created Note.
     */
    public Note createNote(String name, String content, boolean personal) {
        return getInstance().create().note(name, this, content, personal);
    }

    /**
     * Create a note that belongs to this asset.
     *
     * @param name       The name of the note.
     * @param content    The content of the note.
     * @param personal   True if the note is only visible to the currently logged
     *                   in user.
     * @param attributes additional attributes for the Note.
     * @return created Note.
     */
    public Note createNote(String name, String content, boolean personal, Map<String, Object> attributes) {
        return getInstance().create().note(name, this, content, personal, attributes);
    }

    /**
     * Create an attachment that belongs to this asset.
     *
     * @param name     The name of the attachment.
     * @param fileName The name of the original attachment file.
     * @param stream   The read-enabled stream that contains the attachment.
     *                 content to upload.
     * @return {@code Attachment} object with corresponding parameters.
     * @throws AttachmentLengthExceededException
     *          if attachment is too long.
     * @throws ApplicationUnavailableException
     *          if any problem appears during
     *          connection to the server.
     */
    public Attachment createAttachment(String name, String fileName, InputStream stream)
            throws AttachmentLengthExceededException, ApplicationUnavailableException {
        return getInstance().create().attachment(name, this, fileName, stream);
    }

    /**
     * Create an attachment that belongs to this asset.
     *
     * @param name       The name of the attachment.
     * @param fileName   The name of the original attachment file.
     * @param stream     The read-enabled stream that contains the attachment.
     *                   content to upload.
     * @param attributes additional attributes for the Attachment.
     * @return {@code Attachment} object with corresponding parameters.
     * @throws AttachmentLengthExceededException
     *          if attachment is too long.
     * @throws ApplicationUnavailableException
     *          if any problem appears during
     *          connection to the server.
     */
    public Attachment createAttachment(String name, String fileName, InputStream stream, Map<String, Object> attributes)
            throws AttachmentLengthExceededException, ApplicationUnavailableException {
        return getInstance().create().attachment(name, this, fileName, stream, attributes);
    }

    /**
     * Creates conversation which mentioned this asset.
     *
     * @param author Author of conversation.
     * @param content Content of conversation.
     * @return Created conversation
     */
    public Conversation createConversation(Member author, String content) {
        Conversation conversation = getInstance().create().conversation(author, content);
        conversation.getMentions().add(this);
        conversation.save();
        return conversation;
    }

    /**
     * Creates conversation which mentioned this asset.
     * @param author Author of conversation.
     * @param content Content of conversation.
     * @param attributes additional attributes for the Conversation.
     * @return Created conversation
     */
    public Conversation createConversation(Member author, String content, Map<String, Object> attributes) {
        Conversation conversation = getInstance().create().conversation(author, content, attributes);
        conversation.getMentions().add(this);
        conversation.save();
        return conversation;
    }

    /**
     * @return True if the item can be closed.
     */
    public boolean canClose() {
        return canCloseImpl();
    }

    boolean canCloseImpl() {
        return getInstance().canExecuteOperation(this, "Inactivate");
    }

    /**
     * Closes or Inactivates the item
     *
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                                       the Operation, e.g. it is already closed or inactive.
     */
    public void close() throws UnsupportedOperationException {
        save();
        closeImpl();
        clearCache(ASSET_STATE_VALUE);
    }

    abstract void closeImpl() throws UnsupportedOperationException;

    /**
     * @return True if item can be Reactivated.
     */
    public boolean canReactivate() {
        return canReactivateImpl();
    }

    boolean canReactivateImpl() {
        return getInstance().canExecuteOperation(this, "Reactivate");
    }

    /**
     * Reactivates the item.
     *
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                                       the Operation, e.g. it is already open or active.
     */
    public void reactivate() throws UnsupportedOperationException {
        reactivateImpl();
        save();
        clearCache(ASSET_STATE_VALUE);
    }

    abstract void reactivateImpl() throws UnsupportedOperationException;
}
