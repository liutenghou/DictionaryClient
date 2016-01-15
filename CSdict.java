
import java.lang.System;
import java.util.Scanner;
import java.io.IOException;

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
    
    public static void main(String [] args)
    {
		byte cmdString[] = new byte[MAX_LEN];
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
		    System.err.println("Something happened: "+ e.getMessage());
		    inputHandling(scan);
		}
	    scan.close();	
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
			    
			// Start processing the command here.
			} else if(cmd.equals("#")){
				continue;
				
    		} else if(cmd.equals("open")){
				//TODO: handle command: open SERVER PORT
				System.out.println("Inside Open: " + cmd);
				String portNumber = "2628";
				//if missing port, use default 2628
				if(inputStringArray.length == 3){
					portNumber = inputStringArray[2];
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
				//TODO: handle command
				break;
			} else{
				System.out.println("900 Invalid command.");
			}
		}	
    	
    }

    
}
