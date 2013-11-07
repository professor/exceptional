package edu.cmu.semat.entities;

import java.util.ArrayList;

public class EssenceVersion {
	
	private int id;
	private String name;
	private ArrayList<Alpha> alphas;
	
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
	public ArrayList<Alpha> getAlphas() {
		return alphas;
	}
	public void setAlphas(ArrayList<Alpha> alphas) {
		this.alphas = alphas;
	}	

	
}
