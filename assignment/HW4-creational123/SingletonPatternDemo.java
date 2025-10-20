import java.util.HashMap;

class ConfigManager {
    private static ConfigManager instance = null;
    public HashMap<String, String> dataMap;

    protected ConfigManager(String key) {
        try {
            System.out.println("Initiate Config manager for the first time" + key);
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        this.dataMap = new HashMap<>();
        this.dataMap.put(key, "SomeValue");
    }

    private void loadKeyData(String key) {
        if (!this.dataMap.containsKey(key)) {
            this.dataMap.put(key, "loadKeyData" + key);
        }
    }

    public static synchronized ConfigManager getInstance(String key) {
        if (instance == null) {

            instance = new ConfigManager(key);
        } else {
            instance.loadKeyData(key);
        }
        return instance;
    }

    public String lookup(String key) {
        return this.dataMap.getOrDefault(key, "KeyNotFound");
    }
}

public class SingletonPatternDemo {
    public static void main(String[] args) {
        ConfigManager singleton = ConfigManager.getInstance("FOO");
        ConfigManager anotherSingleton = ConfigManager.getInstance("BAR");

        System.out.println("singleton.dataMap:        " + singleton.dataMap);
        System.out.println("anotherSingleton.dataMap: " + anotherSingleton.dataMap);

        System.out.println("Lookup for FOO: " + singleton.lookup("FOO"));
        System.out.println("Lookup for BAR: " + anotherSingleton.lookup("BAR"));
    }
}