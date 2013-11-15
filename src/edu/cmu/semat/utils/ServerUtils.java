package edu.cmu.semat.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.cmu.semat.entities.Alpha;
import edu.cmu.semat.entities.Card;
import edu.cmu.semat.entities.Checklist;
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
			JSONObject alphas_json = new JSONObject(HTTPUtils.sendGet("http://semat.herokuapp.com/api/v1/alphas.json"));
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alphas;
		
	}
	
}
