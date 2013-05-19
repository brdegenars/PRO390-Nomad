import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ContactServer{
	
	public static final int PORT = 8888;
	
	public static void main(String[] args) {
		
		String xml = null;
		
		try{
			Scanner scan = new Scanner(new File("MyContacts.xml"));
			scan.useDelimiter("$");
			xml = scan.next();
		} catch (FileNotFoundException e){
			e.printStackTrace();
			return;
		}
		
		ServerSocket server = null;
		Socket client = null;
		try{
			server = new ServerSocket(PORT);
			server.setSoTimeout(60 * 1000); //60 seconds
			System.out.println("You have one minute exaclty to connect.");
			while (true){
				
				client = server.accept();
				Writer write = new OutputStreamWriter(client.getOutputStream());
				write.write(xml);
				write.flush();
				write.close();
				client.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}finally{
			if (client != null && !client.isClosed()) try{
				client.close();
			} catch (IOException e){
				e.printStackTrace();
			}
			if (server != null) try{
				server.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}	
}
