import java.util.Stack;

interface IUndoableCommand {
    void execute();

    void undo(); // New method
}

/**
 * The Originator (Canvas)
 * Holds the state and knows how to save/restore itself.
 */
class Canvas {
    private byte[] state;

    public Canvas(int size) {
        this.state = new byte[size];
        System.out.println("Canvas initialized with " + size + " bytes.");
    }

    // Creates a Memento and copies the current state into it.
    public CanvasMemento createMemento() {
        // Return a memento with a DEEP COPY of the state.
        return new CanvasMemento(this.state.clone());
    }

    // Restores its state from a Memento.
    public void restoreFromMemento(CanvasMemento memento) {
        // Get the state from the memento and make a DEEP COPY.
        this.state = memento.getState().clone();
        System.out.println("Canvas restored from Memento.");
    }

    // Helper to simulate a change
    public void simulateChange(int index, byte value) {
        this.state[index] = value;
    }
}

// The Memento
class CanvasMemento {
    private final byte[] state;

    public CanvasMemento(byte[] stateToSave) {
        this.state = stateToSave;
    }

    public byte[] getState() {
        return this.state;
    }
}

/**
 * The Receiver Classes (The Brushes)
 * Their methods are passed as lambda functions
 */
class DigitalBrush {
    // The technique now modifies the canvas state it is given.
    public void smoothStroke(Canvas canvas) {
        System.out.println("DigitalBrush: Painting a smooth stroke.");
        canvas.simulateChange(0, (byte) 1);
    }

    public void splattering(Canvas canvas) {
        System.out.println("DigitalBrush: Creating a splatter effect.");
        canvas.simulateChange(1, (byte) 2);
    }

    public void gradientBlend(Canvas canvas) {
        System.out.println("DigitalBrush: Blending with a gradient.");
        canvas.simulateChange(1, (byte) 3);

    }
}

class TextureBrush {
    public void stampPattern(Canvas canvas) {
        System.out.println("TextureBrush: Stamping a repeating pattern.");
        canvas.simulateChange(2, (byte) 1);
    }

    public void roughSurface(Canvas canvas) {
        System.out.println("TextureBrush: Creating a rough surface texture.");
        canvas.simulateChange(2, (byte) 2);
    }

    public void smoothBlend(Canvas canvas) {
        System.out.println("TextureBrush: Blending colors smoothly.");
        canvas.simulateChange(2, (byte) 3);

    }

    public void mosaicEffect(Canvas canvas) {
        System.out.println("TextureBrush: Applying a mosaic tile effect.");
        canvas.simulateChange(2, (byte) 4);
    }
}

/**
 * The ConcreteCommand
 * also acts as the Memento's Caretaker.
 */
class PaintStrokeCommand implements IUndoableCommand {
    private Runnable technique; // The lambda (e.g., () -> digitalBrush.smoothStroke(canvas))
    private Canvas canvas; // The Originator
    private CanvasMemento beforeState; // The Memento

    public PaintStrokeCommand(Canvas canvas, Runnable technique) {
        this.canvas = canvas;
        this.technique = technique;
    }

    @Override
    public void execute() {
        // step 1: Save the "before" state *before* executing.
        this.beforeState = canvas.createMemento();

        // step 2: Execute the command (run the lambda).
        this.technique.run();
    }

    @Override
    public void undo() {
        if (this.beforeState != null) {
            canvas.restoreFromMemento(this.beforeState);
        }
    }
}

/**
 * The Invoker (CommandManager)
 * Manages the undo/redo history stacks.
 */
class CommandManager {
    private Stack<IUndoableCommand> undoHistory = new Stack<>();
    private Stack<IUndoableCommand> redoHistory = new Stack<>();

    public void executeCommand(IUndoableCommand command) {
        System.out.println("--- Executing Command ---");
        command.execute();
        undoHistory.push(command);
        redoHistory.clear();
    }

    public void undo() {
        System.out.println("--- Undoing ---");
        if (undoHistory.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        IUndoableCommand command = undoHistory.pop();
        command.undo();
        redoHistory.push(command);
    }

    public void redo() {
        System.out.println("--- Redoing ---");
        if (redoHistory.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }
        IUndoableCommand command = redoHistory.pop(); // Get the last undone command

        // Re-executing the command will save a *new* 'before' memento
        // (of the state just undid to) and then re-apply the stroke.
        command.execute();

        undoHistory.push(command);
    }
}

public class Memento2 {
    public static void main(String[] args) {
        // Setup all the main objects
        Canvas canvas = new Canvas(1024);
        DigitalBrush digitalBrush = new DigitalBrush();
        TextureBrush textureBrush = new TextureBrush();
        CommandManager manager = new CommandManager();

        // case 1: Smooth Stroke (DigitalBrush) ---
        // Create the lambda for the technique
        Runnable smoothStrokeLambda = () -> digitalBrush.smoothStroke(canvas);
        // Create the command object, passing the lambda and canvas
        IUndoableCommand cmd1 = new PaintStrokeCommand(canvas, smoothStrokeLambda);
        manager.executeCommand(cmd1);

        // case 2: Stamp Pattern (TextureBrush) ---
        Runnable stampPatternLambda = () -> textureBrush.stampPattern(canvas);
        IUndoableCommand cmd2 = new PaintStrokeCommand(canvas, stampPatternLambda);
        manager.executeCommand(cmd2);

        // case 3: Splatter (DigitalBrush) ---
        Runnable splatterLambda = () -> digitalBrush.splattering(canvas);
        IUndoableCommand cmd3 = new PaintStrokeCommand(canvas, splatterLambda);
        manager.executeCommand(cmd3);

        manager.undo(); // undo "Splatter". Canvas restored to after "Stamp".
        manager.undo(); // undo "Stamp". Canvas restored to after "Smooth".

        manager.redo(); // redo "Stamp".

        manager.undo(); // undo "Stamp" again.

    }
}