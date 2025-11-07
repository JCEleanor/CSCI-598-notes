
interface IMoveStrategy {
    void executeMove(Creature creature);
}

class FlyStrategy implements IMoveStrategy {
    @Override
    public void executeMove(Creature creature) {
        System.out.println(creature.getName() + " flies");
    }
}

class TeleportStrategy implements IMoveStrategy {
    @Override
    public void executeMove(Creature creature) {
        System.out.println(creature.getName() + " telesports");
    }
}

class RunStrategy implements IMoveStrategy {
    @Override
    public void executeMove(Creature creature) {
        System.out.println(creature.getName() + " runs");
    }
}

class Creature {
    private String name;
    private int position;

    // composition: The creature "has" a movement strategy
    private IMoveStrategy moveStrategy;

    public Creature(String name, IMoveStrategy initialStrategy) {
        this.name = name;
        this.position = 0;
        this.moveStrategy = initialStrategy; // strategy is injected
    }

    // delegates work
    public void move() {
        this.moveStrategy.executeMove(this);
    }

    public String getName() {
        return this.name;
    }
}

public class client {

    public static void main(String[] args) {
        // reusable strategy objects
        IMoveStrategy flying = new FlyStrategy();
        IMoveStrategy teleporting = new TeleportStrategy();
        IMoveStrategy running = new RunStrategy();

        Creature dragon = new Creature("Dragon", flying);
        Creature mage = new Creature("Mage", teleporting);
        Creature goblin = new Creature("Goblin", running);

        dragon.move();
        mage.move();
        goblin.move();

    }
}