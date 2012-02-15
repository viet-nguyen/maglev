<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><g:layoutTitle default="Maglev"/></title>
    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Le styles -->
    <link href="${resource(dir: 'css', file: 'bootstrap.css')}" rel="stylesheet">
    <style>
    body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
    }
    </style>
    <link href="${resource(dir: 'css', file: 'bootstrap-responsive.css')}" rel="stylesheet">
    <link href="${resource(dir: 'js', file: 'google-code-prettify/prettify.css')}" rel="stylesheet">

    <g:layoutHead/>

</head>

<body>

<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <g:link class="brand" uri="/">Maglev</g:link>
            <div class="nav-collapse">
                <ul class="nav">

                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <g:layoutBody/>
    <hr>
    <footer><p>&copy; Bonheur 2012</p></footer>
</div>

<script src="${resource(dir: 'js', file: 'jquery.js')}"></script>
<script src="${resource(dir: 'js', file: 'google-code-prettify/prettify.js')}"></script>
<script src="${resource(dir: 'js', file: 'bootstrap.js')}"></script>

<g:javascript library="application"/>
</body>
</html>