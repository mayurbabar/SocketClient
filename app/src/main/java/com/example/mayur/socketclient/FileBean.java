package com.example.mayur.socketclient;

public class FileBean {

	String name;
	String estension;

	public void setContent(String content) {
		this.content = content;
	}

	public void setEstension(String estension) {
		this.estension = estension;
	}

	public void setName(String name) {
		this.name = name;
	}

	String content;

	public String getName() {
		return name;
	}

	public String getEstension() {
		return estension;
	}

	public String getContent() {
		return content;
	}
}
