<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="cms-taglib" prefix="cms" %>
<html>
<head>
    <meta content="main" name="layout">
    <title>Product - ${name}</title>
    <cms:links />
</head>
e<body>
<h1>${name}</h1>

<div id="main">
    <cms:contentNodeIterator contentNodeCollectionName="main">
        <cms:includeTemplate/>
    </cms:contentNodeIterator>
    <cms:newBar contentNodeCollectionName="main" paragraph="text, persons, pageLink"/>
</div>

</body>
</html>