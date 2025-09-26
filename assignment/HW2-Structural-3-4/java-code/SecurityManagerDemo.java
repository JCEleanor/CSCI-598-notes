import java.util.ArrayList;
import java.util.List;

// Component Interface
interface NetworkComponent {
    String getName();

    void scanForMalware();

    void updateAntivirus();

    void applySecurityPatches();
}

// Leaf: Client Computer
class ClientComputer implements NetworkComponent {
    private String name;

    public ClientComputer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
    public void scanForMalware() {
        System.out.println("Server " + name + ": Initiating malware scan for sub-network");
        for (NetworkComponent c : children) {
            c.scanForMalware();
        }
    }

    @Override
    public void updateAntivirus() {
        System.out.println("Server " + name + ": Initiating antiVirus update for sub-network");
        for (NetworkComponent c : children) {
            c.updateAntivirus();
        }
    }

    @Override
    public void applySecurityPatches() {
        System.out.println("Server " + name + ": Initiating security patch for sub-network");
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
        System.out.println(component.getName() + ": Running Encryption Check");
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
        System.out.println(component.getName() + ": Running Deep Scan");
        super.scanForMalware();
    }
}

public class SecurityManagerDemo {
    public static void main(String[] args) {
        MainServer mainServer = new MainServer("MainServer");
        SubServer subServerA = new SubServer("subServerA");
        ClientComputer client3 = new ClientComputer("client3");
        ClientComputer client4 = new ClientComputer("client4");

        subServerA.add(client4);

        // add multiple security checks to client3
        NetworkComponent decoratedClient3 = new EncryptionCheckDecorator(new DeepScanCheckDecorator(client3));

        subServerA.add(decoratedClient3);
        mainServer.add(subServerA);

        mainServer.scanForMalware();

    }
}