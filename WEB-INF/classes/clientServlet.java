import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ClientServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String button = request.getParameter("action");
        String sql = request.getParameter("sqlCommand");
        if (sql == null) sql = "";

        // Handle Reset / Clear without touching DB
        if ("Reset Form".equals(button)) {
            request.setAttribute("sqlCommand", "");
            request.setAttribute("message", "Form reset.");
            request.getRequestDispatcher("clientHome.jsp").forward(request, response);
            return;
        }
        if ("Clear Results".equals(button)) {
            request.setAttribute("sqlCommand", sql);
            request.setAttribute("message", "Results cleared.");
            request.setAttribute("table", "");
            request.setAttribute("error", "");
            request.getRequestDispatcher("clientHome.jsp").forward(request, response);
            return;
        }

        // Execute Command button
        request.setAttribute("sqlCommand", sql);

        // Load DB props from client.properties
        Properties props = new Properties();
        try (InputStream in = getServletContext()
                .getResourceAsStream("/WEB-INF/conf/client.properties")) {
            if (in == null) {
                throw new ServletException("Cannot find client.properties");
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

        String message = "";
        String error = "";
        String tableHtml = "";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            if (sql.trim().toLowerCase().startsWith("select")) {
                // Query
                ResultSet rs = stmt.executeQuery(sql);
                tableHtml = buildHtmlTable(rs);
                message = "Result of query:";
            } else {
                // Update / insert / delete / etc.
                int count = stmt.executeUpdate(sql);
                message = "The SQL command executed successfully. " + count + " row(s) affected.";
            }

        } catch (SQLException e) {
            error = "Error executing the SQL command: " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.setAttribute("error", error);
        request.setAttribute("table", tableHtml);

        request.getRequestDispatcher("clientHome.jsp").forward(request, response);
    }

    private String buildHtmlTable(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();

        sb.append("<table>");
        sb.append("<tr>");
        for (int i = 1; i <= colCount; i++) {
            sb.append("<th>").append(meta.getColumnName(i)).append("</th>");
        }
        sb.append("</tr>");

        while (rs.next()) {
            sb.append("<tr>");
            for (int i = 1; i <= colCount; i++) {
                Object val = rs.getObject(i);
                sb.append("<td>");
                sb.append(val == null ? "" : val.toString());
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }
}
