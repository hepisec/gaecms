<div class="container">
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
</div>