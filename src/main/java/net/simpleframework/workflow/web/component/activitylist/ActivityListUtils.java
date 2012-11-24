package net.simpleframework.workflow.web.component.activitylist;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.workflow.engine.EActivityStatus;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.engine.WorkitemBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ActivityListUtils {

	static String getStatusIcon(final PageParameter pParameter, final EActivityStatus status) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<img style='height: 9px; width: 9px; margin-right: 5px;' src='");
		sb.append(ComponentUtils.getCssResourceHomePath(pParameter, ActivityListBean.class));
		sb.append("/images/dot_").append(status.name()).append(".png' />");
		return sb.toString();
	}

	public static String getWorkitemDetail(final PageRequestResponse rRequest) {
		final StringBuilder sb = new StringBuilder();
		final WorkitemBean workitem = WorkflowContextFactory.get().getWorkitemMgr()
				.getBean(rRequest.getParameter(WorkitemBean.workitemId));
		sb.append(workitem);
		return sb.toString();
	}
}
