# The Observer Pattern

Objective: Analyze a completed StockMarket application to understand the Observer pattern's structure, intent, and core benefits (like loose coupling).

### Scenario

You are analyzing a StockMarket application. The core of the system is the StockData (the Subject), which tracks stock prices.

Multiple "display" elements (the Observers) need to be notified immediately whenever a stock price changes. We have a CurrentConditionsDisplay (which shows the current price) and a TradingViewDisplay (which shows a chart).

Your goal is to analyze the completed code to understand how this notification system works.

### Your Tasks

Read the completed C++ or Java scaffolding below and write down your answers to these four design questions:

_1. Why Observer? The scenario says displays need to be notified immediately when there is a change. What is the 'bad' or 'naive' alternative to the Observer pattern? Why would that alternative be inefficient for a StockMarket?"_

The naive alternative is polling. Polling is inefficient and waste of resources, especially for stock prices who fluctuate a lot.

_2. Subject's Job. Look at the StockData class. What specific methods does it have that allow it to manage its list of "subscribers"? (Hint: There are three main ones)._

- `notifyObservers` loops through all the subscribed members.
- add/remove

_3. Observer's Job. Look at the Display interface (or abstract class) and the concrete `CurrentConditionsDisplay`. What single, essential method must an "Observer" implement to be able to receive notifications?_

`update`, which is a callback. The subject doesn't know what the observer will do, it only knows that it can call this update method and pass it the new state.

_4. "Loose Coupling" Test. This is the most important question. Imagine your team needs to add a brand new feature: an `SMSDisplay` that sends a text message every time the price changes.
What exact steps would you take to add this feature?
How many lines of code inside the original StockData class would you need to change?
What does this tell you about how "coupled" the StockData (Subject) is to its Displays (Observers)?_

Just create a new class:

```java
class SMSDisplay implements Display {
    public void update(String symbol, double price) {
        System.out.println("send...");
    }
}
```

`SMSDisplay` is another observer. It doesn't change any lines of code in `StockData`, because **subject is loosely coupled to obsevers**

_5. Push vs. Pull. Examine the notify() method in StockData and the update() method in the Displays. Is the StockData "pushing" the exact data (the stock price) to the Observers?_

Is it just "pulling" (telling the Observers "I changed!" and forcing them to ask for the new data)?

_What are the pros or cons of the approach used here?_

It is pushing. The subject is actively telling the observers what change.

- Pro: This is very simple and convenient for the observers.
- Cons: if the changes grows, it might be hard to maintain.

_6. Refactor to the other Model (push or pull) What one method must you add to The Observer Pattern:_

Add getter method: `getPrice`

Objective: Analyze a completed StockMarket application to understand the Observer pattern's structure, intent, and core benefits (like loose coupling).

