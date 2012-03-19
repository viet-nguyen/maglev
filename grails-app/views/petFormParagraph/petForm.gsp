<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="cms" uri="http://magnolia-cms.com/taglib/templating-components/cms" %>
<%@ taglib prefix="cmsfn" uri="http://magnolia-cms.com/taglib/templating-components/cmsfn" %>

<%@ taglib uri="blossom-taglib" prefix="blossom" %>

<div class="span6">
    <h3>The pet</h3>


    <div>
        <form action="?" method="get">
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