/**
 * Part 2: Abstract Factory Integration
 * TASK:
 * 1. Implement Zone Factories (EU and US).
 * 2. Refactor EmployeeProfile to use the ZoneFactory via constructor
 * (Dependency Injection).
 */

// --- Base Interfaces from Part 1 ---

interface Resource {
    String displayAccessLevel();

    default String use() {
        return "Using resource: " + displayAccessLevel();
    }
}

class PublicResource implements Resource {
    public String displayAccessLevel() {
        return "Public Data - Read Only Access";
    }
}

/**** --- Abstract Factory Pattern --- ****/

// Product Interfaces

interface ComplianceAuditor {
    String auditCheck(LocalizedResource resource);
}

/*
 * LocalizedResource now extends Resource
 * this interface is replacing the ConfidentialResouce class in Part1
 */
interface LocalizedResource extends Resource {
    String getZoneDataFormat();
}

// ---Abstract Factory----
interface ZoneFactory {
    LocalizedResource createResource();

    ComplianceAuditor createAuditor();
}

// --- Concrete Products (The Families) ---

// Family 1 : EU

class GDPRResource implements LocalizedResource {
    public String getZoneDataFormat() {
        return "EU Zone: GDPR-compliant.";
    }

    public String displayAccessLevel() {
        return "Confidential Data - GDPR";
    }
}

class GDPRComplianceAuditor implements ComplianceAuditor {
    public String auditCheck(LocalizedResource r) {
        return "GDPR Check Passed for: " + r.getZoneDataFormat();
    }
}

// Family 2: US

class CCPAResource implements LocalizedResource {
    public String getZoneDataFormat() {
        return "US Zone: CCPA-compliant.";
    }

    public String displayAccessLevel() {
        return "Confidential Data - CCPA";
    }
}

class CCPAComplianceAuditor implements ComplianceAuditor {
    public String auditCheck(LocalizedResource r) {
        return "CCPA Check Passed for: " + r.getZoneDataFormat();
    }
}

// --- Concrete Factories (Needs Implementation) ---

class EUZoneFactory implements ZoneFactory {

    public LocalizedResource createResource() {
        return new GDPRResource();
        // YOUR CODE HERE: Return the EU Resource
    }

    public ComplianceAuditor createAuditor() {
        return new GDPRComplianceAuditor();
        // YOUR CODE HERE: Return the EU Auditor
    }
}

class USZoneFactory implements ZoneFactory {
    public LocalizedResource createResource() {
        return new CCPAResource();
        // YOUR CODE HERE: Return the US Resource
    }

    public ComplianceAuditor createAuditor() {
        return new CCPAComplianceAuditor();
        // YOUR CODE HERE: Return the US Auditor
    }
}

// --- Refactored Concrete Creators (Needs Implementation) ---

// creator hierarchy of Factory Method --same as Part1
abstract class Profile {
    public abstract Resource createResource();

    public String getResourceDetails() {
        Resource resource = createResource();
        return "Profile type: " + this.getClass().getSimpleName() +
                " | Resource created: " + resource.use();
    }
}

// concrete creator1 the Factory Method pattern
class NonEmployeeProfile extends Profile {
    // Retains simple Factory Method logic for public access
    public Resource createResource() {
        return new PublicResource();
    }
}

// --refactor the creator EmployeeProfile---

/*
 * EmployeeProfile will no longer create a Confidential resource
 * now it gets a ConcreteFactory from outside (dependency injection)
 * and creates a resource using this factory.
 *
 * this class is both a creator class of FM is a client code for AbstractFactory
 *
 */

class EmployeeProfile extends Profile {
    private ZoneFactory factory;

    /**
     * TODO 2: Refactor the constructor to accept a ZoneFactory (Abstract Factory).
     */
    public EmployeeProfile(ZoneFactory factory) {

        this.factory = factory;
        // YOUR CODE HERE: Set the injected factory
        //
    }

    /**
     * TODO 3: Update the Factory Method to use the injected Abstract Factory.
     */
    public LocalizedResource createResource() {
        return factory.createResource();
        // YOUR CODE HERE: Use the injected factory to create the resource family
        // member.
    }

    // Method to demonstrate the use of the Abstract Factory's entire product family
    public void performZoneAudit() {
        LocalizedResource resource = createResource();
        ComplianceAuditor auditor = factory.createAuditor();

        System.out.println("Zone Audit performed by EmployeeProfile:");
        System.out.println("  -> Resource: " + resource.displayAccessLevel());
        System.out.println("  -> Audit Result: " + auditor.auditCheck(resource));
    }
}

// --- Client Code (Main Application) ---

public class AbstractFactoryExample {

    public static void main(String[] args) {
        System.out.println("--- Part 2: Abstract Factory Integration Test ---");

        // Non-employee still uses simple Factory Method
        NonEmployeeProfile nonEmployee = new NonEmployeeProfile();
        System.out.println("\nProcessing Non-Employee:");
        System.out.println("  -> " + nonEmployee.getResourceDetails()); // Standard call

        // Employee configured for EU Zone
        EmployeeProfile euEmployee = new EmployeeProfile(new EUZoneFactory());
        System.out.println("\nProcessing EU Employee:");
        System.out.println("  -> " + euEmployee.getResourceDetails()); // Standard call
        euEmployee.performZoneAudit(); // Special call

        // Employee configured for US Zone
        EmployeeProfile usEmployee = new EmployeeProfile(new USZoneFactory());
        System.out.println("\nProcessing US Employee:");
        System.out.println("  -> " + usEmployee.getResourceDetails()); // Standard call
        usEmployee.performZoneAudit(); // Special call

    }
}
