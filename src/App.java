import Controller.ConsoleController.*;;

public class App {
    public static void main(String[] args) {
        // Model m = new Model();
        var controller = new ConsoleController();
        controller.mainLoop();
    }
}
