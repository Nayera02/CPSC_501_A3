import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.FileWriter; 

import java.util.IdentityHashMap;
import java.lang.reflect.*;

public class Serializer {
    private static int uniqueId = 0;

    public static void serializeIt(Object obj){
        Document dom = new Document();

        Class objectClass = obj.getClass();
        System.out.println(objectClass.isPrimitive());

        Element rootElement = new Element("serialized");
        dom.setRootElement(rootElement);

        //System.out.println(dom.getRootElement());

        IdentityHashMap<Object, Integer> uniqueIdentifierMap = new IdentityHashMap<>();
        
        Element objElement = new Element("Object");
        uniqueIdentifierMap.put(objElement, uniqueId);

        objElement.setAttribute("class", objectClass.getName());
        objElement.setAttribute("id", uniqueIdentifierMap.get(objElement).toString() );

        uniqueId++;

        rootElement.addContent(objElement);


      



        for(Field f : objectClass.getFields()){
            try{
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
                    //fieldRef.setText(uniqueIdentifierMap.get(value).toString());
                    fieldElement.addContent(fieldRef);
                }

            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        // Seventh - Print out the document using XMLOutputter class
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = xmlOutputter.outputString(dom);
        System.out.println(xmlString);
        
    }


    public static void main(String[] args){
        Dog doggy = new Dog();
        doggy.setName("Clifford");
        doggy.setType("Wolf");
       // int [] arr = {0,1,2};
        serializeIt(doggy);
    }
}
