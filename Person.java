public class Person{
    public int id;
    public Address address;

    public Person(){
        id = 0;
        address = null;
    }

    // Constructor and getters/setters for name and address
    public Person(int id, Address address) {
        this.id = id;
        this.address = address;
    }

    public int getID() {
        return id;
    }

    public Address getAddress() {
        return address;
    }
	
}