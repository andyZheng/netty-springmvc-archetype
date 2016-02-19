<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@page import="com.apabi.shop.Constants"%>
<%@page import="com.apabi.shop.config.ShopConfig"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="projectType" value="<%=ShopConfig.getProjectType() %>"/>