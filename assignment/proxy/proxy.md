# In-class exercise: Cloud-Based AI Inference Service

- smartpointer in c++ is like a proxy

### Task 1

Define an interface for AI inference that the system can depend on (e.g., IAIInference with a method generateResponse(prompt)).

```java
// subject
interface IAIInference {
    void generateResponse(String prompt);
}
```

### Task 2 & 3

Ensure the interface supports the real AIInferenceEngineand can be proxied without modifying client code.

Identify the interfaces and classes. Determine the real subject (AIInferenceEngine), and implement:

- A virtual proxy (LazyAIInferenceProxy) that delays loading the real engine until needed.
- A protection proxy (SecureAIInferenceProxy) that checks access permissions (e.g., via a simple user role or token validation) before delegating to the subject.

```java
// Real Subject
class AIInferenceEngine implements IAIInference {
    public AIInferenceEngine() {
        System.out.println("Loading... This is expensive.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Model loaded.");
    }

    @Override
    public String generateResponse(String prompt) {
        return "AI response to: '" + prompt + "'";
    }
}

class LazyAIInferenceProxy implements IAIInference {
    private IAIInference realSubject;

    public LazyAIInferenceProxy(IAIInference realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public String generateResponse(String prompt) {
        if (realSubject == null) {
            System.out.println("Loading...");
            realSubject = new AIInferenceEngine();
        }
        return realSubject.generateResponse(prompt);
    }
}

class SecureAIInferenceProxy implements IAIInference {
    private IAIInference realSubject;
    private String userRole;

    public SecureAIInferenceProxy(IAIInference realSubject, String userRole) {
        this.realSubject = realSubject;
        this.userRole = userRole;
    }

    private boolean hasAccess(String prompt) {
        if (!"ADMIN".equals(userRole)) {
            return false;
        }
        return true;
    }

    @Override
    public String generateResponse(String prompt) {
        if (!hasAccess(userRole)) {
            return "Access Denied.";
        }

        return realSubject.generateResponse(prompt);

    }
}

```

### Task 4

Draw a class diagram to illustrate the relationships between the classes and interfaces.
(Note: The protection proxy can wrap either the real subject or the virtual proxy for composition.)

### Client code: Behavior Simulation

simulate:

- A request handler using the virtual proxy (demonstrating lazy loading).
- A request handler using the protection proxy (demonstrating access denial for unauthorized users).
  Hint: Use <b>dependency injection</b> to make the request handler independent of the actual proxy or subject it's using. For simplicity, simulate loading with a print statement (e.g., "Model loaded") and access checks with a boolean flag or method.

- Extensibility in action:
  Imagine a third proxy type, such as a logging proxy (LoggingAIInferenceProxy) that records inference requests for auditing (common in compliance-heavy tech like fintech or healthcare AI).

```java
// Extensibility in action
class LoggingAIInferenceProxy implements IAIInference {
    private final IAIInference realSubject;

    public LoggingAIInferenceProxy(IAIInference realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public String generateResponse(String prompt) {

        System.out.println("[AUDIT LOG]"
                + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String response = realSubject.generateResponse(prompt);

        return "[AUDIT LOG] " + response;
    }

}

public class client {
    public static void main(String[] args) {
        // loading proxy
        LazyAIInferenceProxy lazyAIInference = new LazyAIInferenceProxy(new AIInferenceEngine());
        String response1 = lazyAIInference.generateResponse("Hello world!");
        System.out.println(response1);

        // protection proxy (Access denied)
        SecureAIInferenceProxy secureAIInference = new SecureAIInferenceProxy(new AIInferenceEngine(), "USER");
        String response2 = secureAIInference.generateResponse("Hiii");
        System.out.println(response2);

        // protection proxy (Access granted)
        SecureAIInferenceProxy secureAIInference2 = new SecureAIInferenceProxy(new AIInferenceEngine(), "ADMIN");
        String response3 = secureAIInference2.generateResponse("Hiii");
        System.out.println(response3);

        // logging proxy
        LoggingAIInferenceProxy loggingAIInference = new LoggingAIInferenceProxy(new AIInferenceEngine());
        String response4 = loggingAIInference.generateResponse("Test 123");
        System.out.println(response4);
    }
}

```
