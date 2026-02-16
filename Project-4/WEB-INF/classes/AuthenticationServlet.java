import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Load DB connection info from systemapp.properties
        Properties props = new Properties();
        try (InputStream in = getServletContext()
                .getResourceAsStream("/WEB-INF/conf/systemapp.properties")) {
            if (in == null) {
                throw new ServletException("Cannot find systemapp.properties");
            }
            props.load(in);
        }

        String driver = props.getProperty("driver");
        String url = props.getProperty("url");
        String dbUser = props.getProperty("user");
        String dbPassword = props.getProperty("password");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found", e);
        }

        boolean valid = false;

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String sql = "SELECT * FROM usercredentials "
                       + "WHERE login_username = ? AND login_password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    valid = rs.next();
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error during authentication", e);
        }

        if (!valid) {
            // No matching row in credentialsDB → send to error page
            response.sendRedirect("errorpage.html");
            return;
        }

        // Valid user → redirect to the correct home page
        switch (username) {
            case "root":
                response.sendRedirect("rootHome.jsp");      // later you'll rename to .jsp
                break;
            case "client":
                response.sendRedirect("clientHome.jsp");
                break;
            case "dataentry":
                response.sendRedirect("dataentryHome.jsp");
                break;
            case "theaccountant":
                response.sendRedirect("accountantHome.jsp");
                break;
            default:
                // Some unknown user somehow got in – treat as error
                response.sendRedirect("errorpage.html");
                break;
        }
    }
}
