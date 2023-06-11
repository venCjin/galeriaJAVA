<%@ page import="com.jsuchinski.galeria.model.User" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon"/>
    <link rel="icon" href="img/favicon.ico" type="image/x-icon"/>
    <%
        String title = "GALERIA JAVA - Suchiński";
        String setTitle = (String) session.getAttribute("title");
        if(setTitle != null) {
            title = setTitle;
        }
    %>
    <title><%=title%></title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.min.css">
    <link rel="stylesheet" type="text/css" href="css/jbootstrap.css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Rubik&amp;subset=latin-ext"/>
    <script src="js/jquery-3.1.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>

<body>
<header>
    <%
        String header = "Witaj!";
        User user = (User) session.getAttribute("user");
        if(user != null) {
            header = MessageFormat.format("Witaj <b>{0}</b>!", user.getLogin());
        }
    %>
    <%=header%>
</header>
