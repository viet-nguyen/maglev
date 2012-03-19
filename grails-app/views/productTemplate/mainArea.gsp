<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="cms" uri="http://magnolia-cms.com/taglib/templating-components/cms" %>
<%@ taglib prefix="cmsfn" uri="http://magnolia-cms.com/taglib/templating-components/cmsfn" %>

<div class="row">
    <g:each in="${components}" var="component">
        <cms:component content="${component}"/>
    </g:each>
</div>