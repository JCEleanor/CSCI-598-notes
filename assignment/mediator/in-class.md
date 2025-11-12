# Mediator

## Notes

- you can use observer design pattern along with mediator. i.e., everyone subscribes to mediator.
- if there's a new Collegues that doesn't follow the existing interface, we can use adaptor design pattern to let them work together.

## In-class Exercise

### Scenario: Air Traffic Control (ATC) Design

The core challenge in air traffic control is that every plane (Colleague) must coordinate its movements with all other planes. Without a good design, the system would suffer from **chaotic many-to-many (N-to-N) dependency**.

It would be a mess where every plane's logic must know about every other plane's logic.

(this scenario exemplifies applicability, i.e. when to apply the mediator)

The code at the end of this page implements the Mediator pattern to solve this problem by centralizing all coordination into the `AirportController`.

Your task is to answer the following design questions based on the code given. The same code is written both in Java and C++

### Questions

#### 1. The Full Communication Trace

Trace the full sequence of method calls that happens when a `CommercialAirplane`'s `requestTakeoff()` method is first called. List the method calls in order, starting from the plane's request and ending with the message being printed to the console.

`CommercialAirplane`'s `requestTakeoff()` -> `mediator.requestTakeoff()` -> all the registered airplanes calling `receiveInstruction`

#### 2. The Benefit: Decoupling & Extensibility

- Part A (Decoupling): Look at the `CommercialAirplane` class. How does it demonstrate it is decoupled from its peers (i.e., other planes)? (Hint: What references does it not have?)

`CommercialAirplane` only knows its mediator.

- Part B (Extensibility): Because of this decoupling, if you add a new `CargoPlane` class (that implements Airplane), do you need to modify the existing `CommercialAirplane` or `AirportController` classes? Why or why not?

No, we can create `CargoPlane` and register it to the `AirportController`.

#### 3. The Drawback: The Trade-Off

The Mediator pattern achieves simplicity by **centralizing** all coordination logic into the `AirportController`.

This centralized simplicity comes at a cost. What is the primary design risk or problem introduced by centralizing all this logic into one class? (Hint: Consider what happens if the airport system adds 100 different coordination checks.)

The mediator class, `AirportController`, becomes fat and hard to maintain due to all many if-else statements.
Mediator becomes a **god class**.
Mediator does not follow open-close principles, but Concrete Colleague does.

#### Java

```java
// --- 1. The Mediator Interface ---
public interface ControlTower {
    void requestTakeoff(Airplane airplane);
    void requestLanding(Airplane airplane);
}

// --- 2. The Colleague Interface ---
public interface Airplane {
    void requestTakeoff();
    void requestLanding();
    // NOTE: this method could be a callback method, using observer design pattern
    void receiveInstruction(String message); // call back from Mediator
}

// --- 3. The Concrete Mediator (AirportController) ---
public class AirportController implements ControlTower {

    private List<Airplane> planes = new ArrayList<>(); //implement to interface

    public void registerAirplane(Airplane plane) {
        planes.add(plane);
    }

    public void requestTakeoff(Airplane requestingAirplane) {

        // CORE COORDINATION LOGIC RESIDES HERE.
        // I delegated it to a method

        if (isRunwayClear()) {
            // Mediator sends instruction back to the Colleague
            requestingAirplane.receiveInstruction("Takeoff clearance granted.");
            //and then maybe does some coordination

        } else {
            requestingAirplane.receiveInstruction("Takeoff request denied. Runway is busy.");
            //and then maybe does some coordination like num of airplanes waiting.
        }
    }
     public void requestLanding(Airplane requestingAirplane) {

        // CORE COORDINATION LOGIC RESIDES HERE.
        // I delegated it to a method

        if (isRunwayClear()) {
            // Mediator sends instruction back to the Colleague
            requestingAirplane.receiveInstruction("Landing clearance granted.");
        } else {
            requestingAirplane.receiveInstruction("Landing request denied. Runway is busy.");
        }
    }
    private boolean isRunwayClear(){
        // Logic checks the status of ALL other planes in the 'planes' list
        //maybe other checks as well like the wind and weather
        return true ; // or false based on the conditions above
    }
}

// --- 4. The Concrete Colleague (CommercialAirplane) ---
public class CommercialAirplane implements Airplane {

    private ControlTower mediator;

    public void connectTower(ControlTower controlTower) {
        this.mediator = controlTower;
    }

    public void requestTakeoff() {
        // Step 1: Colleague delegates all requests to the Mediator
        if (mediator != null) {
            mediator.requestTakeoff(this); // Passes 'this' to identify sender
        }
    }

    public void receiveInstruction(String message) {
        // Colleague only receives instructions from the Mediator
        System.out.println("Tower said: " + message);
    }

    public void requestLanding() {
        // Step 1: Colleague delegates all requests to the Mediator
        if (mediator != null) {
            mediator.requestLanding(this); // Passes 'this' to identify sender
        }
    }

}

public class Demo{
  public static void main(String args) {
    ControlTower controller = new AirportController();

    Airplane plane1 = new CommercialAirplane();
    plane1.connectTower(controller);
    controller.registerAirplane(plane1);

    for(int i=0;i<50;i++){
      Airplane plane = new CommercialAirplane();
      plane.connectTower(controller);
      controller.registerAirplane(plane);
    }

    plane1.requestTakeoff();

 }
}
```

