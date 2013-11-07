package edu.cmu.semat.entities;

import java.util.ArrayList;

//aka State Card
public class Card {
	
	private int id;
	private String name;
	private ArrayList<Checklist> checklists;
	
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
	public ArrayList<Checklist> getChecklists() {
		return checklists;
	}
	public void setChecklists(ArrayList<Checklist> checklists) {
		this.checklists = checklists;
	}
	
}
