interface ICommand {
    void execute();
}

// Receiver Class
class DigitalBrush {
    public void smoothStroke() {
        System.out.println("DigitalBrush: Painting a smooth stroke.");
    }

    public void splattering() {
        System.out.println("DigitalBrush: Creating a splatter effect.");
    }

    public void gradientBlend() {
        System.out.println("DigitalBrush: Blending with a gradient.");
    }
}

// Receiver Class
class TextureBrush {
    public void stampPattern() {
        System.out.println("TextureBrush: Stamping a repeating pattern.");
    }

    public void roughSurface() {
        System.out.println("TextureBrush: Creating a rough surface texture.");
    }

    public void smoothBlend() {
        System.out.println("TextureBrush: Blending colors smoothly.");
    }

    public void mosaicEffect() {
        System.out.println("TextureBrush: Applying a mosaic tile effect.");
    }
}

// Concrete Command 1
class SmoothStrokeCommand implements ICommand {
    // The Receiver
    private DigitalBrush brush; 

    public SmoothStrokeCommand(DigitalBrush brush) {
        this.brush = brush;
    }

    @Override
    public void execute() {
        brush.smoothStroke();
    }
}

// Concrete Command 2
class SplatteringCommand implements ICommand {
    // The Receiver
    private DigitalBrush brush; 

    public SplatteringCommand(DigitalBrush brush) {
        this.brush = brush;
    }

    @Override
    public void execute() {
        brush.splattering();
    }
}

class StampPatternCommand implements ICommand {
    // The Receiver
    private TextureBrush brush; 

    public StampPatternCommand(TextureBrush brush) {
        this.brush = brush;
    }

    @Override
    public void execute() {
        brush.stampPattern();
    }
}

class MosaicEffectCommand implements ICommand {
    // The Receiver
    private TextureBrush brush; 

    public MosaicEffectCommand(TextureBrush brush) {
        this.brush = brush;
    }

    @Override
    public void execute() {
        brush.mosaicEffect();
    }
}

//  skip other command classes for brevity

// 4. Invoker Class
class PaintingApplication {
    private ICommand primaryCommand;
    private ICommand secondaryCommand;

    // The client uses these methods during setup
    public void setPrimaryCommand(ICommand command) {
        this.primaryCommand = command;
    }

    public void setSecondaryCommand(ICommand command) {
        this.secondaryCommand = command;
    }

    // These methods are triggered by user input (e.g., mouse listener)
    public void onLeftClick() {
        if (primaryCommand != null) {
            primaryCommand.execute();
        }
    }

    public void onRightClick() {
        if (secondaryCommand != null) {
            secondaryCommand.execute();
        }
    }
}

// 5. Client (Setup and Usage)
public class Client {
    public static void main(String[] args) {
        // The invoker
        PaintingApplication app = new PaintingApplication();

        // --- Artist selects DigitalBrush ---
        System.out.println("Artist selects DigitalBrush...");
        DigitalBrush digitalBrush = new DigitalBrush();

        // Setup: Create commands with the specific receiver
        ICommand smoothCmd = new SmoothStrokeCommand(digitalBrush);
        ICommand splatterCmd = new SplatteringCommand(digitalBrush);

        // Setup: Configure the invoker
        app.setPrimaryCommand(smoothCmd);
        app.setSecondaryCommand(splatterCmd);

        // Artist paints
        System.out.print("Artist LEFT clicks: ");
        app.onLeftClick(); // Ouput: DigitalBrush: Painting a smooth stroke.
        System.out.print("Artist RIGHT clicks: ");
        app.onRightClick(); // Ouput: DigitalBrush: Creating a splatter effect.
        System.out.println("---");

        // --- Artist changes to TextureBrush ---
        System.out.println("Artist selects TextureBrush...");
        TextureBrush textureBrush = new TextureBrush();

        // Setup must be performed again
        ICommand stampCmd = new StampPatternCommand(textureBrush);
        ICommand mosaicCmd = new MosaicEffectCommand(textureBrush);

        // Setup: Re-configure the same invoker with new commands
        app.setPrimaryCommand(stampCmd);
        app.setSecondaryCommand(mosaicCmd);

        // Artist paints
        System.out.print("Artist LEFT clicks: ");
        app.onLeftClick(); // Ouput: TextureBrush: Stamping a repeating pattern.
        System.out.print("Artist RIGHT clicks: ");
        app.onRightClick(); // Ouput: TextureBrush: Applying a mosaic tile effect.
    }
}