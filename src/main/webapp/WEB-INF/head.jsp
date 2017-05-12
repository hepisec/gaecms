<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cms" uri="https://www.hepisec.de/taglib/cms" %>
<%@ taglib prefix="csrf" uri="http://www.owasp.org/index.php/Category:OWASP_CSRFGuard_Project/Owasp.CsrfGuard.tld" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>GAECMS - The CMS for Google App Engine</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="keywords" content="GAECMS, CMS, Blog, Google App Engine, Java" />
        <meta name="description" content="The CMS for Google App Engine" />
        <link rel="stylesheet" href="/css/simplex.css" />
        <link rel="stylesheet" href="/font-awesome/css/font-awesome.min.css" />
        <link rel="stylesheet" href="/css/hepisec.css" />
        <link rel="stylesheet" href="/css/gaecms.css" />
        <% if (request.isUserInRole("admin")) { %>
        <link rel="stylesheet" href="/css/codemirror.css">
        <% } %>
        <cms:ExternalScript script="/js/jquery-1.12.1.min.js" />
        <cms:ExternalScript script="/js/bootstrap.min.js" />
    </head>
    <body>
        <nav class="navbar navbar-default navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="/">GAECMS</a>
                    <button class="navbar-toggle" data-toggle="collapse" data-target=".navHeaderCollapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="collapse navbar-collapse navHeaderCollapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <% if (request.isUserInRole("admin")) {
                                UserService userService = UserServiceFactory.getUserService();
                        %>
                        <li><a href="/admin/Write">Write Blog</a></li>
                        <li><a href="/admin/MediaLibrary">Media Library</a></li>
                        <li><a href="<%= userService.createLogoutURL("/")%>">Logout</a></li>
                            <% }%>
                        <li><a href="/impressum.jsp">Impressum</a></li>
                    </ul>
                </div>
            </div>
        </nav>
