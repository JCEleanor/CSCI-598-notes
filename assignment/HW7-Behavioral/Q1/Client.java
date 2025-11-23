import java.util.ArrayList;
import java.util.List;

// Visitor interface
interface Visitor {
    void visit(SDNController controller);

    void visit(NetworkSwitch sw);

    void visit(NetworkFunction nf);
}

// Visitable interface
interface Visitable {
    void accept(Visitor visitor);
}

abstract class NetworkComponent {
    protected String id;

    public NetworkComponent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class Host extends NetworkComponent {
    public Host(String id) {
        super(id);
    }
}

class SDNController extends NetworkComponent implements Visitable {
    public SDNController(String id) {
        super(id);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    // Mock methods for metrics
    public double getCpuUsage() {
        return 50.0;
    }

    public double getMemoryStat() {
        return 60.0;
    }

    public double getBandwidthUsage() {
        return 20.0;
    }

    public double getControlPlaneLatency() {
        return 0.5;
    }

    public int getFlowRulesInstalled() {
        return 1000;
    }
}

class NetworkSwitch extends NetworkComponent implements Visitable {
    public NetworkSwitch(String id) {
        super(id);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    // Mock methods for metrics
    public double getCpuUsage() {
        return 30.0;
    }

    public double getMemoryStat() {
        return 40.0;
    }

    public double getBandwidthUsage() {
        return 80.0;
    }

    public long getPacketsForwarded() {
        return 100000;
    }

    public long getPacketsDropped() {
        return 100;
    }
}

class NetworkFunction extends NetworkComponent implements Visitable {
    public NetworkFunction(String id) {
        super(id);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    // Mock methods for metrics
    public double getCpuUsage() {
        return 70.0;
    }

    public double getMemoryStat() {
        return 80.0;
    }

    public double getBandwidthUsage() {
        return 40.0;
    }
}

// --- Concrete Visitors for Reports ---

class ResourceUtilizationReportVisitor implements Visitor {
    private StringBuilder report = new StringBuilder();

    @Override
    public void visit(SDNController controller) {
        report.append("SDNController (").append(controller.getId()).append(":)\n");
        report.append("  CPU Usage: ").append(controller.getCpuUsage()).append("%\n");
        report.append("  Memory Stat: ").append(controller.getMemoryStat()).append("%\n");
        report.append("  Bandwidth Usage: ").append(controller.getBandwidthUsage()).append("%\n\n");
    }

    @Override
    public void visit(NetworkSwitch sw) {
        report.append("NetworkSwitch (").append(sw.getId()).append(":)\n");
        report.append("  CPU Usage: ").append(sw.getCpuUsage()).append("%\n");
        report.append("  Memory Stat: ").append(sw.getMemoryStat()).append("%\n");
        report.append("  Bandwidth Usage: ").append(sw.getBandwidthUsage()).append("%\n\n");
    }

    @Override
    public void visit(NetworkFunction nf) {
        report.append("NetworkFunction (").append(nf.getId()).append(":)\n");
        report.append("  CPU Usage: ").append(nf.getCpuUsage()).append("%\n");
        report.append("  Memory Stat: ").append(nf.getMemoryStat()).append("%\n");
        report.append("  Bandwidth Usage: ").append(nf.getBandwidthUsage()).append("%\n\n");
    }

    public String getReport() {
        return "--- Resource Utilization Report ---\n" + report.toString();
    }
}

class PerformanceReportVisitor implements Visitor {
    private StringBuilder report = new StringBuilder();

    @Override
    public void visit(SDNController controller) {
        report.append("SDNController (").append(controller.getId()).append(":)\n");
        report.append("  Control Plane Latency: ").append(controller.getControlPlaneLatency()).append("ms\n");
        report.append("  Flow Rules Installed: ").append(controller.getFlowRulesInstalled()).append("\n\n");
    }

    @Override
    public void visit(NetworkSwitch sw) {
        report.append("NetworkSwitch (").append(sw.getId()).append(":)\n");
        report.append("  Packets Forwarded: ").append(sw.getPacketsForwarded()).append("\n");
        report.append("  Packets Dropped: ").append(sw.getPacketsDropped()).append("\n\n");
    }

    @Override
    public void visit(NetworkFunction nf) {
        // Not part of the performance report as per the requirements
        // No action needed for NetworkFunction in this report
    }

    public String getReport() {
        return "--- Performance Report ---\n" + report.toString();
    }
}

// Main class acts as the client
public class Client {
    public static void main(String[] args) {
        // The SDN controller has a global view of the network components.
        // We create a list of visitable elements.
        List<Visitable> visitableComponents = new ArrayList<>();
        visitableComponents.add(new SDNController("Controller1"));
        visitableComponents.add(new NetworkSwitch("Switch1"));
        visitableComponents.add(new NetworkSwitch("Switch2"));
        visitableComponents.add(new NetworkFunction("Firewall-NF"));

        // Client usage of the pattern:
        // 1. Create a visitor for the desired report.
        // 2. Iterate through the components and call accept() on each.
        // 3. Get the final report from the visitor.

        // --- Generate Resource Utilization Report ---
        ResourceUtilizationReportVisitor resourceVisitor = new ResourceUtilizationReportVisitor();
        System.out.println("Generating Resource Utilization Report...");
        for (Visitable elem : visitableComponents) {
            elem.accept(resourceVisitor);
        }
        System.out.println(resourceVisitor.getReport());

        // --- Generate Performance Report ---
        PerformanceReportVisitor performanceVisitor = new PerformanceReportVisitor();
        System.out.println("Generating Performance Report...");
        for (Visitable elem : visitableComponents) {
            elem.accept(performanceVisitor);
        }
        System.out.println(performanceVisitor.getReport());
    }
}
