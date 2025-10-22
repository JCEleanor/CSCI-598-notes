## Part 1

| Method Name          | Requirement / Role                                                         | Access Modifier | Implementation Type (`final`, `abstract`, or `default`) | Justification                                                            |
| :------------------- | :------------------------------------------------------------------------- | :-------------- | :------------------------------------------------------ | :----------------------------------------------------------------------- |
| `processOrder()`     | **The Template Method.** Must define the fixed sequence of all four steps. | `public`        | `final`                                                 | Guarantees that the **algorithm order** cannot be changed.               |
| `calculateTotal()`   | **Primitive Operation.** Must be implemented by every order type.          | `protected`     | `abstract`                                              | **Forces** specific implementation details to be provided.               |
| `sendConfirmation()` | **Hook Operation.** Optional step. Default is a simple email.              | `protected`     | `default` (concrete)                                    | Provides a **default implementation** that can be optionally overridden. |
| `validate()`         | **Primitive Operation.** Must be implemented by every order type.          | `protected`     | `abstract`                                              | **Forces** the specific validation logic.                                |

## Part 2: Implementation

### Task A: The Abstract Class

Write the full OrderProcessor abstract class based on Part 1.

```java
// TASK A: The Abstract Class
public abstract class OrderProcessor {

    // TODO 1: Define the public final Template Method 'processOrder()'.
    // It must call the four steps in the correct order:
    // validate() -> calculateTotal() -> processPayment() -> sendConfirmation().
    public final void processOrder() {
        validate();

        calculateTotal();

        processPayment();

        sendConfirmation();
    }

    // Concrete Fixed Operation (All subclasses use the same payment gateway call)
    private void processPayment() {
        System.out.println("LOG: Executing payment transaction via Stripe/PayPal.");
    }

    // TODO 2: Define the Primitive Operations.
    protected abstract void validate();
    protected abstract void calculateTotal();

    // TODO 3: Define the protected concrete Hook Operation 'sendConfirmation()'.
    // Include the text: "Sending standard email confirmation."
    protected void sendConfirmation() {
        System.out.println("Sending standard email confirmation.");
    }
}
```

### Task B: The Concrete Subclass

Implement the StandardOrderProcessor class, providing simple placeholder implementations for the required primitive operations.

```java
// TASK B: The Concrete Subclass Implementation
public class StandardOrderProcessor extends OrderProcessor {

    // TODO 4: Implement the required abstract methods.
    // 'validate()': Include the text: "Checking inventory and payment details."
    // 'calculateTotal()': Include the text: "Calculating total with standard shipping."
    @Override
    protected void validate() {
        System.out.println("Checking inventory and payment details.");
    }

    @Override
    protected void calculateTotal() {
        System.out.println("Calculating total with standard shipping.");
    }

    // HINT: You do NOT need to override sendConfirmation() here!
    // ...
}
```

## Extension: The Express Order & Client Decoupling

The ExpressOrderProcessor is provided below. Its purpose is to demonstrate how the Template Method handles variation by overriding the Hook and Primitive Operations.

Provided Implementation:

```java
public class ExpressOrderProcessor extends OrderProcessor {
    protected double calculateTotal() {
        System.out.println("LOG: Calculating total with EXPRESS shipping fee.");
        return 250.00;
    }
    protected void validate() {
        System.out.println("LOG: Checking priority inventory allocation and premium card limits.");
    }

    // Overrides Hook Operation to change default behavior
    protected void sendConfirmation() {
        System.out.println("LOG: Sending SMS confirmation to customer.");
    }
}
```

### Task C: Client Code

This is a controller class that is called by the buttons in the UI. We focus on two buttons: one for standard processing and one for express processing.

This is the client code that uses both concrete processors. The core client logic must depend only on the abstract OrderProcessor type.

```java
 public class CheckoutController {

    // Method called when the "Standard Checkout" button is clicked.
    public void handleStandardOrderClick() {
        System.out.println("\n--- CONTROLLER: Standard Button Clicked ---");

        OrderProcessor processor = new StandardOrderProcessor();

        //TODO process the order by calling the Template method
        processor.processOrder();
    }

    // Method called when the "Express Checkout" button is clicked.
    public void handleExpressOrderClick() {
        System.out.println("\n--- CONTROLLER: Express Button Clicked ---");

        OrderProcessor processor = new ExpressOrderProcessor();

        //TODO process the order by calling the Template method
        processor.processOrderr();
     }
}
```

This is the main method simulating the button clicks.

```java
public class CheckoutClient {
    public static void main(String[] args) {

        // The View/Main method initializes the system and gets the Controller instance.
        CheckoutController controller = new CheckoutController();

        System.out.println("\n--- VIEW SIMULATION: User Clicks Button ---");

        // User clicks the "Standard Checkout" button, calling the Controller method.
        controller.handleStandardOrderClick();

        // User clicks the "Express Checkout" button, calling the Controller method.
        controller.handleExpressOrderClick();
    }
}
```
