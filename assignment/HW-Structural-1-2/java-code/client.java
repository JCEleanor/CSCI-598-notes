
import java.util.ArrayList;
import java.util.List;

interface BuildingComponent {
    void repair();

    void destroy();
}

// leaf
class Wall implements BuildingComponent {
    String name;

    public Wall(String name) {
        this.name = name;
    }

    // @Override
    public void repair() {
        System.out.println("Repairing " + name);
    }

    // @Override
    public void destroy() {
        System.out.println("Destroying " + name);
    }
}

class Roof implements BuildingComponent {
    String name;

    public Roof(String name) {
        this.name = name;
    }

    // @Override
    public void repair() {
        System.out.println("Repairing " + name);
    }

    // @Override
    public void destroy() {
        System.out.println("Destroying " + name);
    }
}

class Gate implements BuildingComponent {
    String name;

    public Gate(String name) {
        this.name = name;
    }

    // @Override
    public void repair() {
        System.out.println("Repairing " + name);
    }

    // @Override
    public void destroy() {
        System.out.println("Destroying " + name);
    }
}

/**
 * New Component: Imagine a new building component, a Cannon.
 * A Cannon is a single piece of equipment that can be added to a Fortress.
 */
class Cannon implements BuildingComponent {
    String name;

    public Cannon(String name) {
        this.name = name;
    }

    // @Override
    public void repair() {
        System.out.println("Repairing " + name);
    }

    // @Override
    public void destroy() {
        System.out.println("Destroying " + name);
    }
}

// composite
class CompositeStructure implements BuildingComponent {
    private String name;
    private List<BuildingComponent> children = new ArrayList<>();

    public CompositeStructure(String name) {
        this.name = name;
    }

    public void add(BuildingComponent component) {
        children.add(component);
    }

    public void remove(BuildingComponent component) {
        children.remove(component);
    }

    @Override
    public void repair() {
        System.out.println("Repairing " + name);
        for (BuildingComponent c : children) {
            c.repair();
        }
    }

    @Override
    public void destroy() {
        System.out.println("Destroying " + name);
        for (BuildingComponent c : children) {
            c.destroy();
        }
    }
}

class Fortess extends CompositeStructure {
    public Fortess(String name) {
        super(name);
    }
}

class Tower extends CompositeStructure {
    public Tower(String name) {
        super(name);
    }
}

class RepairService {
    public void performRepair(BuildingComponent component) {
        component.repair();
    }
}

public class client {
    public static void main(String[] args) {

        /**
         * Building a Structure:
         * Create a Fortress object with several walls and then create a
         * Tower object with walls and a roof and make it a part of the Fortress.
         */
        Fortess fortress = new Fortess("Fortress");
        fortress.add(new Wall("Fortress Wall 1"));
        fortress.add(new Wall("Fortress Wall 2"));

        Tower tower = new Tower("tower");
        tower.add(new Wall("Tower Wall 1"));
        tower.add(new Wall("Tower Wall 2"));

        fortress.add(tower);

        /**
         * Repair Service(using Dependency Injection):
         */
        RepairService repairService = new RepairService();
        // Repair single Wall
        Wall singleWall = new Wall("Single Wall");
        repairService.performRepair(singleWall);

        /** Extensibility in Action: adding a cannon without modifying repairService */

        Cannon cannon = new Cannon("Cannon 1");
        // Add new Cannon component to the fortress
        fortress.add(cannon);

        // Repair a fortress
        repairService.performRepair(fortress);

    }
}
