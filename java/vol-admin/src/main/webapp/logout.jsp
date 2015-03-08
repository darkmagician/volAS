<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<html>
<head>
<meta charset="UTF-8">
<title>注销</title>
<link rel="stylesheet" type="text/css"
	href="./static/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="./static/themes/icon.css">
<link rel="stylesheet" type="text/css" href="./static/style.css">
<link rel="shortcut icon" href="./static/images/favicon.ico"
	type="image/x-icon" />
<script type="text/javascript" src="./static/jquery.min.js"></script>
<script type="text/javascript" src="./static/jquery.easyui.min.js"></script>
</head>
<body>
  <% 
    HttpSession s = request.getSession(false);
    s.invalidate();
   %>
   	<div style="position:absolute;top:50%;left:50%;margin-top:-150px;margin-left:-250px;">
	<div class="easyui-panel" title="注销" style="width: 500px;padding:10px 60px 20px 60px">
		<div class="ftitle">您已经注销！</div>
    <a href="login.html">重新登录</a>
	</div>   
	</div>  
</body>

</html>
