<%@include file="head.jsp" %>
<div class="container">
    <c:forEach var="entry" items="${entries}">
        <h3><c:out value="${entry.title}"/></h3>
        <div class="blog-entry">
            <c:out value="${entry.text}" escapeXml="false"/>
        </div>
        <div class="blog-date">
            <c:choose>
                <c:when test="${entry.datePublished ne null}">
                    Published on <fmt:formatDate value="${entry.datePublished}" pattern="yyyy-MM-dd"/>
                </c:when>
                <c:otherwise>
                    Created on <fmt:formatDate value="${entry.dateCreated}" pattern="yyyy-MM-dd"/>
                </c:otherwise>
            </c:choose>
        </div>        
        <div class="blog-controls">
            <% if (request.isUserInRole("admin")) { %>
            <c:choose>
                <c:when test="${entry.datePublished eq null}">
                    <a href="/admin/Write?id=<c:out value="${entry.id}"/>" class="btn btn-danger">not published</a>
                </c:when>
                <c:otherwise>
                    <span class="btn btn-success">published</span>
                </c:otherwise>
            </c:choose>
            <a href="/admin/Write?id=<c:out value="${entry.id}"/>" class="btn btn-default">edit</a>
            <a href="/admin/Delete?id=<c:out value="${entry.id}"/>" class="btn btn-default">delete</a>
            <% }%>
            <a href="/articles/<c:out value="${entry.id}"/>" class="btn btn-default"><c:out value="${fn:length(entry.comments)}"/> Comments</a>
        </div>
    </c:forEach>
</div>
<%@include file="pager.jsp" %>
<%@include file="foot.jsp" %>