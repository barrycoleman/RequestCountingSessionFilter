<%--
  Created by IntelliJ IDEA.
  User: barry
  Date: 4/24/12
  Time: 10:29 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
    <script type="text/javascript">
        var xhr1, xhr2, xhr3;


        function start() {
            reset();

            xhr1 = new XMLHttpRequest();
            xhr2 = new XMLHttpRequest();
            xhr3 = new XMLHttpRequest();
            xhr4 = new XMLHttpRequest();
            xhr5 = new XMLHttpRequest();

            xhr1.onreadystatechange=function() {
                if (xhr1.readyState == 4 && xhr1.status==200) {
                    document.getElementById("one").innerHTML=xhr1.responseText;
                }
                if (xhr1.readyState == 4 && xhr1.status==403) {
                    document.getElementById("one").innerHTML="Status 403";
                }
            };
            xhr2.onreadystatechange=function() {
                if (xhr2.readyState == 4 && xhr2.status==200) {
                    document.getElementById("two").innerHTML=xhr2.responseText;
                }
                if (xhr2.readyState == 4 && xhr2.status==403) {
                    document.getElementById("two").innerHTML="Status 403";
                }
            };
            xhr3.onreadystatechange=function() {
                if (xhr3.readyState == 4 && xhr3.status==200) {
                    document.getElementById("three").innerHTML=xhr3.responseText;
                }
                if (xhr3.readyState == 4 && xhr3.status==403) {
                    document.getElementById("three").innerHTML="Status 403";
                }
            };
            xhr4.onreadystatechange=function() {
                if (xhr4.readyState == 4 && xhr4.status==200) {
                    document.getElementById("four").innerHTML=xhr4.responseText;
                }
                if (xhr4.readyState == 4 && xhr4.status==403) {
                    document.getElementById("four").innerHTML="Status 403";
                }
            };
            xhr5.onreadystatechange=function() {
                if (xhr5.readyState == 4 && xhr5.status==200) {
                    document.getElementById("five").innerHTML=xhr5.responseText;
                }
                if (xhr5.readyState == 4 && xhr5.status==403) {
                    document.getElementById("five").innerHTML="Status 403";
                }
            };

            xhr1.open("GET","runner.jsp?t="+Math.random(), true);
            xhr2.open("GET","runner.jsp?t="+Math.random(), true);
            xhr3.open("GET","runner.jsp?t="+Math.random(), true);
            xhr4.open("GET","runner.jsp?t="+Math.random(), true);
            xhr5.open("GET","runner.jsp?t="+Math.random(), true);

            xhr1.send();
            xhr2.send();
            xhr3.send();
            xhr4.send();
            xhr5.send();

        }
        function reset() {
            var div1 = document.getElementById("one");
            var div2 = document.getElementById("two");
            var div3 = document.getElementById("three");
            var div4 = document.getElementById("four");
            var div5 = document.getElementById("five");
            div1.innerHTML="";
            div2.innerHTML="";
            div3.innerHTML="";
            div4.innerHTML="";
            div5.innerHTML="";
        }
    </script>
    <style type="text/css">
        div.request {
            border: solid 1px #666666;
        }
    </style>
  </head>
  <body>
    <h1>Calling example</h1>
    <a href="#" onclick="start()">Start</a>
  <br/>
  Request One:
    <div id="one" class="request"></div>
  Request Two:
    <div id="two" class="request"></div>
  Request Three:
    <div id="three" class="request"></div>
  Request Four:
    <div id="four" class="request"></div>
  Request Five:
    <div id="five" class="request"></div>
  </body>
</html>