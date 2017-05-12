<%@page import="com.googlecode.objectify.Ref"%>
<%@include file="../WEB-INF/head.jsp" %>
<div class="container">
    <form class="form-horizontal antivirus-form col-lg-6 col-lg-offset-3" method="post" action="/admin/Delete">
        <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue uri="/admin/Delete"/>" />            
        <fieldset>
            <legend>Do you really want to delete?</legend>
            <h3><c:out value="${entry.title}"/></h3>
            <div class="blog-entry">
                <c:out value="${entry.text}"/>
            </div>
            <div class="form-group">        
                <div class="col-lg-12 text-center">
                    <input type="hidden" name="id" value="<c:out value="${entry.id}"/>" />
                    <input type="submit" name="submit" value="Delete Blog" class="btn btn-danger" />
                </div>
            </div>
        </fieldset>
    </form>
</div>
<%@include file="../WEB-INF/foot.jsp" %>
