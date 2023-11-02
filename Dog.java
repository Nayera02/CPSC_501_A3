
public class Dog{
  public String type;
  public String name;

  // method that returns something
  public String getName(){
    return name;
  }
  
  public void setName(String name){
	  this.name=name;
  }
  
  public void setType(String type){
	this.type=type;
  }
  // standard method
  public void display() {
    System.out.println("I am a dog.");
  }

  // testing private method
  private void makeSound() {
    System.out.println("Bark Bark");
  }

  // testing a parameter
  public void woof(int numWoofs){
    for (int i=0; i<numWoofs;i++){
        System.out.println("Woof");
    }
  }

}