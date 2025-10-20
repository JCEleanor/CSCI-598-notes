# Abstract Factory

In the Abstract Factory pattern, all of your concrete product factories (like BookFactory, VinylFactory, etc.)
must implement the abstract factory interface (in your case, InventoryFactory).

##### Why

1. abstraction: allow client code to work with any type of factory without knowing its specific, concrete class. The client only needs to know about the AbstractFactory interface.
2. Loose coupling: client is decoupled from the concrete factories. New factories can be added in the future without modifying client code
3. polymorphism: client hold a reference to an AbstractFacory object. At runtime, the object can be any of the concrete factory class. The client calls methods on the AbstractFactory, and the correct version for the specific factory is executed.
