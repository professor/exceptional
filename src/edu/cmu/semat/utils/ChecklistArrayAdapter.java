package edu.cmu.semat.utils;

import java.util.ArrayList;
import java.util.Set;

import ws.SendProgressTask;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import edu.cmu.semat.R;
import edu.cmu.semat.entities.Checklist;

public class ChecklistArrayAdapter extends ArrayAdapter<Checklist>{

	private ArrayList<Checklist> checklists;
	private Set<Integer> progress;
	private Context context;
	private Activity activity;
	private int team_id;
	private String auth_token;


	public ChecklistArrayAdapter(Context context, int team_id, String auth_token, int resource, ArrayList<Checklist> objects, Set<Integer> progress, Activity activity) {
		super(context, resource, objects);
		this.context = context;
		this.team_id = team_id;
		this.auth_token = auth_token;
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
				new SendProgressTask(context, checklist_id, team_id, auth_token, isChecked, activity).execute();
			}
		});
		return convertView;
	}
}
