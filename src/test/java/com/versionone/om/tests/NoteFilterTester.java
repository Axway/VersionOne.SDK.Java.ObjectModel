package com.versionone.om.tests;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.Note;
import com.versionone.om.Project;
import com.versionone.om.TransformIterable.ITransformer;
import com.versionone.om.filters.NoteFilter;

/**
 * Note tester
 */
@Ignore("Last version of VesionOne server doesn't support Note")
public class NoteFilterTester extends BaseSDKTester {

	@Override
	protected Project createSandboxProject(Project rootProject) {
		Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Scheme", getDefaultSchemeOid());
		return getInstance().create().project(getSandboxName(),
				rootProject, DateTime.now(), getSandboxSchedule(), attributes);
	}

	private static final String NOTE_CONTENT = "NoteFilterTester This is Note Y.";
	private static final String NOTE_NAME = "NoteFilterTester Note Y";

	@Test
	public void testNameAndContentFilter() {
		ITransformer<Note, String> nameTransformer = new ITransformer<Note, String>() {
			public String transform(Note input) {
				return input.getName();
			}
		};
		String nodeName = NOTE_NAME + new Date().getTime();
		String nodeContent = NOTE_CONTENT + new Date().getTime();
		Note note = getInstance().create().note(nodeName, getSandboxProject(), nodeContent, false);
		note.save();

		resetInstance();

		NoteFilter filter = new NoteFilter();
		filter.name.add(nodeName);

		Collection<Note> actualNotes = getInstance().get().notes(filter);

		Assert.assertEquals(1, actualNotes.size());
		ListAssert.areEqual(new String[] { nodeName }, actualNotes, nameTransformer);
		note.delete();
	}

	@Test
	public void testType() {
		Note expected = getInstance().create().note("Has Type",
				getSandboxProject(), "one", false);
		Note not = getInstance().create().note("No Type",
				getSandboxProject(), "two", false);

		String expectedType = expected.getType().getAllValues()[0];
		expected.getType().setCurrentValue(expectedType);
		expected.save();

		validateType(expected, not, expectedType);
		validateType(not, expected, null);
	}

	void validateType(Note expected, Note not, String expectedType) {
		NoteFilter filter = new NoteFilter();
		filter.asset.add(getSandboxProject());
		filter.type.add(expectedType);

		resetInstance();
		expected = getInstance().get().noteByID(
				expected.getID().getToken());
		not = getInstance().get().noteByID(not.getID().getToken());

		Collection<Note> results = getSandboxProject().getNotes(filter);

		Assert.assertTrue("Expected to find Note that matched filter.",
				findRelated(expected, results));
		Assert.assertFalse(
				"Expected to NOT find Note that doesn't match filter.",
				findRelated(not, results));
		for (Note result : results)
			Assert.assertEquals(expectedType, result.getType()
					.getCurrentValue());
	}

	@Test
	public void testInResponseTo() {
		Note note = getInstance().create().note("No InResponseTo",
				getSandboxProject(), "Content one.", false);
		Note response = note.createResponse("Has InResponseTo", "Content two.",
				false);

		validateInResponseTo(note, response, null);
		validateInResponseTo(response, note, note);
	}

	void validateInResponseTo(Note expected, Note not, Note expectedNote) {
		NoteFilter filter = new NoteFilter();
		filter.asset.add(getSandboxProject());
		filter.inResponseTo.add(expectedNote);

		resetInstance();
		expected = getInstance().get().noteByID(
				expected.getID().getToken());
		not = getInstance().get().noteByID(not.getID().getToken());

		Collection<Note> results = getSandboxProject().getNotes(filter);

		Assert.assertTrue("Expected to find Note that matched filter.",
				findRelated(expected, results));
		Assert.assertFalse(
				"Expected to NOT find Note that doesn't match filter.",
				findRelated(not, results));
		for (Note result : results)
			Assert.assertEquals(expectedNote, result.getInResponseTo());
	}

/******************************************************************************
 * This test does not work in Java because the JVM insist on reusing the
 * connection created when V1Instance is constructed with getMyInstance() when
 * getInstance() is called.  It does not reuse the instance, but at the
 * protocol level it reuses the credentials.  This has caused problems in other
 * places too so if you know the answer please let Jerry know.
 ******************************************************************************
	private final String myUsername = "andre";

	private V1Instance _localInstance;

	private V1Instance getMyInstance() {
		if (_localInstance == null) {
			_localInstance = new V1Instance(this.getApplicationPath(), myUsername,
					myUsername);
			_localInstance.validate();
		}
		return _localInstance;
	}

	@Test
	public void testPersonal() {
		Member member = getMyInstance().getter().getMemberByUserName(myUsername);
		String personalId = getMyInstance().creator().createNote("Note to self", member, "hello", true).getID().getToken();
		String publicId = getMyInstance().creator().createNote("Hear me out", member, "blah",  false).getID().getToken();

		validatePersonal(publicId, personalId, getMyInstance(), true);
		validatePersonal(publicId, personalId, getInstance(), false);
	}

	private void validatePersonal(String publicId, String personalId, V1Instance instance, boolean hasPersonal) {
		Note pub = instance.getter().getNoteByID(publicId);
		Note personal = instance.getter().getNoteByID(personalId);
		Collection<Note> notes = instance.getter().getMemberByUserName(myUsername).getNotes(null);

		Assert.assertTrue(notes.contains(pub));
		Assert.assertFalse(hasPersonal ^ notes.contains(personal));
	}
*/

}
