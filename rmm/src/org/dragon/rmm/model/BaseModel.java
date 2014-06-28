package org.dragon.rmm.model;

/**
 * 
 * @author vane
 * 
 */
public class BaseModel<T> {

	public BaseModel() {
		super();
	}

	public BaseModel(T body, InfoHeader head) {
		super();
		this.body = body;
		this.head = head;
	}

	public T body;
	public InfoHeader head;

}
