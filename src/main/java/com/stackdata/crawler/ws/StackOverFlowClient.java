package com.stackdata.crawler.ws;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("deprecation")
public class StackOverFlowClient {

	public JSONObject get(int page, int pageSize, String tag) throws ParseException {
		String result = null;
		try {
			// TODO HttpClient deprecated
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("https://api.stackexchange.com/2.2/questions?page=" + page + "&pagesize="
					+ pageSize + "&order=desc&sort=activity&tagged=" + tag + "&site=stackoverflow");

			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			StringBuffer szBuffer = new StringBuffer();
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException(response.getStatusLine().getStatusCode() + "");
			}
			GZIPInputStream g = new GZIPInputStream(response.getEntity().getContent());

			byte tByte[] = new byte[1024];

			while (true) {
				int iLength = g.read(tByte, 0, 1024);
				if (iLength < 0)
					break;
				szBuffer.append(new String(tByte, 0, iLength));
			}
			result = szBuffer.toString();
			httpClient.getConnectionManager().shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result != null) {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(result);
			return json;
		}
		return null;
	}

}
