```java
// Part 1: Implement the Singleton pattern using Double-Checked Locking (DCL).
public class ConfigurationManager {

    // TODO 1: Declare the private static volatile instance field.
    private static

    // Placeholder data
    private String resolution;

    // TODO 2: Define a private constructor to prevent outside instantiation.


    // Thread-safe, lazy initialization using DCL
    public static ConfigurationManager getInstance() {
        // the goal of double condition/checks is to make sure there's only one creation
        // first thread
        if (instance == null) {
            // takes the lock (?)
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    public String getResolution() {
        return resolution;
    }
}
```

#### OPTION B: using Enum

```java
public enum ConfigurationManager {

    // The single instance is declared here. This is intrinsically thread-safe,
    // prevents reflection attacks, and serializes correctly.
    INSTANCE;

    // Placeholder data
    private final String resolution;

    // TODO: implement the constructor. set some value to the resolution

    // Access method (we only need the getter for the data)
    public String getResolution() {
        return resolution;
    }
}
```
