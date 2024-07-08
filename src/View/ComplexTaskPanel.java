// package View;

// import java.awt.GridBagConstraints;
// import java.awt.GridBagLayout;

// import javax.swing.JPanel;

// import Common.TaskMap;
// import Controller.Controller;

// public class ComplexTaskPanel extends JPanel {
// public ComplexTaskPanel(Controller controller, TaskMap tasks,
// TaskMap.TaskNode node) {
// setLayout(new GridBagLayout());
// var c = new GridBagConstraints();

// var parent = new TaskPanel(controller, tasks.get(node.taskId));
// c.weightx = 1;
// c.weighty = 1;
// c.gridx = 1;
// c.gridy = 1;
// add(parent, c);

// for (var taskNode : node.children) {
// var child = new TaskPanel(controller, tasks.get(taskNode.taskId));
// c.weightx = 0.8;
// c.weighty = 1;
// c.gridx = 1;
// c.gridy = GridBagConstraints.RELATIVE;
// add(child, c);
// }
// }
// }
