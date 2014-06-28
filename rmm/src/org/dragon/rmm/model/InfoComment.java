package org.dragon.rmm.model;

public class InfoComment {

	public long storeid;
	public long orderid;
	public float level;
	public String content;

	public InfoComment(long storeid, long orderid, float level, String content) {
		super();
		this.storeid = storeid;
		this.orderid = orderid;
		this.level = level;
		this.content = content;
	}

}
