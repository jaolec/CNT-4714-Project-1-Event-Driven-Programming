/* 
 * Name: Leydi De Los Rios
 * Course: CNT 4714 – Fall 2025 – Project Four
 * Assignment title: A Three-Tier Distributed Web-Based Application
 * Date: December 1, 2025
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class RootServlet extends HttpServlet {

    private Properties dbProps;

    @Override
    public void init() throws ServletException {
        dbProps = new Properties();
        try (InputStream in = getServletContext().getResourceAsStream("/WEB-INF/conf/root.properties")) {
            dbProps.load(in);
            Class.forName(dbProps.getProperty("driver"));   // e.g. com.mysql.cj.jdbc.Driver
        } catch (Exception e) {
            throw new ServletException("Unable to load root.properties", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String sql = request.getParameter("sqlCommand");
        request.setAttribute("sqlCommand", sql);

        if ("Reset Form".equals(action)) {
            request.setAttribute("sqlCommand", "");
            request.setAttribute("sqlResult", "");
            request.setAttribute("message", "");
            request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            return;
        }

        if ("Clear Results".equals(action)) {
            request.setAttribute("sqlResult", "");
            request.setAttribute("message", "");
            request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            return;
        }

        String url  = dbProps.getProperty("url");
        String user = dbProps.getProperty("user");
        String pass = dbProps.getProperty("password");

        StringBuilder html = new StringBuilder();
        String msg = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            boolean hasResultSet = stmt.execute(sql);

            // If this was an INSERT/UPDATE/REPLACE affecting shipments,
            // run the business logic.
            String lower = sql.toLowerCase().trim();
            if ((lower.startsWith("insert") || lower.startsWith("update") || lower.startsWith("replace"))
                    && lower.contains("shipments")) {

                int affected = runBusinessLogic(conn);
                msg = "Business Logic Triggered: supplier status adjusted for " + affected + " supplier(s).";
            }

            if (hasResultSet) {
                ResultSet rs = stmt.getResultSet();
                html.append("<table>");
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();

                html.append("<tr>");
                for (int i = 1; i <= cols; i++) {
                    html.append("<th>").append(meta.getColumnName(i)).append("</th>");
                }
                html.append("</tr>");

                while (rs.next()) {
                    html.append("<tr>");
                    for (int i = 1; i <= cols; i++) {
                        html.append("<td>").append(rs.getString(i)).append("</td>");
                    }
                    html.append("</tr>");
                }
                html.append("</table>");
            } else {
                int count = stmt.getUpdateCount();
                html.append("<p>Update count: ").append(count).append("</p>");
            }

        } catch (SQLException e) {
            html.append("<p style='color:red;'>Database error: ").append(e.getMessage()).append("</p>");
        }

        request.setAttribute("sqlResult", html.toString());
        request.setAttribute("message", msg);
        request.getRequestDispatcher("rootHome.jsp").forward(request, response);
    }

    // Non-bonus business logic: bump status for all suppliers who have any shipment with qty >= 100
    private int runBusinessLogic(Connection conn) throws SQLException {
        String update =
            "UPDATE suppliers s " +
            "SET s.status = s.status + 5 " +
            "WHERE EXISTS (" +
            "   SELECT 1 FROM shipments sh " +
            "   WHERE sh.snum = s.snum AND sh.quantity >= 100" +
            ")";
        try (PreparedStatement ps = conn.prepareStatement(update)) {
            return ps.executeUpdate();  // number of suppliers whose status changed
        }
    }
}
