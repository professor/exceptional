package edu.cmu.semat.entities;

import java.util.ArrayList;
import java.util.Iterator;

public class Team {
	
	private int id;
	private String name;
	
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

	
}
