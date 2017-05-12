<%@include file="../WEB-INF/head.jsp" %>
<div class="container">
    <h1>Media Library</h1>
    <div class="row">
        <form class="form-horizontal antivirus-form col-lg-6 col-lg-offset-3" method="post" action="/admin/UploadMediaFile" enctype="multipart/form-data">
            <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue uri="/admin/UploadMediaFile"/>" />            
            <fieldset>
                <legend>Upload Media</legend>
                <p class="alert alert-danger">Attention: All uploaded files are world readable!</p>
                <div class="form-group">        
                    <div class="col-lg-12">
                        <label class="control-label" for="file">File</label>
                    </div>
                    <div class="col-lg-12">
                        <input type="file" id="file" name="file" class="form-control" />
                    </div>
                </div>   
                <div class="form-group">        
                    <div class="col-lg-12 text-center">
                        <input type="submit" name="submit" value="Upload" class="btn btn-danger" />
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
    <div class="row">
        <c:forEach var="mediaFile" items="${entries}">
            <div class="col-xs-12 col-sm-4 col-md-3">
                <cms:MediaFile blobid="${mediaFile.blobId}">${mediaFile.description}</cms:MediaFile>
                </div>
        </c:forEach>
    </div>
</div>
<%@include file="../WEB-INF/pager.jsp" %>
<%@include file="../WEB-INF/foot.jsp" %>