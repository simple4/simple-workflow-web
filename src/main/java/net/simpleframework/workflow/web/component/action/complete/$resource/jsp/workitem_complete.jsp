<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.workflow.engine.WorkitemBean"%>
<%@ page import="net.simpleframework.workflow.engine.WorkflowContextFactory"%>
<%@ page import="net.simpleframework.workflow.web.component.action.complete.WorkitemCompleteUtils"%>
<%
	WorkitemCompleteUtils.doWorkitemComplete(WorkitemCompleteUtils.get(
	request, response), WorkflowContextFactory.get().getWorkitemMgr()
	.getBean(request.getParameter(WorkitemBean.workitemId)));
%>