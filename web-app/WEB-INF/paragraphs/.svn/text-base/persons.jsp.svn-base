<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="cms-taglib" prefix="cms" %>
<%@ taglib uri="blossom-taglib" prefix="blossom" %>
<cms:editBar />

<h1>${content.title}</h1>


    <table width="100%">
        <tr>
            <th align="left">Id</th>
            <th align="right">Name</th>
            <th align="right">Age</th>
        </tr>
        <c:forEach items="${persons}" var="person">
            <tr>
                <td>${person.id}</td>
                <td align="right">${person.name}</td>
                <td align="right">${person.age}</td>
            </tr>
        </c:forEach>
    </table>
