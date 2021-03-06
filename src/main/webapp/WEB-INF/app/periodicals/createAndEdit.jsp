<%@include file="/WEB-INF/includes/header.jsp" %>
<fmt:setBundle basename="i18n.periodical.periodical" var="langPeriodical"/>
<fmt:setBundle basename="i18n.validation.validation" var="validation"/>
<fmt:setBundle basename="i18n.general.general" var="general"/>

<div class="row">
    <div class="col-md-8 col-md-offset-2">
        <c:if test="${periodicalOperationType == 'create'}">
            <h1><fmt:message key="createPeriodical.title" bundle="${langPeriodical}"/></h1>
        </c:if>

        <c:if test="${periodicalOperationType == 'update'}">
            <h1><fmt:message key="editPeriodical.title" bundle="${langPeriodical}"/></h1>
        </c:if>

        <h3><fmt:message key="title.top" bundle="${langPeriodical}"/></h3>

        <form class="form-horizontal" role="form"
              method="post"
              action="/app/periodicals">

            <div class="form-group hidden">
                <input id="entityId" type="text" class="form-control"
                       name="entityId"
                       value="${periodical.id}"/>
            </div>
<%--Name--%>
            <div class="form-group validated required">

                <label for="periodicalName" class="col-sm-3 control-label">
                    <fmt:message key="name.label" bundle="${langPeriodical}"/></label>

                <div class="col-sm-9">
                    <input id="periodicalName" type="text" class="form-control ajax-validated"
                           name="periodicalName" value="${periodical.name}"
                           placeholder="<fmt:message key="name.label" bundle="${langPeriodical}"/>"/>
                    <c:if test="${(not empty messages) && (not empty messages['periodicalName'])}">
                        <label class="messages ${messages['periodicalName'].type == 'ERROR' ? 'error' : ''}">
                            <fmt:message key="${messages['periodicalName'].messageKey}" bundle="${validation}"/>
                        </label>
                    </c:if>
                </div>
            </div>
<%--Category--%>
            <div class="form-group validated required">
                <label for="periodicalCategory" class="col-sm-3 control-label">
                    <fmt:message key="category.label" bundle="${langPeriodical}"/></label>
                <div class="col-sm-9">
                    <select id="periodicalCategory" class="form-control"
                            name="periodicalCategory">
                        <c:forEach items="${periodicalCategories}" var="category">
                            <option value="${category}" ${category == periodical.category ? 'selected' : ''}>
                                <fmt:message key="${category.messageKey}" bundle="${langPeriodical}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>
<%--Publisher--%>
            <div class="form-group validated required">
                <label for="periodicalPublisher" class="col-sm-3 control-label">
                    <fmt:message key="publisher.label" bundle="${langPeriodical}"/></label>
                <div class="col-sm-9">
                    <input id="periodicalPublisher" type="text" class="form-control ajax-validated"
                           name="periodicalPublisher"
                           value="${periodical.publisher}"
                           placeholder="<fmt:message key="publisher.label" bundle="${langPeriodical}"/>"/>
                    <c:if test="${(not empty messages) && (not empty messages['periodicalPublisher'])}">
                        <label class="messages
                            ${messages['periodicalPublisher'].type == 'ERROR' ? 'error' : ''}">
                            <fmt:message key="${messages['periodicalPublisher'].messageKey}" bundle="${validation}"/>
                        </label>
                    </c:if>
                </div>
            </div>
<%--Description--%>
            <div class="form-group">
                <label for="periodicalDescription" class="col-sm-3 control-label">
                    <fmt:message key="description.label" bundle="${langPeriodical}"/></label>
                <div class="col-sm-9">
                    <textarea id="periodicalDescription" class="form-control" rows="4"
                              name="periodicalDescription" maxlength="999"
                              placeholder="<fmt:message key="description.label" bundle="${langPeriodical}"/>">
                        ${periodical.description}
                    </textarea>
                </div>
            </div>
<%--One month cost--%>
            <div class="form-group validated required">
                <label for="periodicalCost" class="col-sm-3 control-label">
                    <fmt:message key="oneMonthCost.label" bundle="${langPeriodical}"/></label>
                <div class="col-sm-9">
                    <input id="periodicalCost" type="number" class="form-control ajax-validated"
                           name="periodicalCost" min="0" max="999999999"
                           value="${periodical.oneMonthCost}"
                           placeholder="<fmt:message key="oneMonthCost.label" bundle="${langPeriodical}"/>"/>
                    <c:if test="${(not empty messages) && (not empty messages['periodicalCost'])}">
                        <label class="messages ${messages['periodicalCost'].type == 'ERROR' ? 'error' : ''}">
                            <fmt:message key="${messages['periodicalCost'].messageKey}" bundle="${validation}"/>
                        </label>
                    </c:if>
                </div>
            </div>
<%--Status--%>
            <div class="form-group">
                <label for="periodicalStatus" class="col-sm-3 control-label">
                    <fmt:message key="status.label" bundle="${langPeriodical}"/></label>
                <div class="col-sm-9">
                    <select id="periodicalStatus" class="form-control"
                            name="periodicalStatus">
                        <c:forEach items="${periodicalStatuses}" var="status">
                            <option ${status == periodical.status ? 'selected' : ''} value="${status}">
                                <fmt:message key="${status}" bundle="${langPeriodical}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-group hidden">
                <input id="periodicalOperationType" type="text" class="form-control"
                       name="periodicalOperationType"
                       value="${periodicalOperationType}"/>
            </div>

            <div class="form-group">
                <div class="col-md-9 col-md-offset-3">
                    <p class="requiredFieldsMessage">
                        <fmt:message key="requiredFieldsMessage" bundle="${general}"/></p>
                </div>
            </div>

            <div class="row text-center">
                <button type="submit"
                        class="btn btn-primary disabled">
                    <fmt:message key="savePeriodicalBtn.label" bundle="${langPeriodical}"/>
                </button>
                <a href="/app/periodicals/${(periodicalOperationType == 'update') ? periodical.id : ''}"
                   class="btn btn-default" role="button">
                    <fmt:message key="cancelBtn.label" bundle="${general}"/>
                </a>
            </div>
        </form>

    </div>

</div>

<%@include file="/WEB-INF/includes/footer.jsp" %>