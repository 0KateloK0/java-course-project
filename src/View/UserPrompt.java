package View;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Controller.Controller;

public class UserPrompt extends JPanel implements ActionListener {
    boolean isRepeat = false;
    JTextField usernameInput;
    Controller controller;

    UserPrompt(Controller controller) {
        this.controller = controller;

        setBorder(new EmptyBorder(300, 500, 300, 500));
        setLayout(new GridBagLayout());

        var usernameLabel = new JLabel("Введите ваше имя пользователя");

        usernameInput = new JTextField();

        var submitButton = new JButton("Войти");
        submitButton.addActionListener(this);

        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.weightx = 1;
        c.weighty = 1;
        add(usernameLabel, c);
        add(usernameInput, c);
        add(submitButton, c);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ignored) {
        controller.verify(getUsername());
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
