package net.simpleframework.workflow.web.component.activitylist;

import net.simpleframework.common.Convert;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.ActivityBean;
import net.simpleframework.workflow.engine.EActivityAbortPolicy;
import net.simpleframework.workflow.engine.EActivityStatus;
import net.simpleframework.workflow.engine.IActivityManager;
import net.simpleframework.workflow.web.component.AbstractListAction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ActivityListAction extends AbstractListAction {

	public IForward doSuspend(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final IActivityManager activityMgr = context.getActivityMgr();
		final ActivityBean activity = activityMgr.getBean(cParameter
				.getParameter(ActivityBean.activityId));
		activityMgr.suspend(activity, activity.getStatus() == EActivityStatus.suspended);
		jsRefreshAction(cParameter, js);
		return js;
	}

	public IForward doAbort(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final IActivityManager activityMgr = context.getActivityMgr();
		final ActivityBean activity = activityMgr.getBean(cParameter
				.getParameter(ActivityBean.activityId));
		activityMgr.abort(
				activity,
				Convert.toEnum(EActivityAbortPolicy.class,
						cParameter.getParameter("activity_abort_policy")));
		js.append("$Actions['activity_abort_window'].close();");
		jsRefreshAction(cParameter, js);
		return js;
	}
}
