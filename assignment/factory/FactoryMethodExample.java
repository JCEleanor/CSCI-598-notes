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

class ConfidentialResource implements Resource {
    public String displayAccessLevel() {
        return "Confidential Data - Full R/W Access";
    }
}

// --- Creator Hierarchy ---

abstract class Profile {
    public abstract Resource createResource();

    public String getResourceDetails() {
        Resource resource = createResource();
        return "Profile type: " + this.getClass().getSimpleName() +
                " | Resource created: " + resource.use();
    }
}

// --- Concrete Creators (Needs Implementation) ---

class EmployeeProfile extends Profile {
    public Resource createResource() {
        // YOUR CODE HERE. create and return a confidential resource
        return new ConfidentialResource();
    }
}

class NonEmployeeProfile extends Profile {
    @Override
    public Resource createResource() {
        // YOUR CODE HERE. create and return a public resource
        return new PublicResource();
    }
}

// --- Client Code (Main Application) ---

public class FactoryMethodExample {

    public static void main(String[] args) {
        Profile profile = new EmployeeProfile();
        System.out.println(profile.getResourceDetails());

        profile = new NonEmployeeProfile();
        System.out.println(profile.getResourceDetails());
    }
}