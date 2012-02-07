<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="cms-taglib" prefix="cms" %>
<%@ taglib uri="blossom-taglib" prefix="blossom" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="sv" lang="sv">
<head>
	<title> hej </title>
	<style type="text/css">

	body {
		font-family: "Lucida Sans Unicode", "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;
		font-size: 13px;
		background-color: #DDDDDD;
	}

	a {
		color: #4040ff
	}

	a:visited {
		color: #4040ff;
	}

	#container {
		margin-left: 50px;
		width: 875px;
	}

	#logo {
		font-family: Georgia, 'Times New Roman', Times, serif;
		font-size: 46px;
		padding: 50px 0px 8px 10px;
		background-color: #ffffff;
	}

	#main {
		width: 625px;
		background-color: #ffffff;
	}

	</style>
</head>

<body>

<cms:mainBar adminButtonVisible="true" dialog="front-page-dialog" label="Properties"/>

<div id="container">

	<h1>Grails Magnolia Demo</h1>

	<div id="main">
		<cms:contentNodeIterator contentNodeCollectionName="main">
			<cms:includeTemplate/>
		</cms:contentNodeIterator>
		<cms:newBar contentNodeCollectionName="main" paragraph="text, persons, pageLink"/>
	</div>

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
</html>

