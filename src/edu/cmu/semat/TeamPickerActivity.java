package edu.cmu.semat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TeamPickerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_picker);
		
		String[] myStringArray = {"Exceptional", "Double Dragons"};
		ArrayAdapter adapter = new ArrayAdapter<String>(this, 
		        android.R.layout.simple_list_item_1, myStringArray);
		
		ListView listView = (ListView) findViewById(R.id.team_picker_list_view);
		listView.setAdapter(adapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
