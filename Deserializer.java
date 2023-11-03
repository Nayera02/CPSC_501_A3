import java.util.IdentityHashMap;

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

import java.util.HashMap;

public class Deserializer {
    public static HashMap<Integer, Object> objectMap = new HashMap<>();


    public static Object deserialize(org.jdom2.Document document){
       Object obj = new Object();
       //HashMap<Integer, Object> objectMap = new HashMap<>();

       // Load the dom into a nicer format and use it
       XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
       String xmlString = xmlOutputter.outputString(document);

       Element rootElement = document.getRootElement();

       List<Element> xmlObjects = rootElement.getChildren("object");
        try{
            //for(Element elementObject: xmlObjects){
                Element elementObject = xmlObjects.get(0);
                    Class classObject = Class.forName(elementObject.getAttributeValue("class"));
                    //System.out.println("class object = " + classObject.getName());
                    if(classObject.isArray()){
                        Class componentTypeClass = classObject.getComponentType();
                        obj = Array.newInstance(componentTypeClass, Integer.parseInt(elementObject.getAttributeValue("length")) );

                        String componentType = classObject.getComponentType().getName();

                        objectMap.put(Integer.parseInt(elementObject.getAttributeValue("id")), obj);

                        if(classObject.getComponentType().isPrimitive()){
                            List<Element> valueElements = elementObject.getChildren("value");
                            int i =0;
                           
                            for(Element valueElement: valueElements ){
                                //System.out.println("value of element in array= " + Integer.parseInt(valueElement.getText()));
                                if (classObject.getComponentType() == int.class) {
                                    Array.set(obj, i, Integer.parseInt(valueElement.getText()));
                                    i++;
                                }  
                            }  
                        }else{
                            List<Element> refElements = elementObject.getChildren("reference");
                            int i =0;
                            for(Element refElement: refElements){
                               int id = Integer.valueOf(refElement.getText());
                               
                                Object refObj = createRefObject(id, xmlObjects);

                                objectMap.put(id, refObj);

                                Array.set(obj, i, refObj);
                                
                                i++;
                            }
                        }
                    }else if(classObject.getName().equals("java.util.ArrayList") ){
                        //System.out.println("workssssssssssssssss\n\n\n");
                        obj = new ArrayList<>();
                        objectMap.put(Integer.parseInt(elementObject.getAttributeValue("id")), obj);

                        List<Element> refElements = elementObject.getChildren("reference");
                        int i =0;
                        for(Element refElement: refElements){
                            int id = Integer.valueOf(refElement.getText());
                            
                            Object refObj = createRefObject(id, xmlObjects);

                            objectMap.put(id, refObj);

                            ((ArrayList) obj).add(refObj);
                            
                            i++;
                        }

                                
                    }else{
                        //System.out.println("its not an array");
                        Constructor constructor = classObject.getDeclaredConstructor(null);
                        constructor.setAccessible(true);
                        obj = constructor.newInstance();

                        objectMap.put(Integer.parseInt(elementObject.getAttributeValue("id")), obj);

                        List<Element> fieldElements = elementObject.getChildren("field");

                        for(Element fieldElement:fieldElements){
                            String declaringClass = fieldElement.getAttributeValue("declaringClass");
                            Class fieldClass = Class.forName(declaringClass);
                            Field f = fieldClass.getDeclaredField(fieldElement.getAttributeValue("name"));
                            //System.out.println(f.getType().getName().equals("java.lang.String"));
                            //System.out.println("bbbbbbbbbbbb");
                            if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")){
                                //System.out.println("notarray");
                                Element valueElement = fieldElement.getChild("value");
                                f.setAccessible(true);
                               // System.out.println(valueElement.getText());
                                if (f.getType() == int.class) {
                                    f.setInt(obj, Integer.parseInt(valueElement.getText()));
                                } else if (f.getType() == float.class) {
                                    f.setFloat(obj, Float.parseFloat(valueElement.getText()));
                                } else if (f.getType() == double.class) {
                                    f.setDouble(obj, Double.parseDouble(valueElement.getText()));
                                } else if (f.getType() == long.class) {
                                    f.setLong(obj, Long.parseLong(valueElement.getText()));
                                } else if (f.getType() == short.class) {
                                    f.setShort(obj, Short.parseShort(valueElement.getText()));
                                } else if (f.getType() == byte.class) {
                                    f.setByte(obj, Byte.parseByte(valueElement.getText()));
                                } else if (f.getType() == char.class) {
                                    String text = valueElement.getText();
                                    if (text != null && text.length() > 0) {
                                        f.setChar(obj, text.charAt(0));
                                    }
                                } else if (f.getType() == boolean.class) {
                                    f.setBoolean(obj, Boolean.parseBoolean(valueElement.getText()));
                                } else if (f.getType().getName().equals("java.lang.String")) {
                                    
                                    f.set(obj, valueElement.getText());
                                }

                            }else{
                                //object with object references
                                if(f.getType().isArray()){
                                    System.out.println("arrayyy");
                                }else{
                                    //System.out.println("not array");
                                    Element refElement = fieldElement.getChild("reference");
                                    int id = Integer.valueOf(refElement.getText());
                                    //System.out.println("id: " + id);
                                    if (!objectMap.containsKey(id)){
                                        Object refObj = createRefObject(id, xmlObjects);

                                        objectMap.put(id, refObj);
                                        f.setAccessible(true);
                                        f.set(obj, refObj);
                                        
                                    }

                                }
                            }
                        }
                    }
                //}     
        }catch(Exception e){ System.out.println(e.getMessage());}
        
