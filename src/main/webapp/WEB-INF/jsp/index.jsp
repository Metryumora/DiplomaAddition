<%--
  Created by IntelliJ IDEA.
  User: Metr_yumora
  Date: 12.06.2017
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
Hello, world!
<c:forEach items="${groups}" varStatus="loop" var="org">
    <p>${group.name}</p>
</c:forEach>
</body>
</html>
