package View;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Common.Task;
import Common.Commands.CreateTaskCommand;
import Controller.Controller;

public class TaskCreationManager extends JPanel implements ActionListener {
    JLabel errorLabel;
    JTextField nameInsert;
    DateSetter dateSetter;
    Controller controller;
    private String SUBMIT_ACTION_COMMAND = "submit";
    // private String CLOSE_ACTION_COMMAND = "submit";

    public TaskCreationManager(Controller controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());

        var submitButton = new JButton("Создать");
        var c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 3;
        c.weighty = 1;
        c.weightx = 0;
        c.ipadx = 20;
        add(submitButton, c);
        submitButton.setActionCommand(SUBMIT_ACTION_COMMAND);

        nameInsert = new JTextField();
        c.gridy = 2;
        c.gridx = 1;
        c.weighty = 1;
        c.weightx = 0.9;
        c.ipadx = 20;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(nameInsert, c);

        dateSetter = new DateSetter();
        c.gridy = 2;
        c.gridx = 2;
        c.weighty = 1;
        c.weightx = 0;
        c.ipadx = 20;
        c.fill = GridBagConstraints.NONE;
        add(dateSetter, c);

        errorLabel = new JLabel("");
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0.1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(errorLabel, c);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);

        // var hideButton = new JButton("Скрыть");
        // c.gridx = 2;
        // c.gridy = 1;
        // c.weighty = 0.1;
        // c.weightx = 1;
        // add(hideButton, c);
        // hideButton.setActionCommand(CLOSE_ACTION_COMMAND);

        // hideButton.addActionListener(this);
        submitButton.addActionListener(this);
    }

    private void showError(String errorText) {
        errorLabel.setText(errorText);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(SUBMIT_ACTION_COMMAND)) {
            if (nameInsert.getText().isEmpty()) {
                showError("Название задания не может быть пустым");
            } else {
                hideError();
                var task = new Task();
                task.name = nameInsert.getText();
                task.deadline = dateSetter.getDate();
                controller.executeCommand(new CreateTaskCommand(controller, task));
            }
        } else {

        }

    }
}
