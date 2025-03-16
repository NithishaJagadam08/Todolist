import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ToDoListApplet extends JFrame {
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField taskInput;

    public ToDoListApplet() {
        setTitle("To-Do List");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        taskInput = new JTextField();
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");

        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeTask());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(addButton);
        panel.add(removeButton);

        add(taskInput, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addTask() {
        String task = taskInput.getText().trim();
        if (!task.isEmpty()) {
            listModel.addElement(task);
            taskInput.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Task cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Select a task to remove!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApplet::new);
    }
}
