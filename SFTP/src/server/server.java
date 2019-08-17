package server;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

public class server {
	
	public static String currentDir() {
		return System.getProperty("user.dir");
	}
	
	public static String goBackDir() {
		return new File(System.getProperty("user.dir")).getParentFile().toString();
	}
	
	public static void main(String argv[]) throws Exception{
		try {
			ServerSocket welcomeSocket = new ServerSocket(6789);

			Account account = new Account();
			MyFiles myFiles = new MyFiles();
			String currentDirectory = currentDir();
			System.out.println(currentDirectory);
			String fileToRename = "";
			String fileToSendLocation = "";
			boolean running = true;
//			myFiles.listAllFiles("files");
			while(running) {
				Socket connectionSocket = welcomeSocket.accept(); 				// only created once its connected
				System.out.println("server 3");

				OutputStream os = connectionSocket.getOutputStream();
				BufferedReader inFromClient = 
						new BufferedReader(new
						    InputStreamReader(connectionSocket.getInputStream())); 
				DataOutputStream  outToClient = 
						new DataOutputStream(os); 
//				
//				outToClient.writeBytes("+ Amals Server SFTP Service\n");
//				outToClient.flush();
//				
//				String command = inFromClient.readLine().substring(0, 4);
				
				Scanner in = new Scanner(System.in);

				String UserCommand = in.nextLine();
				String command = UserCommand.substring(0, 4);
				
				switch (command) {
					case "USER":
//						String user = inFromClient.readLine().substring(4);
						String user = UserCommand.substring(4);
						if(account.isLoggedIn(user)) {
							System.out.println("!<user-id> logged in");
						}
						else if(account.validUser(user)) {
							System.out.println("+User-id valid, send account and password");
						}
						else {
							System.out.println("-Invalid user-id, try again");
						}
						break;
						
					case "ACCT":
						String accountName = UserCommand.substring(4);

						if(account.isLoggedIn(accountName)) {
							System.out.println("! Account valid, logged-in");
						}
						else if(account.validAccount(accountName)) {
							System.out.println("+Account valid, send password");
						}
						else {
							System.out.println("Invalid account, try again");
						}		
						break;
						
					case "PASS":
						String password = UserCommand.substring(4);
						if(account.alreadyInAccount() && account.validPassword(password)){
							System.out.println("! Logged in");
						}
						else if(!account.alreadyInAccount() && account.validPassword(password)) {
							System.out.println("Password ok but you haven't specified the account\"");
						}
						else {
							System.out.println("-Wrong password, try again");
						}	
						break;
					
					case "TYPE":
						String type = UserCommand.substring(4);
						if(type == "A") {
							System.out.println("+Using Ascii mode");
						}
						else if(type == "B") {
							System.out.println("+Using Binary mode");
						}
						else if(type =="C") {
							System.out.println("+Using Continuous mode");
						}
						else {
							System.out.println("-Type not valid");
						}
						break;
					
					case "LIST":
						String format = UserCommand.substring(5,6);
						String dir = UserCommand.substring(6).trim();
						if(format.contentEquals("F")) {
							String toPrint = myFiles.listAllFiles(dir,"F");
							System.out.println(toPrint);
						}
						else if(format.contentEquals("V")) {
							String toPrint = myFiles.listAllFiles(dir,"V");
							System.out.println(toPrint);
						}
						break;
					
					case "CDIR":
					    if(account.alreadyInAccount()) {
							String newDir = UserCommand.substring(5);
                            String checkNewDir = Paths.get(currentDirectory, newDir).toString();
							Path path = Paths.get(checkNewDir);
							
							if(newDir.equals("..")) {
								currentDirectory = goBackDir();
								System.out.println("!Changed working dir to "+currentDirectory);
							}
							else if(newDir.equals("/")) {
								currentDirectory = "C:\\";
								System.out.println("!Changed working dir to "+path);
							}
							else {
								if(Files.exists(path)) {
									currentDirectory = checkNewDir;
									System.out.println("!Changed working dir to "+path);
								}
								else {
									System.out.println("-Can't connect to directory because: directory doesn't exist");
								}
						    }
					    }
					    else {
							System.out.println("-You must send ACCT and PASS to use CDIR");
					    }
					    break;
					    
					case "KILL":
					    if(account.alreadyInAccount()) {
							String fileToDelete = UserCommand.substring(5);
	                        String fileLocation = Paths.get(currentDirectory, fileToDelete).toString();
//							Path path = Paths.get(fileLocation);
					        File file = new File(fileLocation); 
							if(file.delete()) { 
					            System.out.println("+" + fileToDelete + " deleted"); 
					        } 
					        else { 
					            System.out.println("-Not deleted because file doesn't exist"); 
					        }
					    }
						else {
							System.out.println("-You must send ACCT and PASS to use CDIR");
					    }
						break;
					
					case "NAME":
					    if(account.alreadyInAccount()) {
							String tempFileToRename = UserCommand.substring(5);
	                        String fileLocation = Paths.get(currentDirectory, fileToRename).toString();
							Path path = Paths.get(fileLocation);
					        File file = new File(fileLocation); 
							if(Files.exists(path)) {
								fileToRename = tempFileToRename;
								System.out.println("+File exists");
							}
							else {
								System.out.println("-Can't find "+ fileToRename +"\n NAME command is aborted, don't send TOBE.");
							}
					    }
						else {
							System.out.println("-You must send ACCT and PASS to use CDIR");
					    }
						break;
						
					case "TOBE":
						String newFileName = UserCommand.substring(5);
						if(fileToRename.equals("")) {
							System.out.println("-File wasn't renamed because filename was not specified or was invalid");
						}
						else {
	                        String fileLocation = Paths.get(currentDirectory, fileToRename).toString();
	                        String newName = Paths.get(currentDirectory, newFileName).toString();
							File file = new File(fileLocation);
							File fileRenameTo = new File(newName);
							file.renameTo(fileRenameTo);
							newFileName = "";
						}
						break;
					
					case "DONE":
						System.out.println("+Connection Closed");
						running = false;
						break;
						
					case "RETR":
						String fileName = UserCommand.substring(5);
                        String fileLocation = Paths.get(currentDirectory, fileName).toString();
						File file = new File(fileLocation);
						Path path = Paths.get(fileLocation);
						if(Files.exists(path)) {
							fileToSendLocation = fileLocation;
							long fileSize = file.length() ;
							System.out.println(fileSize);
						}
						else {
							System.out.println("-File doesn't exist");
						}
						break;
					
					case "SEND":
						  File fileToSend = new File(fileToSendLocation);
					      byte[] mybytearray = new byte[(int) fileToSend.length()];
					      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
					      bis.read(mybytearray, 0, mybytearray.length);
					      os.write(mybytearray, 0, mybytearray.length);
					      os.flush();	
					      
					      break;
				}
			}
				
		}catch(Exception ioException) {
			System.out.println("server ERROR");
			ioException.printStackTrace();			
		}
	}

}
