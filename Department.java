import java.util.*;

public class Department {
     private int deptId;
     private String deptName;
     private float budget;
     private String location;
     private int isOperational; // 0 or 1

     public Department(int deptId, String deptName, float budget, String location, int isOperational) {
          this.deptId = deptId;
          this.deptName = deptName;
          this.budget = budget;
          this.location = location;
          this.isOperational = isOperational;
     }

     public int getDeptId() {
          return deptId;
     }

     public String getDeptName() {
          return deptName;
     }

     public float getBudget() {
          return budget;
     }

     public String getLocation() {
          return location;
     }

     public int getIsOperational() {
          return isOperational;
     }

     @Override
     public String toString() {
          return "Department{deptId=" + deptId + ", deptName='" + deptName + "', budget=" + budget +
                    ", location='" + location + "', isOperational=" + isOperational + "}";
     }

     // Comparators for sorting
     public static Comparator<Department> byDeptId() {
          return Comparator.comparing(Department::getDeptId);
     }

     public static Comparator<Department> byDeptName() {
          return Comparator.comparing(Department::getDeptName);
     }

     public static Comparator<Department> byBudget() {
          return Comparator.comparing(Department::getBudget);
     }

     public static Comparator<Department> byLocation() {
          return Comparator.comparing(Department::getLocation);
     }

     public static Comparator<Department> byIsOperational() {
          return Comparator.comparing(Department::getIsOperational);
     }
}