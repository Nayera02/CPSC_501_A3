import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.FileWriter; 

import java.util.IdentityHashMap;
import java.lang.reflect.*;

import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.util.List;
import org.jdom2.Attribute;

import java.util.Map;

import java.util.ArrayList;

public class Serializer {
    private static int uniqueId = 0;
    private static IdentityHashMap<Object, Integer> uniqueIdentifierMap = new IdentityHashMap<>();
    public static org.jdom2.Document serializeIt(Object obj){
        Document dom = new Document();

        Class objectClass = obj.getClass();

        Element rootElement = new Element("serialized");
        dom.setRootElement(rootElement);

        //IdentityHashMap<Object, Integer> uniqueIdentifierMap = new IdentityHashMap<>();
        
        Element objElement = new Element("Object");
        uniqueIdentifierMap.put(objectClass, uniqueId);

        objElement.setAttribute("class", objectClass.getName());
        objElement.setAttribute("id", String.valueOf(uniqueIdentifierMap.get(objectClass)) );

        uniqueId++;

        rootElement.addContent(objElement);

      
        
        if (objectClass.isArray()){
           
            objElement.setAttribute("length", String.valueOf(Array.getLength(obj)));
            
            if(objectClass.getComponentType().isPrimitive() || objectClass.getComponentType().getName().equals("java.lang.String")){
                for (int i = 0; i < Array.getLength(obj); i++) {
                    Element arrayValue = new Element("Value");
                    arrayValue.setText(String.valueOf(Array.get(obj, i)));
                    objElement.addContent(arrayValue);
                }
            }else{
                for (int i = 0; i < Array.getLength(obj); i++) {
                    Element arrayRef = new Element("Reference");

                    if(!uniqueIdentifierMap.containsKey(Array.get(obj, i).getClass())){
                            serializeIt(Array.get(obj, i));
                        }
                        
                    arrayRef.setText(String.valueOf(uniqueIdentifierMap.get(Array.get(obj, i).getClass())));
                    objElement.addContent(arrayRef);





                    //fieldRef.setText(identityHashMap.get().toString());
                }
            }


           

        }else{
            for(Field f : objectClass.getDeclaredFields()){
                try{
                    f.setAccessible(true);
                    Object value = f.get(obj);
                    String name = f.getName();

                    Element fieldElement = new Element("Field");
                    fieldElement.setAttribute("name", name);
                    fieldElement.setAttribute("declaringClass", f.getDeclaringClass().getName());
                    objElement.addContent(fieldElement);

                    if(value.getClass().isPrimitive() || value.getClass().getName().equals("java.lang.String")  ){
                        
                        Element fieldValue = new Element("Value");
                        fieldValue.setText(value.toString());
                        fieldElement.addContent(fieldValue);
                    }else{

                        Element fieldRef = new Element("Reference");
                        // check if that class has been serialized yet
                        if(!uniqueIdentifierMap.containsKey(f.get(obj).getClass())){
                            serializeIt(f.get(obj));
                            
                        }
                        
                        fieldRef.setText(String.valueOf(uniqueIdentifierMap.get(f.get(obj).getClass())));
                        fieldElement.addContent(fieldRef);
                    }

                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
       
        return dom;
    }


    public static Object deserialize(org.jdom2.Document document){
        
        Object obj = new Object();
        // Load the dom into a nicer format and use it
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = xmlOutputter.outputString(document);
        System.out.println(xmlString);

        Element rootElement = document.getRootElement();

        List<Element> xmlObjects = rootElement.getChildren("Object");

        for(Element elementObject: xmlObjects){
            System.out.println("Object Details: ");
            //System.out.println("name: " + elementObject.getName());

           
            System.out.println("Object Class: " + elementObject.getAttributeValue("class"));

            List<Element> fields = elementObject.getChildren("Field");

            System.out.println("");

            for(Element field:fields){
                System.out.println("Field name: " + field.getAttributeValue("name"));
                System.out.println("Declaring class: " + field.getAttributeValue("declaringClass"));

                System.out.println("");
			}
            
        }

        return obj;
    }

   
    public static void main(String[] args){
        /*Dog doggy = new Dog();
        doggy.setName("Clifford");
        doggy.setType("Wolf");*/


        //primitive array
        int [] arr = {11, 19};

        //simple
        Address address = new Address("ss", "cc");
        //object with another object reference
        Person person = new Person("p1", address);

        //array of object references
        Address [] addresses = new Address[2];
        addresses[0] = new Address("s1", "c1");
        addresses[1] = new Address("s2", "c2");

        //collection
        ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(new Address("s1", "c1"));
        addressList.add(new Address("s2", "c2"));



       
        Document dom = serializeIt(addresses);



        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = xmlOutputter.outputString(dom);
        System.out.println(xmlString);

        for (Map.Entry<Object, Integer> entry : uniqueIdentifierMap.entrySet()) {
            Object key = entry.getKey();
            int value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
        }
        //writeXmlToFile(dom);
       // deserialize(dom);
    }
}
