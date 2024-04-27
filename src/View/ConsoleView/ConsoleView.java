package View.ConsoleView;

import View.View;

public class ConsoleView extends View {
    public void promptUser(boolean isRepeat) {
        if (isRepeat)
            System.err.println("Такого пользователя не существует");
        System.out.println("Введите имя пользователя: ");
    }

    public void loadMainScreen() {
        System.out.println("Здравствуйте, ");
    }
}
