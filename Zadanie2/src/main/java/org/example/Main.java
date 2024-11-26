package org.example;
import static spark.Spark.*;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final Gson gson = new Gson();
    public static void main(String[] args)  {
        port(4567);
        post("/students", (req, res) -> {
            Student student = gson.fromJson(req.body(), Student.class);
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (first_name, last_name, middle_name, birth_date, group_name, unique_number) VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, student.firstName);
                stmt.setString(2, student.lastName);
                stmt.setString(3, student.middleName);
                stmt.setDate(4, Date.valueOf(student.birthDate));
                stmt.setString(5, student.groupName);
                stmt.setString(6, student.uniqueNumber);
                stmt.executeUpdate();
            }
            return "Student added";
        });
        delete("/students/:uniqueNumber", (req, res) -> {
            String uniqueNumber = req.params(":uniqueNumber");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE unique_number = ?")) {
                stmt.setString(1, uniqueNumber);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    return "Student deleted";
                } else {
                    return "Student not found";
                }
            }
        });
        get("/students", (req, res) -> {
            List<Student> students = new ArrayList<>();
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
                while (rs.next()) {
                    Student student = new Student();
                    student.id = rs.getInt("id");
                    student.firstName = rs.getString("first_name");
                    student.lastName = rs.getString("last_name");
                    student.middleName = rs.getString("middle_name");
                    student.birthDate = rs.getDate("birth_date").toString();
                    student.groupName = rs.getString("group_name");
                    student.uniqueNumber = rs.getString("unique_number");
                    students.add(student);
                }
            }
            return gson.toJson(students);
        });
    }
    static class Student {
        int id;
        String firstName;
        String lastName;
        String middleName;
        String birthDate;
        String groupName;
        String uniqueNumber;
    }
}
