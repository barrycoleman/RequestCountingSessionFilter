<%--
  Created by IntelliJ IDEA.
  User: barry
  Date: 4/24/12
  Time: 12:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
Session id: <%= session.getId() %>
<br/>
Session: <%= session.toString() %>
<br/>
Session Request Count: <%= request.getAttribute("requestcounting_page") %>
<br/>
Session Simultaneous Count: <%= request.getAttribute("requestcounting_session") %>
<br/>
Thread name: <%= Thread.currentThread().getName() %>
<% Thread.currentThread().sleep(5000); %>