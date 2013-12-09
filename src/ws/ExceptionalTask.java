package ws;

import edu.cmu.semat.MyApplication;
import android.app.Activity;
import android.os.AsyncTask;

@SuppressWarnings("unused")
public abstract class ExceptionalTask extends AsyncTask<String, Void, String>{

	protected MyApplication application;
	protected Activity activity;
	protected String auth_token;
	protected String email_address;
	protected int team_id;
	
	public ExceptionalTask(MyApplication application, Activity activity, String auth_token, String email_address, int team_id){
		super();
		this.application = application;
		this.activity = activity;
		this.auth_token = auth_token;
		this.email_address = email_address;
		this.team_id = team_id;
	}
	
}
