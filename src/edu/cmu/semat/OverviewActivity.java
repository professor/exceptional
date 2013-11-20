package edu.cmu.semat;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.utils.HTTPUtils;

public class OverviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);

		new FetchAlphasTask().execute("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.overview, menu);
		return true;
	}


	private static class AlphaArrayAdapter extends ArrayAdapter<Alpha> {
		ArrayList<Alpha> alphas;

		public AlphaArrayAdapter(Context context, int resource, ArrayList<Alpha> objects) {
			super(context, resource, objects);
			alphas = objects;
		}

		public int getCount (){
			return alphas.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				convertView = View.inflate (getContext(), R.layout.grid_item, null);
			}

			TextView alphaTitleText = (TextView) convertView.findViewById(R.id.grid_item__alpha_title);
			alphaTitleText.setText(alphas.get(position).getName());

			TextView cardTitleText = (TextView) convertView.findViewById(R.id.grid_item__card_title);
			cardTitleText.setText(alphas.get(position).getCards().get(0).getName());

			TextView progressText = (TextView) convertView.findViewById(R.id.grid_item__alpha_progress);
			progressText.setText("1/" + alphas.get(position).getCards().size());
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

			setTitle("Overview of Alphas");

			GridView grid = (GridView) findViewById(R.id.gridView1);
			grid.setAdapter(new AlphaArrayAdapter(getApplicationContext(), R.layout.grid_item, alphas));

			grid.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v, int position, long id) {
					Intent i = new Intent(getApplicationContext(), AlphaActivity.class);
					i.putExtra("index", position);
					System.out.println("position " + position);
					startActivity(i);
				}
			});

		}
	}
}
