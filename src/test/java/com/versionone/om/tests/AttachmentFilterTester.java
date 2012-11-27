package com.versionone.om.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.Attachment;
import com.versionone.om.Project;
import com.versionone.om.filters.AttachmentFilter;

/**
 * Attachment tester
 */
// TODO test for raising test coverage
public class AttachmentFilterTester extends BaseSDKTester {

    @Test
    public void testNameDescriptionAndAsset() throws IOException {
        Project project = getSandboxProject();
        Attachment attachment;
        final String content = "This is the first attachment's content. At: "
                + DateTime.now();
        final String attachmentName = "Name of Attachment" + DateTime.now();
        final String description = "Description " + DateTime.now();
        final String fileName = "test.txt";

        InputStream input = new ByteArrayInputStream(content.getBytes());

        try {
            attachment = project.createAttachment(attachmentName, fileName,
                    input);
            attachment.setDescription(description);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                // do nothing
            }
        }
        resetInstance();

        AttachmentFilter filter = new AttachmentFilter();
        filter.name.add(attachmentName);
        filter.asset.add(project);

        Collection<Attachment> newAttachments = getInstance().get()
                .attachments(filter);

        Assert.assertEquals(1, newAttachments.size());
        Attachment newAttachment = newAttachments.iterator().next();
        Project newProject = getSandboxProject();
        Assert.assertEquals(newProject, newAttachment.getAsset());
        Assert.assertEquals("text/plain", attachment.getContentType());
        Assert.assertEquals(fileName, attachment.getFilename());
        Assert.assertEquals(attachmentName, attachment.getName());

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            newAttachment.writeTo(output);
        } finally {

            try {
                output.close();
            } catch (IOException e) {
                // do nothing
            }
        }
        Assert.assertEquals(content, output.toString());// use ASCII

        attachment.delete();
        project.delete();
    }
}
