### Contender 1 The Classic OOP (Inheritance) Model (Slide 15)

This approach uses a pure virtual `Command` interface and concrete classes for each action. The GoodButton example above is already designed for this model.

```c++
#include <iostream>
// 1. The Receiver
class Light {
public:
    void on() { std::cout << "Light is ON" << std::endl; }
    void off() { std::cout << "Light is OFF" << std::endl; }
};
// 2. The Command Interface
class Command {
public:
    virtual ~Command() {}
    virtual void execute() = 0;
};
// 3. The Concrete Command
class LightOnCmd : public Command {
private:
    Light& light;
public:
    LightOnCmd(Light& l) : light(l) {}
    void execute() { light.on(); }
};
// 4. The Invoker (Simple version)
class RemoteControl {
private:
    Command* command;
public:
    void setCmd(Command* c) { command = c; }
    void buttonPress() { command->execute(); }
};
// 5. The Client
int main() {
    Light myLight;
    LightOnCmd lightOn(myLight);

    RemoteControl remote;
    remote.setCmd(&lightOn);
    remote.buttonPress(); // Prints "Light is ON"

    // Note: This simple example uses a raw pointer for simplicity.
    // A real implementation would use smart pointers.
    return 0;
}
```

### Contender 2: The C++ Functor (Function Object) Model (Slide 41)

How this is different:

This approach is a C++ idiom called a "functor" or "function object". It's an object that is disguised to act as if it were a function.

1. The key line is `virtual void operator ()() = 0;`.
   This is C++ syntax for "overloading the function call operator." It means any class that inherits from this can be called _as if it were a function_.
   The `Invoker`'s `buttonPress` method calls `(*command)();`. It's calling the object itself, not a method named `execute`.

```c++
#include <iostream>
#include <string>
#include <memory> // For std::unique_ptr
// 1. The Receiver
class Light {
public:
    void on(const std::string& s) { std::cout << "Light is ON: " << s << std::endl; }
    void off(const std::string& s) { std::cout << "Light is OFF: " << s << std::endl; }
};
// 2. The Command Interface (as Functor)
class Command {
public:
    virtual ~Command() {}
    virtual void operator ()() = 0; // <-- The "function call" operator
};
// 3. The Concrete Command (as Functor)
class LightOnCmd : public Command {
private:
    Light& light;
    std::string str;
public:
    LightOnCmd(Light& l, const std::string& s) : light(l), str(s) {}
    virtual void operator ()() { light.on(str); } // <-- Implements the call
};
// 4. The Invoker
class RemoteControl {
private:
    std::unique_ptr<Command> command;
public:
    void setCmd(Command* cmd) { command.reset(cmd); }
    void buttonPress() {
        if (command) { (*command)(); } // <-- Calls the object like a function
    }
};
// 5. The Client
int main() {
    Light myLight;

    RemoteControl remote;
    remote.setCmd(new LightOnCmd(myLight, "from functor"));
    remote.buttonPress(); // Prints "Light is ON: from functor"

    return 0;
}
```

### Contender 3: The Modern C++ (Lambda / `std::function`) Model (Slide 43)

How this is different:

This modern approach avoids the `Command` inheritance hierarchy completely. It uses two new C++ features: std::function and lambdas. (in Java, lambdas can be used in a similar way and it would be much simpler than this one)

1. `std::function<void()> command;`: This declares a generic variable that can hold any "callable thing" (a function, a functor, or a lambda) that takes no arguments (()) and returns void.
2. `remote.setCmd([&]() { ... });`: This "weird looking" code is a lambda. It's an anonymous, in-line function.
   - `[&]` is the "capture clause," meaning "let the code inside access all local variables (like myLight) by reference."
   - `()` is the (empty) argument list.
   - `{...}` is the function body.
3. We are creating a function "on the fly" and passing it to the `Invoker`.

```c++
#include <iostream>
#include <string>
#include <functional> // Required for std::function
// 1. No Command Interface or Concrete Classes needed.
// 2. The Receiver
class Light {
public:
    void on(const std::string& s) { std::cout << "Light is ON: " << s << std::endl; }
    void off(const std::string& s) { std::cout << "Light is OFF: " << s << std::endl; }
};
// 3. The Invoker
class RemoteControl {
private:
    // Holds any function that takes void and returns void
    std::function<void()> command;
public:
    void setCmd(std::function<void()> cmd) { command = cmd; }
    void buttonPress() {
        if (command) { command(); }
    }
};
// 4. The Client (Example of use)
int main() {
    Light myLight;
    RemoteControl remote;

    // Pass a lambda directly
    remote.setCmd([&]() { myLight.on("from std::function"); });
    remote.buttonPress(); // Prints "Light is ON: from std::function"

    return 0;
}
```

### Your Task

````c++
// The "Good" Way: Decoupled (using Contender 1)
class Command { public: virtual void execute() = 0; }; // The interface
class GoodButton {
private:
    Command* myCommand; // The Button *only* knows the Command interface.
public:
    void setCmd(Command* c) {
        myCommand = c; // Configure the button's action at runtime!
    }

    void onClick() {
        myCommand->execute(); // It just calls execute(). It has no idea
                              // what a "Light" or "Fan" is.
    }
};
```

The `GoodButton` we saw earlier was designed for Contender 1. Your task is to refactor the `GoodButton` to work with Contenders 2 and 3.

### Task 1: Refactor for the Functor (Contender 2)

Sketch out the new `ButtonForFunctor` class.

1. What is the data type of its `myCommand` member? (Hint: Look at Contender 2's `RemoteControl`.)
2. How does its `onClick()` method have to change to call this member?

```c++
// Refactored for Functor (Contender 2)
class  {
private:
    std::unique_ptr<Command> myCommand; // Command from Contender 2 is a functor
public:
    void setCmd(Command* c) {
        myCommand.reset(c); // Take ownership of the command
    }

    void onClick() {
        if (myCommand) { (*myCommand)(); } // Call the functor
    }
};
```

### Task 2: Refactor for the Lambda (Contender 3)

Sketch out `ButtonForLambda`.

1. What is the data type of its `myCommand` member? (Hint: Look at Contender 3's `RemoteControl`.)
2. How does its `onClick()` method have to change to call this member?

```c++
// Refactored for Lambda (Contender 3)
class ButtonForLambda {
private:
    std::function<void()> myCommand; // std::function can hold lambdas
public:
    void setCmd(std::function<void()> cmd) {
        myCommand = cmd;
    }

    void onClick() {
        if (myCommand) { myCommand(); } // Call the stored lambda/function
    }
};
```

### Discussion

1. How did the Button's implementation change for each contender?
2. How is the invoker affected in these two refactorings?

   This shows the real "magic" of Command: the Button's logic is always simple. The implementation of the command object can change, but the Invoker's design stays decoupled and simple.
````
