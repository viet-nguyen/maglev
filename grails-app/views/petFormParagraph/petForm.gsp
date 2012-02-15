<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="cms-taglib" prefix="cms" %>
<div class="span6">
    <cms:editBar/>
    <h3>The pet</h3>

    <div>
        Name: ${pet.name}
    </div>

    <div>
        <form action="" method="post">

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

    <g:each in="${pets}">${it.name}</g:each>
</div>