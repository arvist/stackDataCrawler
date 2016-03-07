package com.stackdata.crawler.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.stackdata.crawler.model.Question;

public class JsonProcessor {
	List<Question> questionList = new ArrayList<Question>();

	public List<Question> processQuestion(JSONObject json) {
		JSONArray objs = (JSONArray) json.get("items");
		for (int i = 0; i < objs.size(); i++) {
			Question question = new Question();

			JSONObject obj = (JSONObject) objs.get(i);
			JSONArray tags = (JSONArray) obj.get("tags");

			List<String> tagList = new ArrayList<>();
			for (int j = 0; j < tags.size(); j++) {
				tagList.add((String) tags.get(j));
			}

			question.setAnswered((boolean) obj.get("is_answered"));
			question.setViewCount((long) obj.get("view_count"));
			try {
				question.setAcceptedAnswerId((long) obj.get("accepted_answer_id"));
			} catch (NullPointerException e) {
				question.setAcceptedAnswerId(-1);
			}
			question.setAnswerCount((long) obj.get("answer_count"));
			question.setScore((long) obj.get("score"));
			question.setCreatedDate(new Date((long) obj.get("creation_date") * 1000));
			question.setQuestionId((long) obj.get("question_id"));
			question.setSelfLink((String) obj.get("link"));
			question.setTitle((String) obj.get("title"));
			question.setTags(tagList);
			questionList.add(question);
		}
		return questionList;
	}
}
