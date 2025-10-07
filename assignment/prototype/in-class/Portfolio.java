import java.util.ArrayList;
import java.util.List;

interface Cloneable {
    Cloneable clone();
}

public class Portfolio implements Cloneable {
    private String name;
    private List<String> stocks = new ArrayList<>();

    public Portfolio(String name) {
        this.name = name;
        // POINT: When creating an object is expensive but copying is cheap, the
        // Prototype is far more efficient.

        // The following simulates a slow operation, like a database query or web
        // access.
        try {
            System.out.println("Creating Portfolio '" + name + "' (Costly operation...)");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addStock(String stock) {
        this.stocks.add(stock);
    }

    public String toString() {
        return "Portfolio [name=" + name + ", stocks=" + stocks.hashCode() + ":" + stocks + "]";
    }

    public Portfolio clone() {
        try {
            // Step 1: Call super.clone(). This performs a "shallow copy."
            // 'name' (String/immutable) is fine, but 'stocks' (ArrayList) is only a
            // copied reference.
            /**
             * invokes the default clone() method inherited from java.lang.Object
             * This method performs a field-by-field copy of the original object's state.
             */
            Portfolio clonedPortfolio = (Portfolio) super.clone();

            // Step 2: Fix the shallow copy (Deep Copy).
            // We create a new ArrayList and copy all elements from the original
            // 'stocks' list into the new one.
            clonedPortfolio.stocks = new ArrayList<>(this.stocks);

            return clonedPortfolio;

        } catch (CloneNotSupportedException e) {
            // Should not happen as we implement Clonable
            throw new InternalError("Portfolio cloning failed: " + e.getMessage());
        }
    }
}