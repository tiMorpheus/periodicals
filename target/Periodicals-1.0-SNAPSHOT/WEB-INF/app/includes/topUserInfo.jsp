<fmt:setBundle basename="i18n.general" var="langGeneral"/>

<p class="text-right"><span class="userFullName">${currentUser.lastName} ${currentUser.firstName}</span>

    <a href="<c:url value="/app/users/currentUser"/>"
       class="btn btn-primary myAccountBtn" role="button">
        <fmt:message key="myaccount.label" bundle="${langGeneral}"/></a>

    <a href="<c:url value="/app/signOut"/>">
        <fmt:message key="signout.label" bundle="${langGeneral}"/></a></p>
