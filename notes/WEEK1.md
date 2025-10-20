# UML

## Inheritance (`extends`) - - ->

A new class (subclass) derives properties and behaviors (fields and methods) from an existing class (the superclass)
The primary relationship is "is-a." For example, a Dog is an Animal.

- Subclasses can reuse code from superclass
- a hierachical classification
- in Java, a class can only extend one superclass

## Implementation (`implements`) --->

A class agrees to a "contract" defined by an interface. The interface specifies a set of methods that the class MUST implement. The primary relationship is "can-do". For example, a Car can move.

- contractual obligation: the class must provide a concrete implementation for all methods defined in the interface.
- in Java, a class can only implement multiple superclasses

`javac *.java && java Main`

```java
public interface Movable {
    void move();
}

public abstract class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void makeSound();
}

public class Dog extends Animal implements Movable {

    public Dog(String name) {
        super(name);
    }

    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }

    @Override
    public void move() {
        System.out.println("The dog runs.");
    }
}

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
```
