package edu.cmu.semat.entities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Alpha {

	private int id;
	private String name;
	private String color;
	private String concern;
	private String definition;
	private String description;
	private ArrayList<Card> cards;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getConcern() {
		return concern;
	}
	public void setConcern(String concern) {
		this.concern = concern;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public static ArrayList<Alpha> makeCollectionfromJSONString(String json){
		ArrayList<Alpha> alphas = new ArrayList<Alpha>();
		JSONObject alphas_json = new JSONObject(json);
		JSONArray alphas_array = (JSONArray) alphas_json.get("alphas");
		for(int i = 0; i < alphas_array.length(); i++){
			JSONObject alpha_json = (JSONObject) alphas_array.get(i);
			Alpha alpha = new Alpha();
			alpha.setId(alpha_json.getInt("id"));
			alpha.setColor(alpha_json.getString("color"));
			alpha.setName(alpha_json.getString("name"));
			alpha.setConcern(alpha_json.getString("concern"));
			alpha.setDefinition(alpha_json.getString("definition"));
			alpha.setDescription(alpha_json.getString("description"));

			JSONArray cards_array = (JSONArray) alpha_json.get("states");
			ArrayList<Card> cards = new ArrayList<Card>();
			for(int j = 0; j < cards_array.length(); j++){
				JSONObject card_json = (JSONObject) cards_array.get(j);
				Card card = new Card();
				card.setId(card_json.getInt("id"));
				card.setName(card_json.getString("name"));

				JSONArray checklists_array = (JSONArray) card_json.get("checklists");
				ArrayList<Checklist> checklists = new ArrayList<Checklist>();
				for(int k = 0; k < checklists_array.length(); k++){
					JSONObject checklist_json = (JSONObject) checklists_array.get(k);
					Checklist checklist = new Checklist();
					checklist.setId(checklist_json.getInt("id"));
					checklist.setName(checklist_json.getString("name"));
					checklists.add(checklist);
				}
				card.setChecklists(checklists);
				cards.add(card);
			}
			alpha.setCards(cards);
			alphas.add(alpha);
		}
		return alphas;
	}
}
