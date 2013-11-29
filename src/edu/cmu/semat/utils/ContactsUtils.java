package edu.cmu.semat.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

public class ContactsUtils {
	
	public static ArrayList<String> userEmailAddresses(Context context) {
		ArrayList<String> list = new ArrayList<String>();
		
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
		        list.add(possibleEmail);
		    }
		}
		return list;
	}
	
	
	public static ArrayList<String> userEmailAddressesTEST(Context context) {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("student.sam@gmail.com");
		list.add("faculty.frank@gmail.com");
		list.add("admin.andy@gmail.com");

		return list;
	}	
}
