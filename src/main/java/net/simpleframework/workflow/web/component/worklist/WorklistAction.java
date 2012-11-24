package net.simpleframework.workflow.web.component.worklist;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.WorkitemBean;
import net.simpleframework.workflow.web.component.AbstractListAction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorklistAction extends AbstractListAction {

	public IForward doRetake(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		context.getWorkitemMgr().retake(WorklistUtils.getWorkitem(cParameter));
		jsRefreshAction(cParameter, js);
		return js;
	}

	public IForward doReadMark(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final WorkitemBean workitem = WorklistUtils.getWorkitem(cParameter);
		context.getWorkitemMgr().readMark(workitem, workitem.isReadMark() ? true : false);
		jsRefreshAction(cParameter, js);
		return js;
	}

	public IForward doFallback(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final WorkitemBean workitem = WorklistUtils.getWorkitem(cParameter);
		context.getActivityMgr().fallback(context.getWorkitemMgr().getActivity(workitem));
		jsRefreshAction(cParameter, js);
		return js;
	}
}
