import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

// 1. Command Interface (Functional Interface) - no changes needed
@FunctionalInterface
interface ICommand {
    void execute();
}

// --- Memento Pattern Components ---

// 2. Memento: Stores the state of the Canvas.
// It's a simple data holder for the byte[] state.
final class CanvasMemento {
    // The state is final and a clone, ensuring the memento is an immutable snapshot.
    private final byte[] state;

    public CanvasMemento(byte[] stateToSave) {
        // Clone the array to ensure the memento holds a true snapshot,
        // not a reference to the live canvas state.
        this.state = stateToSave.clone();
    }

    // This getter is used by the Originator (Canvas) to restore its state.
    public byte[] getState() {
        return this.state;
    }
}

// 3. Originator: The object whose state we want to save.
class Canvas {
    private byte[] content; // The internal state

    public Canvas() {
        // Initialize with a simple 5-byte canvas for demonstration.
        this.content = new byte[5];
        System.out.println("Canvas created. Initial state: " + Arrays.toString(content));
    }

    // A method to simulate drawing, which modifies the state.
    public void draw(String toolName) {
        System.out.println(toolName);
        // Simulate a change by incrementing a byte based on the tool's hash code.
        int changeIndex = Math.abs(toolName.hashCode()) % content.length;
        content[changeIndex]++;
        printState();
    }

    // Creates a memento containing a snapshot of the current state.
    public CanvasMemento save() {
        System.out.println("Canvas: Saving state...");
        return new CanvasMemento(this.content);
    }

    // Restores the state from a memento object.
    public void restore(CanvasMemento memento) {
        System.out.println("Canvas: Restoring state...");
        // Get the state from the memento and clone it back into the canvas.
        this.content = memento.getState().clone();
        printState();
    }

    public void printState() {
        System.out.println("  > Current Canvas State: " + Arrays.toString(content));
    }
}

// 4. Caretaker: Manages the history of mementos for undo/redo.
class History {
    private final Deque<CanvasMemento> undoStack = new ArrayDeque<>();
    private final Deque<CanvasMemento> redoStack = new ArrayDeque<>();
    private final int historyLimit = 10;
    private final Canvas canvas;

    public History(Canvas canvas) {
        this.canvas = canvas;
    }

    // Saves the current state of the canvas before a new action is executed.
    public void saveState() {
        // Clear the redo stack whenever a new action is performed.
        redoStack.clear();
        undoStack.push(canvas.save());

        // Enforce the history limit.
        if (undoStack.size() > historyLimit) {
            undoStack.removeLast();
        }
    }

    public void undo() {
        if (undoStack.size() <= 1) { // Need at least one state to revert TO
            System.out.println("History: Cannot undo further.");
            return;
        }
        // Move the current state to the redo stack
        CanvasMemento currentState = undoStack.pop();
        redoStack.push(currentState);

        // Restore the canvas to the previous state
        canvas.restore(undoStack.peek());
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("History: Cannot redo.");
            return;
        }
        // Move the state from redo back to undo
        CanvasMemento stateToRestore = redoStack.pop();
        undoStack.push(stateToRestore);

        // Restore the canvas
        canvas.restore(stateToRestore);
    }
}


// --- Modified Command Pattern Components ---

// 5. Receiver Classes: Now they operate on the Canvas.
class DigitalBrush {
    public void smoothStroke(Canvas canvas) {
        canvas.draw("DigitalBrush: Painting a smooth stroke.");
    }

    public void splattering(Canvas canvas) {
        canvas.draw("DigitalBrush: Creating a splatter effect.");
    }
}

class TextureBrush {
    public void stampPattern(Canvas canvas) {
        canvas.draw("TextureBrush: Stamping a repeating pattern.");
    }

    public void mosaicEffect(Canvas canvas) {
        canvas.draw("TextureBrush: Applying a mosaic tile effect.");
    }
}

// 6. Invoker: Now integrated with the Canvas and History.
class PaintingApplication {
    private ICommand primaryCommand;
    private ICommand secondaryCommand;
    private final History history;

    public PaintingApplication(History history) {
        this.history = history;
        // Save the initial empty state of the canvas.
        this.history.saveState();
    }

    public void setPrimaryCommand(ICommand command) {
        this.primaryCommand = command;
    }

    public void setSecondaryCommand(ICommand command) {
        this.secondaryCommand = command;
    }

    public void onLeftClick() {
        if (primaryCommand != null) {
            history.saveState(); // Save state BEFORE executing the command
            primaryCommand.execute();
        }
    }

    public void onRightClick() {
        if (secondaryCommand != null) {
            history.saveState(); // Save state BEFORE executing the command
            secondaryCommand.execute();
        }
    }

    public void undo() {
        history.undo();
    }

    public void redo() {
        history.redo();
    }
}

// 7. Client: Orchestrates the setup and demonstrates the patterns.
public class Client2 {
    public static void main(String[] args) {
        // Setup the main components
        Canvas canvas = new Canvas();
        History history = new History(canvas);
        PaintingApplication app = new PaintingApplication(history);

        // Setup the receivers
        DigitalBrush digitalBrush = new DigitalBrush();
        TextureBrush textureBrush = new TextureBrush();

        System.out.println("\n--- Step 1: Perform two actions ---");
        // Use lambdas to create commands that "close over" the canvas and brush instances.
        app.setPrimaryCommand(() -> digitalBrush.smoothStroke(canvas));
        app.setSecondaryCommand(() -> textureBrush.stampPattern(canvas));

        app.onLeftClick();  // Action 1
        app.onRightClick(); // Action 2

        System.out.println("\n--- Step 2: Undo both actions ---");
        app.undo(); // Undoes Action 2
        app.undo(); // Undoes Action 1

        System.out.println("\n--- Step 3: Try to undo again (should fail) ---");
        app.undo(); // Nothing to undo

        System.out.println("\n--- Step 4: Redo one action ---");
        app.redo(); // Redoes Action 1

        System.out.println("\n--- Step 5: Perform a new action ---");
        app.onLeftClick(); // This should clear the redo history

        System.out.println("\n--- Step 6: Try to redo again (should fail) ---");
        app.redo(); // Nothing to redo
    }
}
