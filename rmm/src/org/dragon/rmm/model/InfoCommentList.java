package org.dragon.rmm.model;

import org.dragon.rmm.api.ApiServer;

public class InfoCommentList {

	public long storeid;
	public int start = 0;
	public int limit = ApiServer.PSIZE;

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
