package com.example.chinesejoke.entity;

import java.io.Serializable;

public class Joke implements Serializable{
	private String billNo;
	private String jokeTitle;
	private String jokeContent;
	private int type;

	public Joke() {
		super();

	}

	public Joke(String billNo, String jokeTitle, String jokeContent, int type) {
		super();
		this.billNo = billNo;
		this.jokeTitle = jokeTitle;
		this.jokeContent = jokeContent;
		this.type = type;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getJokeTitle() {
		return jokeTitle;
	}

	public void setJokeTitle(String jokeTitle) {
		this.jokeTitle = jokeTitle;
	}

	public String getJokeContent() {
		return jokeContent;
	}

	public void setJokeContent(String jokeContent) {
		this.jokeContent = jokeContent;

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
