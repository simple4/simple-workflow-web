package net.simpleframework.workflow.web.component.action.startprocess;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.workflow.engine.InitiateItem;
import net.simpleframework.workflow.engine.ProcessModelBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class StartProcessAction extends DefaultAjaxRequestHandler {

	public IForward doStartProcess(final ComponentParameter cParameter) {
		final ComponentParameter nComponentParameter = StartProcessUtils.get(cParameter);
		final InitiateItem initiateItem = StartProcessUtils.getInitiateItem(nComponentParameter);
		final String initiator = nComponentParameter.getParameter("initiator");
		if (StringUtils.hasText(initiator)) {
			final ID selected = ID.Gen.id(initiator);
			initiateItem.setSelectedRoleId(selected);
		}
		return new JavascriptForward(StartProcessUtils.jsStartProcessCallback(nComponentParameter,
				initiateItem));
	}

	public IForward doTransitionSave(final ComponentParameter cParameter) {
		final InitiateItem initiateItem = StartProcessUtils.getInitiateItem(cParameter);
		final String[] transitions = StringUtils.split(cParameter.getParameter("transitions"));
		initiateItem.resetTransitions(transitions);

		final ComponentParameter nComponentParameter = StartProcessUtils.get(cParameter);
		final JavascriptForward js = new JavascriptForward();
		if (initiateItem.getInitiateRoles().size() > 1) {
			js.append("$Actions['process_transition_manual_window'].close();");
			js.append("$Actions['initiator_select_window']('").append(StartProcessUtils.BEAN_ID)
					.append("=").append(nComponentParameter.hashId()).append("&")
					.append(ProcessModelBean.modelId).append("=")
					.append(nComponentParameter.getParameter(ProcessModelBean.modelId)).append("');");
		} else {
			js.append(StartProcessUtils.jsStartProcessCallback(nComponentParameter, initiateItem));
		}
		return js;
	}
}
