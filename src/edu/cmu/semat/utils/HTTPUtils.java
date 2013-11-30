package edu.cmu.semat.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import edu.cmu.semat.MyApplication;

// source http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/

public class HTTPUtils {
	private static final String TAG = "HTTPUtils";

	// HTTP GET request
	public static String sendGet(Activity activity, String url, boolean isOnline) throws Exception {
		
		if(! isOnline){
			Log.v(TAG, "Offline - using cache");
			return SharedPreferencesUtil.getString(activity, url, null);
		}
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		SharedPreferencesUtil.setString(activity, url, response.toString());
		return response.toString();
	}
	
	public static String sendGet(Activity activity, String url) throws Exception {
		return HTTPUtils.sendGet(activity, url, ((MyApplication) activity.getApplication()).isNetworkAvailable());
	}
	
	// HTTP POST request	
	public static String sendPost(Activity activity, String url, String data) throws Exception {
		if(! ((MyApplication) activity.getApplication()).isNetworkAvailable()){
			return null;
		}
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("POST");
		
		con.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}

	public static JSONObject sendPost(Activity activity, String url, JSONObject holder) throws Exception {
		if(! ((MyApplication) activity.getApplication()).isNetworkAvailable()){
			return null;
		}
		HttpPost post = new HttpPost(url);
		DefaultHttpClient client = new DefaultHttpClient();
		String response = null;

		StringEntity se = new StringEntity(holder.toString());
		post.setEntity(se);

		// setup the request headers
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-Type", "application/json");

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		response = client.execute(post, responseHandler);
		return (new JSONObject(response));
	}		
}
