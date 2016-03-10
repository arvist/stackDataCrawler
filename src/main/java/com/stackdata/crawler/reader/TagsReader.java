package com.stackdata.crawler.reader;

import java.util.HashSet;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import com.stackdata.crawler.repository.NamespaceRepository;
import com.stackdata.crawler.repository.ParamRepository;
import com.stackdata.tdb.StackTdbFactory;

public class TagsReader {

	private Model model;
	private StackTdbFactory tdbFactory = StackTdbFactory.getInstance();
	private HashSet<String> tagSet = new HashSet<String>();

	public HashSet<String> getAllTagSet() {
		model = tdbFactory.getModelByName(StackTdbFactory.QUESTION_MODEL);
		Property predicate = model
				.createProperty(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.TAGS_PARAM));
		ResIterator iter = model.listResourcesWithProperty(predicate);
		while (iter.hasNext()) {
			Resource r = iter.nextResource();
			StmtIterator iter2 = r.listProperties();
			while (iter2.hasNext()) {
				Statement st = iter2.next();
				if (st.asTriple().getPredicate()
						.hasURI(NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.TAGS_PARAM))) {
					tagSet.add(st.asTriple().getObject().toString().substring(
							NamespaceRepository.STACKDATA_NAMESPACE.concat(ParamRepository.TAGS_PARAM).length()));
				}
			}
		}
		return tagSet;
	}
}
