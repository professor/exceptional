package edu.cmu.semat.utils;

import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import edu.cmu.semat.R;
import edu.cmu.semat.entities.Card;

public class CardFragment extends ListFragment {
	private int mNum;
	private Card card;
	private Set<Integer> progress;
	private Activity activity;
	private int team_id;
	private String auth_token;

	/**
	 * Create a new instance of CountingFragment, providing "num"
	 * as an argument.
	 */
	static CardFragment newInstance(int num, Card card, Set<Integer> progress, Activity activity, int team_id, String auth_token) {
		CardFragment cardFragment = new CardFragment();

		cardFragment.card = card;
		cardFragment.mNum = num;
		cardFragment.progress = progress;
		cardFragment.activity = activity;
		cardFragment.auth_token = auth_token;
		cardFragment.team_id = team_id;

		return cardFragment;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

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
		setListAdapter(new ChecklistArrayAdapter(getActivity(), team_id, auth_token, R.layout.checklist, card.getChecklists(), progress, activity));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("FragmentList", "Item clicked: " + id);
	}
}
