import Common.DebugInfo;
import Controller.Controller;

public class App {
    public static void main(String[] args) {
        // Model m = new Model();
        DebugInfo.isDebug = true;
        var controller = new Controller();
        controller.mainLoop();
    }
}
