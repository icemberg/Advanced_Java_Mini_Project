import java.util.*;

public class Employee {
     private int empId;
     private String name;
     private float salary;
     private String gender; // Single character as string (e.g., "M" or "F")
     private int isActive; // 0 or 1

     public Employee(int empId, String name, float salary, String gender, int isActive) {
          this.empId = empId;
          this.name = name;
          this.salary = salary;
          this.gender = gender;
          this.isActive = isActive;
     }

     public int getEmpId() {
          return empId;
     }

     public String getName() {
          return name;
     }

     public float getSalary() {
          return salary;
     }

     public String getGender() {
          return gender;
     }

     public int getIsActive() {
          return isActive;
     }

     @Override
     public String toString() {
          return "Employee{empId=" + empId + ", name='" + name + "', salary=" + salary +
                    ", gender='" + gender + "', isActive=" + isActive + "}";
     }

     // Comparators for sorting
     public static Comparator<Employee> byEmpId() {
          return Comparator.comparing(Employee::getEmpId);
     }

     public static Comparator<Employee> byName() {
          return Comparator.comparing(Employee::getName);
     }

     public static Comparator<Employee> bySalary() {
          return Comparator.comparing(Employee::getSalary);
     }

     public static Comparator<Employee> byGender() {
          return Comparator.comparing(Employee::getGender);
     }

     public static Comparator<Employee> byIsActive() {
          return Comparator.comparing(Employee::getIsActive);
     }
}