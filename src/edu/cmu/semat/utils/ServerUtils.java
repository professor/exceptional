package edu.cmu.semat.utils;

import java.util.ArrayList;

import edu.cmu.semat.entities.Alpha;
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
	
	public static ArrayList<Alpha> alphas(){
		ArrayList<Alpha> alphas = new ArrayList<Alpha>();
		try {
			String json = HTTPUtils.sendGet("http://semat.herokuapp.com/api/v1/alphas.json");
			return Alpha.makeCollectionfromJSONString(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alphas;
	}
}
