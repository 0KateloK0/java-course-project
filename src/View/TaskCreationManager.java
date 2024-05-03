package View;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BoxLayout;

import Common.Task;
import Common.Commands.CreateTaskCommand;
import Controller.Controller;

public class TaskCreationManager extends Panel {
    public TaskCreationManager(Controller controller) {
        var submitButton = new Button("Создать");
        var nameInsert = new TextField();
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
