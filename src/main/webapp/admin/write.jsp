<%@page import="com.googlecode.objectify.Ref"%>
<%@include file="../WEB-INF/head.jsp" %>
<div class="container">
    <form class="form-horizontal col-lg-6 col-lg-offset-3" method="post" action="/admin/Write">
        <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue uri="/admin/Write"/>" />
        <fieldset>
            <legend>Write Blog</legend>
            <c:if test="${entry.id eq null}">
                <div class="form-group">        
                    <div class="col-lg-12">
                        <label class="control-label" for="id">ID</label>
                    </div>
                    <div class="col-lg-12">
                        <input type="text" id="id" name="id" value="<c:out value="${entry.id}"/>" class="form-control" />
                    </div>
                </div>            
            </c:if>
            <div class="form-group">        
                <div class="col-lg-12">
                    <label class="control-label" for="title">Title</label>
                </div>
                <div class="col-lg-12">
                    <input type="text" id="title" name="title" value="<c:out value="${entry.title}"/>" class="form-control" />
                </div>
            </div>
            <div class="form-group">        
                <div class="col-lg-12">
                    <label class="control-label" for="text">Text</label>
                </div>
                <div class="col-lg-12">
                    <textarea id="text" name="text" rows="10" cols="60" class="form-control"><c:out value="${entry.text}"/></textarea>
                </div>
            </div>
            <div class="form-group">        
                <div class="col-lg-12 text-center">
                    <c:choose>
                        <c:when test="${entry.id ne null}">
                            <input type="hidden" name="id" value="<c:out value="${entry.id}"/>" />
                            <input type="submit" name="edit" value="Edit Blog" class="btn btn-default" />                            
                        </c:when>
                        <c:otherwise>
                            <input type="submit" name="write" value="Write Blog" class="btn btn-default" />
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${entry.datePublished eq null}">
                        <input type="submit" name="publish" value="Publish" class="btn btn-success" />
                    </c:if>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<script type="text/javascript">
    tinymce.init({
        selector: '#text',
        plugins: 'code'
    });
</script>
<%@include file="../WEB-INF/foot.jsp" %>
