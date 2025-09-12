## Scenario: Mine Safety System Complexity

### Task 1

Your task is to design a Facade that streamlines these operations into clean, high-level methods.

Exposes at least three high-level methods, such as:

- triggerEvacuation(sensorId)
- triggerFireResponse(sensorId)
- triggerSeismicResponse(sensorId)

Each method should:

- Call relevant subsystem(s) services
- Contain no business logic
- Represent a macro operation that simplifies client interaction

Give a UML class diagram and pattern relevant code (pseudocode/Java/C++/C#).

Give a client code that shows how to set up the façade object and how to interact with it.

```java
// Facade class
class MineSafetyFacade {
    // Private instances of the subsystem services
    private NotificationService notificationService;
    private ZoneControlService zoneControlService;
    private LightingService lightingService;
    private RescueService rescueService;
    private EventLogService eventLogService;

    // Constructor to initialize the services
    public MineSafetyFacade() {
        this.notificationService = new NotificationService();
        this.zoneControlService = new ZoneControlService();
        this.lightingService = new LightingService();
        this.rescueService = new RescueService();
        this.eventLogService = new EventLogService();
    }

    public void triggerEvacuation(String sensorId) {
        // 1. Log the event
        this.eventLogService.logEvent("Evacuation", "current_time", "Initiated by sensor " + sensorId);

        // 2. Activate emergency lighting
        this.lightingService.activateEmergencyLighting(sensorId);

        // 3. Notify all workers
        this.notificationService.notifyAllUndergroundWorkers("EMERGENCY! EVACUATE THE MINE NOW!");

        // 4. Alert rescue teams
        this.rescueService.alertRescueTeams(sensorId);

        // 5. Seal off affected zones
        this.zoneControlService.lockDownHazardousAreas();
    }

    public void triggerFireResponse(String sensorId) {
        // 1. Log the event
        this.eventLogService.logEvent("Evacuation", "current_time", "Initiated by sensor " + sensorId);

        // 2. Activate emergency lighting
        this.lightingService.activateEmergencyLighting(sensorId);

        // 3. Notify all workers
        this.notificationService.notifyAllUndergroundWorkers("EMERGENCY! EVACUATE THE MINE NOW!");

        // 4. Alert rescue teams
        this.rescueService.alertRescueTeams(sensorId);

        // 5. Seal off affected zones
        this.zoneControlService.lockDownHazardousAreas();
    }


    public void triggerSeismicResponse(String sensorId) {
        // 1. Log the event
        this.eventLogService.logEvent("Evacuation", "current_time", "Initiated by sensor " + sensorId);

        // 2. Activate emergency lighting
        this.lightingService.activateEmergencyLighting(sensorId);

        // 3. Notify all workers
        this.notificationService.notifyAllUndergroundWorkers("EMERGENCY! EVACUATE THE MINE NOW!");

        // 4. Alert rescue teams
        this.rescueService.alertRescueTeams(sensorId);

        // 5. Seal off affected zones
        this.zoneControlService.lockDownHazardousAreas();
    }
}

public class Main {
    public static void main(String[] args) {
        MineSafetyFacade mineSafetyFacade = new MineSafetyFacade();
        String sensorId = "test123";
        mineSafetyFacade.triggerEvacuation(sensorId);
    }
}
```
