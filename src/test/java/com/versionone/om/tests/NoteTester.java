/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.versionone.om.Goal;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.versionone.om.Note;
import com.versionone.om.Project;

@Ignore("Last version of VersionOne server doesn't support Note")
public class NoteTester extends BaseSDKTester {

	private static final String NOTE_1788 = "Note:1788";
	private static final String NOTE_Z_CONTENT = "This is Note Z.";
	private static final String NOTE_Y_CONTENT = "This is Note Y.";
	private static final String NOTE_Z = "Note Z";
	private static final String NOTE_Y = "Note Y";
	private static final String COMMENT = "Comment";
	private static final String PRIVATE_NOTE_CONTENT = "This is a private note.<br>";
	private static final String PRIVATE_NOTE = "Private Note";
	private static final String STATUS = "Status";
	private static final String PUBLIC_NOTE_CONTENT = "This is a public note.<br>";
	private static final String PUBLIC_NOTE = "Public Note";
	private static final String NOTE_1785 = "Note:1785";

	@Test
	public void testPublicNoteAttributes() {
		Note note = getInstance().get().noteByID(NOTE_1785);
		Project project = getInstance().get().projectByID(SCOPE_ZERO);
		Assert.assertEquals(project, note.getAsset());
		Assert.assertEquals(PUBLIC_NOTE, note.getName());
		Assert.assertEquals(PUBLIC_NOTE_CONTENT, note.getContent());
		Assert.assertFalse(note.isPersonal());
		Assert.assertNotNull(note.getType());
		Assert.assertEquals(STATUS, note.getType().getCurrentValue());
		Assert.assertEquals(getInstance().getLoggedInMember(), note
				.getCreatedBy());

	}

    @Test
    public void testCreateNoteWithAttributes() {
        final String name = "NoteName";
        final String description = "Test for Note creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Personal", false);

        Note note = getSandboxProject().createNote(name, PUBLIC_NOTE, true, attributes);

        resetInstance();

        note = getInstance().get().noteByID(note.getID());

        Assert.assertEquals(name, note.getName());
        Assert.assertEquals(PUBLIC_NOTE, note.getContent());
        Assert.assertEquals(false, note.isPersonal());

        note.delete();
    }

	@Test
	public void testPrivateNoteAttributes() {
		Note note = getInstance().get().noteByID("Note:1786");
		Project project = getInstance().get().projectByID(SCOPE_ZERO);
		Assert.assertEquals(project, note.getAsset());
		Assert.assertEquals(PRIVATE_NOTE, note.getName());
		Assert.assertEquals(PRIVATE_NOTE_CONTENT, note.getContent());
		Assert.assertTrue(note.isPersonal());
		Assert.assertNotNull(note.getType());
		Assert.assertEquals(COMMENT, note.getType().getCurrentValue());
		Assert.assertEquals(getInstance().getLoggedInMember(), note
				.getCreatedBy());
	}

	@Test
	public void testCreate() {
		Project project = getSandboxProject();
		Note noteY = project.createNote(NOTE_Y, NOTE_Y_CONTENT, false);
		Note noteZ = project.createNote(NOTE_Z, NOTE_Z_CONTENT, true);

		String noteYid = noteY.getID().toString();
		String noteZid = noteZ.getID().toString();

		resetInstance();

		Note newNoteY = getInstance().get().noteByID(noteYid);
		Assert.assertEquals(NOTE_Y, newNoteY.getName());
		Assert.assertEquals(NOTE_Y_CONTENT, newNoteY.getContent());
		Assert.assertFalse(newNoteY.isPersonal());

		Note newNoteZ = getInstance().get().noteByID(noteZid);
		Assert.assertEquals(NOTE_Z, newNoteZ.getName());
		Assert.assertEquals(NOTE_Z_CONTENT, newNoteZ.getContent());
		Assert.assertTrue(newNoteZ.isPersonal());
	}

	@Test
	public void testDelete() {
		Note note = getSandboxProject().createNote("New Note",
				"This is a new Note", false);
		String noteId = note.getID().getToken();

		resetInstance();

		note = getInstance().get().noteByID(noteId);
		Assert.assertTrue(note.canDelete());
		note.delete();

		resetInstance();

		Assert.assertNull(getInstance().get().noteByID(noteId));
	}

	@Test
	public void testCreateResponse() {
		Note note = getSandboxProject().createNote("New Note",
				"This is a new Note", false);
		String noteId = note.getID().getToken();

		Note response = note.createResponse("A Response", "Back to you", true);
		String responseID = response.getID().getToken();

		resetInstance();

		note = getInstance().get().noteByID(noteId);
		response = getInstance().get().noteByID(responseID);

		Assert.assertTrue(note.getResponses().contains(response));

		Assert.assertEquals("A Response", response.getName());
		Assert.assertEquals("Back to you", response.getContent());
		Assert.assertTrue(response.isPersonal());
		Assert.assertEquals(note, response.getInResponseTo());

		Collection<Note> notes = getSandboxProject().getNotes(null);
		Assert.assertTrue(notes.contains(note));
		Assert.assertTrue(notes.contains(response));
	}
}
