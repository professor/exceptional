package edu.cmu.semat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.cmu.semat.ws.SendCommentTask;

public class CommentsActivity extends Activity {

	private static int alpha_id;
	private static String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		url = intent.getStringExtra("url");
		int alpha_index = intent.getIntExtra("alpha_index", 0);
		alpha_id = alpha_index + 1;
		TextView title_view = (TextView) findViewById(R.id.coments__title);
		title_view.setText(title);
		this.setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comments, menu);
		return true;
	}

	public void uploadComment(View v){
		EditText et = (EditText) findViewById(R.id.comments__field);
		String comment = et.getText().toString();
		new SendCommentTask(CommentsActivity.this, comment, url, alpha_id).execute();
	}
}