        return obj;
    }

   public static Object createRefObject(int id, List<Element> xmlObjects){
    Object refObj = new Object();
    try{
        for(Element element: xmlObjects){
            int attributeValue  = Integer.valueOf(element.getAttributeValue("id"));
            if (attributeValue==id){
                //System.out.println("works");
                Class refClass = Class.forName(element.getAttributeValue("class"));
                //System.out.println("referenced class:" + refClass.getName());

                if(refClass.isArray()){
                    System.out.println("its an array");
                    
                }else{
                    //System.out.println("its not an array");

                    Constructor constructor = refClass.getDeclaredConstructor(null);
                    constructor.setAccessible(true);
                    refObj = constructor.newInstance();

                    objectMap.put(Integer.parseInt(element.getAttributeValue("id")), refObj);

                    List<Element> fieldElements = element.getChildren("field");

                    for(Element fieldElement:fieldElements){
                        String declaringClass = fieldElement.getAttributeValue("declaringClass");
                        Class fieldClass = Class.forName(declaringClass);
                        Field f = fieldClass.getDeclaredField(fieldElement.getAttributeValue("name"));
                        //System.out.println(f.getType().getName().equals("java.lang.String"));
                        //System.out.println("bbbbbbbbbbbb");
                        if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")){
                            //System.out.println("notarray");
                            Element valueElement = fieldElement.getChild("value");
                            f.setAccessible(true);
                            // System.out.println(valueElement.getText());
                            if (f.getType() == int.class) {
                                f.setInt(refObj, Integer.parseInt(valueElement.getText()));
                            } else if (f.getType() == float.class) {
                                f.setFloat(refObj, Float.parseFloat(valueElement.getText()));
                            } else if (f.getType() == double.class) {
                                f.setDouble(refObj, Double.parseDouble(valueElement.getText()));
                            } else if (f.getType() == long.class) {
                                f.setLong(refObj, Long.parseLong(valueElement.getText()));
                            } else if (f.getType() == short.class) {
                                f.setShort(refObj, Short.parseShort(valueElement.getText()));
                            } else if (f.getType() == byte.class) {
                                f.setByte(refObj, Byte.parseByte(valueElement.getText()));
                            } else if (f.getType() == char.class) {
                                String text = valueElement.getText();
                                if (text != null && text.length() > 0) {
                                    f.setChar(refObj, text.charAt(0));
                                }
                            } else if (f.getType() == boolean.class) {
                                f.setBoolean(refObj, Boolean.parseBoolean(valueElement.getText()));
                            } else if (f.getType().getName().equals("java.lang.String")) {
                                
                                f.set(refObj, valueElement.getText());
                            }

                        }else{
                            //object with object references
                            if(f.getType().isArray()){
                                System.out.println("arrayyy");
                            }else{
                                System.out.println("not array");
                                Element refElement = fieldElement.getChild("reference");
                                int idd = Integer.valueOf(refElement.getText());
                                System.out.println("id: " + id);
                                if (!objectMap.containsKey(idd)){
                                    createRefObject(idd, xmlObjects);
                                    
                                }

                            }
                        }

                    }
                    /*Field newField = obj.getClass().getDeclaredField(refObj.getClass().getName());
                    newField.setAccessible(true); 
                    newField.set(obj, refObj);*/

            
            }



            

        }
        }
        return refObj;
    }catch(Exception e){
        System.out.println(e.getMessage());
        return null;
    }
}




     public static void main(String[] args){
        //primitive array
        int [] arr = {11, 19};

        //simple
        Address address = new Address(1, 2);
        //object with another object reference
        Person person = new Person(10, address);

        //array of object references
        Address [] addresses = new Address[2];
        addresses[0] = new Address(1, 2);
        addresses[1] = new Address(3, 4);

        //collection
        ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(new Address(1, 2));
        addressList.add(new Address(3, 4));



       Serializer serializer = new Serializer();
        Document dom = Serializer.serialize(address);



        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = xmlOutputter.outputString(dom);
        System.out.println(xmlString);

        Object test = deserialize(dom);

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
                        System.out.println("Reference value: " + f.get(test).getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(f.get(test))));
                    }
                }
            }catch(Exception e){System.out.println(e.getMessage());}
        }

     }
}
