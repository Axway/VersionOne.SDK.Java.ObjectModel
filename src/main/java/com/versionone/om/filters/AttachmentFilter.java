package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Attachment;
import com.versionone.om.BaseAsset;
import com.versionone.om.Entity;
import com.versionone.om.listvalue.AttachmentType;

/**
 * A filter for Attachments.
 */
public class AttachmentFilter extends EntityFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Attachment.class;
    }

    /**
     * Item name. Must be complete match.
     */
    public final List<String> name = newList();

    /**
     * Item description. Must be complete match.
     */
    public final List<String> description = newList();

    /**
     * The Attachment's Type. Must be complete match.
     */
    public final List<String> type = newList();

    /**
     * The Attachment's Related Asset.
     */
    public final List<BaseAsset> asset = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Name", name);
        builder.simple("Description", description);

        builder.relation("Asset", asset);

        builder.listRelation("Category", type, AttachmentType.class);
    }
}