```c++
#include <iostream>
#include <vector>
#include <string>
#include <memory>

// --- Observer Interface ---
// All observers must implement this so the Subject can talk to them

// Forward declare Subject
class StockData;

class Display {
public:
    virtual ~Display() {}
    // The "update" method, called by the Subject
    virtual void update(const std::string& symbol, double price) = 0;
};

// ---  The Subject Interface ---
// Defines how Observers can register and unregister
class Subject {
public:
    virtual ~Subject() {}
    virtual void addObserver(std::shared_ptr<Display> observer) = 0;
    virtual void removeObserver(std::shared_ptr<Display> observer) = 0;
    virtual void notifyObservers() = 0;
};

// --- The Concrete Subject ---
// This is the core data class. It "is-a" Subject.
class StockData : public Subject {
private:
    std::vector<std::shared_ptr<Display>> observers;
    std::string symbol;
    double price;

public:
    StockData(const std::string& sym) : symbol(sym), price(0.0) {}

    // --- Implementation of Subject methods ---
    void addObserver(std::shared_ptr<Display> observer) override {
        observers.push_back(observer);
    }

    void removeObserver(std::shared_ptr<Display> observer) override {
      // C++20 and later
      std::erase(observers, observer);
    }

    void notifyObservers() override {
        for (auto& observer : observers) {
            observer->update(symbol, price);
        }
    }

    // --- Core business logic ---
    // When data changes, notify everyone.
    void setPrice(double newPrice) {
        this->price = newPrice;
        std::cout << "\n--- StockData: Price for " << symbol << " changed to $" << price << " ---" << std::endl;
        notifyObservers();
    }
};

// --- Concrete Observers ---

class CurrentConditionsDisplay : public Display {
public:
    void update(const std::string& symbol, double price) override {
        std::cout << "  [CurrentConditionsDisplay]: Stock " << symbol << " is now $" << price << std::endl;
    }
};

class TradingViewDisplay : public Display {
public:
    void update(const std::string& symbol, double price) override {
        std::cout << "  [TradingViewDisplay]: Plotting new data point for " << symbol << " at $" << price << std::endl;
    }
};

// ---  Client Code ---
int main() {
    // 1. Create the Subject
    auto stockData = std::make_shared<StockData>("GOOG");

    // 2. Create Observers
    auto display1 = std::make_shared<CurrentConditionsDisplay>();
    auto display2 = std::make_shared<TradingViewDisplay>();

    // 3. Register Observers with the Subject
    stockData->addObserver(display1);
    stockData->addObserver(display2);

    // 4. Simulate data changes
    stockData->setPrice(150.00);
    stockData->setPrice(150.50);

    // (Imagine we unregister display1 here...)
    // stockData->removeObserver(display1);

    stockData->setPrice(151.00);

    return 0;
}
```

```java
import java.util.ArrayList;
import java.util.List;

// ---  Observer Interface ---
// All observers must implement this so the Subject can talk to them
interface Display {
    // The "update" method, called by the Subject
    public void update(String symbol, double price);
}

// --- The Subject Interface ---
// Defines how Observers can register and unregister
interface Subject {
    public void addObserver(Display observer);
    public void removeObserver(Display observer);
    public void notifyObservers();
}

// --- The Concrete Subject ---
// This is the core data class. It "is-a" Subject.
class StockData implements Subject {
    private List<Display> observers;
    private String symbol;
    private double price;

    public StockData(String symbol) {
        this.observers = new ArrayList<>();
        this.symbol = symbol;
    }

    // --- Implementation of Subject methods ---
    public void addObserver(Display observer) {
        observers.add(observer);
    }

    public void removeObserver(Display observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Display observer : observers) {
            observer.update(symbol, price); // push
        }
    }

    // --- Core business logic ---
    // When data changes, notify everyone.
    public void setPrice(double newPrice) {
        this.price = newPrice;
        System.out.println("\n--- StockData: Price for " + symbol + " changed to $" + price + " ---");
        notifyObservers();
    }
}

// --- Concrete Observers ---

class CurrentConditionsDisplay implements Display {
    public void update(String symbol, double price) {
        System.out.println("  [CurrentConditionsDisplay]: Stock " + symbol + " is now $" + price);
    }
}

class TradingViewDisplay implements Display {
    public void update(String symbol, double price) {
        System.out.println("  [TradingViewDisplay]: Plotting new data point for " + symbol + " at $" + price);
    }
}

// --- Client Code ---
public class StockMarketExample {
    public static void main(String[] args) {
        // 1. Create the Subject
        StockData stockData = new StockData("GOOG");

        // 2. Create Observers
        Display display1 = new CurrentConditionsDisplay();
        Display display2 = new TradingViewDisplay();

        // 3. Register Observers with the Subject
        stockData.addObserver(display1);
        stockData.addObserver(display2);

        // 4. Simulate data changes
        stockData.setPrice(150.00);
        stockData.setPrice(150.50);

        // (Imagine we unregister display1 here...)
        // stockData.removeObserver(display1);

        stockData.setPrice(151.00);
    }
}
```
