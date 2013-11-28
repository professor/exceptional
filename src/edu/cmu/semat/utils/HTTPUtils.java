package edu.cmu.semat.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

// source http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/

public class HTTPUtils {

	// HTTP GET request
	public static String sendGet(String url) throws Exception {
		return HTTPUtils.sendGet(url, null);
	}

	public static String sendGet(String url, String authToken) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		if(authToken != null)
			con.setRequestProperty("Authorization", "Devise " + authToken);

//		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	// HTTP POST request			
	public static JSONObject sendPost(String url, JSONObject holder) throws Exception {
		return HTTPUtils.sendPost(url, holder, null);
	}

	public static JSONObject sendPost(String url, JSONObject holder, String authToken) throws Exception {

		HttpPost post = new HttpPost(url);
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;

		StringEntity se = new StringEntity(holder.toString());
		post.setEntity(se);

		// setup the request headers
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-Type", "application/json");
		if(authToken != null)
			post.setHeader("Authorization", "Devise " + authToken);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		response = client.execute(post, responseHandler);
		return (new JSONObject(response));
	}		
}
