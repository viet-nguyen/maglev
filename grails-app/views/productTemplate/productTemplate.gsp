<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="cms" uri="http://magnolia-cms.com/taglib/templating-components/cms" %>
<%@ taglib prefix="cmsfn" uri="http://magnolia-cms.com/taglib/templating-components/cmsfn" %>
<html>
<head>
    <meta content="main" name="layout">
    <title>Product - ${name}</title>
    <cms:init/>
</head>

<body>
<div class="page-header">
    <h1>${name}</h1>
</div>
<cms:area name="mainArea"/>
</body>
</html>