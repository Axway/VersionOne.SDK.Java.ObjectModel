package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.BaseAsset;
import com.versionone.om.Entity;
import com.versionone.om.Member;
import com.versionone.om.Message;

public class MessageFilter extends BaseAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Message.class;
    }

	/// <summary>
	/// Filter on RelatedAsset.
	/// </summary>
	public List<BaseAsset> relatedAsset = newList();


	private Boolean unread = null;
	/// <summary>
	/// Filter on read or unread messages.
	/// </summary>	
	public void isUnread(boolean value) {
		unread = value;
	}

	/// <summary>
	/// Filter on read or unread messages.
	/// </summary>	
	public boolean isUnread() {
		return unread;
	}

	private Boolean archived = null;

	/// <summary>
	/// Filter on archived or unarchived messages.
	/// </summary>
	public void isArchived(boolean value) {
		archived = value;
	}

	/// <summary>
	/// Filter on archived or unarchived messages.
	/// </summary>
	public boolean isArchived() {
		return archived;
	}

	/// <summary>
	/// Only messages sent to a particular recipient.
	/// </summary>
	public final List<Member> recipient = newList();

	/// <summary>
	/// Only messages sent by a particular recipient.
	/// </summary>
	public List<Member> sender = newList();

	@Override 
	void internalModifyFilter(FilterBuilder builder) {
		super.internalModifyFilter(builder);

		builder.relation("Asset", relatedAsset);
		builder.relation("CreatedBy", sender);

		for(Member member : recipient) {
			builder.root.and(new IFilterTerm[]{new TokenTerm(String.format("Message.Receipts[Recipient='%s';IsDeleted='false']", member.getID().getToken()))});
		}

		// Because we are masking the Message-Receipt relation from the user, 
		// and we will only ever get messages where I am the recipient or creator, 
		// We filter Read and Unread by the receipts, assuming only one.
		if (null != unread) {
			builder.simple("Receipts.IsUnread", unread);
        }

		if (null != archived) {
			builder.simple("Receipts.IsArchived", archived);
        }
	}
	
	
	
}
