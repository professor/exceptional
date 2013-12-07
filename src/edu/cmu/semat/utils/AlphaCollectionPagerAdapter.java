package edu.cmu.semat.utils;

import java.util.Set;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import edu.cmu.semat.entities.Alpha;

public class AlphaCollectionPagerAdapter extends FragmentPagerAdapter {
	private int num_items = 0;
	private Set<Integer> progress;
	private Alpha alpha = null;
	private Activity activity;
	private int team_id;
	private String auth_token;

	public AlphaCollectionPagerAdapter(FragmentManager fm, Alpha alpha, Set<Integer> progress, Activity activity, int team_id, String auth_token) {
		super(fm);
		this.num_items = alpha.getCards().size();
		this.alpha = alpha;
		this.progress = progress;
		this.activity = activity;
		this.team_id = team_id;
		this.auth_token = auth_token;
	}

	@Override
	public int getCount() {
		return num_items;
	}

	@Override
	public Fragment getItem(int position) {
		return CardFragment.newInstance(position, alpha.getCards().get(position), progress, activity, team_id, auth_token);
	}
}
