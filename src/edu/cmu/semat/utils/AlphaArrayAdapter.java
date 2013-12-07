package edu.cmu.semat.utils;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.cmu.semat.R;
import edu.cmu.semat.entities.Alpha;

public class AlphaArrayAdapter extends ArrayAdapter<Alpha> {
	ArrayList<Alpha> alphas;
	Map<Integer, Integer> currentAlphaStates;

	public AlphaArrayAdapter(Context context, int resource, ArrayList<Alpha> objects, Map<Integer, Integer> currentAlphaStates) {
		super(context, resource, objects);
		alphas = objects;
		this.currentAlphaStates = currentAlphaStates;
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
		progressText.setText(currentAlphaStates.get(position) + "/" + alphas.get(position).getCards().size());
		return convertView;
	}

}
