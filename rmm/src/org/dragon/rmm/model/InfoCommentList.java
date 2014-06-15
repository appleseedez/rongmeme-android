package org.dragon.rmm.model;

public class InfoCommentList {

	public long storeid;
	public int start = 0;
	public int limit = 100;

	public InfoCommentList(long storeid) {
		this.storeid = storeid;
	}

	public InfoCommentList(long storeid, int start, int limit) {
		super();
		this.storeid = storeid;
		this.start = start;
		this.limit = limit;
	}

}
