package com.stackdata.tdb;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;

public class StackTdbFactory {

	private Dataset dataset = TDBFactory.createDataset("src/main/resources/tbd");
	public static final String QUESTION_MODEL = "Question";

	private static StackTdbFactory instance = null;

	protected StackTdbFactory() {
	}

	public static StackTdbFactory getInstance() {
		if (instance == null) {
			instance = new StackTdbFactory();
		}
		return instance;
	}

	public void outputModelInFile(String modelName) {
		Model model = getModelByName(modelName);
		try {
			File file = new File("src/main/resources/out.rdf");
			model.write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false))), "" + "TTL");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Model getModelByName(String modelName) {
		if (dataset.getNamedModel(modelName) == null) {
			return ModelFactory.createDefaultModel();
		} else {
			return dataset.getNamedModel(QUESTION_MODEL);
		}
	}
}
