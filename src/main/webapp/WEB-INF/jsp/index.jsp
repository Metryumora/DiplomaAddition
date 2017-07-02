<%--
  Created by IntelliJ IDEA.
  User: Metr_yumora
  Date: 12.06.2017
  Time: 15:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="/css/custom.css" rel="stylesheet">
    <title>Додатки до диплома про вищу освіту</title>
</head>
<body>
<h1>Додатки до диплома про вищу освіту</h1>
<div id="center">
    <div class="selector-container">
        <div class="description">Факультет:</div>
        <select class="selector" id="departments" onchange="fire_ajax_submit('groups')" title="Факультет">
        </select>
    </div>
    <div class="selector-container">
        <div class="description">Група:</div>
        <form class="form" method="post" action="/g">
            <select class="selector" name="id" id="groups" onchange="fire_ajax_submit('students')"
                    title="Група">
            </select>
            <input class="submit-button" id="submitGroup" type="submit" value="Згенерувати для групи">
        </form>
    </div>
    <div class="selector-container">
        <div class="description">Студент:</div>
        <form class="form" method="post" action="/s">
            <select class="selector" name="id" id="students" title="Студент"
                    onchange="$('#submitStudent').prop('disabled', false);">
            </select>
            <input class="submit-button" id="submitStudent" type="submit" value="Згенерувати для студента">
        </form>
    </div>

    <p id="error"></p>

    <p>${message}</p>

    <script type="text/javascript" src="/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="/js/main.js"></script>
</div>
</body>
</html>
