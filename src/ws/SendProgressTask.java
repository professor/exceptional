package ws;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.savagelook.android.UrlJsonAsyncTask;

import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class SendProgressTask extends UrlJsonAsyncTask{
	private int checklist_id;
	private int teamId;
	private boolean isChecked;
	private String exceptionMessage;
	private String auth_token;
	private Activity activity;

	public SendProgressTask(Context context, int checklist_id, int teamId, String auth_token, boolean isChecked, Activity activity) {
		super(context);
		this.checklist_id = checklist_id;
		this.isChecked = isChecked;
		this.activity = activity;
		this.teamId = teamId;
		this.auth_token = auth_token;
		this.setMessageLoading("Updating server");
	}

	@Override
	protected JSONObject doInBackground(String... urls) {
		JSONObject json = new JSONObject();			
		try {
			JSONObject holder = new JSONObject();

			holder.put("checklist_id",  checklist_id);
			holder.put("checked", isChecked);
			holder.put("team_id", teamId);
			holder.put("user_token", auth_token);
			holder.put("user_email", SharedPreferencesUtil.getCurrentEmailAddress((Activity)context, ""));

			json = HTTPUtils.sendPost(activity, "https://semat.herokuapp.com/api/v1/progress/" + teamId + "/mark.json", holder);
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
		if(json == null){
			Toast.makeText(context, "Not synced - device offline", Toast.LENGTH_LONG).show();
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