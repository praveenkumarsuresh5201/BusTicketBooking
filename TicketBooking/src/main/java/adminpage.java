import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.http.HttpServletResponse;

public class adminpage {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/busbooking";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private String name;
    private String pass;
    private HttpServletResponse response;

    public adminpage(String name, String pass, HttpServletResponse response) {
        this.name = name;
        this.pass = pass;
        this.response = response;
    }

    public void admin() {
        try {
            Class.forName(DB_DRIVER);
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                // Check if the given name and password match with the data in the database (case-sensitive)
                String sql = "SELECT * FROM admin  WHERE  admin_name  = ? AND  admin_password = ?";
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, pass);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
  

                            // Redirect to another page with user details
                            response.sendRedirect("book-registration.html");
                        } else {
                            // Name and password do not match, show an alert message
                            try (PrintWriter out = response.getWriter()) {
                                out.println("<script type='text/javascript'>");
                                out.println("alert('Name and password do not match.');");
                                out.println("window.location='admin.html';");
                                out.println("</script>");
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Use a logging framework in a real application
        } catch (SQLException | IOException e) {
            e.printStackTrace(); // Use a logging framework in a real application
        }
    }
}