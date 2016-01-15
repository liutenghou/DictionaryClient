import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;

//testing

public class DictCommands{

	public static void open(String server, int port){

	}

	public static void define(String word){
		System.out.println("define: " + word);
	}

	public static void match(String word){
		System.out.println("match: " + word);
	}

	public static void prefixMatch(String word){
		System.out.println("prefixmatch: " + word);
	};

	public static void close(){
		System.out.println("close");
	}
}