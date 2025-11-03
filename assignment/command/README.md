# Command

## Core Components:

1.  Command (Interface/Abstract Class): Declares an interface for executing an operation. It typically has a single method, `execute()`.
2.  Concrete Command: Implements the Command interface. It binds a Receiver object with an action. It knows which Receiver method to call to carry out the request.
3.  Receiver: The object that performs the actual work. It knows how to perform the operations associated with carrying out a request. Any class can act as a Receiver.
4.  Invoker: Asks the Command to carry out the request. It holds a Command object and, when instructed, calls the Command's execute() method. The Invoker doesn't know
    anything about the Receiver or the specific action.
5.  Client: Creates a Concrete Command object and sets its Receiver. It then passes the Command object to the Invoker.
