<%@include file="head.jsp" %>
<div class="container">
    <div class="col-lg-8 col-lg-offset-2">
        <div class="blog-entry panel panel-default">
            <div class="panel-heading">
                <h3><c:out value="${entry.title}"/></h3>
            </div>
            <div class="panel-body">
                <c:out value="${entry.text}" escapeXml="false"/>
            </div>
            <div class="panel-footer">
                <c:choose>
                    <c:when test="${entry.datePublished ne null}">
                        Published on <fmt:formatDate value="${entry.datePublished}" pattern="yyyy-MM-dd"/>
                    </c:when>
                    <c:otherwise>
                        Created on <fmt:formatDate value="${entry.dateCreated}" pattern="yyyy-MM-dd"/>
                    </c:otherwise>
                </c:choose>
            </div>        
        </div>
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
    </div>       
    <div class="blog-comments col-lg-6 col-lg-offset-3">
        <h4>Comments</h4>
        <c:choose>
            <c:when test="${fn:length(entry.comments) eq 0}">
                <p class="alert alert-warning">There are no comments yet. Feel free to write one.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="comment" items="${entry.comments}">
                    <div class="blog-comment panel panel-default">
                        <div class="panel-body"><c:out value="${comment.text}"/></div>
                        <div class="panel-footer">by <c:out value="${comment.name}"/> on <fmt:formatDate value="${comment.dateCreated}" pattern="yyyy-MM-dd, HH:mm"/></div>
                    </div>
                </c:forEach>                
            </c:otherwise>
        </c:choose>
    </div>
    <form class="form-horizontal col-lg-6 col-lg-offset-3" method="POST" action="/Comments">
        <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue uri="/Comments"/>" />                    
        <fieldset>
            <legend>Write Comment</legend>
            <div class="form-group">        
                <div class="col-lg-12">
                    <label class="control-label" for="name">Name</label>
                </div>
                <div class="col-lg-12">
                    <input type="text" id="name" name="name" class="form-control" />
                </div>
            </div>
            <div class="form-group">        
                <div class="col-lg-12">
                    <label class="control-label" for="text">Text</label>
                </div>
                <div class="col-lg-12">
                    <textarea id="text" name="text" rows="10" cols="60" class="form-control"></textarea>
                </div>
            </div>
            <div class="form-group">        
                <div class="col-lg-12 text-center">
                    <input type="hidden" name="blog_id" value="<c:out value="${entry.id}"/>" />
                    <input type="submit" name="submit" value="Write Comment" class="btn btn-default" />
                </div>
            </div>            
        </fieldset>
    </form>
</div>    
<%@include file="foot.jsp" %>