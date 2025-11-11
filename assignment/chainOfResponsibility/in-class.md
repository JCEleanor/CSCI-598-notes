### Part 1: Design and Code for Analysis

Read the complete, working code provided below. Both Java and C++ versions do the same. This code was written to implement the scenario above using the chain of responsibility.

Answer the following questions based on the code given.

#### 1. The Chain: In a $3000 expense, trace the exact path of the request. Which process() method is called first? Which one is called second? Which one is called third? Why did the Manager's process() method call super.process()

1.  `Manager.process()` is called first. Since $3000 is over the manager's $500 limit, it cannot approve the request.
2.  The manager calls super.process(), which is the process method in the ApprovalHandler class. This method's purpose is to pass the request to the next link in the chain.
3.  `Director.process()` is called next. The ApprovalHandler calls the process method on the successor, which is the director. The director's limit is $5000, which is enough to approve a $3000 expense. The request is approved by the Director, and the chain ends.

#### 2. Decoupling: The scenario requires that the Employee (Sender) should not be coupled to the approval hierarchy. Look at the Employee class. How does it achieve this? (Hint: What is the type of the manager field? Is it Manager or ApprovalHandler?)

The Employee class achieves decoupling by holding a reference to `ApprovalHandler` (the abstract handler) for its manager field, instead of a concrete handler like `Manager` or `Director`. This means the `Employee` class interacts with the approval process through a generic interface, making it **unaware of the specific concrete handlers or the structure** of the approval chain. The actual chain is configured externally, allowing changes to the hierarchy without modifying the `Employee` class.

#### 3. Extensibility: A new role, "VP (Vice President)", is added between Director and CFO. They can approve up to $20,000. What specific lines of code in main() would you change to add this new rule? Do you have to change the Employee, Manager, or Director classes at all?

No, we can just configure the new chain in client code.

#### 4. Evaluating the "Bad" Alternative: Now, imagine the "bad" alternative (no CoR). The Employee::submitExpense method would have a giant if/else block. If the company adds a new role, what classes would you have to change? How does the provided solution (CoR) show it is a better design?

The `Employee.submitExpense` method would contain a large if/else if/else block to route the expense to
the correct approver. `if/else` is not open for extension, and it's breaking SRP.

Why CoR is a Better Design: The Chain of Responsibility solution is better because:

1. It follows the Open/Closed Principle. To add a new role (like a VP), you simply create a new VP handler class and insert it into the chain in the main method. No existing classes (`Employee`, `Manager`, `Director`) need to be changed.
2. It promotes the **Single Responsibility Principle (SRP)**. Each handler class has one job: to decide if it can approve the expense. The `Employee` class has one job: to submit the expense. In the if/else version, the Employee class has the added responsibility of knowing the entire approval hierarchy, which it shouldn't.
3. It decouples the sender from the receivers. The `Employee` doesn't know who will ultimately handle the request. It just sends the request into the chain, making the system more flexible and easier to maintain.

#### 5. Runtime Configuration:

A new "Fast-Track Employee" needs to be hired. Their expenses should skip the `Manager` and go straight to the `Director` for approval. What single line of code would you write inside the main method to create this new employee? Do you do need to change any handler classes?

```java
    Employee fastTrackEmployee = new Employee("Fast-Track Employee", director);
```

### Part 2: Coding Extension (after class)

#### Java version:

```java
import java.text.NumberFormat;

// --- The Request Object ---
class ExpenseReport {
    public final double amount;
    private String status = "Pending";

    public ExpenseReport(double amount) { this.amount = amount; }

    public void approve(String approverName) { this.status = "Approved by " + approverName; }
    public String getStatus() { return this.status; }
}

// --- The Abstract Handler ---
abstract class ApprovalHandler {
    protected ApprovalHandler successor;
    public void setSuccessor(ApprovalHandler successor) { this.successor = successor; }

    // Default "pass-along" behavior (from your EmailSorter slide)
    // NOTE: we can make this method final -> abstract as a default implementation (template design pattern)
    public void process(ExpenseReport report) {
        if (successor != null) {
            successor.process(report);
        } else {
            System.out.println("Expense for " + formatCurrency(report.amount) + " could not be approved.");
        }
    }

    // Helper for printing
    protected String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }
}

// --- Concrete Handlers ---
class Manager extends ApprovalHandler {
    private final double APPROVAL_LIMIT = 500.0;
    @Override
    public void process(ExpenseReport report) {
        if (report.amount <= APPROVAL_LIMIT) {
            report.approve("Manager");
        } else {
            System.out.println("Manager: Passing " + formatCurrency(report.amount) + " up.");
            super.process(report); // Pass to next
        }
    }
}

class Director extends ApprovalHandler {
    private final double APPROVAL_LIMIT = 5000.0;
    @Override
    public void process(ExpenseReport report) {
        if (report.amount <= APPROVAL_LIMIT) {
            report.approve("Director");
        } else {
            System.out.println("Director: Passing " + formatCurrency(report.amount) + " up.");
            super.process(report); // Pass to next
        }
    }
}

class CFO extends ApprovalHandler {
    @Override
    public void process(ExpenseReport report) {
        // CFO can approve anything and is the end of the line
        report.approve("CFO");
    }
}

class VP extends ApprovalHandler {
    private final double APPROVAL_LIMIT = 20000.0;
    @Override
    public void process(ExpenseReport report){
        if (report.amount <= APPROVAL_LIMIT){
            report.approve("VP");
        } else {
            System.out.println("Manager: Passing " + formatCurrency(report.amount) + " up.");
            super.process(report);
        }
    }
}


// --- The Client (Sender) ---
class Employee {
    private String name;
    private ApprovalHandler manager; // Employee only knows their direct "handler"

    public Employee(String name, ApprovalHandler manager) {
        this.name = name;
        this.manager = manager;
    }

    public void submitExpense(ExpenseReport report) {
        System.out.println("Submitting " + NumberFormat.getCurrencyInstance().format(report.amount) + " for " + this.name);
        this.manager.process(report);
        System.out.println("Result: " + report.getStatus());
    }

    // --- Main method (Client Configuration) ---
    public static void main(String[] args) {

        // --- Build the approval chain ---
        Manager manager = new Manager();
        Director director = new Director();
        VP vp = new VP();
        CFO cfo = new CFO();

        // Link them in order: Manager -> Director -> VP -> CFO
        manager.setSuccessor(director);
        director.setSuccessor(vp);
        vp.setSuccessor(cfo);

        // --- Create a standard employee ---
        Employee dev = new Employee("Dev", manager);

        // --- Run Tests ---
        System.out.println("--- Test 1: Standard Employee, Small Expense ---");
        dev.submitExpense(new ExpenseReport(300.0));
        // Expected: Approved by Manager

        System.out.println("\n--- Test 2: Standard Employee, Medium Expense ---");
        dev.submitExpense(new ExpenseReport(3000.0));
        // Expected: Approved by Director

        System.out.println("\n--- Test 3: Standard Employee, Large Expense ---");
        dev.submitExpense(new ExpenseReport(30000.0));
        // Expected: Approved by CFO

        System.out.println("\n--- Test 4: Standard Employee, Large Expense ---");
        dev.submitExpense(new ExpenseReport(15000.0));
        // Expected: Approved by VP
    }
}
```

