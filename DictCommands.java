import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;

//testing

public class DictCommands{

	public static void define(Socket socket, String word){
		System.out.println("define: " + word);
	}

	public static void match(Socket socket, String word){
		System.out.println("match: " + word);
	}

	public static void prefixMatch(Socket socket, String word){
		System.out.println("prefixmatch: " + word);
	};

	public static void close(Socket socket){
		System.out.println("close");
	}
}