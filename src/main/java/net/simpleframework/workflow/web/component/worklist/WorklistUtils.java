package net.simpleframework.workflow.web.component.worklist;

import static net.simpleframework.common.I18n.$m;

import java.awt.Dimension;

import javax.servlet.http.HttpSession;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.html.js.JavascriptUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.PagerUtils;
import net.simpleframework.workflow.engine.EWorkitemStatus;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.IWorkflowForm;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.engine.WorkitemBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class WorklistUtils {
	public static final String STATUS = "status";

	public static EWorkitemStatus getWorkitemStatus(final PageRequestResponse rRequest) {
		final String status = rRequest.getParameter(STATUS);
		if (!"false".equals(status)) {
			return StringUtils.hasText(status) ? EWorkitemStatus.valueOf(status)
					: EWorkitemStatus.running;
		} else {
			return null;
		}
	}

	public static WorkitemBean getWorkitem(final PageRequestResponse rRequest) {
		return context.getWorkitemMgr().getBean(rRequest.getParameter(WorkitemBean.workitemId));
	}

	public static String getFormResponseText(final PageRequestResponse rRequest) {
		final WorkitemBean workitem = getWorkitem(rRequest);
		final IWorkflowForm workflowForm = (IWorkflowForm) context.getActivityMgr().getWorkflowForm(
				context.getWorkitemMgr().getActivity(workitem));
		final StringBuilder sb = new StringBuilder();
		if (workflowForm != null) {
			sb.append("<script type='text/javascript'>");
			sb.append("(function() {");
			if (!workitem.isReadMark()) {
				sb.append("$Actions['").append(PagerUtils.get(rRequest).getBeanProperty("name"))
						.append("'].refresh();");
			}
			sb.append("var win = $Actions['workflowFormWindow'].window;");
			final String title = workflowForm.getTitle();
			if (StringUtils.hasText(title)) {
				sb.append("win.setHeader(\"").append(JavascriptUtils.escape(title)).append("\");");
			}
			final Dimension d = workflowForm.getSize();
			if (d != null) {
				sb.append("win.setSize(").append(d.width).append(", ").append(d.height)
						.append(").center();");
			}
			sb.append("})();");
			sb.append("</script>");
			final IForward forward = new UrlForward(workflowForm.getFormForward());
			if (forward != null) {
				sb.append(forward.getResponseText(rRequest));
			}
		} else {
			sb.append("<p style='text-align: center;' class='important-tip f2'>")
					.append($m("WorklistUtils.0")).append("</p>");
		}
		return sb.toString();
	}

	public static String jsWorkflowForm(final ComponentParameter cParameter) {
		final StringBuilder sb = new StringBuilder();
		final HttpSession httpSession = cParameter.getSession();
		final Object id = httpSession.getAttribute(WorkitemBean.workitemId);
		if (id != null) {
			httpSession.removeAttribute(WorkitemBean.workitemId);
			final WorkitemBean workitem = context.getWorkitemMgr().getBean(id);
			if (workitem != null) {
				final IWorklistHandler lHandle = (IWorklistHandler) cParameter.getComponentHandler();
				sb.append("$ready(function() {");
				sb.append(lHandle.jsWorkflowFormAction(workitem));
				sb.append("});");
			}
		}
		return sb.toString();
	}

	public static final IWorkflowContext context = WorkflowContextFactory.get();
}
