# Database Viewer - GUI Application

A sophisticated Java-based GUI application that provides an interactive interface for viewing and managing database records. The application demonstrates advanced Java concepts including Swing components, JDBC connectivity, Java Collections Framework, and custom data models.

## Overview

**Database Viewer** is a desktop application built with Java Swing that connects to a MySQL database and allows users to:
- View employee and department records from a MySQL database
- Sort data by different columns
- Store data in various Java collection types (ArrayList, LinkedList, HashSet, TreeSet, Stack)
- Add new employee and department records with validation
- Visualize data in a responsive, styled table interface with a modern gradient UI

## Project Architecture

### Core Components

#### 1. **DatabaseViewer.java** (Main Application)
The main GUI application class that orchestrates all UI components and database operations.

**Key Features:**
- **Gradient UI**: Custom purple-to-pink gradient background for modern aesthetics
- **Rounded Buttons**: Custom styled buttons with hover effects
- **Database Connection**: JDBC connectivity to MySQL database
- **Dual Table Support**: Manages both Employees and Departments tables
- **Dynamic Forms**: Contextual forms that change based on selected table
- **Collection Selection**: Users can choose how data is stored (ArrayList, LinkedList, HashSet, TreeSet, Stack)
- **Data Sorting**: Sort records by any column with appropriate comparators

**Main UI Panels:**
- **Input Panel (North)**: Table selection, column sorting, collection type selection, and fetch button
- **Output Panel (Center)**: Displays data in a styled JTable with alternating row colors
- **Collection Label Panel (South)**: Shows the currently selected collection type
- **Form Panel (East)**: Dynamic form for adding new records

**Database Configuration:**
- Database: `my_database`
- Host: `localhost:3306`
- Tables: `Employees`, `Departments`

#### 2. **Employee.java**
Data model class representing an employee record.

**Attributes:**
- `empId` (int): Unique employee identifier (Primary Key)
- `name` (String): Employee full name
- `salary` (float): Employee salary
- `gender` (String): Single character (M/F)
- `isActive` (int): Boolean flag (0 or 1) indicating if employee is active

**Features:**
- Getters for all attributes
- Built-in comparators for sorting by each field (`byEmpId()`, `byName()`, `bySalary()`, `byGender()`, `byIsActive()`)
- `toString()` method for debugging and logging

#### 3. **EmployeeTableModel.java**
Custom Swing TableModel for displaying employee data in a JTable.

**Responsibilities:**
- Maps employee object properties to table columns
- Handles row and column count
- Converts data types for display (e.g., boolean to "Yes"/"No")
- Provides column names and table structure

**Columns:**
- Emp ID, Name, Salary, Gender, Is Active

#### 4. **Department.java**
Data model class representing a department record.

**Attributes:**
- `deptId` (int): Unique department identifier (Primary Key)
- `deptName` (String): Department name
- `budget` (float): Department budget
- `location` (String): Department location
- `isOperational` (int): Boolean flag (0 or 1) indicating if department is operational

**Features:**
- Getters for all attributes
- Built-in comparators for sorting by each field
- `toString()` method for debugging

#### 5. **DepartmentTableModel.java**
Custom Swing TableModel for displaying department data in a JTable.

**Responsibilities:**
- Maps department object properties to table columns
- Handles row and column count
- Converts data types for display

**Columns:**
- Dept ID, Dept Name, Budget, Location, Is Operational

## How It Works

### Data Flow

1. **Initialization**: When the application starts:
   - MySQL JDBC driver is loaded
   - Database connection is established
   - Tables are created if they don't exist
   - Sample data is inserted if tables are empty

2. **Fetching Data**:
   - User selects a table (Employees or Departments)
   - User selects a sort column
   - User selects a collection type
   - User clicks "Fetch Data"
   - Application retrieves data from database and loads into selected collection
   - Data is sorted using the appropriate comparator
   - Results are displayed in the table

3. **Adding Records**:
   - User fills in all required fields in the form panel
   - Input validation occurs (correct data types, non-empty fields)
   - Upon clicking "Add Row", data is inserted into the database
   - User receives confirmation or error message
   - Table is refreshed to show new data

### Collection Types

The application demonstrates the usage of different Java collection types:

- **ArrayList**: Ordered, allows duplicates, good for random access
- **LinkedList**: Ordered, allows duplicates, efficient insertion/deletion
- **HashSet**: Unordered, no duplicates, fast lookups
- **TreeSet**: Sorted (uses comparators), no duplicates, maintains order
- **Stack**: LIFO (Last In, First Out), useful for undo operations

## Database Schema

