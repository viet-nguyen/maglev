<%@ taglib uri="cms-taglib" prefix="cms" %>
<div class="span8">
    <h2>Persons</h2>
    <cms:editBar/>
    <p>
        Test cms:out [<cms:out nodeDataName="testString"/>]
    </p>

    <p>
        Test contentMap [${contentMap.testString}]
    </p>

    <p>
        Test cms:out to a var, <cms:out nodeDataName="testString" var="newVar"/> [${newVar}]
    </p>

    <table width="100%">
        <tr>
            <th align="left">Id</th>
            <th align="right">Name</th>
            <th align="right">Age</th>
        </tr>
        <g:each in="${persons}" var="person">
            <tr>
                <td>${person.id}</td>
                <td align="right">${person.name}</td>
                <td align="right">${person.age}</td>
            </tr>
        </g:each>
    </table>
</div>