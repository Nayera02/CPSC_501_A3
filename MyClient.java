import java.io.*;
import java.net.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
public class MyClient {
	
	public static void sendDocument(Document dom){
        try {
            // Create a socket and connect to the server on the server address (localhost) and port 8080
            Socket socket = new Socket("localhost", 8080);
            System.out.println("Connected to the server.");    // send a message to show that you connected successfully

            // Create output stream for communication using the BufferedOutputStream
            OutputStream outputStream = socket.getOutputStream();
			BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream);

			// prepare output 
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			String xmlString = xmlOutputter.outputString(dom);
			
			// Stream we will use to output to the server
			ByteArrayOutputStream byteoutStream = new ByteArrayOutputStream();

			// set the XMLOutputter object to send output through our stream
       	    xmlOutputter.output(dom, byteoutStream);
			
			// all bytes should be fed into a byteList so it can be written
            byte[] byteList = byteoutStream.toByteArray();

            // Send the JDOM to the server
            bufferedStream.write(byteList);
			
			// flush the stream to get everything out of there, then close
            bufferedStream.flush();
			bufferedStream.close();	
			byteoutStream.close();
            socket.close();
			
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	    public static void main(String[] args) {
            Dog doggy = new Dog();
            doggy.setName("Clifford");
            doggy.setType("Wolf");
            String [] arr = {"12","13"};
            Document dom = Serializer.serializeIt(doggy);
		    sendDocument(dom);
	}
}
