import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class TaskManagementApp extends JFrame {

    private JPanel employeeListPanel;
    private DefaultListModel<String> taskListModel;
    private Map<String, DefaultListModel<String>> employeeTasks;
    private JList<String> taskList;
    private int employeeCount;
    private JPanel taskControlPanel; // Panel for task controls (Add/Remove Task)

    public TaskManagementApp() {
        // Set up the main frame
        setTitle("Task Management App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        employeeCount = 10;
        employeeTasks = new HashMap<>();

        // Left panel for employee sections and Add Employee button
        employeeListPanel = new JPanel();
        employeeListPanel.setLayout(new BoxLayout(employeeListPanel, BoxLayout.Y_AXIS));
        JScrollPane employeeScrollPane = new JScrollPane(employeeListPanel);
        employeeScrollPane.setPreferredSize(new Dimension(200, 0));

        // Wrapper panel to hold both employee list and add button
        JPanel leftPanel = new JPanel(new BorderLayout());

        // Add employee sections (default 10 employees)
        JPanel employeeButtonPanel = new JPanel();
        employeeButtonPanel.setLayout(new BoxLayout(employeeButtonPanel, BoxLayout.Y_AXIS));

        for (int i = 1; i <= 10; i++) {
            addEmployeeSection(employeeButtonPanel, "Employee" + i);
        }

        // Add Employee button at the bottom of the left panel
        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));  // Make button size consistent
        addEmployeeButton.addActionListener(e -> addNewEmployee(employeeButtonPanel));

        // Add the employee button panel and the add employee button to the left panel
        employeeListPanel.add(employeeButtonPanel);
        leftPanel.add(employeeScrollPane, BorderLayout.CENTER);
        leftPanel.add(addEmployeeButton, BorderLayout.SOUTH);

        // Right panel for task management
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane taskScrollPane = new JScrollPane(taskList);
        taskScrollPane.setPreferredSize(new Dimension(400, 400));

        // Task control panel for Add Task and Remove Task buttons
        taskControlPanel = new JPanel();
        taskControlPanel.setLayout(new FlowLayout());
        taskControlPanel.setVisible(false); // Initially hidden

        // Add task button
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(e -> addTask());

        // Remove task button
        JButton removeTaskButton = new JButton("Remove Task");
        removeTaskButton.addActionListener(e -> removeTask());

        // Add task buttons to task control panel
        taskControlPanel.add(addTaskButton);
        taskControlPanel.add(removeTaskButton);

        // Close button at top-right corner
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> System.exit(0));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(closeButton);

        // Add components to the main frame
        add(leftPanel, BorderLayout.WEST);
        add(taskScrollPane, BorderLayout.CENTER);
        add(taskControlPanel, BorderLayout.SOUTH);  // This will only appear when an employee is selected
        add(topPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    // Add a new employee dynamically
    private void addNewEmployee(JPanel employeeButtonPanel) {
        employeeCount++;
        String employeeName = "Employee" + employeeCount;
        addEmployeeSection(employeeButtonPanel, employeeName);
        revalidate();
        repaint();
    }

    // Add a section for an employee on the left panel
    private void addEmployeeSection(JPanel employeeButtonPanel, String employeeName) {
        JButton employeeButton = new JButton(employeeName);
        employeeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));  // Make buttons uniformly sized
        employeeButton.addActionListener(e -> loadEmployeeTasks(employeeName));
        employeeButtonPanel.add(employeeButton);

        // Initialize task list for this employee
        employeeTasks.put(employeeName, new DefaultListModel<>());
    }

    // Load the tasks for the selected employee on the right side
    private void loadEmployeeTasks(String employeeName) {
        taskListModel.clear();
        DefaultListModel<String> tasks = employeeTasks.get(employeeName);
        for (int i = 0; i < tasks.size(); i++) {
            taskListModel.addElement(tasks.getElementAt(i));
        }

        // Make the task control panel visible when an employee is selected
        taskControlPanel.setVisible(true);
        taskControlPanel.revalidate(); // Refresh the panel
        taskControlPanel.repaint(); // Repaint to show changes
    }

    // Add a task to the selected employee's task list
    private void addTask() {
        String selectedEmployee = getSelectedEmployee();
        if (selectedEmployee != null) {
            String task = JOptionPane.showInputDialog("Enter Task for " + selectedEmployee);
            if (task != null && !task.isEmpty()) {
                employeeTasks.get(selectedEmployee).addElement(task);
                loadEmployeeTasks(selectedEmployee);  // Refresh task list for the employee
            }
        }
    }

    // Remove the selected task from the selected employee's task list
    private void removeTask() {
        String selectedEmployee = getSelectedEmployee();
        String selectedTask = taskList.getSelectedValue();
        if (selectedEmployee != null && selectedTask != null) {
            employeeTasks.get(selectedEmployee).removeElement(selectedTask);
            loadEmployeeTasks(selectedEmployee);  // Refresh task list for the employee
        }
    }

    // Get the currently selected employee (last clicked employee button)
    private String getSelectedEmployee() {
        for (Component component : employeeListPanel.getComponents()) {
            if (component instanceof JButton && component.hasFocus()) {
                return ((JButton) component).getText();
            }
        }
        JOptionPane.showMessageDialog(this, "Please select an employee first!");
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagementApp::new);
    }
}