### Employees Table
```sql
CREATE TABLE Employees (
    emp_id INT PRIMARY KEY,
    name VARCHAR(255),
    salary FLOAT,
    gender CHAR(1),
    is_active BOOLEAN
);
```

### Departments Table
```sql
CREATE TABLE Departments (
    dept_id INT PRIMARY KEY,
    dept_name VARCHAR(255),
    budget FLOAT,
    location VARCHAR(255),
    is_operational BOOLEAN
);
```

## Prerequisites

- **Java**: JDK 8 or higher
- **MySQL**: Server running on localhost:3306
- **MySQL JDBC Driver**: `mysql-connector-java` (included in `/lib` directory or add to classpath)
- **Database**: Create a database named `my_database`

## Setup Instructions

### 1. Database Setup
Create the MySQL database:
```sql
CREATE DATABASE my_database;
USE my_database;
```

### 2. Configure Database Connection
Edit the database credentials in [DatabaseViewer.java](DatabaseViewer.java#L23-L25):
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/my_database?useSSL=false&serverTimezone=UTC";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password";
```

### 3. Compile
```bash
javac -cp lib/* *.java
```

### 4. Run
```bash
java -cp lib/*:. DatabaseViewer
```

## Usage Guide

1. **Select Table**: Choose between "Employees" or "Departments" from the first dropdown
2. **Sort By**: Select which column to sort the data by
3. **Select Collection**: Choose the Java collection type for data storage
4. **Fetch Data**: Click to retrieve and display the data
5. **Add Records**: Fill in the form on the right panel with valid data and click "Add Row"

### Input Validation

The application validates all inputs:
- **Integer fields**: Must be valid integers
- **Float fields**: Must be valid floating-point numbers
- **String fields**: Cannot be empty
- **Character fields**: Must be exactly one character
- **Boolean fields**: Must be "true" or "false" (case-insensitive)
- **Primary Keys**: Must be unique (enforced by database)

## Technical Highlights

### Design Patterns Used
- **Model-View-Controller (MVC)**: Separation of data models from UI display
- **Factory Pattern**: Creation of different collection types
- **Comparator Pattern**: Flexible sorting strategies

### Advanced Java Concepts
- **Swing Components**: Custom UI components (GradientPanel, RoundedButton)
- **JDBC**: Database connectivity and SQL operations
- **Collections Framework**: Diverse collection types and sorting
- **Generics**: Type-safe collections and comparators
- **Prepared Statements**: SQL injection prevention
- **Lambda Expressions**: Event listeners and comparators

### UI Features
- Gradient background for modern aesthetics
- Custom rounded buttons with hover effects
- Alternating row colors in table for readability
- Dynamic form generation based on table selection
- Responsive error handling with dialog boxes
- Non-blocking UI with proper exception handling

## File Structure

```
AJava/
├── DatabaseViewer.java       # Main GUI application
├── Employee.java             # Employee data model
├── EmployeeTableModel.java   # Employee table model
├── Department.java           # Department data model
├── DepartmentTableModel.java # Department table model
├── README.md                 # This file
└── lib/                      # External libraries (mysql-connector-java)
```

## Sample Data

The application initializes with sample data:

**Employees:**
- Alice (ID: 1, Salary: 50000.0, Gender: F, Active: Yes)
- Bob (ID: 2, Salary: 60000.0, Gender: M, Active: No)
- Charlie (ID: 3, Salary: 55000.0, Gender: M, Active: Yes)

**Departments:**
- HR (ID: 1, Budget: 100000.0, Location: North, Operational: Yes)
- IT (ID: 2, Budget: 150000.0, Location: South, Operational: No)

## Error Handling

The application includes comprehensive error handling:
- Database connection failures
- SQL execution errors
- Input validation errors
- Type conversion errors
- Duplicate key violations (with helpful error messages)

All errors are displayed in user-friendly dialog boxes and logged to console.

## Future Enhancements

Potential improvements for the application:
- **Search/Filter**: Add search functionality to filter records
- **Edit Records**: Allow editing existing database records
- **Delete Records**: Add delete functionality with confirmation
- **Export**: Export data to CSV or Excel format
- **Advanced Sorting**: Multi-column sorting and custom sort orders
- **Database Management**: Create/drop tables, modify schemas
- **Theme Customization**: Allow users to customize UI colors
- **Pagination**: Handle large datasets with pagination

## License

This project is provided as-is for educational purposes.

## Author Notes

This application demonstrates professional Java development practices including:
- Clean code organization and separation of concerns
- Proper resource management (try-with-resources for database connections)
- Input validation and error handling
- Custom UI components and styling
- JDBC best practices with prepared statements
- Effective use of Java Collections Framework
