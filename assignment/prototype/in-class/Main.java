public class Main {
    public static void main(String[] args) {
        PortfolioRegistry registry = new PortfolioRegistry();

        // --- SETUP: Load the expensive prototypes ONCE ---
        Portfolio balanced = new Portfolio("Balanced");
        balanced.addStock("AAPL");
        balanced.addStock("GOOGL");
        registry.addPrototype("BalancedGrowth", balanced);
        // From now on, you will use this portfolio instance as the prototype.

        // --- DEMO 1: PERFORMANCE ---
        System.out.println("\n--- Performance Demo ---");
        System.out.println("Creating new object is slow...");
        new Portfolio("Slow Portfolio");
        System.out.println("Cloning an existing object is nearly instant.");
        // Example client use:
        // Calling the registry to create a clone of the "BalancedGrowth" prototype.
        registry.createClone("BalancedGrowth");

        // --- DEMO 2: DECOUPLING & INDEPENDENCE ---

        // Decoupling means the Main class (the client) doesn't need to know the
        // specific name of the class it's creating.
        // Without Prototype you would have to write new Portfolio("Balanced")
        // and know all the specific setup steps. Your Main class would be tightly
        // coupled to the Portfolio class.
        // With prototype, the Main class only needs to know a simple string,
        // "BalancedGrowth".

        System.out.println("\n--- Independence Demo ---");
        // TODO: Create a clone for Client 1 by calling the registry.

        Portfolio client1Portfolio = registry.createClone("BalancedGrowth");
        // TODO: Create another clone for Client 2.
        Portfolio client2Portfolio = registry.createClone("BalancedGrowth");

        // This will cause a NullPointerException until you complete the TODOs above.
        client1Portfolio.addStock("MSFT"); // Modify the first clone
        client1Portfolio.addStock("SOFI"); // Modify the second clone

        System.out.println("Original Prototype: " + balanced);
        System.out.println("Client 1 Portfolio: " + client1Portfolio);
        System.out.println("Client 2 Portfolio: " + client2Portfolio);

        // Independence:each cloned object is a completely separate, new object in
        // memory.
        // A change to one clone will not affect the original prototype or any other
        // clones.

        // --- DEMO 3: RUN-TIME CONFIGURATION ---
        System.out.println("\n--- Runtime Configuration Demo ---");
        // TODO: Create a clone to use as a base for a new prototype.
        Portfolio techPortfolio = registry.createClone("BalancedGrowth");

        // This will also cause a NullPointerException.
        techPortfolio.addStock("NVDA");
        techPortfolio.addStock("AMD");

        // register the updated clone as a new prototype
        registry.addPrototype("TechFocused", techPortfolio);

        // TODO: Create a clone of the new "TechFocused" prototype.
        Portfolio client3Portfolio = registry.createClone("TechFocused");
        System.out.println("New Tech Portfolio for Client 3: " + client3Portfolio);
    }
}