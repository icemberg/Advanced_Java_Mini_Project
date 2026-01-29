import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.table.*;

@SuppressWarnings("unused")
public class DatabaseViewer extends JFrame {
     private JComboBox<String> tableComboBox;
     private JComboBox<String> columnComboBox;
     private JComboBox<String> collectionComboBox;
     private JButton fetchButton;
     private JTable resultTable;
     private JLabel collectionLabel;
     private JTextField[] inputFields;
     private JButton addButton;
     private JPanel formPanel;

     private static final String DB_URL = "jdbc:mysql://localhost:3306/my_database?useSSL=false&serverTimezone=UTC";
     private static final String DB_USER = "root";
     private static final String DB_PASSWORD = "abhi@2004";
     private final String[] tables = { "Employees", "Departments" };
     private final String[] collections = { "ArrayList", "LinkedList", "HashSet", "TreeSet", "Stack" };
     private final Map<String, String[]> columnsMap = new HashMap<>();
     private final Map<String, String[]> columnTypes = new HashMap<>();
     private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
     private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
     private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 12);

     // Custom panel for gradient background
     private static class GradientPanel extends JPanel {
          @Override
          protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2d = (Graphics2D) g;
               int w = getWidth();
               int h = getHeight();
               Color color1 = new Color(127, 0, 255); // Purple (#7F00FF)
               Color color2 = new Color(225, 0, 255); // Pink (#E100FF)
               GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
               g2d.setPaint(gp);
               g2d.fillRect(0, 0, w, h);
          }
     }

     // Custom rounded button
     private static class RoundedButton extends JButton {
          public RoundedButton(String text) {
               super(text);
               setContentAreaFilled(false);
               setFocusPainted(false);
               setFont(LABEL_FONT);
               setForeground(Color.WHITE);
               setBackground(new Color(100, 50, 200)); // Dark purple
               setBorder(new EmptyBorder(8, 15, 8, 15));
               addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                         setBackground(new Color(150, 100, 250)); // Lighter purple on hover
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                         setBackground(new Color(100, 50, 200));
                    }
               });
          }

          @Override
          protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2.setColor(getBackground());
               g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
               super.paintComponent(g2);
               g2.dispose();
          }

          @Override
          protected void paintBorder(Graphics g) {
               // No border
          }
     }

     public DatabaseViewer() {
          super("Database Viewer");
          initializeDatabase();
          setupUI();
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          pack();
          setLocationRelativeTo(null);
          setVisible(true);
     }

     private void setupUI() {
          // Create gradient panel as content pane
          GradientPanel contentPane = new GradientPanel();
          contentPane.setLayout(new BorderLayout(10, 10));
          setContentPane(contentPane);

          // Input panel for dropdowns and fetch button
          JPanel inputPanel = new JPanel(new GridLayout(4, 2, 15, 15));
          inputPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(15, 15, 15, 15),
                    BorderFactory.createLineBorder(Color.WHITE, 1, true)));
          inputPanel.setOpaque(false);

          // Table selection
          JLabel tableLabel = new JLabel("📋 Select Table:");
          tableLabel.setFont(LABEL_FONT);
          tableLabel.setForeground(Color.WHITE);
          inputPanel.add(tableLabel);
          tableComboBox = new JComboBox<>(tables);
          tableComboBox.setFont(TEXT_FONT);
          tableComboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
          inputPanel.add(tableComboBox);

          // Column selection
          JLabel columnLabel = new JLabel("📊 Sort by Column:");
          columnLabel.setFont(LABEL_FONT);
          columnLabel.setForeground(Color.WHITE);
          inputPanel.add(columnLabel);
          columnsMap.put("Employees", new String[] { "emp_id", "name", "salary", "gender", "is_active" });
          columnsMap.put("Departments",
                    new String[] { "dept_id", "dept_name", "budget", "location", "is_operational" });
          columnTypes.put("Employees", new String[] { "int", "string", "float", "char", "boolean" });
          columnTypes.put("Departments", new String[] { "int", "string", "float", "string", "boolean" });
          columnComboBox = new JComboBox<>(columnsMap.get("Employees"));
          columnComboBox.setFont(TEXT_FONT);
          columnComboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
          inputPanel.add(columnComboBox);

          // Collection selection
          JLabel collectionLabelInput = new JLabel("📚 Select Collection:");
          collectionLabelInput.setFont(LABEL_FONT);
          collectionLabelInput.setForeground(Color.WHITE);
          inputPanel.add(collectionLabelInput);
          collectionComboBox = new JComboBox<>(collections);
          collectionComboBox.setFont(TEXT_FONT);
          collectionComboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
          inputPanel.add(collectionComboBox);

          // Fetch button
          inputPanel.add(new JLabel(""));
          fetchButton = new RoundedButton("Fetch Data");
          inputPanel.add(fetchButton);

          // Result table (output panel) with padding
          resultTable = new JTable() {
               @Override
               public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    if (row % 2 == 0) {
                         c.setBackground(new Color(240, 240, 255)); // Light purple
                    } else {
                         c.setBackground(Color.WHITE);
                    }
                    return c;
               }
          };
          resultTable.setFont(TEXT_FONT);
          resultTable.getTableHeader().setFont(LABEL_FONT);
          resultTable.getTableHeader().setBackground(new Color(100, 50, 200));
          resultTable.getTableHeader().setForeground(Color.WHITE);
          JScrollPane scrollPane = new JScrollPane(resultTable);
          scrollPane.setOpaque(false);
          scrollPane.getViewport().setOpaque(false);
          scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
          // Wrap scrollPane in a panel for padding
          JPanel outputPanel = new JPanel(new BorderLayout());
          outputPanel.setOpaque(false);
          outputPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // 15px left/right padding
          outputPanel.add(scrollPane, BorderLayout.CENTER);

          // Collection display label (below table)
          JPanel collectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          collectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
          collectionPanel.setOpaque(false);
          JLabel collectionTextLabel = new JLabel("Selected Collection:");
          collectionTextLabel.setFont(LABEL_FONT);
          collectionTextLabel.setForeground(Color.WHITE);
          collectionPanel.add(collectionTextLabel);
          collectionLabel = new JLabel(collections[0]);
          collectionLabel.setFont(LABEL_FONT);
          collectionLabel.setForeground(Color.WHITE);
          collectionPanel.add(collectionLabel);

          // Form panel for adding rows with increased padding
          formPanel = new JPanel();
          formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
          TitledBorder formBorder = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1, true),
                    "Add New Employee",
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION,
                    TITLE_FONT,
                    Color.WHITE);
          formPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 20, 10, 20), // Increased to 20px left/right
                    formBorder));
          formPanel.setBackground(new Color(255, 255, 255, 100)); // Semi-transparent white
          updateFormPanel("Employees");

          // Layout
          contentPane.add(inputPanel, BorderLayout.NORTH);
          contentPane.add(outputPanel, BorderLayout.CENTER);
          contentPane.add(collectionPanel, BorderLayout.SOUTH);
          contentPane.add(formPanel, BorderLayout.EAST);

          // Listeners
          tableComboBox.addActionListener(e -> {
               updateColumnComboBox();
               updateFormPanel((String) tableComboBox.getSelectedItem());
          });
          collectionComboBox.addActionListener(e -> updateCollectionLabel());
          fetchButton.addActionListener(e -> fetchAndDisplayData());
     }

     private void updateColumnComboBox() {
          String selectedTable = (String) tableComboBox.getSelectedItem();
          columnComboBox.removeAllItems();
          for (String column : columnsMap.get(selectedTable)) {
               columnComboBox.addItem(column);
          }
     }

     private void updateCollectionLabel() {
          String selectedCollection = (String) collectionComboBox.getSelectedItem();
          collectionLabel.setText(selectedCollection);
     }

     private void updateFormPanel(String table) {
          formPanel.removeAll();
          TitledBorder formBorder = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1, true),
                    "Add New " + table.substring(0, table.length() - 1),
                    TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION,
                    TITLE_FONT,
                    Color.WHITE);
          formPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 20, 10, 20), // Increased to 20px left/right
                    formBorder));
          String[] columns = columnsMap.get(table);
          String[] types = columnTypes.get(table);
          inputFields = new JTextField[columns.length];

          for (int i = 0; i < columns.length; i++) {
               JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
               fieldPanel.setOpaque(false);
               JLabel label = new JLabel(columns[i] + " (" + types[i] + "):");
               label.setFont(LABEL_FONT);
               label.setForeground(Color.WHITE);
               fieldPanel.add(label);
               inputFields[i] = new JTextField(12);
               inputFields[i].setFont(TEXT_FONT);
               inputFields[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
               fieldPanel.add(inputFields[i]);
               formPanel.add(fieldPanel);
          }

          addButton = new RoundedButton("Add Row");
          addButton.addActionListener(e -> addRow(table));
          JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
          buttonPanel.setOpaque(false);
          buttonPanel.add(addButton);
          formPanel.add(buttonPanel);

          formPanel.revalidate();
          formPanel.repaint();
     }

     private void addRow(String table) {
          String[] columns = columnsMap.get(table);
          String[] types = columnTypes.get(table);
          StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
          for (int i = 0; i < columns.length; i++) {
               sql.append(columns[i]);
               if (i < columns.length - 1) {
                    sql.append(", ");
               }
          }
          sql.append(") VALUES (");
          for (int i = 0; i < columns.length; i++) {
               sql.append("?");
               if (i < columns.length - 1) {
                    sql.append(", ");
               }
          }
          sql.append(")");

          try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

               // Validate and set parameters
               for (int i = 0; i < columns.length; i++) {
                    String input = inputFields[i].getText().trim();
                    if (input.isEmpty()) {
                         throw new IllegalArgumentException(columns[i] + " cannot be empty");
                    }

                    switch (types[i]) {
                         case "int":
                              try {
                                   pstmt.setInt(i + 1, Integer.parseInt(input));
                              } catch (NumberFormatException e) {
                                   throw new IllegalArgumentException(columns[i] + " must be an integer");
                              }
                              break;
                         case "float":
                              try {
                                   pstmt.setFloat(i + 1, Float.parseFloat(input));
                              } catch (NumberFormatException e) {
                                   throw new IllegalArgumentException(columns[i] + " must be a float");
                              }
                              break;
                         case "char":
                              if (input.length() != 1) {
                                   throw new IllegalArgumentException(columns[i] + " must be a single character");
                              }
                              pstmt.setString(i + 1, input);
                              break;
                         case "string":
                              pstmt.setString(i + 1, input);
                              break;
                         case "boolean":
                              String lowerInput = input.toLowerCase();
                              if (!lowerInput.equals("true") && !lowerInput.equals("false")) {
                                   throw new IllegalArgumentException(
                                             columns[i] + " must be 'true' or 'false' (case-insensitive)");
                              }
                              pstmt.setBoolean(i + 1, Boolean.parseBoolean(lowerInput));
                              break;
                    }
               }

               pstmt.executeUpdate();
               JOptionPane.showMessageDialog(this, "Row added successfully to " + table);
               for (JTextField field : inputFields) {
                    field.setText("");
               }
               fetchAndDisplayData();
          } catch (SQLException e) {
               String errorMsg = "Error adding row: " + e.getMessage();
               if (e.getMessage().contains("Duplicate entry")) {
                    errorMsg = "Error: Duplicate primary key (" + columns[0] + "). Please use a unique ID.";
               }
               System.err.println("SQL Error: " + e.getMessage());
               e.printStackTrace();
               JOptionPane.showMessageDialog(this, errorMsg);
          } catch (IllegalArgumentException e) {
               System.err.println("Validation Error: " + e.getMessage());
               JOptionPane.showMessageDialog(this, "Validation error: " + e.getMessage());
          }
     }

     private void initializeDatabase() {
          try {
               Class.forName("com.mysql.cj.jdbc.Driver");
          } catch (ClassNotFoundException e) {
               JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found: " + e.getMessage());
               System.err.println("Driver Error: " + e.getMessage());
               e.printStackTrace();
               return;
          }

          try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    Statement stmt = conn.createStatement()) {
               stmt.execute("CREATE TABLE IF NOT EXISTS Employees (" +
                         "emp_id INT PRIMARY KEY, " +
                         "name VARCHAR(255), " +
                         "salary FLOAT, " +
                         "gender CHAR(1), " +
                         "is_active BOOLEAN)");
               stmt.execute("CREATE TABLE IF NOT EXISTS Departments (" +
                         "dept_id INT PRIMARY KEY, " +
                         "dept_name VARCHAR(255), " +
                         "budget FLOAT, " +
                         "location VARCHAR(255), " +
                         "is_operational BOOLEAN)");

               ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Employees");
               if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO Employees VALUES (1, 'Alice', 50000.0, 'F', TRUE)");
                    stmt.execute("INSERT INTO Employees VALUES (2, 'Bob', 60000.0, 'M', FALSE)");
                    stmt.execute("INSERT INTO Employees VALUES (3, 'Charlie', 55000.0, 'M', TRUE)");
               }
               rs = stmt.executeQuery("SELECT COUNT(*) FROM Departments");
               if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO Departments VALUES (1, 'HR', 100000.0, 'North', TRUE)");
                    stmt.execute("INSERT INTO Departments VALUES (2, 'IT', 150000.0, 'South', FALSE)");
               }
          } catch (SQLException e) {
               JOptionPane.showMessageDialog(this, "Database initialization error: " + e.getMessage());
               System.err.println("DB Init Error: " + e.getMessage());
               e.printStackTrace();
          }
     }

     private void fetchAndDisplayData() {
          String selectedTable = (String) tableComboBox.getSelectedItem();
          String selectedColumn = (String) columnComboBox.getSelectedItem();
          String selectedCollection = (String) collectionComboBox.getSelectedItem();

          try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM " + selectedTable)) {

               if (selectedTable.equals("Employees")) {
                    List<Employee> empList = new ArrayList<>();
                    while (rs.next()) {
                         empList.add(new Employee(
                                   rs.getInt("emp_id"),
                                   rs.getString("name"),
                                   rs.getFloat("salary"),
                                   rs.getString("gender"),
                                   rs.getBoolean("is_active") ? 1 : 0));
                    }
                    displayEmployees(empList, selectedColumn, selectedCollection);
               } else {
                    List<Department> deptList = new ArrayList<>();
                    while (rs.next()) {
                         deptList.add(new Department(
                                   rs.getInt("dept_id"),
                                   rs.getString("dept_name"),
                                   rs.getFloat("budget"),
                                   rs.getString("location"),
                                   rs.getBoolean("is_operational") ? 1 : 0));
                    }
                    displayDepartments(deptList, selectedColumn, selectedCollection);
               }
          } catch (SQLException e) {
               JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
               System.err.println("Fetch Error: " + e.getMessage());
               e.printStackTrace();
          }
     }

     private void displayEmployees(List<Employee> empList, String column, String collectionType) {
          Comparator<Employee> comparator = getEmployeeComparator(column);
          Collection<Employee> result;

          switch (collectionType) {
               case "ArrayList":
                    result = new ArrayList<>();
                    break;
               case "LinkedList":
                    result = new LinkedList<>();
                    break;
               case "HashSet":
                    result = new HashSet<>();
                    break;
               case "TreeSet":
                    result = new TreeSet<>(comparator);
                    break;
               case "Stack":
                    result = new Stack<>();
                    break;
               default:
                    result = new ArrayList<>();
          }

          for (Employee emp : empList) {
               result.add(emp);
          }

          List<Employee> displayList;
          if (collectionType.equals("Stack")) {
               // For Stack, reverse the order to show latest added first (LIFO)
               displayList = new ArrayList<>();
               Stack<Employee> stack = (Stack<Employee>) result;
               while (!stack.isEmpty()) {
                    displayList.add(stack.pop());
               }
          } else {
               if (result instanceof List) {
                    ((List<Employee>) result).sort(comparator);
               }
               displayList = result instanceof List ? (List<Employee>) result : new ArrayList<>(result);
          }

          resultTable.setModel(new EmployeeTableModel(displayList));
     }

     private void displayDepartments(List<Department> deptList, String column, String collectionType) {
          Comparator<Department> comparator = getDepartmentComparator(column);
          Collection<Department> result;

          switch (collectionType) {
               case "ArrayList":
                    result = new ArrayList<>();
                    break;
               case "LinkedList":
                    result = new LinkedList<>();
                    break;
               case "HashSet":
                    result = new HashSet<>();
                    break;
               case "TreeSet":
                    result = new TreeSet<>(comparator);
                    break;
               case "Stack":
                    result = new Stack<>();
                    break;
               default:
                    result = new ArrayList<>();
          }

          for (Department dept : deptList) {
               result.add(dept);
          }

          List<Department> displayList;
          if (collectionType.equals("Stack")) {
               // For Stack, reverse the order to show latest added first (LIFO)
               displayList = new ArrayList<>();
               Stack<Department> stack = (Stack<Department>) result;
               while (!stack.isEmpty()) {
                    displayList.add(stack.pop());
               }
          } else {
               if (result instanceof List) {
                    ((List<Department>) result).sort(comparator);
               }
               displayList = result instanceof List ? (List<Department>) result : new ArrayList<>(result);
          }

          resultTable.setModel(new DepartmentTableModel(displayList));
     }

     private Comparator<Employee> getEmployeeComparator(String column) {
          switch (column) {
               case "emp_id":
                    return Employee.byEmpId();
               case "name":
                    return Employee.byName();
               case "salary":
                    return Employee.bySalary();
               case "gender":
                    return Employee.byGender();
               case "is_active":
                    return Employee.byIsActive();
               default:
                    return Employee.byEmpId();
          }
     }

     private Comparator<Department> getDepartmentComparator(String column) {
          switch (column) {
               case "dept_id":
                    return Department.byDeptId();
               case "dept_name":
                    return Department.byDeptName();
               case "budget":
                    return Department.byBudget();
               case "location":
                    return Department.byLocation();
               case "is_operational":
                    return Department.byIsOperational();
               default:
                    return Department.byDeptId();
          }
     }

     public static void main(String[] args) {
          SwingUtilities.invokeLater(DatabaseViewer::new);
     }
}