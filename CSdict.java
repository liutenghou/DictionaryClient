
import java.lang.System;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
import java.net.*;

//
// This is an implementation of a simplified version of a command 
// line dictionary client. The program takes no arguments.
//

public class CSdict
{
    static final int MAX_LEN = 255;
    static final int PERMITTED_ARGUMENT_COUNT = 1;
    static Boolean debugOn = false;
    static Boolean quitCommandExecuted = false;
    static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	//default dictionary, search all dictionaries
	static String currentDict = "*";
	
    //error messages
    static String e900 = "900 Invalid command";
    static String e901 = "901 Incorrect number of arguments";
    static String e902 = "902 Invalid argument";
    static String e903 = "903 Supplied command not expected at this time";
    static String e920 = "920 Control connection to xxx on port yyy failed to open";
    static String e925 = "925 Control connection I/O error, closing control connection";
    static String e930 = "930 Dictionary does not exist";
    static String e996 = "996 Too many command line options - Only -d is allowed";
    static String e997 = "997 Invalid command line option - Only -d is allowed";
    static String e998 = "998 Input error while reading commands, terminating";
    static String e999 = "999 Processing error.";

    public static void main(String [] args)
    {
		//don't need this anymore
    	//byte cmdString[] = new byte[MAX_LEN];
		Scanner scan = new Scanner(System.in);
		
		if (args.length == PERMITTED_ARGUMENT_COUNT) {
		    debugOn = args[0].equals("-d");
		    if (debugOn) {
		    	System.out.println("Debugging output enabled");
		    } else {
				System.out.println("997 Invalid command line option - Only -d is allowed");
				scan.close();
				return;
	        } 
		} else if (args.length > PERMITTED_ARGUMENT_COUNT) {
		    System.out.println("996 Too many command line options - Only -d is allowed");
		    scan.close();
		    return;
		}
		
		//read input
		while(!quitCommandExecuted){
			try{
				inputHandling(scan);
			} catch(NoSuchElementException e){
				System.out.println(); //new line
				quitCommandExecuted = true;
				//don't do anything, input is ctrl-d
			} catch(SocketException e){
				System.out.println(e925);
				if(socket != null && !socket.isClosed()){
					socket = null;
			    	out = null;
			    	in = null;
			    	currentDict = "*";
				}
			} catch(IOException e){
				System.out.println(e925);
				if(socket != null && !socket.isClosed()){
					socket = null;
			    	out = null;
			    	in = null;
			    	currentDict = "*";
				}
			} catch (Exception e) {
			    System.out.println(e999 + " " + e.getMessage());
			    //TEST for exception type.name
			    //System.out.println("exception name: " + e.getClass().getName());
			}
		}
		
	    scan.close();	
    }
    
    public static void closeConnection(Socket socket, PrintWriter out, BufferedReader in, String currentDict) throws Exception{
    	if(socket != null && !socket.isClosed()){
			socket.close();
			socket = null;
	    	out = null;
	    	in = null;
	    	currentDict = "*";
		}
    }
    
