package edu.cmu.semat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.savagelook.android.UrlJsonAsyncTask;

import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.entities.Card;
import edu.cmu.semat.entities.Checklist;
import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class AlphaActivity extends FragmentActivity {

	private static final String TAG = "AlphaActivity";

	AlphaCollectionPagerAdapter mAdapter;
	ViewPager mPager;
	int alpha_index;
	static int teamId;
	static String auth_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		teamId = SharedPreferencesUtil.getCurrentTeamId(this, 1);
		setContentView(R.layout.activity_alpha);

		Intent intent = getIntent();
		alpha_index = intent.getIntExtra("index", 0);

		auth_token = SharedPreferencesUtil.getAuthToken(this, "");
		
		new FetchAlphasTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alpha, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.goto_overview){
			Intent i = new Intent(getApplicationContext(), OverviewActivity.class);
			startActivity(i);
		}
		if(item.getItemId() == R.id.goto_team_picker){
			Intent i = new Intent(getApplicationContext(), TeamPickerActivity.class);
			startActivity(i);
		}
		return true;
	}

	public static class AlphaCollectionPagerAdapter extends FragmentPagerAdapter {
		private int num_items = 0;
		private Set<Integer> progress;
		private Alpha alpha = null;
		private Activity activity;
		
		public AlphaCollectionPagerAdapter(FragmentManager fm, Alpha alpha, Set<Integer> progress, Activity activity) {
			super(fm);
			this.num_items = alpha.getCards().size();
			this.alpha = alpha;
			this.progress = progress;
			this.activity = activity;
		}

		@Override
		public int getCount() {
			return num_items;
		}

		@Override
		public Fragment getItem(int position) {
			return CardFragment.newInstance(position, alpha.getCards().get(position), progress, activity);
		}
	}

	public static class CardFragment extends ListFragment {
		int mNum;
		Card card;
		Set<Integer> progress;
		Activity activity;

		/**
		 * Create a new instance of CountingFragment, providing "num"
		 * as an argument.
		 */
		static CardFragment newInstance(int num, Card card, Set<Integer> progress, Activity activity) {
			CardFragment cardFragment = new CardFragment();

			cardFragment.card = card;
			cardFragment.mNum = num;
			cardFragment.progress = progress;
			cardFragment.activity = activity;

			return cardFragment;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
			View tv = v.findViewById(R.id.text);
			((TextView)tv).setText(this.card.getName());
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(new ChecklistArrayAdapter(getActivity(), R.layout.checklist, card.getChecklists(), progress, activity));
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}
	}

	private static class ChecklistArrayAdapter extends ArrayAdapter<Checklist> {
		ArrayList<Checklist> checklists;
		Set<Integer> progress;
		Context context;
		Activity activity;
		

		public ChecklistArrayAdapter(Context context, int resource, ArrayList<Checklist> objects, Set<Integer> progress, Activity activity) {
			super(context, resource, objects);
			this.context = context;
			this.checklists = objects;
			this.progress = progress;
			this.activity = activity;
		}

		public int getCount (){
			return checklists.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				convertView = View.inflate (getContext(), R.layout.checklist, null);
			}

			TextView checkboxText = (TextView) convertView.findViewById(R.id.textView1);
			checkboxText.setText("" + checklists.get(position).getName());
			CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
			cb.setChecked(progress.contains(checklists.get(position).getId()));
			cb.setTag(R.integer.checklist_id_tag, checklists.get(position).getId());
			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int checklist_id = (Integer) buttonView.getTag(R.integer.checklist_id_tag);
					new SendProgressTask(context, checklist_id, isChecked, activity).execute();
				}
			});
			return convertView;
		}

	}

	private class FetchAlphasTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {

			if(! ((MyApplication) getApplication()).containsKey("alphas")){
				// params comes from the execute() call: params[0] is the url.
				try {
					return HTTPUtils.sendGet(AlphaActivity.this, "http://semat.herokuapp.com/api/v1/alphas.json");
				} catch (IOException e) {
					return "Unable to retrieve web page. URL may be invalid.";
				} catch (Exception e) {
					e.printStackTrace();
					return "Unable to retrieve web page. URL may be invalid.";
				}
			}
			return null;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if(result == null){
				Toast.makeText(AlphaActivity.this, "No data received!", Toast.LENGTH_LONG).show();
				return;
			}
			ArrayList<Alpha> alphas = Alpha.makeCollectionfromJSONString(result);
			((MyApplication) getApplication()).set("alphas", alphas);
			new FetchCurrentAlphaStateTask().execute();
		}
	}

	private class FetchCurrentAlphaStateTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// params comes from the execute() call: params[0] is the url.
			try {
				return HTTPUtils.sendGet(AlphaActivity.this, "http://semat.herokuapp.com/api/v1/progress/" + teamId + "/current_alpha_states.json");
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			} catch (Exception e) {
				e.printStackTrace();
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if(result == null){
				Toast.makeText(AlphaActivity.this, "No data received!", Toast.LENGTH_LONG).show();
				return;
			}
			JSONObject r = new JSONObject(result);
			JSONObject s = (JSONObject) r.get("current_alpha_states");
			HashMap<Integer, Integer> currentAlphaStates = new HashMap<Integer, Integer>();
			
			@SuppressWarnings("unchecked")
			ArrayList<Alpha> alphas = (ArrayList<Alpha>) ((MyApplication) getApplication()).get("alphas");
			
			for(int alpha = 1; alpha <= alphas.size(); alpha++){
				int card = s.getInt(Integer.toString(alpha));
				currentAlphaStates.put(alpha-1, card);
			}
			((MyApplication) getApplication()).set("currentAlphaStates", currentAlphaStates);
			new FetchProgressTask().execute();
		}
	}

	private class FetchProgressTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			try {
				return HTTPUtils.sendGet(AlphaActivity.this, "http://semat.herokuapp.com/api/v1/progress/" + teamId + ".json");
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			} catch (Exception e) {
				e.printStackTrace();
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if(result == null){
				Toast.makeText(AlphaActivity.this, "No data received!", Toast.LENGTH_LONG).show();
				return;
			}
			
			JSONObject r = new JSONObject(result);
			JSONArray p = (JSONArray) r.get("checkboxes");
			HashSet<Integer> progress = new HashSet<Integer>();
			for(int i=0; i < p.length(); i++){
				progress.add(p.getInt(i));
			}
			((MyApplication) getApplication()).set("progress", progress);

			@SuppressWarnings("unchecked")
			ArrayList<Alpha> alphas = (ArrayList<Alpha>) ((MyApplication) getApplication()).get("alphas");
			setTitle("Alpha " + alpha_index + ": " + alphas.get(alpha_index).getName());
			mAdapter = new AlphaCollectionPagerAdapter(getSupportFragmentManager(), alphas.get(alpha_index), progress, AlphaActivity.this);

			mPager = (ViewPager)findViewById(R.id.pager);
			mPager.setAdapter(mAdapter);

			@SuppressWarnings("unchecked")
			HashMap<Integer, Integer> currentAlphaStates = (HashMap<Integer, Integer>) ((MyApplication) getApplication()).get("currentAlphaStates");
			mPager.setCurrentItem(currentAlphaStates.get(alpha_index) - 1);

			// Watch for button clicks.
			Button button = (Button)findViewById(R.id.notes);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), CommentsActivity.class);
					i.putExtra("title", "Notes");
					i.putExtra("url", "https://semat.herokuapp.com/api/v1/progress/" + teamId + "/save_notes.json");
					i.putExtra("alpha_index", alpha_index);
					startActivity(i);
				}
			});

			button = (Button)findViewById(R.id.actions);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), CommentsActivity.class);
					i.putExtra("title", "Actions");
					i.putExtra("url", "https://semat.herokuapp.com/api/v1/progress/" + teamId + "/save_actions.json");
					i.putExtra("alpha_index", alpha_index);
					startActivity(i);
				}
			});
		}
	}

	// Uses AsyncTask to create a task away from the main UI thread.
	private static class SendProgressTask extends UrlJsonAsyncTask {
		private int checklist_id;
		private boolean isChecked;
		private String exceptionMessage;
		private Activity activity;

		public SendProgressTask(Context context, int checklist_id, boolean isChecked, Activity activity) {
			super(context);
			this.checklist_id = checklist_id;
			this.isChecked = isChecked;
			this.activity = activity;
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

				Log.v(TAG, holder.toString());

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
			Log.v(TAG, "SendProgressTask onPostExecute()");
			if(json == null){
				Toast.makeText(context, "Not synced - device offline", Toast.LENGTH_LONG).show();
				super.onPostExecute(json);
				return;
			}
			
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
