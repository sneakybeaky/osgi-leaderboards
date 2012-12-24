<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Spring MVC In Karaf Example</title>
    <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>

    <script>
        $(document).ready(function() {
            $("#accordion").accordion({ collapsible: true, active: 2 });
        });
    </script>
</head>
<body>
<div id="accordion">
    <h3><a href="#">Deployed Bundles</a></h3>
    <div>
        <p>
        <ul>
            <c:forEach items="${bundles}" var="bundle">
                <li><c:out value="${bundle}"/></li>
            </c:forEach>
        </ul>
        </p>
    </div>
    <h3><a href="#">Configurations</a></h3>
    <div>
        <p>
        <ul>
            <c:forEach items="${configurations}" var="configuration">
                <li><c:out value="${configuration.pid}"/></li>
            </c:forEach>
        </ul>
        </p>
    </div>
</div>
</body>
</html>