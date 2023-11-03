import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
public class Sender {

    public static void main(String[] args) {
        Object obj = objectCreator();
        Document dom = Serializer.serialize(obj);
        sendDocument(dom);
    }

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

    public static Object objectCreator(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome! Choose an Object: Simple, PrimitiveArray, Complex, ObjectReferenceArray, Collection:");
        String objectType = scanner.nextLine();

        try{

            if(objectType.equals("Simple")){
                
                System.out.println("An instance of class Address will be created.\nEnter street number.");
                int street = scanner.nextInt();
                System.out.println("Enter city code");
                int city = scanner.nextInt();
                Address address = new Address(street, city);

                return address;
            
            }else if(objectType.equals("PrimitiveArray")){

                System.out.println("An array of type int and length 3 will be created.");

                int[] myArray = new int[3];
                for (int i = 0; i < 3; i++) {
                    System.out.println("Enter element " + (i + 1) + ": ");
                    myArray[i] = scanner.nextInt();
                }

                return myArray;

            }else if(objectType.equals("Complex")){
                System.out.println("An instance of class Person will be created.\nEnter person's id.");
                int id = scanner.nextInt();

                System.out.println("Enter the person's street number.");
                int street = scanner.nextInt();
                System.out.println("Enter the person's city code");
                int city = scanner.nextInt();
                Address address = new Address(street, city);

                Person person = new Person(id, address);

                return person;

            }else if(objectType.equals("ObjectReferenceArray")){
                System.out.println("An array of type Address and length 2 will be created.");
                Address [] addresses = new Address[2];

                for (int i = 0; i < 2; i++) {
                    System.out.println("Enter street number for address "+ (i+1) +":");
                    int street = scanner.nextInt();
                    System.out.println("Enter city code for address " + (i+1) + ":");
                    int city = scanner.nextInt();
                    addresses[i] = new Address(street, city);
                }

                return addresses;

            }else if(objectType.equals("Collection")){
                System.out.println("An arraylist of type Address will be created.");
                ArrayList<Address> addressList = new ArrayList<>();

                for (int i = 0; i < 2; i++) {
                    System.out.println("Enter street number for address "+ (i+1) +":");
                    int street = scanner.nextInt();
                    System.out.println("Enter city code for address " + (i+1) + ":");
                    int city = scanner.nextInt();
                    addressList.add(new Address(street, city));

                    
                }

                return addressList;

            }else{
                System.out.println("Invalid Object Type.");
               
                System.exit(0);
                return null;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    
}
