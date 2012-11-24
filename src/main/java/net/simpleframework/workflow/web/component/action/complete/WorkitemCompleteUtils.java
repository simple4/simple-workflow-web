package net.simpleframework.workflow.web.component.action.complete;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestUtils;
import net.simpleframework.workflow.engine.ActivityComplete;
import net.simpleframework.workflow.engine.IWorkflowForm;
import net.simpleframework.workflow.engine.TransitionUtils;
import net.simpleframework.workflow.engine.WorkitemBean;
import net.simpleframework.workflow.engine.WorkitemComplete;
import net.simpleframework.workflow.schema.TransitionNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkitemCompleteUtils {

	public static final String BEAN_ID = "workitemcomplete_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static void doWorkitemComplete(final ComponentParameter cParameter,
			final WorkitemBean workitem) throws IOException {
		final KVMap kv = new KVMap();
		try {
			final WorkitemComplete workitemComplete = WorkitemComplete.get(workitem);

			// 绑定变量
			final IWorkflowForm workflowForm = (IWorkflowForm) workitemComplete.getWorkflowForm();
			if (workflowForm != null) {
				workflowForm.bindVariables(workitemComplete.getVariables());
			}

			final IWorkitemCompleteHandler hdl = (IWorkitemCompleteHandler) cParameter
					.getComponentHandler();
			if (!workitemComplete.isAllCompleted()) {
				hdl.complete(cParameter, workitemComplete);
			} else {
				// 是否有手动情况
				final ActivityComplete activityComplete = workitemComplete.getActivityComplete();
				final boolean transitionManual = activityComplete.isTransitionManual();
				final boolean participantManual = activityComplete.isParticipantManual();
				if (transitionManual || participantManual) {
					kv.add(
							"responseText",
							UrlForward.getResponseText(cParameter,
									ComponentUtils.getResourceHomePath(WorkitemCompleteBean.class)
											+ "/jsp/workitem_complete_route.jsp"));
					kv.add("transitionManual", transitionManual);
					kv.add("participantManual", participantManual);
				} else {
					hdl.complete(cParameter, workitemComplete);
				}
			}
		} catch (final Throwable ex) {
			kv.add("exception", AjaxRequestUtils.createException(ex));
		}
		final Writer out = cParameter.getResponseWriter();
		out.write(kv.toJSON());
		out.flush();
	}

	public static Collection<TransitionNode> getTransitions(final PageRequestResponse rRequest,
			final WorkitemBean workitem) {
		final ArrayList<TransitionNode> al = new ArrayList<TransitionNode>();
		final String[] transitions = StringUtils.split(rRequest.getParameter("transitions"));
		final ActivityComplete activityComplete = WorkitemComplete.get(workitem)
				.getActivityComplete();
		// 通过手动方式选取的路由
		if (transitions != null && transitions.length > 0) {
			for (final String id : transitions) {
				final TransitionNode transition = activityComplete.getTransitionById(id);
				if (TransitionUtils.isTransitionManual(transition)) {
					al.add(transition);
				}
			}
		} else {
			al.addAll(activityComplete.getTransitions());
		}
		return al;
	}
}
