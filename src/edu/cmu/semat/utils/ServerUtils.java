package edu.cmu.semat.utils;

import java.util.ArrayList;

import android.content.Context;
import edu.cmu.semat.entities.Team;

public class ServerUtils {

	public static ArrayList<Team> teamsForUser(String currentEmailAddress) {

		
		
		
		return null;
	}

	public static ArrayList<Team> teamsForUserTEST(String currentEmailAddress) {
		ArrayList<Team> list = new ArrayList<Team>();
		
		Team current;
		current = new Team(1, "Exceptional");
		list.add(current);

		current = new Team(2, "Driven");
		list.add(current);
		
		return list;
	}		
	
	
}
