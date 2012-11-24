package net.simpleframework.workflow.web.component.processlist;

import net.simpleframework.common.Convert;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.EProcessAbortPolicy;
import net.simpleframework.workflow.engine.EProcessStatus;
import net.simpleframework.workflow.engine.IProcessManager;
import net.simpleframework.workflow.engine.ProcessBean;
import net.simpleframework.workflow.web.component.AbstractListAction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProcessListAction extends AbstractListAction {

	public IForward doSuspend(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final IProcessManager processMgr = context.getProcessMgr();
		final ProcessBean process = processMgr
				.getBean(cParameter.getParameter(ProcessBean.processId));
		processMgr.suspend(process, process.getStatus() == EProcessStatus.suspended);
		jsRefreshAction(cParameter, js);
		return js;
	}

	public IForward doDelete(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		context.getProcessMgr().delete(cParameter.getParameter(ProcessBean.processId));
		jsRefreshAction(cParameter, js);
		return js;
	}

	public IForward doAbort(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final IProcessManager processMgr = context.getProcessMgr();
		final ProcessBean process = processMgr
				.getBean(cParameter.getParameter(ProcessBean.processId));
		processMgr.abort(
				process,
				Convert.toEnum(EProcessAbortPolicy.class,
						cParameter.getParameter("process_abort_policy")));
		js.append("$Actions['process_list_abort_window'].close();");
		jsRefreshAction(cParameter, js);
		return js;
	}
}