    public static void inputHandling(Scanner scan) throws Exception{
    	
    	while(true) {
			System.out.print("csdict> ");
			String inputString = scan.nextLine().trim();
			if(inputString == null){ //shouldn't ever be triggered
				System.out.println(e998);
				break;
			}
			//input string to array, delimited by 0 or more spaces or tabs
			String[] inputStringArray = inputString.split("\\s+");
			
			if(inputStringArray.length > 3){
				System.out.println(e901);
				continue;
			}
			
			//first input to cmd, converted to all lower case
			String cmd = inputStringArray[0].toLowerCase().trim();
			
			if (inputString.length() <= 0){ //length of input too short
			    continue;
			} else if(cmd.equals("#")){
				continue;
				
			// Start processing the command here.	
    		} else if(cmd.equals("open")){
    			//error out if control connection already open
    			if(socket != null && !socket.isClosed()){
    				System.out.println(e903);
    				continue;
    			}

				String portNumber = "2628"; //default port
				String domain = null;				
				
				//override default port if port present
				if(inputStringArray.length == 3){
					portNumber = inputStringArray[2];
					domain = inputStringArray[1];		
				//incorrect number of arguments	
				} else if(inputStringArray.length == 2){
					domain = inputStringArray[1];					
				} else if(inputStringArray.length <= 1 || inputStringArray.length > 3){
					System.out.println(e901);
					continue;
				}
				
				try{
					//convert portNumber to int
					int portNumberInt = Integer.valueOf(portNumber);
					
					//start connection to domain and port

					socket = new Socket(domain, portNumberInt);
					out = new PrintWriter(socket.getOutputStream(), true);
	           		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	           		
					//test input for domain and port
	           		displayResponse(in);
				}catch(NoRouteToHostException e){ //domain is not domain
					System.out.println(e902);
				}catch(NumberFormatException e){ //port is not number
					System.out.println(e902); 
				}catch(ConnectException e){
					System.out.println("920 Control connection to " + domain + " on port " + portNumber + " failed to open");
					portNumber = "2628";
					domain = null;
				}catch(IllegalArgumentException e){ //port out of range
					System.out.println(e902); 
				}
				
			} else if(cmd.equals("dict")){
				//error
				if(socket == null || socket.isClosed()){
					System.out.println(e903);
					continue;
				}	
				//check number of arguments
				if(inputStringArray.length != 1){
					System.out.println(e901);
					continue;
				}

				String commandString = "SHOW DB";
				if(debugOn){
					System.out.println("--> " + commandString);
				}
				
				out.println(commandString);
				displayResponse(in);
				
			} else if(cmd.equals("set")){
				if(socket == null || socket.isClosed()){
					System.out.println(e903);
					continue;
				}
				//check number of arguments
				if(inputStringArray.length != 2){
					System.out.println(e901);
					continue;
				}
				currentDict = inputStringArray[1];	
	
			} else if(cmd.equals("currdict")){
				if(socket == null || socket.isClosed()){
					System.out.println(e903);
					continue;
				}
				//check number of arguments
				if(inputStringArray.length != 1){
					System.out.println(e901);
					continue;
				}
				System.out.println(currentDict);

			} else if(cmd.equals("define")){
				//errors
				if(socket == null || socket.isClosed()){
					System.out.println(e903);
					continue;
				}
				//check number of arguments
				if(inputStringArray.length != 2){
					System.out.println(e901);
					continue;
				}
				
				String commandString = "DEFINE " + currentDict + " " + inputStringArray[1];
				if(debugOn){
					System.out.println("--> " + commandString);
				}
				
				out.println(commandString);

				String response = displayResponse(in).substring(0,4);
				
				//no definition, cascade to match
				if(response.matches("^552\\s")){
					System.out.println("**No definition found**");
					response = match("*", inputStringArray[1], true, out, in).substring(0,4);
				}
				//no matches, print no dicts
				if(response.matches("^552\\s")){
					System.out.println("***No dictionaries have a definition for this word***");					
				}						

			} else if(cmd.equals("match")){
				//errors
				if(socket == null || socket.isClosed()){
					System.out.println(e903);
					continue;
				}
				//check number of arguments
				if(inputStringArray.length != 2){
					System.out.println(e901);
					continue;
				}
				String response = match(currentDict, inputStringArray[1], true, out, in).substring(0,4);
				
				if(response.matches("^552\\s")){
					System.out.println("****No matching word(s) found****");					
				}		

			} else if(cmd.equals("prefixmatch")){
				//errors
				if(socket == null || socket.isClosed()){
					System.out.println(e903);
					continue;
				}
				//check number of arguments
				if(inputStringArray.length != 2){
					System.out.println(e901);
					continue;
				}
				
				
				String response = match(currentDict, inputStringArray[1], false, out, in).substring(0,4);

				if(response.matches("^552\\s")){
					System.out.println("*****No prefix matches found*****");					
				}	

			} else if(cmd.equals("close")){
				//errors
				if(socket == null || socket.isClosed()){
					System.out.println(e903);
					continue;
				}
				//check number of arguments
				if(inputStringArray.length != 1){
					System.out.println(e901);
					continue;
				}
				
				closeConnection(socket, out, in, currentDict);
				currentDict = "*";
			} else if(cmd.equals("quit")){
				//check number of arguments
				if(inputStringArray.length != 1){
					System.out.println(e901);
					continue;
				}
				quitCommandExecuted = true;
				closeConnection(socket, out, in, currentDict);
				break;
			} else{
				System.out.println(e900);
			}
		}	
    	
    }

    public static String displayResponse(BufferedReader in) throws Exception {
    	String displayString = null;

    	while((displayString = in.readLine()) != null){

			if(displayString.length() > 4){
				String responseMessage = displayString.substring(0,4);
				//responses that start with a 2,4 or 5 are completion responses
				if(responseMessage.matches("^[245]\\d\\d\\s") && !responseMessage.matches("^552 ") && !responseMessage.matches("^550 ")){ 
					if(debugOn){
						System.out.println("<-- " + displayString);
					}
					break;
				} else if(responseMessage.matches("^1\\d\\d\\s")){
					if(debugOn){
						System.out.println("<-- " + displayString);
					}
					displayString = displayString.replaceFirst("^151\\s\"[A-Za-z]+\"\\s","@ ");
				} else if(responseMessage.matches("^550 ")){ //invalid dictionary
					if(debugOn){
						System.out.println(displayString);
					}
					System.out.println(e930);
					break;
				}
				else if(responseMessage.matches("^552 ")){ //word not found
					break;
				}
			}

			System.out.println(displayString);
		}

		return displayString;
	}

	private static String match(String dictionary, String word, Boolean isExact, PrintWriter out, BufferedReader in) throws Exception {
		String method = "exact";
		if(!isExact){
			method = "prefix";
		}

		String commandString = "MATCH " + dictionary + " " + method + " " + word;

		if(debugOn){
			System.out.println("--> " + commandString);
		}
		out.println(commandString);

		return displayResponse(in);
	}

    
}
