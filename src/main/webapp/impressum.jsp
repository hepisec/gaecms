<%@include file="WEB-INF/head.jsp" %>
<div class="container intro-header">
    <h1>Impressum</h1>
</div>
<div class="container about">
    <div class="row">
        <div class="col-lg-6 col-lg-offset-3">
            <cms:EditableContent id="impressum" mode="inline" />
        </div>
    </div>
</div>
<a name="datenschutz"></a>
<div class="container intro-header">
    <h1>Datenschutzerkl&auml;rung</h1>
</div>
<div class="container about">
    <div class="row">
        <div class="col-lg-6 col-lg-offset-3">
            <cms:EditableContent id="datenschutz" mode="markdown" />
        </div>
    </div>
</div>
<div class="container intro-header">
    <h1>HTML-Text</h1>
</div>
<div class="container about">
    <div class="row">
        <div class="col-lg-6 col-lg-offset-3">
            <cms:EditableContent id="html-text" mode="html" />
        </div>
    </div>
</div>
<div class="container intro-header">
    <h1>Plain-Text</h1>
</div>
<div class="container about">
    <div class="row">
        <div class="col-lg-6 col-lg-offset-3">
            <cms:EditableContent id="plain-text" mode="plain" />
        </div>
    </div>
</div>
<%@include file="WEB-INF/foot.jsp" %>