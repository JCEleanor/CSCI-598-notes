### Task 1 & 2

- Define a pricing interface: Define an interface for pricing that the system can depend on.
- Support extension without modifying client code: Ensure the interface supports the existing Product.getPrice() method and can be extended to support multiple discount types without modifying client code.

```java
interface Priceable {
    double getPrice();
}

public class Product implements Priceable {
    private String name;
    private double basePrice;

    public Product(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    @Override
    public double getPrice() {
        return basePrice;
    }
}
```

### Task 3

- Identify decorators and decorated components: Identify the interfaces and classes. - Determine which one needs to be decorated and which ones should act as decorators.

```java
// Decorator base class
abstract class DiscountDecorator implements Priceable {
    protected Priceable priceable;

    public DiscountDecorator(Priceable priceable) {
        this.priceable = priceable;
    }

    @Override
    public double getPrice() {
        return priceable.getPrice();
    }
}

// All the discount type will be decorators
class SeasonalDiscount extends DiscountDecorator {
    private double discountPercentage;

    public SeasonalDiscount(Priceable priceable, double discountPercentage) {
        super(priceable);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double getPrice() {
        return super.getPrice() * (1 - discountPercentage / 100);
    }
}

class LoyaltyDiscount extends DiscountDecorator {
    private double discountPercentage;

    public LoyaltyDiscount(Priceable priceable, double discountPercentage) {
        super(priceable);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double getPrice() {
        return super.getPrice() * (1 - discountPercentage / 100);
    }
}

class PromotionalDiscount extends DiscountDecorator {
    private double discountPercentage;

    public PromotionalDiscount(Priceable priceable, double discountPercentage) {
        super(priceable);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double getPrice() {
        return super.getPrice() * (1 - discountPercentage / 100);
    }
}

// New ReferralDiscount
class ReferralDiscount extends DiscountDecorator {
    private double fixedDiscount = 100.00;

    public ReferralDiscount(Priceable priceable, double fixedDiscount) {
        super(priceable);
        this.fixedDiscount = fixedDiscount;
    }

    @Override
    public double getPrice() {
        return super.getPrice() - fixedDiscount;
    }
}
```

### Task 4

- Support dynamic addition/removal of discounts: Have support for both adding and removing a specific discount from a decorated product.

```java
public static Priceable removeDiscount(Priceable priceable, Class<?> discountType) {
    DiscountDecorator decorator = (DiscountDecorator) priceable;
    if (decorator.getClass().equals(discountType)) {
        return decorator.priceable;
    } else {
        decorator.priceable = removeDiscount(decorator.priceable, discountType);
    }
}
```

### Task 5

- Draw a class diagram to illustrate the relationships between the classes and interfaces. Do not put code in the class diagram. (see attachment)

### Client Code: Behavior Simulation

#### Object Construction

1. Write code to create a product with no discounts

```java
Priceable product = new Product("Product-1", 1000);
System.out.println("No discount: " + product.getPrice());
```

2. Write code to create a product with a seasonal discount

```java
Priceable seasonalProduct = new SeasonalDiscount(product, 0.1);
System.out.println("Seasonal discount: " + seasonalProduct.getPrice());
```

3. Write code to create a product with seasonal + loyalty + promotional discounts applied in combination

```java
Priceable cheapProduct = new PromotionalDiscount(new LoyaltyDiscount(new SeasonalDiscount(product, 0.1), 0.2), 0.3);
System.out.println("Cheap product price: " + cheapProduct.getPrice());

```

4. Write a method that removes a specific discount (e.g., loyalty) dynamically. Use dependency injection to determine what discount to withdraw.

```java
Priceable withoutLoyalty = removeDiscount(cheapProduct, LoyaltyDiscount.class);

```

#### Price Calculation Process

1. Show how a client can calculate the price of each of the four objects created in the previous step
   Using the objects constructed above, show how the client code performs the final price calculation. This code should be independent of the specific discounts applied.
2. Draw a sequence diagram for calculating the price for a product built in item 3.

```
seasonal + loyalty + promotional:
Client -> SeasonalDiscount : getPrice()
                                        -> LoyaltyDiscount : getPrice()
                                                                        -> PromotionalDiscount : getPrice()
                                                                        <-
                                        <-
Client    <- final price
```
