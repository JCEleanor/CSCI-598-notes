public class Main {
    public static void main(String[] args) {
        Dog myDog = new Dog("Buddy");

        // Method from Animal superclass (Inheritance)
        System.out.println("My dog's name is " + myDog.getName());

        // Method from Animal superclass (Inheritance)
        myDog.makeSound();

        // Method from Movable interface (Implementation)
        myDog.move();
    }
}
