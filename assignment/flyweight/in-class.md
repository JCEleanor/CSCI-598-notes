// TODO: Define the ParticleType (Flyweight) class. Make its fields final.

// (e.g., private final String texture, private final String color)

#### flyweight

```java
public class ParticleType {

    // TODO: Fields and constructor
    private final String texture;
    private final String  color;

    public ParticleType (String texture, String color){
        this.texture = texture;
        this.color = color;
    }

    // note that we need the extrinsic states to perform the job
      public void render(double x, double y) {

          System.out.println("Drawing " + texture + " in color " + color + " at (" + x + ", " + y + ")");

      }
}
```

#### context

```java
// TODO: Define the Particle (Context) class.
// It should store extrinsic state like x, y, vx, vy.
// It should hold a reference to a ParticleType object.

// TODO: implement public void draw()

public class Particle {

    // TODO: Fields and constructor
    private double x, y vx, vy; // position
    private int velocity;
    private final ParticleType type;

    public Particle (int position, int velocity, ParticleType type){
        this.position = position;
        this.velocity = velocity;
        this.type = type;
    }

    public void draw(double x, double y) {
        type.render(x, y)
    }

    public void update(double vx, double vy) {
       x+=vx;
       y+=vy;
    }
 }
```

#### Flyweight Factory:

```java
import java.util.Hashtable;
import java.util.Map;

// Examine the code and see how sharing is realized.

// Note that the getType() method returns an existing flyweight or creates a new one.

public class ParticleTypeFactory {

  private static final Map<String, ParticleType> types = new Hashtable<>();

  public static synchronized ParticleType getType(String texture, String color) {
    String key = texture + "_" + color;
    if (!types.containsKey(key)) {
      types.put(key, new ParticleType(texture, color));
    }
    return types.get(key);
  }

}
```

#### Client code

```java
public class GameSimulation {

    public static void main(String[] args) {

        // A list to hold all Particle instances.

        List<Particle> particles = new ArrayList<>();


        // Create 50 "smoke_puff" particles using "smoke.png", "black" as attributes.
        // TODO: Get the shared type from the factory.
        // NOTE: use ParticleTypeFactory.getType
        ParticleType flyweightSmoke = ParticleTypeFactory.getType("smoke.png", "black");

        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(Math.random() * 100, Math.random() * 100, flyweightSmoke));
        }

        // Create 30 "red_spark" particles. "spark.png" and "red" attributes.
        // TODO: Get the other shared type from the factory.
        ParticleType flyweightSpark = ParticleTypeFactory.getType("spark.png", "red");
        // Loop 30 times to create and add red sparks to the particles.
        for (int i = 0; i < 30; i++) {
            particles.add(new Particle(Math.random() * 100, Math.random() * 100, flyweightSpark));
        }

        // Drawing all particles. Using flyweight from client's perspective.
        for (Particle p : particles) { p.draw(); }

        // Updating the scene. One unit of time is passed so update the particles.
        // Using flyweight from client's perspective.
        for (Particle p : particles) { p.update(); }
    }
}
```
