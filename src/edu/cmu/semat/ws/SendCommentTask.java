package edu.cmu.semat.ws;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.savagelook.android.UrlJsonAsyncTask;

import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class SendCommentTask extends UrlJsonAsyncTask {
	private String comment;
	private String exceptionMessage;
	private String url;
	private int alpha_id;

	public SendCommentTask(Context context, String comment, String url, int alpha_id) {
		super(context);
		this.comment = comment;
		this.url = url;
		this.alpha_id = alpha_id;
		this.setMessageLoading("Updating server");
	}

	@Override
	protected JSONObject doInBackground(String... urls) {
		JSONObject json = new JSONObject();			
		try {
			JSONObject holder = new JSONObject();

			holder.put("notes",  comment);
			holder.put("actions", comment);
			holder.put("user_token", SharedPreferencesUtil.getAuthToken((Activity)context, ""));
			holder.put("user_email", SharedPreferencesUtil.getCurrentEmailAddress((Activity)context, ""));
			holder.put("alpha_id", alpha_id);
			json = HTTPUtils.sendPost((Activity) context, url, holder);

		} catch (JSONException e) {
			exceptionMessage = e.getMessage();
			e.printStackTrace();
			return new JSONObject();
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
			e.printStackTrace();
			return new JSONObject();
		}			
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject json) {
		if(json == null){
			Toast.makeText(context, "Not synced - Device offline", Toast.LENGTH_LONG).show();
			super.onPostExecute(json);
			return;
		}

		if (json.getBoolean("response")) {
			Toast.makeText(context, "Synced with server", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "Uploading progress failed! " + exceptionMessage, Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(json);
	}

}
