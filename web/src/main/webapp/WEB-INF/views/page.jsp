<%--@elvariable id="page" type="com.ninedemons.leaderboard.api.Page"--%>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Page ${page.pageNumber} from ${leaderboardName}</title>
</head>
<body>
    <p>
    <ul>

        <c:forEach items="${page.entries}" var="entry">
            <li><c:out value="${entry.rank}. ${entry.userId} with a score of ${entry.score}"/></li>
        </c:forEach>
    </ul>
    </p>

</body>
</html>