#### C++ version:

```c++
#include <iostream>
#include <string>
#include <memory>
#include <iomanip>
#include <sstream>

// --- The Request Object ---
class ExpenseReport {
public:
    const double amount;
private:
    std::string status;
public:
    ExpenseReport(double amt) : amount(amt), status("Pending") {}

    void approve(const std::string& approverName) { status = "Approved by " + approverName; }
    std::string getStatus() const { return status; }
};

// Helper for printing
std::string formatCurrency(double amount) {
    std::stringstream ss;
    ss << std::fixed << std::setprecision(2) << "$" << amount;
    return ss.str();
}

// --- The Abstract Handler ---
class ApprovalHandler {
protected:
    std::shared_ptr<ApprovalHandler> successor;
public:
    virtual ~ApprovalHandler() = default;

    void setSuccessor(std::shared_ptr<ApprovalHandler> succ) {
        this->successor = succ;
    }

    // Default "pass-along" behavior
    virtual void process(std::shared_ptr<ExpenseReport> report) {
        if (successor) {
            successor->process(report);
        } else {
            std::cout << "Expense for " << formatCurrency(report->amount) << " could not be approved." << std::endl;
        }
    }
};

// --- Concrete Handlers ---
class Manager : public ApprovalHandler {
private:
    const double APPROVAL_LIMIT = 500.0;
public:
    void process(std::shared_ptr<ExpenseReport> report) override {
        if (report->amount <= APPROVAL_LIMIT) {
            report->approve("Manager");
        } else {
            std::cout << "Manager: Passing " << formatCurrency(report->amount) << " up." << std::endl;
            ApprovalHandler::process(report); // Pass to next
        }
    }
};

class Director : public ApprovalHandler {
private:
    const double APPROVAL_LIMIT = 5000.0;
public:
    void process(std::shared_ptr<ExpenseReport> report) override {
        if (report->amount <= APPROVAL_LIMIT) {
            report->approve("Director");
        } else {
            std::cout << "Director: Passing " << formatCurrency(report->amount) << " up." << std::endl;
            ApprovalHandler::process(report); // Pass to next
        }
    }
};

class CFO : public ApprovalHandler {
public:
    void process(std::shared_ptr<ExpenseReport> report) override {
        // CFO can approve anything and is the end of the line
        report->approve("CFO");
    }
};

// --- The Client (Sender) ---
class Employee {
private:
    std::string name;
    std::shared_ptr<ApprovalHandler> manager; // Employee only knows their direct "handler"
public:
    Employee(const std::string& name, std::shared_ptr<ApprovalHandler> manager)
        : name(name), manager(manager) {}

    void submitExpense(std::shared_ptr<ExpenseReport> report) {
        std::cout << "Submitting " << formatCurrency(report->amount) << " for " << this->name << std::endl;
        this->manager->process(report);
        std::cout << "Result: " << report->getStatus() << std::endl;
    }
};

// --- Main function (Client Configuration) ---
int main() {

    // --- Build the approval chain ---
    auto manager = std::make_shared<Manager>();
    auto director = std::make_shared<Director>();
    auto cfo = std::make_shared<CFO>();

    // Link them in order: Manager -> Director -> CFO
    manager->setSuccessor(director);
    director->setSuccessor(cfo);

    // --- Create a standard employee ---
    Employee dev("Dev", manager);

    // --- Run Tests ---
    std::cout << "--- Test 1: Standard Employee, Small Expense ---" << std::endl;
    dev.submitExpense(std::make_shared<ExpenseReport>(300.0));
    // Expected: Approved by Manager

    std::cout << "\n--- Test 2: Standard Employee, Medium Expense ---" << std::endl;
    dev.submitExpense(std::make_shared<ExpenseReport>(3000.0));
    // Expected: Approved by Director

    std::cout << "\n--- Test 3: Standard Employee, Large Expense ---" << std::endl;
    dev.submitExpense(std::make_shared<ExpenseReport>(30000.0));
    // Expected: Approved by CFO

    return 0;
}
```
