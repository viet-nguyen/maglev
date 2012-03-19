<%@ taglib uri="cms-taglib" prefix="cms" %>
<div>
    <h2>Persons</h2>

    <div>
        <form action="?" method="post">
            <table>
                <tr>
                    <th align="right">Add A new Person here</th>
                </tr>
                <tr>
                    <th align="right">Name</th>
                    <th align="right">Age</th>
                </tr>
                <tr>
                    <td align="right"><g:textField name="name"/></td>
                    <td align="right"><g:textField name="age"/></td>
                </tr>
                <tr>
                    <td align="right"><input type="submit"/></td>
                </tr>

            </table>
        </form>
    </div>

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