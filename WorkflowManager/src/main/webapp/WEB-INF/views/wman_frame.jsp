<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <c:set var='appRoot' value='${ pageContext.request.contextPath }' />
    <jsp:include page='fragments/html_head.jsp' >
        <jsp:param name='appRoot' value='${ appRoot }' />
        <jsp:param name='title' value='WorkflowManager (R)' />
    </jsp:include>
</head>
<body>

    <div id='header-wrapper'>
        <jsp:include page='fragments/page_header.jsp' >
            <jsp:param name='appRoot' value='${ appRoot }' />
        </jsp:include>
    </div>
    
    <div id='content-wrapper'>
        <jsp:include page='${ pageName }.jsp' >
            <jsp:param name='appRoot' value='${ appRoot }' />
        </jsp:include>
    </div> 
    
    <div id='footer-wrapper' >
    </div>
    
</body>
</html>