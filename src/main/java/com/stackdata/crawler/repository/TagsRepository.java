package com.stackdata.crawler.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagsRepository {

	private List<String> tags = new ArrayList<String>(Arrays.asList("javascript", "java", "c#", "php", "android",
			"jquery", "python", "html", "c++", "ios", "mysql", "css", "sql", "asp.net", "objective-c", "ruby-on-rails",
			".net", "iphone", "c", "arrays", "sql-server", "angularjs", "ruby", "json", "ajax", "regex", "xml",
			"asp.net-mvc", "r", "linux", "wpf", "django", "node.js", "database", "xcode", "vb.net", "eclipse", "string",
			"windows", "wordpress", "html5", "excel", "spring", "swift", "multithreading", "facebook", "image", "forms",
			"git", "oracle", "osx", "winforms", "bash", "algorithm", "apache", "twitter-bootstrap", "performance",
			"mongodb", "swing", "matlab", "vba", "entity-framework", "linq", "ruby-on-rails-3", "visualstudio",
			"hibernate", "list", "postgresql", "css3", "perl"));

	public String getTag(int i) {
		return tags.get(i);
	}
}
