### SOLID

### L -> Liskove substitution

### Design pattern

- structural pattern

  - adaptor: making incompatible classes work together
    - aka wrapper
    - in c++, std method use adaptor frequently
  - Bridge
  - Composite
  - Decorator
  - Façade
  - Flyweight
  - proxy

- creational pattern
- behavioral pattern

#### For adaptor homework

IPayment.

// dependency injection
payment = new Adaptor (new QuickPay())
payment.pay()

### 0910 Composite Pattern

- decorator has decorator. the decorator at runtime is a linked list (omg)

```
// composition
t = new ScrollBar(t)
t = new Border(t)

// keep adding features....
```

- adding behaviors in runtime
- decoration in action: ( a ) -> ( b ) -> ( c ): execute c first, and then b, and then a
- flexible alternative to inheritance:

  - adding/removing responsibility at runtime
  - mix and match responsibility
  - decorators are simple and stackable

- can mix composite with decorators (ugly)
- adaptor vs. decorator
  - adaptor is altering existing functionalities
  - decorator is adding new functionalities
