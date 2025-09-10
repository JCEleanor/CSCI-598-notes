### Notes
- Façade doesn't have logic. It makes sequence of calls/actions

- Façade vs Adaptor 
    - they are all putting something in the middle (like proxy)
    - adaptor is adapting one logic 
    - façade is extracting many logic into one façade function
    

### assignment
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

    // High-level method for a "triggerEvacuation" event
    public void triggerEvacuation(String sensorId) {
        // 1. Log the event
        this.eventLogService.logEvent("Evacuation", "current_time", "Initiated by sensor " + sensorId);
        
        // 2. Activate emergency lighting
        this.lightingService.activateEmergencyLighting();
        
        // 3. Notify all workers
        this.notificationService.notifyAllUndergroundWorkers("EMERGENCY! EVACUATE THE MINE NOW!");
        
        // 4. Alert rescue teams
        this.rescueService.alertRescueTeams(sensorId);
        
        // 5. Seal off affected zones
        this.zoneControlService.lockDownHazardousAreas();
    }

    // High-level method for a "FireResponse" event
    public void fireResponse(String sensorId) {
        // This method would coordinate the services needed for a fire
        // ... you will write the logic here based on the instructions.
    }

    // You can add more methods here for other emergency types, like "SeismicEvacuation"
}
```