import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.lang.reflect.*;
public class Receiver {
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
            Object test = Deserializer.deserialize(dom);
            visualizer(test);
			//

            // Close the output streams and socket
			clientSocket.close();
            serverSocket.close();
            in.close();
			
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void visualizer(Object test){
        System.out.println("Object: " + test.getClass().getName());

        if(test.getClass().isArray()){
            
            //System.out.println("test is an array");
            String componentType = test.getClass().getComponentType().getName();
            System.out.println("Array of component type: " + componentType);
            int length = Array.getLength(test);
            System.out.println("Length: " + length);
            if(test.getClass().getComponentType().isPrimitive()){
                 System.out.println("Array Content:");
                for(int i = 0; i< length; i++){
                System.out.println(i + ": " + Array.get(test, i));
                }
                
            }else{
                for(int i = 0; i< length; i++){
                    System.out.println("Array Element: " + i);
                    Object arrayElement = Array.get(test, i);

                    
                    Field[] fields = arrayElement.getClass().getDeclaredFields();
                    for(Field f: fields){
                    try{
                            f.setAccessible(true);
                            // Field name
                            System.out.println("Field: " + f.getName());

                            // Type
                            System.out.println("Type: " + f.getType().getName());

                            // Current value
                            if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                                System.out.println("Value: " + f.get(arrayElement));
                            } else {
                                
                                if (f.get(arrayElement) == null) {
                                    System.out.println("Value: null");
                                    
                                } else {
                                    Object nestedObj = f.get(arrayElement);

                                    Field [] addf = nestedObj.getClass().getDeclaredFields();
                                    System.out.println("variables in object reference:");
                                    for(Field ff: addf){
                                        try{
                                        ff.setAccessible(true);
                                        System.out.println(ff.getName()+ ": "+ ff.get(nestedObj));
                                        
                                            
                                        }catch(Exception e){}
                                    }
                                }
                            }
                        }catch(Exception e){System.out.println(e.getMessage());}
                        System.out.println("");
                    }
                    System.out.println("");
                }
            } 
        }else if(test instanceof ArrayList){
            ArrayList<Object> arrayList = (ArrayList<Object>) test;

            int length = arrayList.size();
            System.out.println("ArrayList length: " + length);

            for(int i = 0; i< length; i++){
                System.out.println("ArrayList Element: " + i);
                Object arrayElement = arrayList.get( i);

                
                Field[] fields = arrayElement.getClass().getDeclaredFields();
                for(Field f: fields){
                try{
                        f.setAccessible(true);
                        // Field name
                        System.out.println("Field: " + f.getName());

                        // Type
                        System.out.println("Type: " + f.getType().getName());

                        // Current value
                        if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                            System.out.println("Value: " + f.get(arrayElement));
                        } else {
                            
                            if (f.get(arrayElement) == null) {
                                System.out.println("Value: null");
                                
                            } else {
                                Object nestedObj = f.get(arrayElement);

                                Field [] addf = nestedObj.getClass().getDeclaredFields();
                                System.out.println("variables in object reference:");
                                for(Field ff: addf){
                                    try{
                                    ff.setAccessible(true);
                                    System.out.println(ff.getName()+ ": "+ ff.get(nestedObj));
                                    
                                        
                                    }catch(Exception e){}
                                }
                            }
                        }
                    }catch(Exception e){System.out.println(e.getMessage());}
                    System.out.println("");
                }
                System.out.println("");
            }


        }else{

            Field[] fields = test.getClass().getDeclaredFields();
            for(Field f: fields){
            try{
                    f.setAccessible(true);
                    // Field name
                    System.out.println("Field: " + f.getName());

                    // Type
                    System.out.println("Type: " + f.getType().getName());

                    // Current value
                    if (f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")) {
                        System.out.println("Value: " + f.get(test));
                    } else {
                        
                        if (f.get(test) == null) {
                            System.out.println("Value: null");
                            
                        } else {
                            Object nestedObj = f.get(test);

                            Field [] addf = nestedObj.getClass().getDeclaredFields();
                            System.out.println("variables in object reference:");
                            for(Field ff: addf){
                                try{
                                ff.setAccessible(true);
                                System.out.println(ff.getName()+ ": "+ ff.get(nestedObj));
                                
                                    
                                }catch(Exception e){}
                            }
                        }
                    }
                }catch(Exception e){System.out.println(e.getMessage());}
                System.out.println("");
            }
        }
    }

}
