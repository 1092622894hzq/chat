<%--
  Created by IntelliJ IDEA.
  User: blue
  Date: 2019/9/13
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>error</title>
</head>
<body>
<c:if test="${not empty errCode}">
    <h1>${errCode}</h1>
</c:if>

<c:if test="${not empty errMsg}">
    <h2>${errMsg}</h2>
</c:if>
</body>
</html>