#### C++

```c++
// --- 1. The Mediator Interface ---
class Airplane; // Forward declaration

class ControlTower {  //act as interface since all methods are pure virtual
public:
    virtual ~ControlTower() {} // Virtual destructor for base class
    virtual void requestTakeoff(Airplane* airplane) = 0;
    virtual void requestLanding(Airplane* airplane) = 0;
};

// --- 2. The Colleague Interface ---
class Airplane {
public:
    virtual ~Airplane() {} // Virtual destructor for base class
    virtual void requestTakeoff() = 0;
    virtual void requestLanding() = 0;
    virtual void receiveInstruction(const std::string& message) = 0; // call back
};

// --- 3. The Concrete Mediator (AirportController) ---
class AirportController : public ControlTower {
private:
    // List of all Colleagues it coordinates
    std::vector<Airplane*> planes;

    bool isRunwayClear() {
        // Logic checks the status of ALL other planes in the 'planes' list
        // maybe other checks as well like the wind and weather
        return true; // or false based on the conditions above
    }

public:
    void registerAirplane(Airplane* plane) {
        planes.push_back(plane);
    }

    void requestTakeoff(Airplane* requestingAirplane) override {
        // CORE COORDINATION LOGIC RESIDES HERE.
        // I put it in the method isRunwayClear

        if (isRunwayClear()) {
            // Mediator sends instruction back to the Colleague
            requestingAirplane->receiveInstruction("Takeoff clearance granted.");
        } else {
            requestingAirplane->receiveInstruction("Takeoff request denied. Runway is busy.");
        }
    }

    void requestLanding(Airplane* requestingAirplane) override {
        // CORE COORDINATION LOGIC RESIDES HERE.
        // I put it in a method for readability

        if (isRunwayClear()) {
            // Mediator sends instruction back to the Colleague
            requestingAirplane->receiveInstruction("Landing clearance granted.");
        } else {
            requestingAirplane->receiveInstruction("Landing request denied. Runway is busy.");
            //and then maybe does some coordination like num of airplanes waiting.
        }
    }
};

// --- 4. The Concrete Colleague (CommercialAirplane) ---
class CommercialAirplane : public Airplane {
private:
    ControlTower* mediator;

public:
    CommercialAirplane() : mediator(nullptr) {}

    void connectTower(ControlTower* controlTower) {
        this->mediator = controlTower;
    }

    void requestTakeoff() override {
        // Step 1: Colleague delegates all requests to the Mediator
        if (mediator != nullptr) {
            mediator->requestTakeoff(this); // Passes 'this' to identify sender
        }
    }

    void requestLanding() override {
        // Step 1: Colleague delegates all requests to the Mediator
        if (mediator != nullptr) {
            mediator->requestLanding(this); // Passes 'this' to identify sender
        }
    }

    void receiveInstruction(const std::string& message) override {
        // Colleague only receives instructions from the Mediator
        std::cout << "Tower said: " << message << std::endl;
    }
};

// --- Example Usage (How to wire it up in C++) ---
int main() {
    ControlTower* controller = new AirportController();

    Airplane* plane1 = new CommercialAirplane();
    plane1->connectTower(controller);
    controller->registerAirplane(plane1);

    for(int i=0;i<50;i++){
      Airplane* plane = new CommercialAirplane();
      plane->connectTower(controller);
      controller->registerAirplane(plane);
    }

    plane1->requestTakeoff();

    // Clean up memory....
}
```
