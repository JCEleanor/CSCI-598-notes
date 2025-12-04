package Q3;

import java.util.ArrayList;
import java.util.List;

// Note: For this single-file demonstration, supporting classes and interfaces
// are defined here without the 'public' modifier, making them package-private.

/**
 * The Observer interface. Observers are notified of changes in the Subject.
 */
interface Follower {
    void update(Profile profile);
}

/**
 * The Iterator interface. Provides a standard way to traverse a collection.
 */
interface INotificationIterator {
    boolean hasNext();
    String next();
}

/**
 * The Subject class (Observable). This is the user profile that others follow.
 */
class Profile {
    private List<Follower> followers = new ArrayList<>();
    private String latestPost;
    private String userName;

    public Profile(String userName) {
        this.userName = userName;
    }

    public void follow(Follower follower) {
        followers.add(follower);
    }

    public void unfollow(Follower follower) {
        followers.remove(follower);
    }

    public void notifyFollowers() {
        for (Follower follower : followers) {
            follower.update(this); // Pass reference to self for pull model
        }
    }

    public void post(String message) {
        System.out.println("\n" + userName + " posted: \"" + message + "\"");
        this.latestPost = message;
        notifyFollowers();
    }

    public String getUserName() {
        return this.userName;
    }

    // Getter for the state, used by observers to "pull" data.
    public String getLatestPost() {
        return this.latestPost;
    }
}

/**
 * A Concrete Observer (implements Follower) and the Concrete Aggregate for the Iterator pattern.
 * This represents a user's notification feed.
 */
class NotificationBar implements Follower {
    private String barOwner;
    private List<String> notifications = new ArrayList<>();

    public NotificationBar(String owner) {
        this.barOwner = owner;
    }

    @Override
    public void update(Profile profile) {
        // Pulls the needed data from the profile
        String notification = profile.getUserName() + " just posted: \"" + profile.getLatestPost() + "\"";
        notifications.add(notification);
        System.out.println("  -> " + barOwner + "'s notification bar received an update.");
    }

    public void printNotifications() {
        System.out.println("\n--- " + barOwner + "'s Notification Feed ---");
        INotificationIterator iterator = createIterator();
        while(iterator.hasNext()) {
            System.out.println("- " + iterator.next());
        }
        System.out.println("------------------------------------\n");
    }

    // Factory method to create an iterator
    public INotificationIterator createIterator() {
        return new NotificationIterator(notifications);
    }
}

/**
 * The Concrete Iterator. Implements the traversal mechanism for the notification list.
 */
class NotificationIterator implements INotificationIterator {
    private List<String> notifications;
    private int position = 0;

    public NotificationIterator(List<String> notifications) {
        this.notifications = notifications;
    }

    @Override
    public boolean hasNext() {
        return position < notifications.size();
    }

    @Override
    public String next() {
        if (this.hasNext()) {
            return notifications.get(position++);
        }
        return null;
    }
}

/**
 * The public Client class containing the main method to run the demonstration.
 */
public class Client {
    public static void main(String[] args) {
        // --- Setup ---
        // Create users (Subjects)
        Profile alice = new Profile("Alice");
        Profile bob = new Profile("Bob");

        // Create notification bars for followers (Observers)
        NotificationBar charlieBar = new NotificationBar("Charlie");
        NotificationBar davidBar = new NotificationBar("David");

        // Charlie follows Alice and Bob
        System.out.println("Charlie is following Alice and Bob.");
        alice.follow(charlieBar);
        bob.follow(charlieBar);

        // David follows only Alice
        System.out.println("David is following Alice.");
        alice.follow(davidBar);

        // --- Demonstrate Observer Pattern ---
        // Alice posts an announcement
        alice.post("Hello everyone! I'll be presenting at the design patterns conference tomorrow.");

        // Bob posts a meeting link
        bob.post("Join my weekly design discussion: https://meet.example.com/xyz-abc");

        // --- Demonstrate Iterator Pattern ---
        // Charlie checks his notification feed. The client uses the Iterator.
        charlieBar.printNotifications();

        // David checks his notification feed
        davidBar.printNotifications();

        // --- More interaction ---
        System.out.println("\nDavid unfollows Alice.");
        alice.unfollow(davidBar);
        
        alice.post("Just finished my presentation, it went great!");

        charlieBar.printNotifications();
        davidBar.printNotifications(); // Should have no new notifications from Alice
    }
}
