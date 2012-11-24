package net.simpleframework.workflow.web.component.action.complete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.workflow.engine.ActivityComplete;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.engine.WorkitemBean;
import net.simpleframework.workflow.engine.WorkitemComplete;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkitemCompleteAction extends DefaultAjaxRequestHandler {
	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = WorkitemCompleteUtils.get(cParameter);
			return nComponentParameter.getBeanProperty("selector");
		}
		return super.getBeanProperty(cParameter, beanProperty);
	}

	public IForward doTransitionSave(final ComponentParameter cParameter) {
		final WorkitemBean workitem = context.getWorkitemMgr().getBean(
				cParameter.getParameter(WorkitemBean.workitemId));
		final String transitions = cParameter.getParameter("transitions");
		final String[] transitionIds = StringUtils.split(transitions);
		final WorkitemComplete workitemComplete = WorkitemComplete.get(workitem);
		final ActivityComplete activityComplete = workitemComplete.getActivityComplete();
		final JavascriptForward js = new JavascriptForward();
		final ComponentParameter nComponentParameter = WorkitemCompleteUtils.get(cParameter);
		if (activityComplete.isParticipantManual(transitionIds)) {
			js.append("$Actions['participantManualWindow']('").append(WorkitemCompleteUtils.BEAN_ID)
					.append("=").append(nComponentParameter.hashId()).append("&")
					.append(WorkitemBean.workitemId).append("=").append(workitem.getId())
					.append("&transitions=").append(transitions).append("');");
		} else {
			activityComplete.resetTransitions(transitionIds);
			final IWorkitemCompleteHandler hdl = (IWorkitemCompleteHandler) nComponentParameter
					.getComponentHandler();
			hdl.complete(nComponentParameter, workitemComplete);

			js.append("$Actions['transitionManualWindow'].close();");
			final String jsCallback = hdl.jsCompleteCallback(nComponentParameter);
			if (StringUtils.hasText(jsCallback)) {
				js.append(jsCallback);
			}
		}
		return js;
	}

	public IForward doParticipantSave(final ComponentParameter cParameter) {
		final WorkitemBean workitem = context.getWorkitemMgr().getBean(
				cParameter.getParameter(WorkitemBean.workitemId));
		final WorkitemComplete workitemComplete = WorkitemComplete.get(workitem);
		final ActivityComplete activityComplete = workitemComplete.getActivityComplete();
		final Map<String, String[]> participantIds = new HashMap<String, String[]>();
		for (final Object o : JsonUtils.toObject(cParameter.getParameter("json"), List.class)) {
			final Map<?, ?> map = (Map<?, ?>) o;
			participantIds.put((String) map.get("transition"),
					StringUtils.split((String) map.get("participant")));
		}
		activityComplete.resetParticipants(participantIds);

		final ComponentParameter nComponentParameter = WorkitemCompleteUtils.get(cParameter);
		final IWorkitemCompleteHandler hdl = (IWorkitemCompleteHandler) nComponentParameter
				.getComponentHandler();
		hdl.complete(nComponentParameter, workitemComplete);

		final JavascriptForward js = new JavascriptForward();
		js.append("$Actions['participantManualWindow'].close();");
		js.append("$Actions['transitionManualWindow'].close();");
		final String jsCallback = hdl.jsCompleteCallback(nComponentParameter);
		if (StringUtils.hasText(jsCallback)) {
			js.append(jsCallback);
		}
		return js;
	}
}
