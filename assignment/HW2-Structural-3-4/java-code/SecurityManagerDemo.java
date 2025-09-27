import java.util.*;

// Component Interface
interface NetworkComponent {
    String getName();

    void scanForMalware();

    void updateAntivirus();

    void applySecurityPatches();

    void recordCheckResult(boolean passed);

    boolean needsDeepScan();
}

// Leaf: Client Computer
class ClientComputer implements NetworkComponent {
    private String name;
    private Queue<Boolean> lastFiveChecks = new LinkedList<>();

    public ClientComputer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void recordCheckResult(boolean passed) {
        if (lastFiveChecks.size() >= 5) {
            lastFiveChecks.poll(); // Remove oldest result
        }
        lastFiveChecks.offer(passed);

        System.out.println(name + " check result: " + (passed ? "PASSED" : "FAILED"));
        System.out.println(name + " last " + lastFiveChecks.size() + " checks: " + lastFiveChecks);
    }

    @Override
    public boolean needsDeepScan() {
        // Needs deep scan if any of the last 5 checks failed
        for (Boolean result : lastFiveChecks) {
            if (!result) {
                return true;
            }
        }
        return false;
    }

    // Implement the basic checks for the leaf node
    @Override
    public void scanForMalware() {
        System.out.println("Client " + name + ": Scanning for malware.");
    }

    @Override
    public void updateAntivirus() {
        System.out.println("Client " + name + ": Updating antivirus software.");
    }

    @Override
    public void applySecurityPatches() {
        System.out.println("Client " + name + ": Applying security patches.");
    }

}

// Abstract Composite: AbstractServer
abstract class AbstractServer implements NetworkComponent {
    protected String name;
    protected List<NetworkComponent> children = new ArrayList<>();
    private Queue<Boolean> lastFiveChecks = new LinkedList<>();

    public AbstractServer(String name) {
        this.name = name;
    }

    public void add(NetworkComponent c) {
        children.add(c);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void recordCheckResult(boolean passed) {
        if (lastFiveChecks.size() >= 5) {
            lastFiveChecks.poll();
        }
        lastFiveChecks.offer(passed);

        System.out.println(name + " check result: " + (passed ? "PASSED" : "FAILED"));
        System.out.println(name + " last " + lastFiveChecks.size() + " checks: " + lastFiveChecks);
    }

    @Override
    public boolean needsDeepScan() {
        for (Boolean result : lastFiveChecks) {
            if (!result) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void scanForMalware() {
        System.out.println("Server " + name + ": Initiating malware scan");
        for (NetworkComponent c : children) {
            c.scanForMalware();
        }
    }

    @Override
    public void updateAntivirus() {
        System.out.println("Server " + name + ": Initiating antiVirus update");
        for (NetworkComponent c : children) {
            c.updateAntivirus();
        }
    }

    @Override
    public void applySecurityPatches() {
        System.out.println("Server " + name + ": Initiating security patch");
        for (NetworkComponent c : children) {
            c.applySecurityPatches();
        }
    }
}

class MainServer extends AbstractServer {
    public MainServer(String name) {
        super(name);
    }
}

class SubServer extends AbstractServer {
    public SubServer(String name) {
        super(name);
    }
}

abstract class SecurityDecorator implements NetworkComponent {
    protected NetworkComponent component;

    public SecurityDecorator(NetworkComponent component) {
        this.component = component;
    }

    @Override
    public String getName() {
        return component.getName();
    }

    @Override
    public void recordCheckResult(boolean passed) {
        component.recordCheckResult(passed);
    }

    @Override
    public boolean needsDeepScan() {
        return component.needsDeepScan();
    }

    @Override
    public void scanForMalware() {
        component.scanForMalware();
    }

    @Override
    public void updateAntivirus() {
        component.updateAntivirus();
    }

    @Override
    public void applySecurityPatches() {
        component.applySecurityPatches();
    }
}

// Encryption Check Decorator (Components with sensitive data)
class EncryptionCheckDecorator extends SecurityDecorator {
    public EncryptionCheckDecorator(NetworkComponent c) {
        super(c);
    }

    @Override
    public void scanForMalware() {
        System.out.println(component.getName() + " Running Encryption Check");
        super.scanForMalware();
    }
}

// Deep Scan Check Decorator (Components that recently failed checks)
class DeepScanCheckDecorator extends SecurityDecorator {
    public DeepScanCheckDecorator(NetworkComponent c) {
        super(c);
    }

    @Override
    public void scanForMalware() {
        System.out.println(component.getName() + " Running Deep Scan");
        super.scanForMalware();
    }
}

public class SecurityManagerDemo {
    private static Random random = new Random();

    // Simulate security check results
    private static boolean simulateSecurityCheck() {
        return random.nextBoolean();
    }

    // Utility function to apply decorators based on component needs
    private static NetworkComponent applyDecorators(NetworkComponent component, boolean hasSensitiveData) {
        NetworkComponent decorated = component;

        // Apply encryption check for components with sensitive data
        if (hasSensitiveData) {
            System.out.println("Applying Encryption Check decorator to " + component.getName());
            decorated = new EncryptionCheckDecorator(decorated);
        }

        // Apply deep scan for components that failed recent checks
        if (component.needsDeepScan()) {
            System.out.println("Applying Deep Scan decorator to " + component.getName());
            decorated = new DeepScanCheckDecorator(decorated);
        }

        return decorated;
    }

    public static void main(String[] args) {
        MainServer mainServer = new MainServer("MainServer");
        SubServer subServerA = new SubServer("subServerA");
        ClientComputer client3 = new ClientComputer("client3");
        ClientComputer client4 = new ClientComputer("client4");

        subServerA.add(client4);
        subServerA.add(client3);
        mainServer.add(subServerA);

        // Simulate 6 rounds of security checks
        for (int round = 1; round <= 6; round++) {
            System.out.println("Round " + round);

            // Simulate check results for each component
            boolean mainServerResult = simulateSecurityCheck();
            boolean subServerResult = simulateSecurityCheck();
            boolean client3Result = simulateSecurityCheck();
            boolean client4Result = simulateSecurityCheck();

            // Record results
            mainServer.recordCheckResult(mainServerResult);
            subServerA.recordCheckResult(subServerResult);
            client3.recordCheckResult(client3Result);
            client4.recordCheckResult(client4Result);

            // Apply decorators dynamically based on needs
            NetworkComponent _decoratedClient3 = applyDecorators(client3, true);
            NetworkComponent _decoratedClient4 = applyDecorators(client4, false);
            NetworkComponent _decoratedSubServer = applyDecorators(subServerA, true);
            NetworkComponent _decoratedMainServer = applyDecorators(mainServer, true);

            // Can add more checks based on needs:
            // decoratedClient3.scanForMalware();
            System.out.println("\n");
        }
    }
}