package com.pasali.ulak;

public class Message {

	private String id;
	private String no;
	private String body;
	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	public Message(String no, String body) {
		this.no = no;
		this.body = body;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
