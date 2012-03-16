<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib prefix="cms" uri="http://magnolia-cms.com/taglib/templating-components/cms" %>
<%@ taglib prefix="cmsfn" uri="http://magnolia-cms.com/taglib/templating-components/cmsfn" %>

<%@ taglib uri="blossom-taglib" prefix="blossom" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="sv" lang="sv">
<head>
    <title>${content?.title ?: "ingen titel"}</title>
    <cms:init/>
</head>

<body>

<div class="row-fluid">

    <h1>${content?.title ?: "ingen titel"}</h1>

    <div></div>

    <div id="mainArea">
        <cms:area name="mainArea"/>
    </div>

</body>
</html>

