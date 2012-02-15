<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="cms-taglib" prefix="cms" %>
<%@ taglib uri="blossom-taglib" prefix="blossom" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="sv" lang="sv">
<head>
    <meta content="main" name="layout">
    <title>a title</title>
    <cms:links/>
</head>

<body>

<cms:mainBar adminButtonVisible="true" dialog="front-page-dialog" label="Properties"/>

<div class="row-fluid">

    <h1>Grails Magnolia Demo</h1>

    <div id="main">
        <cms:contentNodeIterator contentNodeCollectionName="main">
            <cms:includeTemplate/>
        </cms:contentNodeIterator>
        <cms:newBar contentNodeCollectionName="main" paragraph="text, persons, pageLink,petFormParagraph"/>
    </div>

</html>

