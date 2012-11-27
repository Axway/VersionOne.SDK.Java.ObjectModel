/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.listvalue.AttachmentType;
import com.versionone.util.V1Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents an attachment in the VersionOne system.
 */
@MetaDataAttribute("Attachment")
public class Attachment extends Entity {

    Attachment(V1Instance instance) {
        super(instance);
        set(CONTENT_VALUE, "");
    }

    Attachment(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * @return URL to VersionOne Detail Page for this Attachment.
     */
    public String getURL() {
        return getEntityURL();
    }

    /**
     * @return VersionOne URL where the contents of this attachment may be
     *         downloaded from.
     */
    public String getContentURL() {
        return getInstance().getAttachmentURL(this);
    }

    /**
     * @return Asset this attachment is related to.
     */
    public BaseAsset getAsset() {
        return getRelation(BaseAsset.class, ASSET_VALUE);
    }

    /**
     * @param asset this attachment is related to.
     */
    public void setAsset(BaseAsset asset) {
        setRelation(ASSET_VALUE, asset);
    }

    /**
     * @return Description of this attachment.
     */
    public String getDescription() {
        return (String) get(DESCRIPTION_VALUE);
    }

    /**
     * @param description of this attachment.
     */
    public void setDescription(String description) {
        set(DESCRIPTION_VALUE, description);
    }

    /**
     * @return Content Type of this attachment.
     */
    public String getContentType() {
        return (String) get(CONTENT_TYPE_VALUE);
    }

    /**
     * @param contentType of this attachment.
     */
    public void setContentType(String contentType) {
        set(CONTENT_TYPE_VALUE, contentType);
    }

    /**
     * @return Filename of this attachment.
     */
    public String getFilename() {
        return (String) get(FILENAME_VALUE);
    }

    /**
     * @param filename Filename of this attachment.
     */
    public void setFilename(String filename) {
        set(FILENAME_VALUE, filename);
    }

    /**
     * @return Name of this attachment.
     */
    public String getName() {
        return (String) get(NAME_VALUE);
    }

    /**
     * @param name Name of this attachment.
     */
    public void setName(String name) {
        set(NAME_VALUE, name);
    }

    /**
     * @return Type of this attachment.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(AttachmentType.class, "Category");
    }

    /**
     * Write this attachment's content to the output stream.
     *
     * @param output Stream to write the content to.
     * @throws ApplicationUnavailableException if coping fails.
     */
    public void writeTo(OutputStream output)
            throws ApplicationUnavailableException {
        InputStream input = getInstance().getReader(this);

        try {
            V1Util.copyStream(input, output);
        } catch (IOException e) {
            throw new ApplicationUnavailableException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    /**
     * Read the attachment's content from the input stream. Set the ContentType
     * and Filename properties before calling this method.
     *
     * @param input Stream to read the content from.
     * @throws ApplicationUnavailableException if appears any problem with
     *                 connection to service.
     * @throws AttachmentLengthExceededException if attachment is too long.
     */
    public void readFrom(InputStream input)
            throws ApplicationUnavailableException,
            AttachmentLengthExceededException {
        OutputStream output = null;

        try {
            output = getInstance().getWriter(this, getContentType());
            V1Util.copyStream(input, output);
            getInstance().commitWriteStream(this);
        } catch (IOException e) {
            throw new ApplicationUnavailableException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    /**
     * @return True if the attachment can be deleted.
     */
    public boolean canDelete() {
        return getInstance().canExecuteOperation(this, DELETE_OPERATION);
    }

    /**
     * Deletes the attachment.
     *
     * @throws UnsupportedOperationException The item is an invalid state for
     *                 the Operation.
     */
    public void delete() {
        save();
        getInstance().executeOperation(this, DELETE_OPERATION);
    }
}
