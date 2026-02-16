<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Client-Level Interface</title>
    <style>
        body {
            background-color: black;
            color: #00FF00;
            font-family: Arial, Helvetica, sans-serif;
            text-align: center;
        }
        h1 {
            color: #00FF00;
            margin-top: 20px;
        }
        h2 {
            color: #FFFF00;
        }
        h3 {
            color: cyan;
        }
        .panel {
            margin: 20px auto;
            width: 80%;
            border: 2px solid #0000FF;
            background-color: #000044;
            padding: 10px;
        }
        textarea {
            width: 95%;
            height: 150px;
            background-color: #0000AA;
            color: #FFFFFF;
            font-family: Consolas, monospace;
            font-size: 1em;
        }
        .buttons {
            margin-top: 10px;
        }
        .btn {
            background-color: #00CC00;
            border: 1px solid #003300;
            color: #000000;
            font-weight: bold;
            padding: 6px 16px;
            margin: 0 5px;
            cursor: pointer;
        }
        .results {
            margin: 20px auto;
            width: 90%;
            min-height: 150px;
            border: 2px solid #00FF00;
            background-color: #001100;
            padding: 10px;
            text-align: left;
            color: #FFFFFF;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            color: #FFFFFF;
        }
        th, td {
            border: 1px solid #00FF00;
            padding: 4px 6px;
            font-size: 0.9em;
        }
        th {
            background-color: #003300;
        }
        .msg {
            color: #FFFF00;
            font-weight: bold;
        }
        .err {
            color: #FF4444;
            font-weight: bold;
        }
    </style>
</head>
<body>

<h1>Welcome to the Fall 2025 Project 4 Enterprise System</h1>
<h2>Client-Level User Interface</h2>
<h3>You are connected as: client</h3>

<div class="panel">
    <form method="post" action="client">
        <p style="text-align:left; color:#FFFFFF; margin-left:2%;">Enter SQL Command:</p>
        <textarea name="sqlCommand"><%= request.getAttribute("sqlCommand") == null ? "" : request.getAttribute("sqlCommand") %></textarea>

        <div class="buttons">
            <input class="btn" type="submit" name="action" value="Execute Command">
            <input class="btn" type="submit" name="action" value="Reset Form">
            <input class="btn" type="submit" name="action" value="Clear Results">
        </div>
    </form>
</div>

<div class="results">
    <p><span class="msg">
        <%= request.getAttribute("message") == null ? "" : request.getAttribute("message") %>
    </span></p>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null && !error.isEmpty()) {
    %>
        <p class="err"><%= error %></p>
    <%
        }
        String tableHtml = (String) request.getAttribute("table");
        if (tableHtml != null && !tableHtml.isEmpty()) {
            out.println(tableHtml);
        }
    %>
</div>

</body>
</html>
