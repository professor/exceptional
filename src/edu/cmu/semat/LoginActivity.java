package edu.cmu.semat;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.cmu.semat.utils.ContactsUtils;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class LoginActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		ArrayList<String> emails = ContactsUtils.userEmailAddressesTEST(this);
			
		if(emails.size() == 1) {
			SharedPreferencesUtil.setCurrentEmailAddress(this, emails.get(0));
			Intent intent = new Intent(this, TeamPickerActivity.class);
			startActivity(intent);			
		}
		
		setContentView(R.layout.login);
		
//	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//	            R.layout.login_email_list_item, emails);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, emails);
		
		setListAdapter(adapter); 	    
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}	    


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
