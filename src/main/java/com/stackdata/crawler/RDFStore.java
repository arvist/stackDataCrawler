package com.stackdata.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.shared.Lock;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;

public class RDFStore {

	private Dataset dataset = TDBFactory.createDataset("src/main/resources/tbd");
	private static final String QUESTION_MODEL = "Question";

	private static final boolean RDF_WRITE_SUCCESSFUL = true;
	private Question question;

	private Model model;

	public void outputModelInFile() {
		model = getModelByName(QUESTION_MODEL);
		try {
			File file = new File("src/main/resources/out.rdf");
			model.write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false))), "RDF/XML");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean writeQuestion(Question question) {
		this.question = question;
		model = getModelByName(QUESTION_MODEL);
		try {
			model.enterCriticalSection(Lock.WRITE);

			Resource subject = model.createResource(Question.STACK_NAMESPACE.concat(Question.QUESTIONS)
					.concat(String.valueOf(question.getQuestionId())));
			createSameAs(subject);
			createTitle(subject);
			model.commit();
			TDB.sync(model);
		} finally {
			model.leaveCriticalSection();
		}
		return RDF_WRITE_SUCCESSFUL;
	}

	private void createTitle(Resource subject) {
		Property predicate = model.createProperty(Question.DUBLIN_CORE_NAMESPACE.concat("title"));
		Resource object = model.createResource(Question.STACK_NAMESPACE.concat("/title").concat(question.getTitle()));
		connect(subject, predicate, object);
		return;
	}

	private void createSameAs(Resource subject) {
		Property predicate = model.createProperty(Question.OWL_NAMESPACE.concat(Question.SAME_AS));
		Resource object = model.createResource(Question.API_STACKEXCHANGE.concat(Question.QUESTIONS)
				.concat(String.valueOf(question.getQuestionId()).concat(Question.SITE_STACKOVERFLOW_QUERY_PARAM)));
		connect(subject, predicate, object);
		return;
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
}
