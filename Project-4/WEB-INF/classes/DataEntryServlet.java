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

public class DataEntryServlet extends HttpServlet {

    private Properties dbProps;

    @Override
    public void init() throws ServletException {
        dbProps = new Properties();
        try (InputStream in = getServletContext()
                .getResourceAsStream("/WEB-INF/conf/dataentry.properties")) {
            dbProps.load(in);
            Class.forName(dbProps.getProperty("driver"));
        } catch (Exception e) {
            throw new ServletException("Unable to load dataentry.properties", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String form = request.getParameter("form");
        String url  = dbProps.getProperty("url");
        String user = dbProps.getProperty("user");
        String pass = dbProps.getProperty("password");

        String statusMessage   = "";
        String statusClass     = "status-ok";   // CSS class in JSP
        String businessMessage = "";

        // If no button value, just redisplay page
        if (form == null) {
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {

            // ---------- SUPPLIERS ----------
            if ("Enter Supplier Record Into Database".equals(form)) {
                String sql = "INSERT INTO suppliers (snum, sname, status, city) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, request.getParameter("snum"));
                    ps.setString(2, request.getParameter("sname"));
                    ps.setInt(3, Integer.parseInt(request.getParameter("status")));
                    ps.setString(4, request.getParameter("scity"));
                    int count = ps.executeUpdate();
                    statusMessage = count + " supplier row inserted.";
                }

            // ---------- PARTS ----------
            } else if ("Enter Part Record Into Database".equals(form)) {
                String sql = "INSERT INTO parts (pnum, pname, color, weight, city) " +
                             "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, request.getParameter("pnum"));
                    ps.setString(2, request.getParameter("pname"));
                    ps.setString(3, request.getParameter("color"));
                    ps.setInt(4, Integer.parseInt(request.getParameter("weight")));
                    ps.setString(5, request.getParameter("pcity"));
                    int count = ps.executeUpdate();
                    statusMessage = count + " part row inserted.";
                }

            // ---------- JOBS ----------
            } else if ("Enter Job Record Into Database".equals(form)) {
                String sql = "INSERT INTO jobs (jnum, jname, numworkers, city) " +
                             "VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, request.getParameter("jnum"));
                    ps.setString(2, request.getParameter("jname"));
                    ps.setInt(3, Integer.parseInt(request.getParameter("numworkers")));
                    ps.setString(4, request.getParameter("jcity"));
                    int count = ps.executeUpdate();
                    statusMessage = count + " job row inserted.";
                }

            // ---------- SHIPMENTS ----------
            } else if ("Enter Shipment Record Into Database".equals(form)) {
                String sql = "INSERT INTO shipments (snum, pnum, jnum, quantity) " +
                             "VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, request.getParameter("ssnum"));
                    ps.setString(2, request.getParameter("spnum"));
                    ps.setString(3, request.getParameter("sjnum"));
                    ps.setInt(4, Integer.parseInt(request.getParameter("quantity")));
                    int count = ps.executeUpdate();
                    statusMessage = count + " shipment row inserted.";
                }

                // run the same business logic as root
                int affected = runBusinessLogic(conn);
                businessMessage =
                        "Business Logic Triggered: supplier status adjusted for "
                        + affected + " supplier(s).";

            // ---------- CLEAR BUTTONS ----------
            } else if ("Clear Supplier Data and Results".equals(form)
                    || "Clear Part Data and Results".equals(form)
                    || "Clear Job Data and Results".equals(form)
                    || "Clear Shipment Data and Results".equals(form)) {

                // We don't need to touch the DB; just clear messages
                statusMessage = "";
                businessMessage = "";
            }

        } catch (SQLException | NumberFormatException e) {
            statusClass = "status-error";
            statusMessage = "Error: " + e.getMessage();
        }

        request.setAttribute("statusMessage", statusMessage);
        request.setAttribute("statusClass", statusClass);
        request.setAttribute("businessMessage", businessMessage);
        request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
    }

    // Business rule: any supplier that appears in shipments with quantity >= 100
    // gets its status increased by 5.
    private int runBusinessLogic(Connection conn) throws SQLException {
        String update =
                "UPDATE suppliers s " +
                "SET s.status = s.status + 5 " +
                "WHERE EXISTS (" +
                "   SELECT 1 FROM shipments sh " +
                "   WHERE sh.snum = s.snum AND sh.quantity >= 100" +
                ")";
        try (PreparedStatement ps = conn.prepareStatement(update)) {
            return ps.executeUpdate();
        }
    }
}
