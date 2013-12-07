package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.cmu.semat.CommentsActivity;
import edu.cmu.semat.MyApplication;
import edu.cmu.semat.R;
import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.utils.AlphaCollectionPagerAdapter;
import edu.cmu.semat.utils.HTTPUtils;

public class FetchProgressTask extends AsyncTask<String, Void, String>{

	private FragmentActivity activity;
	private MyApplication application;
	private String auth_token;
	private String email_address;
	private int team_id;
	private int alpha_index;
	
	
	public FetchProgressTask(FragmentActivity activity, MyApplication application, String auth_token, String email_address, int team_id, int alpha_index){
		super();
		this.activity = activity;
		this.application = application;
		this.auth_token = auth_token;
		this.email_address = email_address;
		this.team_id = team_id;
		this.alpha_index = alpha_index;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		try {
			String data = String.format(auth_token, email_address);
			String url = String.format("https://semat.herokuapp.com/api/v1/progress/%s.json", team_id);

			return HTTPUtils.sendGet(activity, url + data);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		if(result == null){
			Toast.makeText(activity, "No data received!", Toast.LENGTH_LONG).show();
			return;
		}

		JSONObject r = new JSONObject(result);
		JSONArray p = (JSONArray) r.get("checkboxes");
		HashSet<Integer> progress = new HashSet<Integer>();
		for(int i=0; i < p.length(); i++){
			progress.add(p.getInt(i));
		}
		application.set("progress", progress);

		@SuppressWarnings("unchecked")
		ArrayList<Alpha> alphas = (ArrayList<Alpha>) application.get("alphas");
		activity.setTitle("Alpha " + alpha_index + ": " + alphas.get(alpha_index).getName());
		AlphaCollectionPagerAdapter mAdapter = new AlphaCollectionPagerAdapter(activity.getSupportFragmentManager(), alphas.get(alpha_index), progress, activity, team_id, auth_token);

		ViewPager mPager = (ViewPager)activity.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		@SuppressWarnings("unchecked")
		HashMap<Integer, Integer> currentAlphaStates = (HashMap<Integer, Integer>) application.get("currentAlphaStates");
		mPager.setCurrentItem(currentAlphaStates.get(alpha_index) - 1);

		// Watch for button clicks.
		Button button = (Button)activity.findViewById(R.id.notes);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(activity.getApplicationContext(), CommentsActivity.class);
				i.putExtra("title", "Notes");
				i.putExtra("url", "https://semat.herokuapp.com/api/v1/progress/" + team_id + "/save_notes.json");
				i.putExtra("alpha_index", alpha_index);
				application.startActivity(i);
			}
		});

		button = (Button)activity.findViewById(R.id.actions);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(activity.getApplicationContext(), CommentsActivity.class);
				i.putExtra("title", "Actions");
				i.putExtra("url", "https://semat.herokuapp.com/api/v1/progress/" + team_id + "/save_actions.json");
				i.putExtra("alpha_index", alpha_index);
				application.startActivity(i);
			}
		});
	}
}
