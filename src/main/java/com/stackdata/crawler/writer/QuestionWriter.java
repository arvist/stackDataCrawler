package com.stackdata.crawler.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.shared.Lock;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;

import com.stackdata.crawler.model.Question;
import com.stackdata.crawler.repository.NamespaceRepository;
import com.stackdata.crawler.repository.ParamRepository;

public class QuestionWriter {

	private Dataset dataset = TDBFactory.createDataset("src/main/resources/tbd");
	private static final String QUESTION_MODEL = "Question";
	private static final boolean RDF_WRITE_SUCCESSFUL = true;
	private Model model;

	public void outputModelInFile() {
		model = getModelByName(QUESTION_MODEL);
		try {
			File file = new File("src/main/resources/out.rdf");
			model.write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false))), "" + "TTL");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean writeQuestion(List<Question> questions) {
		model = getModelByName(QUESTION_MODEL);
		try {
			model.enterCriticalSection(Lock.WRITE);
			for (Question question : questions) {
				Resource subject = model.createResource(NamespaceRepository.STACKDATA_NAMESPACE
						.concat(ParamRepository.QUESTIONS_PARAM).concat(String.valueOf(question.getQuestionId())));
				createId(subject, String.valueOf(question.getQuestionId()));
				createSameAs(subject, String.valueOf(question.getQuestionId()));
				createTitle(subject, question.getTitle());
				createTags(subject, question.getTags());
				createIsAnswered(subject, question.isAnswered());
				if (question.isAnswered()) {
					createAnswer(subject, String.valueOf(question.getAcceptedAnswerId()),
							String.valueOf(question.getQuestionId()));
				}
				createViewCount(subject, String.valueOf(question.getViewCount()));
				createAnswerCount(subject, String.valueOf(question.getAnswerCount()));
				createScore(subject, String.valueOf(question.getScore()));
				createCreatedDate(subject, question.getCreatedDate());
				model.commit();
				TDB.sync(model);
			}
		} finally {
			model.leaveCriticalSection();
		}
		return RDF_WRITE_SUCCESSFUL;
	}

	private void createTitle(Resource subject, String title) {
		Property predicate = model
				.createProperty(NamespaceRepository.DUBLIN_CORE_NAMESPACE.concat(ParamRepository.TITLE_PARAM));
		Resource object = model.createResource(
				NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.TITLE_PARAM).concat(title));
		connect(subject, predicate, object);
	}

	private void createId(Resource subject, String id) {
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.QUESTION_ID_PARAM));
		Resource object = model.createResource(
				NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.QUESTION_ID_PARAM).concat(id));
		connect(subject, predicate, object);
	}

	private void createSameAs(Resource subject, String id) {
		Property predicate = model
				.createProperty(NamespaceRepository.OWL_NAMESPACE.concat(ParamRepository.SAME_AS_PARAM));
		Resource object = model.createResource(NamespaceRepository.STACKEXCHANGE_API
				.concat(ParamRepository.QUESTIONS_PARAM).concat(id.concat(ParamRepository.SITE_SO_QUERY_PARAM)));
		connect(subject, predicate, object);
	}

	private void createTags(Resource subject, List<String> tags) {
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.TAGS_PARAM));
		for (String tag : tags) {
			Resource object = model.createResource(
					NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.TAGS_PARAM).concat(tag));
			connect(subject, predicate, object);
		}
	}

	private void createIsAnswered(Resource subject, boolean value) {
		Property predicate = model.createProperty("isAnswered");
		Literal object = model.createTypedLiteral(value);
		connect(subject, predicate, object);
	}

	private void createCreatedDate(Resource subject, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("k:m:s");
		TypeMapper typeMapper = new TypeMapper();
		RDFDatatype xsdDateTimeDataType = typeMapper
				.getSafeTypeByName(NamespaceRepository.XSD_NAMESPACE.concat(ParamRepository.DATETIME_TYPE));
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.CREATED_DATE));
		Literal object = model.createTypedLiteral(
				(String) dateFormat.format(date).toString().concat(timeFormat.format(date)), xsdDateTimeDataType);
		connect(subject, predicate, object);
	}

	private void createAnswer(Resource subject, String answerId, String questionId) {
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.ANSWERS_PARAM));
		Resource object = model
				.createResource(NamespaceRepository.STACK_OVERFLOW_SITE.concat(ParamRepository.QUESTIONS_PARAM)
						.concat(questionId).concat("/").concat(ParamRepository.ANSWERS_PARAM_SO).concat(answerId));
		connect(subject, predicate, object);
	}

	private void createViewCount(Resource subject, String viewCount) {
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.VIEW_COUNT));
		Resource object = model.createResource(
				NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.VIEW_COUNT).concat(viewCount));
		connect(subject, predicate, object);
	}

	private void createAnswerCount(Resource subject, String answerCount) {
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.ANSWER_COUNT));
		Resource object = model.createResource(
				NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.ANSWER_COUNT).concat(answerCount));
		connect(subject, predicate, object);
	}

	private void createScore(Resource subject, String score) {
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.SCORE));
		Resource object = model
				.createResource(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.SCORE).concat(score));
		connect(subject, predicate, object);
	}

	private Model getModelByName(String modelName) {
		if (dataset.getNamedModel(modelName) == null) {
			return ModelFactory.createDefaultModel();
		} else {
			return dataset.getNamedModel(QUESTION_MODEL);
		}
	}

	private Statement connect(Resource subject, Property predicate, Resource object) {
		model.add(subject, predicate, object);
		return model.createStatement(subject, predicate, object);
	}

	private Statement connect(Resource subject, Property predicate, Literal object) {
		model.add(subject, predicate, object);
		return model.createStatement(subject, predicate, object);
	}
}
