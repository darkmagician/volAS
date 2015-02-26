<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<html>
<head>
<title>注销</title>
</head>

<body>
  <% 
    HttpSession s = request.getSession(false);
    s.invalidate();
   %>
   <h1>注销</h1>

   <p>您已经注销！</p> 
    <a href="login.html">重新登录</a>
</body>

</html>
