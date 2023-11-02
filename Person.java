public class Person{
    public String name;
    public Address address;

    // Constructor and getters/setters for name and address
    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
	
}