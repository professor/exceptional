package edu.cmu.semat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import edu.cmu.semat.utils.SharedPreferencesUtil;

public class MainActivity extends Activity {

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		Intent intent = new Intent(this, TeamPickerActivity.class);
//		startActivity(intent);	
//		return;
//	}
//	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Just for debugging
//		SharedPreferencesUtil.setSplashScreenSeenByUser(this, false);
				
		if(!SharedPreferencesUtil.getSplashScreenSeenByUser(this)) {
			 System.out.println("introductionActivity");			
			  Intent intent = new Intent(this, IntroductionActivity.class);
			  startActivity(intent);
			
		} else {
			 System.out.println("loginActivity");			
			  Intent intent = new Intent(this, LoginActivity.class);
			  startActivity(intent);
		}
		
	}

//	   @Override
//	   protected void onSaveInstanceState(Bundle outState)
//	   {
//	      super.onSaveInstanceState(outState);
////	      
////	      outState.putDouble(BILL_TOTAL, currentBillTotal);
////	      outState.putInt(CUSTOM_PERCENT, currentCustomPercent);
//	   } 	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
