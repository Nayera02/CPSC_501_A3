import java.io.*;
import java.net.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
public class MyServer {
    public static void main(String[] args) {
		receiveDom();
	}
	public static void receiveDom(){
        try {
            // Create a server socket and bind it to port 8080
            ServerSocket serverSocket = new ServerSocket(8080);

            System.out.println("Server is running. Waiting for connections...");

            // Wait for a client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create input stream for communication with clientsocket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Rebuild the XML using saxBuilder
            SAXBuilder saxBuilder = new SAXBuilder();
            Document dom = saxBuilder.build(in);

            // Load the JDOM into a nicer format and now implement your saxBuilder
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            String xmlString = xmlOutputter.outputString(dom);
            System.out.println(xmlString);
			
			//
			// Now Deserialize here!!
			//

            // Close the output streams and socket
			clientSocket.close();
            serverSocket.close();
            in.close();
			
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
