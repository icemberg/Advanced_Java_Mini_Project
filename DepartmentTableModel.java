import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DepartmentTableModel extends AbstractTableModel {
     private final List<Department> departments;
     private final String[] columnNames = { "Dept ID", "Dept Name", "Budget", "Location", "Is Operational" };

     public DepartmentTableModel(List<Department> departments) {
          this.departments = departments;
     }

     @Override
     public int getRowCount() {
          return departments.size();
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
          Department dept = departments.get(rowIndex);
          switch (columnIndex) {
               case 0:
                    return dept.getDeptId();
               case 1:
                    return dept.getDeptName();
               case 2:
                    return dept.getBudget();
               case 3:
                    return dept.getLocation();
               case 4:
                    return dept.getIsOperational() == 1 ? "Yes" : "No";
               default:
                    return null;
          }
     }
}