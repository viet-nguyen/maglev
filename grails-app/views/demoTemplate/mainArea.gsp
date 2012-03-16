<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://magnolia-cms.com/taglib/templating-components/cms" %>

<g:if test="${content.header}">
    <h2>${content.header}</h2>
</g:if>
<div>
    <g:each in="${components}" var="component">
        <cms:component content="${component}"/>
    </g:each>
</div>


