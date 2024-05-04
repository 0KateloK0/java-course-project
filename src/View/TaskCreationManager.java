package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Common.Task;
import Common.Commands.CreateTaskCommand;
import Controller.Controller;

public class TaskCreationManager extends JPanel {
    public TaskCreationManager(Controller controller) {
        var submitButton = new JButton("Создать");
        var nameInsert = new JTextField();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(submitButton);
        add(nameInsert);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var task = new Task();
                task.name = nameInsert.getText();
                controller.executeCommand(new CreateTaskCommand(controller, task));
            }
        });
    }
}
