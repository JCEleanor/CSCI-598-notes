import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

// Component interface
interface NetworkComponent {
    boolean performSecurityCheck();

    String getName();
}

// Composite classes (simplified for pattern focus)
class MainServer implements NetworkComponent {
    private List<NetworkComponent> components = new ArrayList<>();
    private String name;

    public MainServer(String name) {
        this.name = name;
    }

    public void add(NetworkComponent component) {
        components.add(component);
    }

    @Override
    public boolean performSecurityCheck() {
        System.out.println("Main Server: Performing basic security checks");
        boolean allPassed = true;
        for (NetworkComponent component : components) {
            if (!component.performSecurityCheck()) {
                allPassed = false;
            }
        }
        return allPassed;
    }

    @Override
    public String getName() {
        return name;
    }
}

class SubServer implements NetworkComponent {
    private List<NetworkComponent> components = new ArrayList<>();
    private String name;

    public SubServer(String name) {
        this.name = name;
    }

    public void add(NetworkComponent component) {
        components.add(component);
    }

    @Override
    public boolean performSecurityCheck() {
        System.out.println("Sub Server: Performing basic security checks");
        boolean allPassed = true;
        for (NetworkComponent component : components) {
            if (!component.performSecurityCheck()) {
                allPassed = false;
            }
        }
        return allPassed;
    }

    @Override
    public String getName() {
        return name;
    }
}

class ClientComputer implements NetworkComponent {
    private String name;

    public ClientComputer(String name) {
        this.name = name;
    }

    @Override
    public boolean performSecurityCheck() {
        System.out.println("Client Computer: Scanning malware, updating antivirus, applying patches");
        return true; // Simplified
    }

    @Override
    public String getName() {
        return name;
    }
}

// Base Decorator
abstract class SecurityDecorator implements NetworkComponent {
    protected NetworkComponent component;

    public SecurityDecorator(NetworkComponent component) {
        this.component = component;
    }

    @Override
    public boolean performSecurityCheck() {
        return component.performSecurityCheck();
    }

    @Override
    public String getName() {
        return component.getName();
    }
}

// Concrete Decorators
class EncryptionCheckDecorator extends SecurityDecorator {

    public EncryptionCheckDecorator(NetworkComponent component) {
        super(component);
    }

    @Override
    public boolean performSecurityCheck() {
        boolean baseResult = super.performSecurityCheck();
        boolean encryptionResult = performEncryptionCheck();
        System.out.println("Applied encryption check to " + getName());
        return baseResult && encryptionResult;
    }

    private boolean performEncryptionCheck() {
        System.out.println("Performing encryption validation check");
        return true; // Simplified
    }

    @Override
    public String getName() {
        return super.getName() + " (Encryption Check)";
    }
}

class DeepScanDecorator extends SecurityDecorator {

    public DeepScanDecorator(NetworkComponent component) {
        super(component);
    }

    @Override
    public boolean performSecurityCheck() {
        boolean baseResult = super.performSecurityCheck();
        boolean deepScanResult = performDeepScan();
        System.out.println("Applied deep scan to " + getName());
        return baseResult && deepScanResult;
    }

    private boolean performDeepScan() {
        System.out.println("Performing comprehensive deep security scan");
        return true; // Simplified
    }

    @Override
    public String getName() {
        return super.getName() + " (Deep Scan)";
    }
}

// Security Manager for dynamic behavior
class SecurityManager {
    private Map<String, Queue<Boolean>> failureHistory = new HashMap<>();
    private Set<String> sensitiveDataComponents = new HashSet<>();

    public void addSensitiveDataComponent(String componentName) {
        sensitiveDataComponents.add(componentName);
    }

    public NetworkComponent applyDynamicChecks(NetworkComponent component) {
        NetworkComponent decoratedComponent = component;

        // Apply encryption check for sensitive data components
        if (needsEncryptionCheck(component)) {
            decoratedComponent = new EncryptionCheckDecorator(decoratedComponent);
        }

        // Apply deep scan for components with recent failures
        if (needsDeepScan(component)) {
            decoratedComponent = new DeepScanDecorator(decoratedComponent);
        }

        return decoratedComponent;
    }

    public void recordCheckResult(NetworkComponent component, boolean result) {
        String name = component.getName();
        failureHistory.computeIfAbsent(name, k -> new LinkedList<>());

        Queue<Boolean> history = failureHistory.get(name);
        history.offer(result);

        // Keep only last 5 results
        if (history.size() > 5) {
            history.poll();
        }
    }

    private boolean needsEncryptionCheck(NetworkComponent component) {
        return sensitiveDataComponents.contains(component.getName().split(" \\(")[0]);
    }

    private boolean needsDeepScan(NetworkComponent component) {
        String name = component.getName().split(" \\(")[0];
        Queue<Boolean> history = failureHistory.get(name);

        if (history == null || history.size() < 5) {
            return false;
        }

        // Count failures in last 5 checks
        long failures = history.stream().mapToLong(result -> result ? 0 : 1).sum();
        return failures > 0;
    }
}

// Client usage example
public class SecuritySystemDemo {
    public static void main(String[] args) {
        // Create network hierarchy
        MainServer mainServer = new MainServer("Corporate Main Server");
        SubServer subServer1 = new SubServer("Finance Sub Server");
        SubServer subServer2 = new SubServer("HR Sub Server");
        ClientComputer client1 = new ClientComputer("Finance Workstation 1");
        ClientComputer client2 = new ClientComputer("HR Workstation 1");

        // Build hierarchy
        subServer1.add(client1);
        subServer2.add(client2);
        mainServer.add(subServer1);
        mainServer.add(subServer2);

        // Create security manager
        SecurityManager securityManager = new SecurityManager();
        securityManager.addSensitiveDataComponent("Finance Sub Server");
        securityManager.addSensitiveDataComponent("Finance Workstation 1");

        // Simulate some check failures
        securityManager.recordCheckResult(client2, false);
        securityManager.recordCheckResult(client2, false);

        // Apply dynamic decorators
        NetworkComponent decoratedSubServer1 = securityManager.applyDynamicChecks(subServer1);
        NetworkComponent decoratedClient2 = securityManager.applyDynamicChecks(client2);

        // Perform security checks
        System.out.println("=== Security Check Results ===");
        System.out.println("Sub Server 1 (with encryption): " + decoratedSubServer1.performSecurityCheck());
        System.out.println("Client 2 (with deep scan): " + decoratedClient2.performSecurityCheck());

        // Show decorator stacking
        NetworkComponent doubleDecorated = new DeepScanDecorator(
                new EncryptionCheckDecorator(client1));
        System.out.println("Double decorated client: " + doubleDecorated.performSecurityCheck());
    }
}