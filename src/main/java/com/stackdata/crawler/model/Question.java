package com.stackdata.crawler.model;

import java.util.Date;
import java.util.List;

public class Question {

	private long questionId;
	private List<String> tags;
	private boolean isAnswered;
	private long viewCount;
	private long answerCount;
	private long acceptedAnswerId;
	private long score;
	private Date createdDate;
	private String selfLink; // done
	private String title;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public boolean isAnswered() {
		return isAnswered;
	}

	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}

	public long getViewCount() {
		return viewCount;
	}

	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}

	public long getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(long answerCount) {
		this.answerCount = answerCount;
	}

	public long getAcceptedAnswerId() {
		return acceptedAnswerId;
	}

	public void setAcceptedAnswerId(long acceptedAnswerId) {
		this.acceptedAnswerId = acceptedAnswerId;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", tags=" + tags + ", isAnswered=" + isAnswered + ", viewCount="
				+ viewCount + ", answerCount=" + answerCount + ", acceptedAnswerId=" + acceptedAnswerId + ", score="
				+ score + ", createdDate=" + createdDate + ", selfLink=" + selfLink + ", title=" + title + "]";
	}

}
