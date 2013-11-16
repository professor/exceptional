package edu.cmu.semat.entities;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Team {
	
	private int id;
	private String name;
	
	public Team() {
	}

	public Team(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static String[] arrayListToNames(ArrayList<Team> teams) {
		String[] names = new String[teams.size()];
		int index = 0;
		
		Iterator<Team> it = teams.iterator();
		while (it.hasNext()) {
			Team current = (Team) it.next();
			names[index] = current.getName();
			index++;
		}
		return names;
	}
	
	public static ArrayList<Team> makeCollectionfromJSONString(String json){
		ArrayList<Team> teams = new ArrayList<Team>();
		JSONObject teams_json = new JSONObject(json);
		JSONArray teams_array = (JSONArray) teams_json.get("teams");
		for(int i = 0; i < teams_array.length(); i++){
			JSONObject team_json = (JSONObject) teams_array.get(i);
			Team team = new Team();
			team.setId(team_json.getInt("id"));
			team.setName(team_json.getString("name"));
			teams.add(team);
		}
		return teams;
	}

	
}
