import java.util.HashMap;
import java.util.Map;

// Part 1: Implement the Singleton pattern using Double-Checked Locking (DCL).
// public class ConfigurationManager {

//     // TODO 1: Declare the private static volatile instance field.
//     private static volatile ConfigurationManager instance;

//     // Placeholder data
//     private String resolution;

//     // TODO 2: Define a private constructor to prevent outside instantiation.
//     private ConfigurationManager() {
//         // some implementation
//         System.out.println("ConfigurationManager initialized");
//     }

//     // Thread-safe, lazy initialization using DCL
//     public static ConfigurationManager getInstance() {
//         if (instance == null) {
//             synchronized (ConfigurationManager.class) {
//                 if (instance == null) {
//                     instance = new ConfigurationManager();
//                 }
//             }
//         }
//         return instance;
//     }

//     public String getResolution() {
//         return resolution;
//     }
// }

// option B: enum
// I took off "public" to run this in a single file
enum ConfigurationManager {
    // The single instance is declared here. This is intrinsically thread-safe,
    // prevents reflection attacks, and serializes correctly.
    INSTANCE;

    // Placeholder data
    private final String resolution;

    // TODO: implement the constructor. set some value to the resolution
    ConfigurationManager() {
        this.resolution = "1920x1080";
        System.out.println("ConfigurationManager initialized (Enum).");
    }

    // Access method (we only need the getter for the data)
    public String getResolution() {
        return resolution;
    }
}

// =================================================================
// PART 2: Service Registry and Subclassing (Completed implementation)
// The Service class acts as the Singleton Registry.
// =================================================================

// Base class acting as the Registry holder
abstract class Service {

    // The central registry map: stores a unique instance for each service key
    private static final Map<String, Service> REGISTRY = new HashMap<String, Service>();

    // Protected constructor: ensures the instance registers itself upon creation
    protected Service(String key, Service instance) {
        if (REGISTRY.containsKey(key)) {
            throw new IllegalStateException("Service already registered for key: " + key);
        }
        REGISTRY.put(key, instance);
        System.out.println("Registered service: " + key);
    }

    // Public static retrieval method
    public static Service getService(String key) {
        return REGISTRY.get(key);
    }

    public abstract void activate();
}

// CONCRETE SINGLETON SUBCLASS 1: Logger
class Logger extends Service {
    private static final Logger INSTANCE = new Logger();

    private Logger() {
        super("Logger", INSTANCE);
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    @Override
    public void activate() {
        System.out.println("Logger: Performing logging operations.");
    }
}

// CONCRETE SINGLETON SUBCLASS 2: AssetManager
class AssetManager extends Service {
    // TODO
    private static final AssetManager INSTANCE = new AssetManager();

    private AssetManager() {
        super("something", INSTANCE);
    }

    public static AssetManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void activate() {
        System.out.println("Logger: Performing logging operations.");
    }
}

// =================================================================
// CLIENT CODE SIMULATION (Part 3)
// =================================================================

public class GameSimulation {
    public static void main(String[] args) {
        System.out.println("--- Part 1: ConfigurationManager (Access Test) ---");

        // TODO: Get the first instance of ConfigurationManager.
        ConfigurationManager configA = ConfigurationManager.INSTANCE;

        // TODO: Get the second instance of ConfigurationManager.
        ConfigurationManager configB = ConfigurationManager.INSTANCE;

        System.out.println("Config A Resolution: " + configA.getResolution());
        System.out.println("Are A and B the same instance? " + (configA == configB));
        System.out.println("-----------------------------------------------------");

        System.out.println("\n--- Part 2: Singleton Registry Test ---");

        // 1. Force the instantiation (and thus registration) of the two services
        Logger.getInstance().activate();
        AssetManager.getInstance().activate();

        System.out.println("-----------------------------------------------------");

        // 2. Retrieve the instances using the generic Registry getter
        Service s1 = Service.getService("Logger");
        /* TODO: make s2 an "AssetManager" using the Service */
        Service s2 = Service.getService("AssetManager");

        // 3. Test for Singleton property using the registry
        /* TODO: make s3 a "Logger" using the Service */
        Service s3 = Service.getService("Logger");

        System.out.println("Registry Retrieval Test:");
        System.out.println("Retrieved s1 is a Logger? " + (s1 instanceof Logger));
        System.out.println("Retrieved s2 is an AssetManager? " + (s2 instanceof AssetManager));
        System.out.println("Retrieval from registry is consistent? " + (s1 == s3)); // Must be true
    }
}