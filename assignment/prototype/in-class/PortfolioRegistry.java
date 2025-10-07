import java.util.HashMap;
import java.util.Map;
import assignment.prototype.*;

// This registry allows us to avoid a large class hierarchy of factories.
// We have one manager that can produce clones of any registered prototype.

public class PortfolioRegistry {
    private Map<String, Portfolio> prototypes = new HashMap<>();

    public void addPrototype(String key, Portfolio prototype) {
        // TODO: Add the prototype to the map.
        this.prototypes.put(key, prototype);
    }

    public Portfolio createClone(String key) {
        // TODO: Get the prototype from the map and return a CLONE of it.
        return this.prototypes.get(key).clone();

        // BEWARE: prototype is not flyweight! There is no sharing of instances
        // after retrieving the correct object from the map
        // you must clone it and return the clone.
        // flyweight returns the object itself, not the clone. that makes the sharing.
    }
}