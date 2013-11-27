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
import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.entities.Card;
import edu.cmu.semat.entities.Checklist;
import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class AlphaActivity extends FragmentActivity {

	private static final String TAG = "AlphaActivity";

	AlphaCollectionPagerAdapter mAdapter;
	ViewPager mPager;
	int index;
	static int teamId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		teamId = SharedPreferencesUtil.getCurrentTeamId(getParent(), 1);
		teamId = SharedPreferencesUtil.getCurrentTeamId(this, 1);
		setContentView(R.layout.activity_alpha);

		System.out.println("executing alphas background task");
		Intent intent = getIntent();
		index = intent.getIntExtra("index", 0);

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
		return true;
	}

	public static class AlphaCollectionPagerAdapter extends FragmentPagerAdapter {
		private int num_items = 0;
		private Set<Integer> progress;
		private Alpha alpha = null;

		public AlphaCollectionPagerAdapter(FragmentManager fm, Alpha alpha, Set<Integer> progress) {
			super(fm);
			this.num_items = alpha.getCards().size();
			this.alpha = alpha;
			this.progress = progress;
		}

		@Override
		public int getCount() {
			return num_items;
		}

		@Override
		public Fragment getItem(int position) {
			return CardFragment.newInstance(position, alpha.getCards().get(position), progress);
		}
	}

	public static class CardFragment extends ListFragment {
		int mNum;
		Card card;
		Set<Integer> progress;

		/**
		 * Create a new instance of CountingFragment, providing "num"
		 * as an argument.
		 */
		static CardFragment newInstance(int num, Card card, Set<Integer> progress) {
			CardFragment cardFragment = new CardFragment();

			cardFragment.card = card;
			cardFragment.mNum = num;
			cardFragment.progress = progress;

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
			setListAdapter(new ChecklistArrayAdapter(getActivity(), R.layout.checklist, card.getChecklists(), progress));
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}
	}

	private static class ChecklistArrayAdapter extends ArrayAdapter<Checklist> {
		ArrayList<Checklist> checklists;
		Set<Integer> progress;

		public ChecklistArrayAdapter(Context context, int resource, ArrayList<Checklist> objects, Set<Integer> progress) {
			super(context, resource, objects);
			checklists = objects;
			this.progress = progress;
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
					String data = String.format("checklist_id=%d&checked=%s&team_id=%d", checklist_id, isChecked, teamId);
					System.out.println(data);
					
// This needs to be in it's own UrlJsonAsyncTask, like LoginActivity					
//					try {
//						JSONObject json = new JSONObject();			
//						JSONObject holder = new JSONObject();
//						
//
//						holder.put("checklist_id",  checklist_id);
//						holder.put("checked", isChecked);
//						holder.put("team_id", teamId);
//						holder.put("auth_token", SharedPreferencesUtil.getAuthToken((Activity)getContext(), ""));
//						
//						Log.v(TAG, holder.toString());
//
//						json = HTTPUtils.sendPost("https://semat.herokuapp.com/api/v1/progress", holder);
//					} catch (JSONException e) {
//						Toast.makeText(getContext(), "Uploading progress failed!" + e.getMessage(), Toast.LENGTH_LONG).show();						
//						e.printStackTrace();
//					} catch (Exception e) {
//						Toast.makeText(getContext(), "Uploading progress failed!" + e.getMessage(), Toast.LENGTH_LONG).show();						
//						e.printStackTrace();
//					}					
					
					
					
//					try {
//						HTTPUtils.sendPost("http://semat.herokuapp.com/api/v1/progress", data);
//					} catch (Exception e) {
//						Toast.makeText(getContext(), "Uploading progress failed!", Toast.LENGTH_LONG).show();
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			});
			return convertView;
		}

	}

	private class FetchAlphasTask extends AsyncTask<String, Void, String> {

		private int index;

		@Override
		protected String doInBackground(String... urls) {

			if(! ((MyApplication) getApplication()).containsKey("alphas")){
				System.out.println("fetching alphas from server");
				// params comes from the execute() call: params[0] is the url.
				try {
					return HTTPUtils.sendGet("http://semat.herokuapp.com/api/v1/alphas.json");
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
			if(result != null){
				System.out.println("Performing alpha fetch callback");
				ArrayList<Alpha> alphas = Alpha.makeCollectionfromJSONString(result);
				((MyApplication) getApplication()).set("alphas", alphas);
			}
			new FetchCurrentAlphaStateTask().execute();
		}
	}

	private class FetchCurrentAlphaStateTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {

			System.out.println("fetching alphas from server");
			// params comes from the execute() call: params[0] is the url.
			try {
				return HTTPUtils.sendGet("http://semat.herokuapp.com/api/v1/progress/" + teamId + "/current_alpha_states.json");
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
			JSONObject r = new JSONObject(result);
			JSONObject s = (JSONObject) r.get("current_alpha_states");
			HashMap<Integer, Integer> currentAlphaStates = new HashMap<Integer, Integer>();
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

			System.out.println("fetching progress from server");
			// params comes from the execute() call: params[0] is the url.
			try {
				return HTTPUtils.sendGet("http://semat.herokuapp.com/api/v1/progress/" + teamId + ".json");
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
			System.out.println("Performing alpha fetch callback");

			JSONObject r = new JSONObject(result);
			JSONArray p = (JSONArray) r.get("checkboxes");
			HashSet<Integer> progress = new HashSet<Integer>();
			for(int i=0; i < p.length(); i++){
				progress.add(p.getInt(i));
			}
			((MyApplication) getApplication()).set("progress", progress);

			ArrayList<Alpha> alphas = (ArrayList<Alpha>) ((MyApplication) getApplication()).get("alphas");
			setTitle("Alpha " + index + ": " + alphas.get(index).getName());
			mAdapter = new AlphaCollectionPagerAdapter(getSupportFragmentManager(), alphas.get(index), progress);

			mPager = (ViewPager)findViewById(R.id.pager);
			mPager.setAdapter(mAdapter);

			HashMap<Integer, Integer> currentAlphaStates = (HashMap<Integer, Integer>) ((MyApplication) getApplication()).get("currentAlphaStates");
			mPager.setCurrentItem(currentAlphaStates.get(index) - 1);

			// Watch for button clicks.
			Button button = (Button)findViewById(R.id.goto_first);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mPager.setCurrentItem(0);
				}
			});

			button = (Button)findViewById(R.id.goto_last);
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mPager.setCurrentItem(mAdapter.getCount()-1);
				}
			});
		}
	}
}
