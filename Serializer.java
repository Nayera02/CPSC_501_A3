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
///////////////////////////////////////////////////////////////////////////

public class Serializer {
    private static int uniqueId = 0;
    private static IdentityHashMap<Object, Integer> uniqueIdentifierMap = new IdentityHashMap<>();
    public static org.jdom2.Document serialize(Object obj){
        Document dom = new Document();

        Class objectClass = obj.getClass();

        Element rootElement = new Element("serialized");
        dom.setRootElement(rootElement);

        //IdentityHashMap<Object, Integer> uniqueIdentifierMap = new IdentityHashMap<>();
        
        Element objElement = new Element("object");
        //uniqueIdentifierMap.put(objectClass, uniqueId);

        objElement.setAttribute("class", objectClass.getName());
       

        //uniqueId++;

        rootElement.addContent(objElement);

      
        ////////////////////////////////////////////////////////////////////////////////////////////////

        if (objectClass.isArray()){
           
            objElement.setAttribute("length", String.valueOf(Array.getLength(obj)));
            
            if(objectClass.getComponentType().isPrimitive() ){
                for (int i = 0; i < Array.getLength(obj); i++) {
                    Element arrayValue = new Element("value");
                    Object item = Array.get(obj, i);
                    //arrayValue.addContent(String.valueOf(item));
                    arrayValue.setText(item.toString());
                    objElement.addContent(arrayValue);
                }
            }else{
                for (int i = 0; i < Array.getLength(obj); i++) {
                    Element arrayRef = new Element("reference");

                    if(!uniqueIdentifierMap.containsKey(Array.get(obj, i))){
                            //serializeIt(Array.get(obj, i));
                            serializeRef(Array.get(obj, i), rootElement);
                        }
                        
                    arrayRef.setText(String.valueOf(uniqueIdentifierMap.get(Array.get(obj, i))));
                    objElement.addContent(arrayRef);

                }
            }
        }else if(obj instanceof ArrayList){
            //collection serializiation
            ArrayList<?> arrayList = (ArrayList<?>) obj;
            // Collection serialization
           // System.out.println("Collection:" + obj.getClass().getName());
            //System.out.println(String.valueOf(arrayList.size()));
            objElement.setAttribute("length", String.valueOf(arrayList.size()));

            for(int i = 0; i< arrayList.size(); i++){
                 Element arrayRef = new Element("reference");

                if(!uniqueIdentifierMap.containsKey(arrayList.get(i))){
                        //serializeIt(Array.get(obj, i));
                        serializeRef(arrayList.get(i), rootElement);
                    }
                    
                arrayRef.setText(String.valueOf(uniqueIdentifierMap.get(arrayList.get(i))));
                objElement.addContent(arrayRef);
            }


        }else{
            for(Field f : objectClass.getDeclaredFields()){
                try{
                    f.setAccessible(true);
                    Object value = f.get(obj);
                    String name = f.getName();

                    Element fieldElement = new Element("field");
                    fieldElement.setAttribute("name", name);
                    fieldElement.setAttribute("declaringClass", f.getDeclaringClass().getName());
                    objElement.addContent(fieldElement);
     
                    if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")  ){
                        
                        Element fieldValue = new Element("value");
                        fieldValue.setText(value.toString());
                        fieldElement.addContent(fieldValue);
                    }else{

                        Element fieldRef = new Element("reference");
                        // check if that class has been serialized yet
                        if(!uniqueIdentifierMap.containsKey(f.get(obj))){
                            //serializeIt(f.get(obj));
                            serializeRef(f.get(obj), rootElement);
                            
                            ////////////////////////////////////////////////////////////////////////////trial

                            
                        }
                        
                        fieldRef.setText(String.valueOf(uniqueIdentifierMap.get(f.get(obj))));
                        fieldElement.addContent(fieldRef);
                    }

                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        
        uniqueIdentifierMap.put(obj, uniqueId);
        uniqueId++;
         objElement.setAttribute("id", String.valueOf(uniqueIdentifierMap.get(obj)) );
       
        return dom;
    }

    public static void serializeRef(Object refObj, Element rootElement){
        Element refElement = new Element("object");
        Class refClass = refObj.getClass();

        uniqueIdentifierMap.put(refObj, uniqueId);

        refElement.setAttribute("class", refClass.getName());
        refElement.setAttribute("id", String.valueOf(uniqueIdentifierMap.get(refObj)) );

        uniqueId++;

        rootElement.addContent(refElement);

         
        if (refClass.isArray()){
           
            refElement.setAttribute("length", String.valueOf(Array.getLength(refObj)));
            
            if(refClass.getComponentType().isPrimitive() || refClass.getComponentType().getName().equals("java.lang.String")){
                for (int i = 0; i < Array.getLength(refObj); i++) {
                    Element arrayValue = new Element("value");
                    arrayValue.setText(String.valueOf(Array.get(refObj, i)));
                    refElement.addContent(arrayValue);
                }
            }else{
                for (int i = 0; i < Array.getLength(refObj); i++) {
                    Element arrayRef = new Element("reference");

                    if(!uniqueIdentifierMap.containsKey(Array.get(refObj, i))){
                            //serializeIt(Array.get(refObj, i));
                            System.out.println("works");
                        }
                        
                    arrayRef.setText(String.valueOf(uniqueIdentifierMap.get(Array.get(refObj, i))));
                    refElement.addContent(arrayRef);





                    //fieldRef.setText(identityHashMap.get().toString());
                }
            }
        }else{
            for(Field f : refClass.getDeclaredFields()){
                try{
                    f.setAccessible(true);
                    Object value = f.get(refObj);
                    String name = f.getName();

                    Element fieldElement = new Element("field");
                    fieldElement.setAttribute("name", name);
                    fieldElement.setAttribute("declaringClass", f.getDeclaringClass().getName());
                    refElement.addContent(fieldElement);
     
                    if(f.getType().isPrimitive() || f.getType().getName().equals("java.lang.String")  ){
                        
                        Element fieldValue = new Element("value");
                        fieldValue.setText(value.toString());
                        fieldElement.addContent(fieldValue);
                    }else{

                        Element fieldRef = new Element("reference");
                        // check if that class has been serialized yet
                        if(!uniqueIdentifierMap.containsKey(f.get(refObj))){
                            //serializeIt(f.get(refObj));
                            serializeRef(f.get(refObj), rootElement);

                            ////////////////////////////////////////////////////////////////////////////trial

                            
                        }
                        
                        fieldRef.setText(String.valueOf(uniqueIdentifierMap.get(f.get(refObj))));
                        fieldElement.addContent(fieldRef);
                    }

                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }

        }


    }
     
}
