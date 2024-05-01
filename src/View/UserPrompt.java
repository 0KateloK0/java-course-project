package View;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;

public class UserPrompt extends Panel {
    boolean isRepeat = false;
    TextField usernameInput;

    UserPrompt(ActionListener onSubmit) {
        Label usernameLabel = new Label("Введите ваше имя пользователя");
        usernameLabel.setFont(View.FONT);
        usernameInput = new TextField();
        Button submitButton = new Button("Войти");
        submitButton.addActionListener(onSubmit);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(usernameLabel);
        add(usernameInput);
        add(submitButton);

        setVisible(true);
    }

    public void setIsRepeat(boolean value) {
        if (value == isRepeat)
            return;

        isRepeat = value;
        if (value) {
            Label incorrectUsernameLabel = new Label("Такого пользователя не существует");
            add(incorrectUsernameLabel);
        }
    }

    public boolean getIsRepeat() {
        return isRepeat;
    }

    public String getUsername() {
        return usernameInput.getText();
    }
}
