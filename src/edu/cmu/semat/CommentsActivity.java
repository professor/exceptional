package edu.cmu.semat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.savagelook.android.UrlJsonAsyncTask;

import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class CommentsActivity extends Activity {

	private static final String TAG = "CommentsActivity";
	
	private static int alpha_id;
	private static String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		url = intent.getStringExtra("url");
		int alpha_index = intent.getIntExtra("alpha_index", 0);
		alpha_id = alpha_index + 1;
		TextView title_view = (TextView) findViewById(R.id.coments__title);
		title_view.setText(title);
		this.setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comments, menu);
		return true;
	}

	public void uploadComment(View v){
		EditText et = (EditText) findViewById(R.id.comments__field);
		String comment = et.getText().toString();
		new SendCommentTask(CommentsActivity.this, comment).execute();
	}

	// Uses AsyncTask to create a task away from the main UI thread.
	private static class SendCommentTask extends UrlJsonAsyncTask {
		private String comment;
		private String exceptionMessage;

		public SendCommentTask(Context context, String comment) {
			super(context);
			this.comment = comment;
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
				Log.v(TAG, holder.toString());
				json = HTTPUtils.sendPost(url, holder);

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

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(JSONObject json) {
			Log.v(TAG, "SendProgressTask onPostExecute()");
			Log.v(TAG, json.toString());

			if (json.getBoolean("response")) {
				Log.v(TAG, "Synced with server");
				Toast.makeText(context, "Synced with server", Toast.LENGTH_LONG).show();
			} else {
				Log.v(TAG, "Uploading progress failed! " + exceptionMessage );
				Toast.makeText(context, "Uploading progress failed! " + exceptionMessage, Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(json);
		}
	}
}
