
import java.lang.System;
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
    public static void main(String [] args)
    {
		byte cmdString[] = new byte[MAX_LEN];
		
		if (args.length == PERMITTED_ARGUMENT_COUNT) {
		    debugOn = args[0].equals("-d");
		    if (debugOn) {
			System.out.println("Debugging output enabled");
		    } else {
			System.out.println("997 Invalid command line option - Only -d is allowed");
			return;
	            } 
		} else if (args.length > PERMITTED_ARGUMENT_COUNT) {
		    System.out.println("996 Too many command line options - Only -d is allowed");
		    return;
		}
			
		try {
		    for (int len = 1; len > 0;) {
				System.out.print("csdict> ");
				len = System.in.read(cmdString);
				if (len <= 0) 
				    break;
				// Start processing the command here.
				else {
					// Convert the cmdString byte array to a string and make all lowercase
					String inputString = new String(cmdString);
					inputString = inputString.trim();

					//Make the string case insensitive
					String cmd = inputString.toLowerCase();
					

					if(cmd.startsWith("open")){
						//TODO: handle command: open SERVER PORT
						String[] params = extractParameters("open" , inputString);
						System.out.println(params[0]);
						System.out.println(params[1]);

					} else if(cmd.startsWith("dict")){
						//TODO: handle command
						
					} else if(cmd.startsWith("set")){
						//TODO: handle command: set DICTIONARY
						String[] params = extractParameters("set" , inputString);

					} else if(cmd.startsWith("currdict")){
						//TODO: handle command

					} else if(cmd.startsWith("define")){
						//TODO: handle command: define WORD
						String[] params = extractParameters("define" , inputString);

					} else if(cmd.startsWith("match")){
						//TODO: handle command: match WORD
						String[] params = extractParameters("match" , inputString);

					} else if(cmd.startsWith("prefixmatch")){
						//TODO: handle command: prefixmatch WORD
						String[] params = extractParameters("prefixmatch" , inputString);

					} else if(cmd.startsWith("close")){
						//TODO: handle command

					} else if(cmd.startsWith("quit")){
						//TODO: handle command

					} else{
						System.out.println("900 Invalid command.");
					}
					cmdString = new byte[MAX_LEN];
				}			
		    }
		} catch (IOException exception) {
		    System.err.println("998 Input error while reading commands, terminating.");
		}
    }

    //command: the command that will be removed so we are only left with the params
    //input: the whole input string
    //will always return a String[] of size 2, with the second element being empty if there's
    //only one param
    private static String[] extractParameters(String command, String input){
    	String[] paramArr = new String[2];
    	//remove the command from the input and trim the whitespace
    	String paramStr = input.substring(command.length() , input.length()).trim();
    	int firstSpaceIdx = paramStr.indexOf(" ");

    	if(firstSpaceIdx < 0){
    		paramArr[0] = paramStr;
    	} else{
    		paramArr[0] = paramStr.substring(0,firstSpaceIdx).trim();
    		paramArr[1] = paramStr.substring(firstSpaceIdx + 1 , paramStr.length()).trim();
    	}

    	return paramArr;
    }
}
