package edu.cmu.semat;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.entities.Card;
import edu.cmu.semat.entities.Checklist;
import edu.cmu.semat.utils.HTTPUtils;

public class AlphaActivity extends FragmentActivity {

	AlphaCollectionPagerAdapter mAdapter;
	ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alpha);

		System.out.println("executing alphas background task");
		new FetchAlphasTask().execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alpha, menu);
		return true;
	}

	public static class AlphaCollectionPagerAdapter extends FragmentPagerAdapter {
		private int num_items = 0;
		private Alpha alpha = null;

		public AlphaCollectionPagerAdapter(FragmentManager fm, Alpha alpha) {
			super(fm);
			this.num_items = alpha.getCards().size();
			this.alpha = alpha;
		}

		@Override
		public int getCount() {
			return num_items;
		}

		@Override
		public Fragment getItem(int position) {
			return CardFragment.newInstance(position, alpha.getCards().get(position));
		}
	}

	public static class CardFragment extends ListFragment {
		int mNum;
		Card card;

		/**
		 * Create a new instance of CountingFragment, providing "num"
		 * as an argument.
		 */
		static CardFragment newInstance(int num, Card card) {
			CardFragment cardFragment = new CardFragment();

			cardFragment.card = card;
			cardFragment.mNum = num;

			return cardFragment;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		/**
		 * The Fragment's UI is just a simple text view showing its
		 * instance number.
		 */
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
			setListAdapter(new ChecklistArrayAdapter(getActivity(), R.layout.checklist, card.getChecklists()));
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}
	}
	
	private static class ChecklistArrayAdapter extends ArrayAdapter<Checklist> {
		ArrayList<Checklist> checklists;
		
		public ChecklistArrayAdapter(Context context, int resource, ArrayList<Checklist> objects) {
			super(context, resource, objects);
			checklists = objects;
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
			return convertView;
		}

	}



	// Uses AsyncTask to create a task away from the main UI thread. This task takes a 
	// URL string and uses it to create an HttpUrlConnection. Once the connection
	// has been established, the AsyncTask downloads the contents of the webpage as
	// an InputStream. Finally, the InputStream is converted into a string, which is
	// displayed in the UI by the AsyncTask's onPostExecute method.
	private class FetchAlphasTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

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

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			System.out.println("Performing alpha fetch callback");
			ArrayList<Alpha> alphas = Alpha.makeCollectionfromJSONString(result);

			mAdapter = new AlphaCollectionPagerAdapter(getSupportFragmentManager(), alphas.get(0));

			mPager = (ViewPager)findViewById(R.id.pager);
			mPager.setAdapter(mAdapter);


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
