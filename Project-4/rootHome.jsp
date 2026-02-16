<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Root-Level User Interface</title>
    <style>
        body { background-color: black; color: #00FF00; font-family: Arial, sans-serif; }
        h1 { color: #00FF00; text-align: center; }
        h2 { color: #FFFF00; text-align: center; }
        h3 { color: red; text-align: center; }
        .container { width: 80%; margin: 0 auto; }
        textarea {
            width: 100%; height: 200px;
            background-color: blue; color: white;
            font-family: Consolas, monospace;
        }
        .buttons { text-align: center; margin-top: 10px; }
        .buttons input { margin: 0 10px; padding: 6px 18px; font-weight: bold; }
        .results { margin-top: 20px; background-color: black; color: #00FF00;
                   padding: 10px; border: 2px solid #00FF00; }
        table { border-collapse: collapse; width: 100%; color: #00FF00; }
        th, td { border: 1px solid #00FF00; padding: 4px 8px; }
        th { background-color: #003300; }
        .msg { color: cyan; text-align:center; margin-top:10px; }
        .err { color: #FF4444; font-weight:bold; }
    </style>
</head>
<body>
<div class="container">
    <h1>Welcome to the Fall 2025 Project 4 Enterprise System</h1>
    <h2>Root-Level User Interface</h2>
    <h3>You are connected as: root</h3>

    <form method="post" action="root">
        <p>Enter SQL Command:</p>
        <textarea name="sqlCommand"><%= (request.getAttribute("sqlCommand") != null ? request.getAttribute("sqlCommand") : "") %></textarea>
        <div class="buttons">
            <input type="submit" name="action" value="Execute Command">
            <input type="submit" name="action" value="Reset Form">
            <input type="submit" name="action" value="Clear Results">
        </div>
    </form>

    <div class="msg">
        <%= (request.getAttribute("message") != null ? request.getAttribute("message") : "") %>
    </div>

    <div class="results">
        <h3>Execution Results:</h3>
        <%
            String err = (String) request.getAttribute("error");
            if (err != null && !err.isEmpty()) {
        %>
            <p class="err"><%= err %></p>
        <%
            }
            String res = (String) request.getAttribute("sqlResult");
            if (res != null) out.print(res);
        %>
    </div>
</div>
</body>
</html>
