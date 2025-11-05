import java.util.ArrayList;
import java.util.List;

interface Villager {
    void wander();
}

interface Warrior {
    void train();

    void fight();
}

class FireVillager implements Villager {
    @Override
    public void wander() {
        System.out.println("Fire Villager is harvesting fire.");
    }
}

class FireWarrior implements Warrior {
    @Override
    public void train() {
        System.out.println("Fire Warrior is training");
    }

    @Override
    public void fight() {
        System.out.println("Fire Warrior throws fire!");
    }
}

class AirVillager implements Villager {
    @Override
    public void wander() {
        System.out.println("Air Villager is catching food.");
    }
}

class AirWarrior implements Warrior {
    @Override
    public void train() {
        System.out.println("Air Warrior is training");
    }

    @Override
    public void fight() {
        System.out.println("Air Warrior makes a storm!");
    }
}

class IceVillager implements Villager {
    @Override
    public void wander() {
        System.out.println("Ice Villager is building an igloo.");
    }
}

class IceWarrior implements Warrior {
    @Override
    public void train() {
        System.out.println("Ice Warrior is training in the cold.");
    }

    @Override
    public void fight() {
        System.out.println("Ice Warrior freezes the target!");
    }
}

interface TribeFactory {
    Villager createVillager();

    Warrior createWarrior();
}

class FireTribeFactory implements TribeFactory {
    @Override
    public Villager createVillager() {
        return new FireVillager();
    }

    @Override
    public Warrior createWarrior() {
        return new FireWarrior();
    }
}

class AirTribeFactory implements TribeFactory {
    @Override
    public Villager createVillager() {
        return new AirVillager();
    }

    @Override
    public Warrior createWarrior() {
        return new AirWarrior();
    }
}

class IceTribeFactory implements TribeFactory {
    @Override
    public Villager createVillager() {
        return new IceVillager();
    }

    @Override
    public Warrior createWarrior() {
        return new IceWarrior();
    }
}

public class GameEngine {
    public static void main(String[] args) {
        List<TribeFactory> tribes = new ArrayList<>();
        tribes.add(new FireTribeFactory());
        tribes.add(new AirTribeFactory());
        tribes.add(new IceTribeFactory());

        for (TribeFactory tribe : tribes) {
            Villager villager = tribe.createVillager();
            Warrior warrior = tribe.createWarrior();

            villager.wander();
            warrior.train();
            warrior.fight();

            System.out.println();
        }
    }
}