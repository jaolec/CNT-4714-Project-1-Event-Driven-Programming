<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Data Entry Application</title>
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
            color: #00FFFF; /* cyan */
            margin-top: 5px;
        }
        .info {
            text-align: center;
            color: white;
            margin: 10px 0 20px 0;
            font-size: 0.9rem;
        }
        .info span.role {
            color: #FF4444; /* red/orange */
            font-weight: bold;
        }
        .container {
            width: 95%;
            margin: 0 auto;
        }
        fieldset {
            border: 2px solid #FFFF00;
            margin-bottom: 18px;
            padding: 8px;
        }
        legend {
            color: #FFFF00;
            font-weight: bold;
            padding: 0 5px;
        }
        table {
            width: 100%;
        }
        th, td {
            padding: 4px;
            color: #00FF00;
        }
        input[type="text"] {
            width: 95%;
            background-color: #666600;
            border: 1px solid #CCCC00;
            color: #FFFFFF;
        }
        .btn-row {
            text-align: center;
            margin-top: 6px;
        }
        .btn-green {
            background-color: #00AA00;
            color: #000;
            font-weight: bold;
            padding: 4px 14px;
            border: 1px solid #00FF00;
            cursor: pointer;
        }
        .btn-red {
            background-color: #CC0000;
            color: #FFF;
            font-weight: bold;
            padding: 4px 14px;
            border: 1px solid #FF4444;
            cursor: pointer;
        }
        .results-bar {
            margin-top: 10px;
            padding: 6px;
            background-color: #0000AA;
            color: #FFFF00;
            text-align: center;
            min-height: 24px;
        }
        .status-ok { color: #00FF00; }
        .status-error { color: #FF4444; }
        .business-msg { color: #00FFFF; margin-top: 4px; }
        .results-label {
            text-align: center;
            margin-top: 15px;
            color: #FFFFFF;
        }
    </style>
</head>
<body>
<h1>Welcome to the Fall 2025 Project 4 Enterprise System</h1>
<h2>Data Entry Application</h2>

<div class="info">
    You are connected to the Project 4 Enterprise System database as a
    <span class="role">dataentry-level</span> user.
    Enter the data values in a form below to add a new record to the corresponding database table.
</div>

<div class="container">
    <form method="post" action="dataentry">

        <!-- Suppliers -->
        <fieldset>
            <legend>Suppliers Record Insert</legend>
            <table>
                <tr>
                    <th>snum</th>
                    <th>sname</th>
                    <th>status</th>
                    <th>city</th>
                </tr>
                <tr>
                    <td><input type="text" name="snum"></td>
                    <td><input type="text" name="sname"></td>
                    <td><input type="text" name="status"></td>
                    <td><input type="text" name="scity"></td>
                </tr>
            </table>
            <div class="btn-row">
                <input type="submit" class="btn-green" name="form"
                       value="Enter Supplier Record Into Database">
                <input type="submit" class="btn-red" name="form"
                       value="Clear Supplier Data and Results">
            </div>
        </fieldset>

        <!-- Parts -->
        <fieldset>
            <legend>Parts Record Insert</legend>
            <table>
                <tr>
                    <th>pnum</th>
                    <th>pname</th>
                    <th>color</th>
                    <th>weight</th>
                    <th>city</th>
                </tr>
                <tr>
                    <td><input type="text" name="pnum"></td>
                    <td><input type="text" name="pname"></td>
                    <td><input type="text" name="color"></td>
                    <td><input type="text" name="weight"></td>
                    <td><input type="text" name="pcity"></td>
                </tr>
            </table>
            <div class="btn-row">
                <input type="submit" class="btn-green" name="form"
                       value="Enter Part Record Into Database">
                <input type="submit" class="btn-red" name="form"
                       value="Clear Part Data and Results">
            </div>
        </fieldset>

        <!-- Jobs -->
        <fieldset>
            <legend>Jobs Record Insert</legend>
            <table>
                <tr>
                    <th>jnum</th>
                    <th>jname</th>
                    <th>numworkers</th>
                    <th>city</th>
                </tr>
                <tr>
                    <td><input type="text" name="jnum"></td>
                    <td><input type="text" name="jname"></td>
                    <td><input type="text" name="numworkers"></td>
                    <td><input type="text" name="jcity"></td>
                </tr>
            </table>
            <div class="btn-row">
                <input type="submit" class="btn-green" name="form"
                       value="Enter Job Record Into Database">
                <input type="submit" class="btn-red" name="form"
                       value="Clear Job Data and Results">
            </div>
        </fieldset>

        <!-- Shipments -->
        <fieldset>
            <legend>Shipments Record Insert</legend>
            <table>
                <tr>
                    <th>snum</th>
                    <th>pnum</th>
                    <th>jnum</th>
                    <th>quantity</th>
                </tr>
                <tr>
                    <td><input type="text" name="ssnum"></td>
                    <td><input type="text" name="spnum"></td>
                    <td><input type="text" name="sjnum"></td>
                    <td><input type="text" name="quantity"></td>
                </tr>
            </table>
            <div class="btn-row">
                <input type="submit" class="btn-green" name="form"
                       value="Enter Shipment Record Into Database">
                <input type="submit" class="btn-red" name="form"
                       value="Clear Shipment Data and Results">
            </div>
        </fieldset>

        <div class="results-label">Execution Results:</div>
        <div class="results-bar">
            <span class="<%= (request.getAttribute("statusClass") != null
                               ? request.getAttribute("statusClass")
                               : "status-ok") %>">
                <%= (request.getAttribute("statusMessage") != null
                        ? request.getAttribute("statusMessage")
                        : "") %>
            </span>
            <div class="business-msg">
                <%= (request.getAttribute("businessMessage") != null
                        ? request.getAttribute("businessMessage")
                        : "") %>
            </div>
        </div>

    </form>
</div>
</body>
</html>
