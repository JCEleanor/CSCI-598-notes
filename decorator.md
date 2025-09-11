### composite

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

### Task

- Define a pricing interface: Define an interface for pricing that the system can depend on.
- Support extension without modifying client code: Ensure the interface supports the existing Product.getPrice() method and can be extended to support multiple discount types without modifying client code.
- Identify decorators and decorated components: Identify the interfaces and classes. - Determine which one needs to be decorated and which ones should act as decorators.
- Support dynamic addition/removal of discounts: Have support for both adding and removing a specific discount from a decorated product.
- Draw a class diagram to illustrate the relationships between the classes and interfaces. Do not put code in the class diagram.

```java

// Ensure the interface supports the existing Product.getPrice()


```
