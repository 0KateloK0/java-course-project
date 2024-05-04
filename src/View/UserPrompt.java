package View;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserPrompt extends JPanel {
    boolean isRepeat = false;
    JTextField usernameInput;

    UserPrompt(ActionListener onSubmit) {
        var usernameLabel = new JLabel("Введите ваше имя пользователя");
        usernameLabel.setFont(View.FONT);
        usernameInput = new JTextField();
        var submitButton = new JButton("Войти");
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
            var incorrectUsernameLabel = new JLabel("Такого пользователя не существует");
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
