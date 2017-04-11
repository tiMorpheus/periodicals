<fmt:setBundle basename="i18n.general" var="general"/>

<nav id="mainMenu" class="navbar navbar-default navbar-static-top">
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <%--TODO custom tag to check user rights--%>
<%--            <custom:if-authorized mustHaveRoles="admin">
                <li><a href="<c:url value="/app/adminPanel"/>">
                    <fmt:message key="menu.adminpanel.label" bundle="${general}"/></a></li>
            </custom:if-authorized>--%>

            <li><a href="<c:url value="/app/periodicals"/>">
                <fmt:message key="menu.periodicals.label" bundle="${general}"/></a></li>

<%--            <custom:if-authorized mustHaveRoles="admin">
                <li><a href="<c:url value="/app/user"/>">
                    <fmt:message key="menu.users.label" bundle="${general}"/></a></li>
            </custom:if-authorized>--%>
        </ul>
    </div>
</nav>