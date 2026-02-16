/* 
 * Name: Jack Muir
 * Course: CNT 4714 – Fall 2025 – Project Four
 * Assignment title: A Three-Tier Distributed Web-Based Application
 * Date: December 1, 2025
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class AccountantServlet extends HttpServlet {

    private Properties dbProps;

    @Override
    public void init() throws ServletException {
        dbProps = new Properties();
        try (InputStream in = getServletContext()
                .getResourceAsStream("/WEB-INF/conf/accountant.properties")) {
            dbProps.load(in);
            Class.forName(dbProps.getProperty("driver"));
        } catch (Exception e) {
            throw new ServletException("Unable to load accountant.properties", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String report = request.getParameter("report");
        String url  = dbProps.getProperty("url");
        String user = dbProps.getProperty("user");
        String pass = dbProps.getProperty("password");

        StringBuilder html = new StringBuilder();
        String statusMessage = "";
        String statusClass   = "ok";

        if (report == null) {
            statusClass = "error";
            statusMessage = "No report was selected.";
            request.setAttribute("statusClass", statusClass);
            request.setAttribute("statusMessage", statusMessage);
            request.setAttribute("sqlResult", "");
            request.getRequestDispatcher("accountantHome.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            String callSql;

            // Map each radio button to the correct stored procedure
            switch (report) {
                case "1":
                    // Get The Maximum Status Value Of All Suppliers
                    callSql = "{ call Get_The_Maximum_Status_Of_All_Suppliers() }";
                    break;
                case "2":
                    // Get The Total Weight Of All Parts
                    callSql = "{ call Get_The_Sum_Of_All_Parts_Weights() }";
                    break;
                case "3":
                    // Get The Total Number Of Shipments
                    callSql = "{ call Get_The_Total_Number_Of_Shipments() }";
                    break;
                case "4":
                    // Get The Name And Number Of Workers Of The Job With The Most Workers
                    callSql = "{ call Get_The_Name_Of_The_Job_With_The_Most_Workers() }";
                    break;
                case "5":
                    // List The Name And Status Of Every Supplier
                    callSql = "{ call List_The_Name_And_Status_Of_All_Suppliers() }";
                    break;
                default:
                    statusClass = "error";
                    statusMessage = "Unknown report selection.";
                    callSql = null;
            }

            if (callSql != null) {
                try (CallableStatement cs = conn.prepareCall(callSql)) {
                    boolean hasResultSet = cs.execute();

                    if (hasResultSet) {
                        try (ResultSet rs = cs.getResultSet()) {
                            ResultSetMetaData meta = rs.getMetaData();
                            int cols = meta.getColumnCount();

                            html.append("<table><tr>");
                            for (int i = 1; i <= cols; i++) {
                                html.append("<th>")
                                    .append(meta.getColumnName(i))
                                    .append("</th>");
                            }
                            html.append("</tr>");

                            while (rs.next()) {
                                html.append("<tr>");
                                for (int i = 1; i <= cols; i++) {
                                    html.append("<td>")
                                        .append(rs.getString(i))
                                        .append("</td>");
                                }
                                html.append("</tr>");
                            }
                            html.append("</table>");
                        }
                    } else {
                        int count = cs.getUpdateCount();
                        html.append("<p>Procedure executed. Update count: ")
                            .append(count)
                            .append("</p>");
                    }
                }
            }

        } catch (SQLException e) {
            statusClass = "error";
            statusMessage = "Database error: " + e.getMessage();
        }

        request.setAttribute("statusClass", statusClass);
        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("sqlResult", html.toString());
        request.getRequestDispatcher("accountantHome.jsp").forward(request, response);
    }
}

