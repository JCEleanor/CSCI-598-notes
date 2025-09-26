import java.util.ArrayList;
import java.util.List;

// Component interface
interface NetworkComponent {
    void performSecurityCheck();
}

// Leaf
class ClientComputer implements NetworkComponent {
    private String name;

    public ClientComputer(String name) {
        this.name = name;
    }

    @Override
    public void performSecurityCheck() {
        System.out.println(name + ": Basic security checks");
    }
}

// Composite
class SubServer implements NetworkComponent {
    private String name;
    private List<NetworkComponent> children = new ArrayList<>();

    public SubServer(String name) {
        this.name = name;
    }

    public void add(NetworkComponent c) {
        children.add(c);
    }

    @Override
    public void performSecurityCheck() {
        System.out.println(name + ": Basic security checks");
        for (NetworkComponent c : children) {
            c.performSecurityCheck();
        }
    }
}

// Composite
class MainServer implements NetworkComponent {
    private String name;
    private List<NetworkComponent> children = new ArrayList<>();

    public MainServer(String name) {
        this.name = name;
    }

    public void add(NetworkComponent c) {
        children.add(c);
    }

    @Override
    public void performSecurityCheck() {
        System.out.println(name + ": Basic security checks");
        for (NetworkComponent c : children) {
            c.performSecurityCheck();
        }
    }
}

abstract class SecurityDecorator implements NetworkComponent {
    protected NetworkComponent component;

    public SecurityDecorator(NetworkComponent component) {
        this.component = component;
    }

    public void performSecurityCheck() {
        component.performSecurityCheck();
    }

}

// Servers handling sensitive data must perform an additional encryption check.
class EncryptionCheckDecorator extends SecurityDecorator {
    public EncryptionCheckDecorator(NetworkComponent c) {
        super(c);
    }

    @Override
    public void performSecurityCheck() {
        super.performSecurityCheck();
        System.out.println("Encryption check performed\n");
    }
}

// Any component that has failed any of the last five security checks requires
// an extra deep security scan.
class DeepScanCheckDecorator extends SecurityDecorator {
    public DeepScanCheckDecorator(NetworkComponent c) {
        super(c);
    }

    @Override
    public void performSecurityCheck() {
        super.performSecurityCheck();
        System.out.println("Deep scan performed\n");
    }
}

// Client usage example
public class client {
    public static void main(String[] args) {
        // Create network hierarchy
        MainServer mainServer1 = new MainServer("Main Server 1");
        SubServer subServer1 = new SubServer("Sub Server 1");
        SubServer subServer2 = new SubServer("Sub Server 2");
        ClientComputer client1 = new ClientComputer("Client Workstation 1");
        ClientComputer client2 = new ClientComputer("Client Workstation 2");

        // Build hierarchy
        subServer1.add(client1);
        subServer2.add(client2);
        mainServer1.add(subServer1);
        mainServer1.add(subServer2);

        // undecoorated check
        mainServer1.performSecurityCheck();

        System.out.println("\n================================\n");

        // applying encryption check to main server
        SecurityDecorator securityCheck = new EncryptionCheckDecorator(mainServer1);
        securityCheck.performSecurityCheck();

        System.out.println("\n================================\n");

        // applying deep scan a& encryption check to sub server 2
        MainServer mainServer2 = new MainServer("Main Server 2");
        mainServer2.add(new SubServer("Sub Server 3"));
        SecurityDecorator multipleChecks = new DeepScanCheckDecorator(new EncryptionCheckDecorator(mainServer2));
        multipleChecks.performSecurityCheck();
    }
}