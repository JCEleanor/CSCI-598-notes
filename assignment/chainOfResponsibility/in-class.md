### Part 1: Design and Code for Analysis

Read the complete, working code provided below. Both Java and C++ versions do the same. This code was written to implement the scenario above using the chain of responsibility.

Answer the following questions based on the code given.

1. The Chain: In a $3000 expense, trace the exact path of the request. Which process() method is called first? Which one is called second? Which one is called third? Why did the Manager's process() method call super.process()

**Manager -> Director**

2. Decoupling: The scenario requires that the Employee (Sender) should not be coupled to the approval hierarchy. Look at the Employee class. How does it achieve this? (Hint: What is the type of the manager field? Is it Manager or ApprovalHandler?)

3. Extensibility: A new role, "VP (Vice President)", is added between Director and CFO. They can approve up to $20,000. What specific lines of code in main() would you change to add this new rule? Do you have to change the Employee, Manager, or Director classes at all?

4. Evaluating the "Bad" Alternative:
   Now, imagine the "bad" alternative (no CoR). The Employee::submitExpense method would have a giant if/else block. If the company adds a new role, what classes would you have to change? How does the provided solution (CoR) show it is a better design?

   **`if/else` is not open for extension, and it's breaking SRP.**

5. Runtime Configuration:
   A new "Fast-Track Employee" needs to be hired. Their expenses should skip the Manager and go straight to the Director for approval. What single line of code would you write inside the main method to create this new employee? Do you do need to change any handler classes?

### Part 2: Coding Extension (after class)

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
