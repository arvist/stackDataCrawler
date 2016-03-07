package com.rdf.data.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import com.stackdata.crawler.StackDataApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StackDataApplication.class)
@WebAppConfiguration
public class StackOverflowWikiClientApplicationTests {

	@Test
	public void contextLoads() {
	}

}
