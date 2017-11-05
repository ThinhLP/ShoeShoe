<%-- 
    Document   : login.jsp
    Created on : Nov 6, 2017, 2:33:56 AM
    Author     : ThinhLPSE61759
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng nhập</title>
    </head>
    <body>
        <form action="ProcessServlet" method="POST">
            Tên đăng nhập: <input type="text" value="admin" name="txtUsername" required/> <br/>
            Mật khẩu: <input type="password" value="1234" name="txtPassword" required/><br/>
            <input type="hidden" value="${requestScope.PREVIOUS_URL}" name="previousUrl" />
            <button type="submit" value="Login" name="btAction">Đăng nhập</button>
        </form>
    </body>
</html>
