#include <iostream>
#include <vector>
#include <string>
#include <memory>
#include <utility>

// Forward declarations
class SDNController;
class NetworkSwitch;
class NetworkFunction;

// Visitor interface
class Visitor {
public:
    virtual ~Visitor() = default;
    virtual void visit(SDNController& controller) = 0;
    virtual void visit(NetworkSwitch& sw) = 0;
    virtual void visit(NetworkFunction& nf) = 0;
};

// Visitable interface
class Visitable {
public:
    virtual ~Visitable() = default;
    virtual void accept(Visitor& visitor) = 0;
};

// --- Network Components ---

class NetworkComponent {
public:
    explicit NetworkComponent(std::string id) : id_(std::move(id)) {}
    virtual ~NetworkComponent() = default;
    const std::string& getId() const { return id_; }

protected:
    std::string id_;
};

class Host : public NetworkComponent {
public:
    explicit Host(const std::string& id) : NetworkComponent(id) {}
};

class SDNController : public NetworkComponent, public Visitable {
public:
    explicit SDNController(const std::string& id) : NetworkComponent(id) {}

    void accept(Visitor& visitor) override {
        visitor.visit(*this);
    }

    // Mock methods for metrics
    double getCpuUsage() { return 50.0; }
    double getMemoryStat() { return 60.0; }
    double getBandwidthUsage() { return 20.0; }
    double getControlPlaneLatency() { return 0.5; }
    int getFlowRulesInstalled() { return 1000; }
};

class NetworkSwitch : public NetworkComponent, public Visitable {
public:
    explicit NetworkSwitch(const std::string& id) : NetworkComponent(id) {}

    void accept(Visitor& visitor) override {
        visitor.visit(*this);
    }

    // Mock methods for metrics
    double getCpuUsage() { return 30.0; }
    double getMemoryStat() { return 40.0; }
    double getBandwidthUsage() { return 80.0; }
    long getPacketsForwarded() { return 100000; }
    long getPacketsDropped() { return 100; }
};

class NetworkFunction : public NetworkComponent, public Visitable {
public:
    explicit NetworkFunction(const std::string& id) : NetworkComponent(id) {}

    void accept(Visitor& visitor) override {
        visitor.visit(*this);
    }

    // Mock methods for metrics
    double getCpuUsage() { return 70.0; }
    double getMemoryStat() { return 80.0; }
    double getBandwidthUsage() { return 40.0; }
};

// --- Concrete Visitors for Reports ---

class ResourceUtilizationReportVisitor : public Visitor {
public:
    void visit(SDNController& controller) override {
        report_ += "SDNController (" + controller.getId() + "):\n";
        report_ += "  CPU Usage: " + std::to_string(controller.getCpuUsage()) + "%\n";
        report_ += "  Memory Stat: " + std::to_string(controller.getMemoryStat()) + "%\n";
        report_ += "  Bandwidth Usage: " + std::to_string(controller.getBandwidthUsage()) + "%\n\n";
    }

    void visit(NetworkSwitch& sw) override {
        report_ += "NetworkSwitch (" + sw.getId() + "):\n";
        report_ += "  CPU Usage: " + std::to_string(sw.getCpuUsage()) + "%\n";
        report_ += "  Memory Stat: " + std::to_string(sw.getMemoryStat()) + "%\n";
        report_ += "  Bandwidth Usage: " + std::to_string(sw.getBandwidthUsage()) + "%\n\n";
    }

    void visit(NetworkFunction& nf) override {
        report_ += "NetworkFunction (" + nf.getId() + "):\n";
        report_ += "  CPU Usage: " + std::to_string(nf.getCpuUsage()) + "%\n";
        report_ += "  Memory Stat: " + std::to_string(nf.getMemoryStat()) + "%\n";
        report_ += "  Bandwidth Usage: " + std::to_string(nf.getBandwidthUsage()) + "%\n\n";
    }

    std::string getReport() const {
        return "--- Resource Utilization Report ---\n" + report_;
    }

private:
    std::string report_;
};

class PerformanceReportVisitor : public Visitor {
public:
    void visit(SDNController& controller) override {
        report_ += "SDNController (" + controller.getId() + "):\n";
        report_ += "  Control Plane Latency: " + std::to_string(controller.getControlPlaneLatency()) + "ms\n";
        report_ += "  Flow Rules Installed: " + std::to_string(controller.getFlowRulesInstalled()) + "\n\n";
    }

    void visit(NetworkSwitch& sw) override {
        report_ += "NetworkSwitch (" + sw.getId() + "):\n";
        report_ += "  Packets Forwarded: " + std::to_string(sw.getPacketsForwarded()) + "\n";
        report_ += "  Packets Dropped: " + std::to_string(sw.getPacketsDropped()) + "\n\n";
    }

    void visit(NetworkFunction& nf) override {
        // Not part of the performance report as per the requirements
    }

    std::string getReport() const {
        return "--- Performance Report ---\n" + report_;
    }

private:
    std::string report_;
};


// Main function acts as the client
int main() {
    // The SDN controller has a global view of the network components.
    // We create a list of visitable elements.
    std::vector<std::unique_ptr<Visitable>> visitable_components;
    visitable_components.push_back(std::make_unique<SDNController>("Controller1"));
    visitable_components.push_back(std::make_unique<NetworkSwitch>("Switch1"));
    visitable_components.push_back(std::make_unique<NetworkSwitch>("Switch2"));
    visitable_components.push_back(std::make_unique<NetworkFunction>("Firewall-NF"));


    // Client usage of the pattern:
    // 1. Create a visitor for the desired report.
    // 2. Iterate through the components and call accept() on each.
    // 3. Get the final report from the visitor.

    // --- Generate Resource Utilization Report ---
    ResourceUtilizationReportVisitor resourceVisitor;
    std::cout << "Generating Resource Utilization Report..." << std::endl;
    for (const auto& elem : visitable_components) {
        elem->accept(resourceVisitor);
    }
    std::cout << resourceVisitor.getReport() << std::endl;

    // --- Generate Performance Report ---
    PerformanceReportVisitor performanceVisitor;
    std::cout << "Generating Performance Report..." << std::endl;
    for (const auto& elem : visitable_components) {
        elem->accept(performanceVisitor);
    }
    std::cout << performanceVisitor.getReport() << std::endl;

    return 0;
}