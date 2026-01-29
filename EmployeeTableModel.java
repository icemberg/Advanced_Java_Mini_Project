import javax.swing.table.AbstractTableModel;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
     private final List<Employee> employees;
     private final String[] columnNames = { "Emp ID", "Name", "Salary", "Gender", "Is Active" };

     public EmployeeTableModel(List<Employee> employees) {
          this.employees = employees;
     }

     @Override
     public int getRowCount() {
          return employees.size();
     }

     @Override
     public int getColumnCount() {
          return columnNames.length;
     }

     @Override
     public String getColumnName(int column) {
          return columnNames[column];
     }

     @Override
     public Object getValueAt(int rowIndex, int columnIndex) {
          Employee emp = employees.get(rowIndex);
          switch (columnIndex) {
               case 0:
                    return emp.getEmpId();
               case 1:
                    return emp.getName();
               case 2:
                    return emp.getSalary();
               case 3:
                    return emp.getGender();
               case 4:
                    return emp.getIsActive() == 1 ? "Yes" : "No";
               default:
                    return null;
          }
     }
}