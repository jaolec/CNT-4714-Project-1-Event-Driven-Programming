<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Accountant-Level User Interface</title>
    <style>
        body {
            background-color: black;
            color: #00FF00;
            font-family: Arial, sans-serif;
        }
        h1 {
            text-align: center;
            color: #00FF00;
            margin-top: 20px;
        }
        h2 {
            text-align: center;
            color: #00FFFF;
            margin-top: 5px;
        }
        h3 {
            text-align: center;
            color: #00FF00;
            margin-top: 5px;
        }
        .info {
            text-align: center;
            color: white;
            margin: 10px 0 15px 0;
            font-size: 0.9rem;
        }
        .info span.role { color: #FFAA00; font-weight: bold; }

        .panel {
            width: 90%;
            margin: 0 auto;
            background-color: #404040;
            padding: 15px 25px;
            border-radius: 4px;
        }
        .panel ul {
            list-style-type: disc;
            color: #FFFFFF;
        }
        .panel li {
            margin: 8px 0;
            font-size: 0.95rem;
        }
        .panel label {
            color: #0000FF; /* blue link-style text */
            cursor: pointer;
            text-decoration: underline;
        }
        .buttons {
            text-align: center;
            margin-top: 15px;
        }
        .btn-green {
            background-color: #00AA00;
            color: #000;
            font-weight: bold;
            padding: 4px 16px;
            border: 1px solid #00FF00;
            cursor: pointer;
            margin-right: 10px;
        }
        .btn-red {
            background-color: #CC0000;
            color: #FFF;
            font-weight: bold;
            padding: 4px 16px;
            border: 1px solid #FF4444;
            cursor: pointer;
        }
        .results-heading {
            text-align: center;
            margin-top: 18px;
            color: #FFFFFF;
        }
        .results-box {
            width: 90%;
            margin: 0 auto 20px auto;
            border: 2px solid #FF0000;
            padding: 8px;
            min-height: 40px;
            background-color: #000000;
            color: #00FF00;
            text-align: center;
        }
        table {
            border-collapse: collapse;
            margin: 0 auto;
            color: #00FF00;
        }
        th, td {
            border: 1px solid #00FF00;
            padding: 4px 8px;
        }
        th { background-color: #003300; }
        .error { color: #FF4444; font-weight: bold; }
        .ok { color: #00FF00; font-weight: bold; }
    </style>
</head>
<body>
<h1>Welcome to the Fall 2025 Project 4 Enterprise System</h1>
<h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
<h3>Accountant-Level User Interface</h3>

<div class="info">
    You are connected to the Project 4 Enterprise System database as an
    <span class="role">accountant-level</span> user.<br>
    Please select the operation you would like to perform from the list below.
</div>

<form method="post" action="accountant">
    <div class="panel">
        <ul>
            <li>
                <input type="radio" name="report" value="1" id="r1" checked>
                <label for="r1">Get The Maximum Status Value Of All Suppliers</label>
                (Returns a maximum value)
            </li>
            <li>
                <input type="radio" name="report" value="2" id="r2">
                <label for="r2">Get The Total Weight Of All Parts</label>
                (Returns a sum)
            </li>
            <li>
                <input type="radio" name="report" value="3" id="r3">
                <label for="r3">Get The Total Number Of Shipments</label>
                (Returns the current number of shipments in total)
            </li>
            <li>
                <input type="radio" name="report" value="4" id="r4">
                <label for="r4">Get The Name And Number Of Workers Of The Job With The Most Workers</label>
                (Returns two values)
            </li>
            <li>
                <input type="radio" name="report" value="5" id="r5">
                <label for="r5">List The Name And Status Of Every Supplier</label>
                (Returns a list of supplier names with their current status)
            </li>
        </ul>

        <div class="buttons">
            <input type="submit" class="btn-green" value="Execute Command">
            <input type="submit" class="btn-red" name="clear" value="Clear Results">
        </div>
    </div>

    <div class="results-heading">Execution Results:</div>
    <div class="results-box">
        <%
            String statusClass = (String) request.getAttribute("statusClass");
            String statusMessage = (String) request.getAttribute("statusMessage");
            String sqlResult = (String) request.getAttribute("sqlResult");

            if (statusClass == null) statusClass = "ok";

            if (statusMessage != null && !statusMessage.isEmpty()) {
        %>
            <div class="<%= statusClass %>"><%= statusMessage %></div>
        <%
            }

            if (sqlResult != null && !sqlResult.isEmpty()) {
                out.print(sqlResult);
            }
        %>
    </div>
</form>
</body>
</html>
