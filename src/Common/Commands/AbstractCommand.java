package Common.Commands;

public abstract class AbstractCommand {
    public abstract void execute();

    public abstract Boolean undo();
}
