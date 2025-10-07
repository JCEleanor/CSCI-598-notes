import java.util.*;

class Player {
    String name;
    int health = 100, strength = 100;

    Player(String name) {
        this.name = name;

    }

    public void details() {
        System.out.println("Player name:" + name + " health: " + health +
                " strength: " + strength);
    }
}

// Flyweight
interface ICollectible {
    void draw(int x, int y);

    void activateEffect(Player p);

    void calculateValue();
}

// Concrete Flyweight
class Orb implements ICollectible {
    enum rarity {
        common, rare, scarce
    }

    enum effect {
        healing, increasesize
    }

    private effect effectType;
    private rarity rarityType;
    private int size;
    private String texture;

    Orb(effect effectType, rarity rarityType, int size, String texture)

    {
        this.effectType = effectType;
        this.rarityType = rarityType;
        this.size = size;
        this.texture = texture;
    }

    @Override
    public void draw(int x, int y) {
        System.out.println("Position of the orb is:" + x + "," + y);
    }

    @Override
    public void calculateValue() {
        int orbValue = 0;
        // rarity,size,effect
        switch (rarityType) {
            case common:
                orbValue += 5;
                break;
            case rare:
                orbValue += 15;
                break;
            case scarce:
                orbValue += 25;
                break;
        }
        switch (effectType) {
            case healing:
                orbValue += 5;
                break;
            case increasesize:
                orbValue += 15;
                break;
        }

        orbValue += size * 3;
        System.out.println("Orb Value : " + orbValue);
    }

    @Override
    public void activateEffect(Player p)

    {
        if (p == null) {
            return;
        } else {
            switch (effectType) {
                case healing:
                    p.health += 5;
                    break;
                case increasesize:
                    p.strength += 10;
                    break;
            }
            System.out.println("Effect " + effectType + " activated.");
        }
    }
}

// Concrete Flyweight
class Gem implements ICollectible {
    enum name {
        ruby, sapphire, emerald
    }

    enum rarity {
        common, rare, scarce
    }

    enum effect {
        strength, health
    }

    private int size;
    private String texture;
    private effect effectType;
    private rarity rarityType;
    private name gemType;

    Gem(name gemType, rarity rarityType, effect effectType, int size, String texture) {
        this.gemType = gemType;
        this.rarityType = rarityType;
        this.effectType = effectType;
        this.size = size;
        this.texture = texture;
    }

    @Override
    public void draw(int x, int y) {
        System.out.println("Position of the gem is:" + x + "," + y);
    }

    @Override
    public void calculateValue() {
        int gemValue = 0;
        // rarity,size,name
        switch (rarityType) {
            case common:
                gemValue += 5;
                break;
            case rare:
                gemValue += 15;
                break;
            case scarce:
                gemValue += 25;
                break;
        }
        switch (gemType) {
            case ruby:
                gemValue += 5;
                break;
            case sapphire:
                gemValue += 15;
                break;
            case emerald:
                gemValue += 25;
                break;
        }
        gemValue += size * 3;
        System.out.println("Gem value is: " + gemValue);
    }

    @Override
    public void activateEffect(Player p) {
        if (p == null) {
            return;
        } else {
            switch (effectType) {
                case strength:
                    p.strength += 5;
                    break;

                case health:
                    p.health += 10;
                    break;
            }
            System.out.println("Effect " + effectType + " activated.");
        }
    }
}

class CollectibleExtrinsicState {
    private int x, y;
    private Player bearer;
    private ICollectible type;

    public CollectibleExtrinsicState(ICollectible type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        type.draw(x, y);
    }

    public void setbarer(Player p) {
        this.bearer = p;
        type.activateEffect(bearer);
    }

    public void move(int xPosition, int yPosition) {
        x += xPosition;
        y += yPosition;
        System.out.print("Item moved,New ");
        type.draw(x, y);
    }
}
// Flyweight Factory

class CollectibleFactory {
    private static Map<String, ICollectible> cache = new HashMap<>();

    public static ICollectible getOrb(Orb.effect effectType,
            Orb.rarity rarityType, int size, String texture) {
        String key = "orb-" + effectType + "-" + size + "-" + rarityType + "-" + texture;
        ICollectible orb = cache.get(key);
        if (orb == null) {
            orb = new Orb(effectType, rarityType, size, texture);
            cache.put(key, orb);
        }
        return orb;
    }

    public static ICollectible getGem(Gem.name gemType, Gem.rarity rarityType,
            Gem.effect effectType, int size, String texture) {
        String key = "gem-" + gemType + "-" + size + "-" + rarityType + "-" + texture + "-" + effectType;
        ICollectible gem = cache.get(key);
        if (gem == null) {
            gem = new Gem(gemType, rarityType, effectType, size, texture);
            cache.put(key, gem);
        }
        return gem;
    }
}

// Client
class GameDemo {
    public static void main(String[] args) {
        Player player1 = new Player("Denise");
        ICollectible orb1 = CollectibleFactory.getOrb(Orb.effect.healing,
                Orb.rarity.rare, 5, "orb.png");
        ICollectible gem1 = CollectibleFactory.getGem(Gem.name.emerald,
                Gem.rarity.common, Gem.effect.strength, 5, "gem.png");
        CollectibleExtrinsicState c1 = new CollectibleExtrinsicState(orb1, 2, 3);

        System.out.println("-----------------Orb details----------------- ");
        orb1.calculateValue();
        c1.draw();
        player1.details();
        c1.setbarer(player1);
        c1.move(3, 5);
        player1.details();
        CollectibleExtrinsicState c2 = new CollectibleExtrinsicState(orb1, 6, 7);
        c2.draw();
        player1.details();
        c2.setbarer(player1);
        player1.details();
        CollectibleExtrinsicState g1 = new CollectibleExtrinsicState(gem1, 9, 10);
        System.out.println("-----------------Gem details----------------- ");
        gem1.calculateValue();
        g1.draw();
        player1.details();
        g1.setbarer(player1);
        player1.details();
    }
}
