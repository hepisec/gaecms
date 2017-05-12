<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="https://www.hepisec.de/taglib/cms" %>
<div class="row">
    <c:forEach var="mediaFile" items="${entries}">
        <div class="col-xs-12 col-sm-4 col-md-3">
            <a href="javascript:insertMedia('<c:out value="${param.id}" />', '<c:out value="${mediaFile.blobId}" />')">
                <cms:MediaFile blobid="${mediaFile.blobId}">${mediaFile.description}</cms:MediaFile>
                </a>
            </div>
    </c:forEach>
</div>
<ul class="pager">
    <c:choose>
        <c:when test="${offset le 0}">
            <li class="previous disabled"><a href="#">Previous</a></li>                
        </c:when>
        <c:otherwise>
            <li class="previous"><a href="/page/${previousPage}">Previous</a></li>                
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${offset + entriesPerPage ge numberOfEntries}">
            <li class="next disabled"><a href="#">Next</a></li>                
        </c:when>
        <c:otherwise>
            <li class="next"><a href="/page/${nextPage}">Next</a></li>                
        </c:otherwise>
    </c:choose>
</ul> 
