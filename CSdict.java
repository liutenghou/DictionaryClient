
import java.lang.System;
import java.util.Scanner;

//
// This is an implementation of a simplified version of a command 
// line dictionary client. The program takes no arguments.
//

public class CSdict
{
    static final int MAX_LEN = 255;
    static final int PERMITTED_ARGUMENT_COUNT = 1;
    static Boolean debugOn = false;
    
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
    static String e999 = "999 Processing error. yyyy";

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
		try{
			inputHandling(scan);
		} catch (Exception e) {
		    System.err.println(e999 + ": " + e.getMessage());
		    inputHandling(scan);
		}
	    scan.close();	
    }
    
    public static boolean isNumber(String n){
    	if(n.matches("[0-9]+")){
			return true;
		}else{
			return false;
		}
    }
    
    public static boolean isDomain(String n){
    	if(n.matches("^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\."
    		+"([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
    		+"\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
    		+"\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$")
    		||
    		n.matches("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$")
    		){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static void inputHandling(Scanner scan){
    	while(true) {
			System.out.print("csdict> ");
			String inputString = scan.nextLine();
			inputString = inputString.trim();
			
			System.out.println("You typed: "+inputString); //TEST
			
			//input string to array, delimited by 0 or more spaces or tabs
			String[] inputStringArray = inputString.split("[ *\t*]");
			if(inputStringArray.length > 3){
				System.out.println(e901);
				continue;
			}
			
			//TEST: input is read correctly
			for(String x: inputStringArray){
				System.out.println(x);
			}
			
			//first input to cmd, converted to all lower case
			String cmd = inputStringArray[0].toLowerCase();
			
			
			if (inputString.length() <= 0){ //length of input too short
			    continue;
			} else if(cmd.equals("#")){
				continue;
				
			// Start processing the command here.	
    		} else if(cmd.equals("open")){
				//TODO: handle command: open SERVER PORT
    			//error out if control connection already open
    			
				System.out.println("Inside Open: " + cmd);
				String portNumber = "2628"; //default port
				String domain = null;
				
				
				//override default port if port present
				if(inputStringArray.length == 3){
					portNumber = inputStringArray[2];
					domain = inputStringArray[1];
					//check that the port number is a number, error out if not
					if(!isNumber(portNumber)){
						System.out.println(e902);
						continue;
					}
					//check that the domain is formatted correctly
					if(!isDomain(domain)){
						System.out.println(e902);
						continue;
					}
					
				//test input for domain and port
				System.out.println("domain: " + domain + " port: " + portNumber);
				//incorrect number of arguments	
				}else if(inputStringArray.length <= 1 || inputStringArray.length > 3){
					System.out.println(e901);
					continue;
				}
				

			} else if(cmd.equals("dict")){
				//TODO: handle command
				
			} else if(cmd.equals("set")){
				//TODO: handle command: set DICTIONARY
				

			} else if(cmd.equals("currdict")){
				//TODO: handle command

			} else if(cmd.equals("define")){
				//TODO: handle command: define WORD
			

			} else if(cmd.equals("match")){
				//TODO: handle command: match WORD
				

			} else if(cmd.equals("prefixmatch")){
				//TODO: handle command: prefixmatch WORD
			

			} else if(cmd.equals("close")){
				//TODO: handle command

			} else if(cmd.equals("quit")){
				System.out.println("Thanks for visiting, come back soon!");
				break;
			} else{
				System.out.println(e900);
			}
		}	
    	
    }

    
}
