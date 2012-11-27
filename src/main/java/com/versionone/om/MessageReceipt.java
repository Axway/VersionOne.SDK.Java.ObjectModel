package com.versionone.om;

@MetaDataAttribute("MessageReceipt")
public class MessageReceipt extends Entity {
	
	MessageReceipt(AssetID id, V1Instance instance)
	{
		super(id, instance); 
	}
	
	MessageReceipt(V1Instance instance) 
	{
		super(instance);
	}

	/// <summary>
	/// Member this message is addressed to.
	/// </summary>
	public Member getRecipient()
	{ 
		return getRelation(Member.class, "Recipient"); 
	}
	
	/// <summary>
	/// Member this message is addressed to.
	/// </summary>
	public void setRecipient(Member value)
	{ 
		setRelation("Recipient", value); 
	}

	/// <summary>
	/// Member this message is addressed to.
	/// </summary>
	public Message getMessage()
	{ 
		return getRelation(Message.class, "Message"); 
	}
	
	/// <summary>
	/// Member this message is addressed to.
	/// </summary>
	public void setMessage(Message value)
	{ 
		setRelation("Message", value); 
	}
	
	/// <summary>
	/// True if the item has not been read
	/// </summary>
	public boolean isUnread()
	{ 
		return (Boolean) get("IsUnread");  
	}

	/// <summary>
	/// True if the item has been read
	/// </summary>
	public boolean isRead()
	{
		return (Boolean) get("IsRead"); 
	}

	/// <summary>
	/// True if the item has not been archived
	/// </summary>
	public boolean isUnarchived()
	{ 
		return (Boolean) get("IsUnarchived");
	}

	/// <summary>
	/// True if the item has been archived
	/// </summary>
	public boolean isArchived() 
	{ 
		return (Boolean) get("IsArchived"); 
	}

	/// <summary>
	/// Mark the message as having been read by the recipient.
	/// </summary>
	public void markAsRead()
	{
		getInstance().executeOperation(MessageReceipt.class, this, "MarkAsRead");
	}

	/// <summary>
	/// Mark the message as having not been read by the recipient.
	/// </summary>
	public void markAsUnread()
	{
		getInstance().executeOperation(MessageReceipt.class, this, "MarkAsUnread");
	}

	/// <summary>
	/// Mark the message as having been archived.
	/// </summary>
	public void markAsArchived()
	{
		getInstance().executeOperation(MessageReceipt.class, this, "MarkAsArchived");
	}

	/// <summary>
	/// Mark the message as not having been archived.
	/// </summary>
	public void markAsUnarchived()
	{
		getInstance().executeOperation(MessageReceipt.class, this, "MarkAsUnarchived");
	}

	/// <summary>
	/// Mark the message as having been archived.
	/// </summary>
	public void delete()
	{
		getInstance().executeOperation(MessageReceipt.class, this, "Delete");
	}	
}
