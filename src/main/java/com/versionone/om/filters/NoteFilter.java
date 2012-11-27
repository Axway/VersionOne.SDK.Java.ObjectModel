package com.versionone.om.filters;

import java.util.Arrays;
import java.util.List;

import com.versionone.om.BaseAsset;
import com.versionone.om.Entity;
import com.versionone.om.Member;
import com.versionone.om.Note;
import com.versionone.om.listvalue.NoteType;

/**
 * A filter for Notes.
 * @deprecated since VersionOne 10.3 this type is not supported.
 */
public class NoteFilter extends EntityFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Note.class;
    }

    /**
     * Item name. Must be complete match.
     */
    public final List<String> name = newList();

    /**
     * Item Content. Must be complete match.
     */
    public final List<String> content = newList();

    /**
     * The Note's Type.  Must be complete match.
     */
    public final List<String> type = newList();

    /**
     * The Note's Related Asset.
     */
    public final List<BaseAsset> asset = newList();

    /**
     * Who the note is related to.
     */
	public final List<Note>  inResponseTo = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);


		builder.relation("PersonalTo", Arrays.asList(new Member[]{ null, builder.instance.getLoggedInMember() }));

        builder.simple("Name", name);
        builder.simple("Content", content);

        builder.relation("Asset", asset);

		builder.relation("InResponseTo", inResponseTo);

        builder.listRelation("Category", type, NoteType.class);
    }
}
