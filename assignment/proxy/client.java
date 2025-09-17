import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

interface IAIInference {
    String generateResponse(String prompt);
}

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
