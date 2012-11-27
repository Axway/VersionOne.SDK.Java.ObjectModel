/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.listvalue.NoteType;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a Note in the VersionOne system.
 * @deprecated since VersionOne 10.3 this type is not supported.
 */
@MetaDataAttribute("Note")
public class Note extends Entity {

    Note(V1Instance instance) {
        super(instance);
    }

    Note(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	Note(Note responseTo, V1Instance instance)
	{
		super(instance);
		setRelation("InResponseTo", responseTo);
	}

    /**
     * @return The type this Note belongs to.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(NoteType.class, "Category");
    }

    /**
     * @return Asset this Note is related to.
     */
    public BaseAsset getAsset() {
        return getRelation(BaseAsset.class, "Asset");
    }

    /**
     * @param value Asset this Note is related to.
     */
    public void setAsset(BaseAsset value) {
        setRelation("Asset", value);
    }

    /**
     * @return True if this Note is visible only to the currently logged in
     *         user.
     */
    public boolean isPersonal() {
        return (Boolean) get("Personal");
    }

    /**
     * @param value True if this note is visible only to the currently logged in
     *                user.
     */
    public void setPersonal(boolean value) {
        set("Personal", value);
    }

    /**
     * @return Content of this Note.
     */
    public String getContent() {
        return (String) get(CONTENT_VALUE);
    }

    /**
     * @param value Content of this Note.
     */
    public void setContent(String value) {
        set(CONTENT_VALUE, value);
    }

    /**
     * @return Name of this Note.
     */
    public String getName() {
        return (String) get(NAME_VALUE);
    }

    /**
     * @param value Name of this Note.
     */
    public void setName(String value) {
        set(NAME_VALUE, value);
    }

    /**
     * @return True if the Note can be deleted.
     */
    public boolean canDelete() {
        return getInstance().canExecuteOperation(this, DELETE_OPERATION);
    }

    /**
     * Deletes the Note.
     *
     * @throws UnsupportedOperationException The item is an invalid state for
     *                 the Operation.
     */
    public void delete() throws UnsupportedOperationException {
        save();
        getInstance().executeOperation(this, DELETE_OPERATION);
    }

    /**
     * Create a response to this note
     *
     * @param name     The name of the note
     * @param content  The content of the note
     * @param personal True if the note is only visible to the currently logged in user
     * @return a new note
     */
	public Note createResponse(String name, String content, boolean personal) {
		return getInstance().create().note(this, name, content, personal);
	}

    /**
     * Create a response to this note.
     *
     * @param name     The name of the note
     * @param content  The content of the note
     * @param personal True if the note is only visible to the currently logged in user
     * @param attributes additional attributes for response to this note.
     * @return a new note
     */
	public Note createResponse(String name, String content, boolean personal, Map<String, Object> attributes) {
		return getInstance().create().note(this, name, content, personal, attributes);
	}

	/**
	 * Note this note is a response to
	 */
	public Note getInResponseTo() { return getRelation(Note.class, "InResponseTo"); }

	/**
	 * Responses to this Note
	 */
	public Collection<Note> getResponses() { return getMultiRelation("Responses"); }

}
