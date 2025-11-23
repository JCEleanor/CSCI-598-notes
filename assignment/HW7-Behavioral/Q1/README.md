1. Give the name of the design pattern(s) you are applying to the problem.
2. Present your reasons why your chosen pattern(s) solve the problem.
   Specifically, map the problem’s requirements (e.g., ”must be configurable,” ”add new
   types without modifying code”) to the specific roles and collaborations in your pattern.
   If there is an alternative pattern, explain why you preferred this one.
3. Show you design with a UML class diagram. If the pattern collaborations would be more
   visible with another diagram (e.g. sequence diagram), give that diagram as well.
   (a) Your diagram should show every participant in the pattern including the pattern
   related methods and attributes.
   (b) In pattern related classes, give the member (method and attribute) names that
   play a role in the pattern and effected by the pattern. Optionally, include the
   member names mentioned in the question. You are encouraged to omit the other
   methods and fields.
   (c) For the non-pattern related classes, you are not expected to give detailed class
   names etc. You may give a high-level component, like “UserInterface” or
   “DBManagement”
4. Give Java or C++ code for your design showing how you have implemented the pattern.
   (a) Pattern related methods and attributes should appear in the code
   (b) Client usage of the pattern should appear in the code
   (c) Non-pattern related parts of the methods could be a simple print. (e.g.
   ”System.out.println()”, ”cout”)
5. Explain how this design solves the problem, using the specific names and roles of your
   designed classes to demonstrate how the requirements are met.
6. Evaluate your design with respect to SOLID principles. Each principle should be
   addressed, if a principle is not applicable to the current pattern, say so.

# Virtual Software Defined Network (SDN) Design Prototype

We are implementing a prototype to design and analyze virtual Software Defined Networks (SDN), a much simpler version of [Mininet](https://mininet.org/).

## Network Structure

A network consists of at least one SDN controller, network switches, network functions, and hosts. For simplicity, assume there is one SDN Controller.

The network structure is a graph, and there can be cycles. The SDN controller is the brain of the network. There is at least one path to each host from the SDN controller through a series of network switches or network functions.

- Each host is connected to a switch.
- A switch could be connected to another switch, a host, and to the SDN controller.

We can think of switches and network functions as intermediate nodes in the graph and hosts as leaves.

## Component Responsibilities

Each component has its primary responsibility:

- **SDN Controller:** Maintain network topology, install flow rules.
- **Network Switches:** Forward packets, maintain flow tables.
- **Network Functions (NF):** Perform specific network services.
- **Host:** Consume the network service.

**Note:** Assume these responsibilities are already implemented. (DO NOT implement these functionalities.)

## SDN Controller's Global View

The SDN Controller has a global view of the network and is aware of all the components and their connections. In other words:

- The SDN Controller can provide information about all the components in the network, including SDN Controllers, Network Switches, Network Functions, and Hosts.
- The SDN Controller can provide details about the connections between the components, such as which switches are connected to which hosts or network functions.

## Required Reports

We want to generate two reports on this network.

### 1. Resource Utilization Report

This report provides information about resource usage in the network. It could show the CPU, memory, and bandwidth usage of each component (except host), helping us identify potential bottlenecks or underutilized resources.

For the sake of simplicity, assume there are `getCPUusage()`, `getMemoryStat()`, and `getBandwidthUsage()` methods in the controller, switch, and NF classes.

### 2. Performance Report

This report collects performance metrics from switches and the controller.

- **Switches:** Report on the number of packets forwarded, dropped, or lost.
- **SDN Controller:** Reports the control plane latency and the number of flow rules installed.

Assume these components' APIs provide methods to get these metrics.

## Design Goals

Propose a design that allows a client to generate these reports. Your primary design goals must be:

- **Extensibility:** It must be easy to add new, future reports (e.g., a `SecurityAuditReport` or `TopologyHealthReport`) with minimal effort.
- **Low Coupling:** The core component classes (`SDNController`, `NetworkSwitch`, `NetworkFunction`) should be minimally impacted, if at all, by the addition of new reporting types.
