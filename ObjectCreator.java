import java.util.ArrayList;
import java.util.Scanner;
import java.lang.reflect.*;

import java.util.ArrayList;

public class ObjectCreator {
    



public static void main(String[] args){
    

    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome! Choose an Object: Simple, PrimitiveArray, Complex, ObjectReferenceArray, Collection:");
    String objectType = scanner.nextLine();

    try{

        if(objectType.equals("Simple")){
            
            System.out.println("An instance of class Address will be created.\nEnter street name.");
            String street = scanner.nextLine();
            System.out.println("Enter city name");
            String city = scanner.nextLine();
            Address address = new Address(street, city);
           
        }else if(objectType.equals("PrimitiveArray")){

            System.out.println("An array of type int and length 3 will be created.");

            int[] myArray = new int[3];
            for (int i = 0; i < 3; i++) {
                System.out.println("Enter element " + (i + 1) + ": ");
                myArray[i] = scanner.nextInt();
            }

        }else if(objectType.equals("Complex")){
            System.out.println("An instance of class Person will be created.\nEnter person's name.");
            String name = scanner.nextLine();

            System.out.println("Enter the person's street name.");
            String street = scanner.nextLine();
            System.out.println("Enter the person's city name");
            String city = scanner.nextLine();
            Address address = new Address(street, city);

            Person person = new Person(name, address);


        }else if(objectType.equals("ObjectReferenceArray")){
            System.out.println("An array of type Address and length 2 will be created.");
            Address [] addresses = new Address[2];

            for (int i = 0; i < 2; i++) {
                System.out.println("Enter street name for address "+ (i+1) +":");
                String street = scanner.nextLine();
                System.out.println("Enter city name for address " + (i+1) + ":");
                String city = scanner.nextLine();
               addresses[i] = new Address(street, city);
            }


        }else if(objectType.equals("Collection")){
            System.out.println("An arraylist of type Address will be created.");
            ArrayList<Address> addressList = new ArrayList<>();

             for (int i = 0; i < 2; i++) {
                System.out.println("Enter street name for address "+ (i+1) +":");
                String street = scanner.nextLine();
                System.out.println("Enter city name for address " + (i+1) + ":");
                String city = scanner.nextLine();
                addressList.add(new Address(street, city));
            }
        }else{
            System.out.println("Invalid Object Type.");

        }
        



    }catch(Exception e){
         System.out.println(e.getMessage());

    }



    

   


}
}
