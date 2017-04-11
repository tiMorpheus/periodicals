<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form name="LoginForm" action="controller" method="post">
    <input type="hidden" name="command" value="Login">
    Email:<br/>
    <input type="text" name="Email" value="" />
    <br/>Password:<br/>
    <input type="password" name="password" value="" />
    <br/>
    ${errorEmailPassMessage}
    <br/>
    ${wrongAction}
    <br/>
    ${nullPage}
    <br/>
    <input type="submit" value="Log in">
</form>
<hr/>
</body>
</html>
