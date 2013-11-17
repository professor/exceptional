package edu.cmu.semat;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.cmu.semat.entities.Team;
import edu.cmu.semat.utils.ContactsUtils;
import edu.cmu.semat.utils.HTTPUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class LoginActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 System.out.println("loginActivity onCreate");			
		super.onCreate(savedInstanceState);
	
//		ArrayList<String> emails = ContactsUtils.userEmailAddresses(this);
		ArrayList<String> emails = ContactsUtils.userEmailAddressesTEST(this);
			
		if(emails.size() == 1) {
			moveToNextIntent(emails.get(0));
		}
		
		setContentView(R.layout.login);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, emails);
		
		setListAdapter(adapter); 	    
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		
		moveToNextIntent(item);
	}	    

	private void moveToNextIntent(String selectedEmailAddress) {
		System.out.println("LoginActivity: selected email: " + selectedEmailAddress);
		SharedPreferencesUtil.setCurrentEmailAddress(this, selectedEmailAddress);
		
//		System.out.println("executing FindOrRegisterUserTask background task");
//		new FindOrRegisterUserTask().execute("");		
		
		Intent intent = new Intent(this, TeamPickerActivity.class);
		startActivity(intent);			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

	// Uses AsyncTask to create a task away from the main UI thread. This task takes a 
	// URL string and uses it to create an HttpUrlConnection. Once the connection
	// has been established, the AsyncTask downloads the contents of the webpage as
	// an InputStream. Finally, the InputStream is converted into a string, which is
	// displayed in the UI by the AsyncTask's onPostExecute method.
	private class FindOrRegisterUserTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			System.out.println("find_or_register user with server");
			// params comes from the execute() call: params[0] is the url.
			try {
				String email = SharedPreferencesUtil.getCurrentEmailAddress(LoginActivity.this, "");
				return HTTPUtils.sendPost("http:///semat.herokuapp.com/api/v1/users/find_or_register", "email:" + email);
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
			System.out.println("find_or_register user performing fetch callback");
			System.out.println(result);
			
			Intent intent = new Intent(LoginActivity.this, TeamPickerActivity.class);
			startActivity(intent);			
			
		}
	}	
}
