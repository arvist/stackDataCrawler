package com.stackdata.crawler.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.jena.query.Dataset;
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
			model.write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false))), "RDF/XML");
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
				createSameAs(subject, String.valueOf(question.getQuestionId()));
				createTitle(subject, question.getTitle());
				model.commit();
				TDB.sync(model);
			}
		} finally {
			model.leaveCriticalSection();
		}
		return RDF_WRITE_SUCCESSFUL;
	}

	private void createTitle(Resource subject, String title) {
		Property predicate = model.createProperty(NamespaceRepository.DUBLIN_CORE_NAMESPACE.concat("title"));
		Resource object = model.createResource(NamespaceRepository.STACKDATA_NAMESPACE.concat("/title").concat(title));
		connect(subject, predicate, object);
		return;
	}

	private void createSameAs(Resource subject, String id) {
		Property predicate = model
				.createProperty(NamespaceRepository.OWL_NAMESPACE.concat(ParamRepository.SAME_AS_PARAM));
		Resource object = model.createResource(NamespaceRepository.STACKEXCHANGE_API
				.concat(ParamRepository.QUESTIONS_PARAM).concat(id.concat(ParamRepository.SITE_SO_QUERY_PARAM)));
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
