package Controller.ConsoleController;

import Controller.Controller;
import java.util.Scanner;

import View.View;
import View.ConsoleView.*;
import Model.Model;

public class ConsoleController extends Controller {
    private Model model;
    private View view;
    private Scanner stdin;

    public void mainLoop() {
        this.stdin = new Scanner(System.in);

        this.model = new Model();
        this.view = new ConsoleView();

        this.loadUser();

        view.loadMainScreen();

        this.stdin.close();
    }

    private void loadUser() {
        view.promptUser(false);
        var uncheckedUser = stdin.nextLine();
        while (!model.verifyUser(uncheckedUser)) {
            view.promptUser(true);
            uncheckedUser = stdin.nextLine();
        }

        model.loadUser(uncheckedUser);
    }
}
