## 6. SOLID

S (Single Responsibility Principle): Followed.

DigitalBrush (Receiver) is only responsible for painting logic.

PaintingApplication (Invoker) is only responsible for invoking a command.

O (Open/Closed Principle): Followed.

The PaintingApplication is closed for modification as long as the operation (left click and right click) reamins the same.

The system is open to extension. You can add a new brush class that follows the same function signature (return void and take no parameter) without touching any existing code.

L (Liskov Substitution Principle): N/A. Since we use lambda to create concrete command object in the client code, there's no concrete command subclasses.

I (Interface Segregation Principle): Followed.

The ICommand interface is minimal and has only one method, execute().

D (Dependency Inversion Principle): Followed.

This pattern is a classic example of DIP. The high-level PaintingApplication (Invoker) does not depend on the low-level DigitalBrush (Receiver).

Instead, both depend on an abstraction: ICommand. This decouples the sender and receiver.
