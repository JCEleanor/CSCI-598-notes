### Task 1

Define an interface for payment processing that the system can depend on.
Ensure the interface supports the existing PayFast method and can be extended to support QuickPay without modifying client code.

```java
// 1. Target Interface: What the Client expects
public interface IPaymentGateway {
    void pay(double amount);
}

// 2. Concrete Target: The existing class that works with the client
public class PayFast implements IPaymentGateway {
    @Override
    public void pay(double amount) {
        System.out.println("Processing payment of " + amount + " via PayFast.");
    }
}

// 3. Adaptee: The new, incompatible class
public class QuickPay {
    public void qpay(double value) {
        System.out.println("Processing payment of " + value + " via QuickPay.");
    }
}
```

### Task 2

Identify the interfaces and classes. Determine which one needs to be adapted and which ones should act as adaptee.

```java
// Adapter: The class that makes the Adaptee compatible
public class QuickPayAdapter implements IPaymentGateway {

    /* Declares a private instance variable of type QuickPay. [composition]
     * Composition
     *
     * Declares a private instance variable of type QuickPay
     *
     * The QuickPayAdapter doesn't inherit from QuickPay, but it contains or wraps an instance of it
     * This is how the adapter gets access to the qpay() method of the QuickPay system
     */
    private QuickPay quickPayApi;

    // dependency injection
    public QuickPayAdapter(QuickPay quickPayApi) {
        this.quickPayApi = quickPayApi;
    }

    @Override
    public void pay(double amount) {
        this.quickPayApi.qpay(amount);
    }
}
```

### Task 3

Draw a class diagram to illustrate the relationships between the classes and interfaces. (see image)

### Task 4

Client code: Behavior Simulation

```java
// 5. Client: The main application that uses the interface
public class ECommercePlatform {
    private IPaymentGateway paymentGateway;

    // dependency injection
    public ECommercePlatform(IPaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void checkout(double orderTotal) {
        paymentGateway.pay(orderTotal);
    }
}

public class Main {
    public static void main(String[] args) {
        // Scenario 1: Using the existing PayFast system.
        IPaymentGateway payFastProvider = new PayFast();
        ECommercePlatform platform1 = new ECommercePlatform(payFastProvider);
        platform1.checkout(99.50);

        // Scenario 2: Using the new QuickPay system via the Adapter.
        QuickPay quickPayApi = new QuickPay();
        IPaymentGateway quickPayAdapter = new QuickPayAdapter(quickPayApi);
        ECommercePlatform platform2 = new ECommercePlatform(quickPayAdapter);
        platform2.checkout(150.00);
    }
}
```

### Task 5

Extensibility in action:
Imagine a third provider, CryptoPay with a method send(amount).
Extend your design to support it without modifying existing client code.

```java
// Extensibility in action: a third provider, CryptoPay with a method send(amount)
public class CryptoPay {
    public void send(double value) {
        // some action
    }
}

public class CryptoPayAdaptor implements IPaymentGateway {
    private CryptoPay cryptoPayApi;

    // dependency injection
    public CryptoPayAdaptor(CryptoPay cryptoPayApi) {
        this.cryptoPayApi = cryptoPayApi;
    }

    @Override
    public void pay(double amount) {
        this.cryptoPayApi.send(amount);
    }

}
```
