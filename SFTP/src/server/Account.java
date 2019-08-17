package server;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class Account {
	static String user = "";
	static String account = "";
	static String password = "";
	int userFoundInLine;
	static String filepath = "C:\\Users\\Amal\\Downloads\\Uni\\Semester 2\\COMPSYS 725\\Assignments\\Assignment-1\\SFTP\\src\\server\\data.txt";
	boolean loggedIn = false;
	
	private static Scanner x;
	
	public boolean isLoggedIn(String user) {
		if(this.user == user) {
			return loggedIn;
		}
		else {
			return false;
		}
	}
	
	public boolean alreadyInAccount() {
//		if(account == ""){
//			return false;
//		}
		return true;
	}
	
	int passwordFound;
	public boolean validPassword(String password) {
		boolean found = false;
		try {
			String tempPassword = "";
			x = new Scanner(new File(filepath));
			x.useDelimiter("[,\n]");
			
			int count = 0;
			while(count<userFoundInLine) {
				x.nextLine();
				count++;
			}
			while(x.hasNext() && !found) {
				x.next();
				x.next();
				tempPassword = x.next();
								
				if(tempPassword.trim().equals(password.trim())) {
					found = true; 
				}
			}
			x.close();			
		}catch(Exception e) {
			System.out.println("Error");
		}
		return found;
	}
	
	public boolean validAccount(String account) {
		boolean found = false;
		try {
			String tempAccount = "";
			x = new Scanner(new File(filepath));
			x.useDelimiter("[,\n]");
			int count = 0;
			while(count<userFoundInLine) {
				x.nextLine();
				count++;
			}
			while(x.hasNext() && !found) {
				x.next();
				tempAccount = x.next();
				x.next();
								
				if(tempAccount.trim().equals(account.trim())) {
					this.account = account;
					found = true; 
				}
			}
			x.close();
			
		}catch(Exception e) {
			System.out.println("Error");
		}
		return found;
	}
	
	public boolean validUser(String user) {
		boolean found = false;
		userFoundInLine = 0;
		try {
			String tempUser = "";
			x = new Scanner(new File(filepath));
			x.useDelimiter("[,\n]");
			
			while(x.hasNext() && !found) {
				tempUser = x.next();
				x.next();
				x.next();
				if(tempUser.trim().equals(user.trim())) {
					found = true; 
				}
				else {
					userFoundInLine = userFoundInLine + 1 ;
				}
			}
			x.close();
			
		}catch(Exception e) {
			System.out.println("Error");
		}
		return found;
	}
}
