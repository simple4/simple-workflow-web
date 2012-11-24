package net.simpleframework.workflow.web.component.modellist;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.ProcessBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.engine.WorkitemBean;
import net.simpleframework.workflow.web.component.action.startprocess.DefaultStartProcessHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyStartProcessHandler extends DefaultStartProcessHandler {

	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public String jsStartProcessCallback(final ComponentParameter cParameter,
			final ProcessBean process) {
		final WorkitemBean workitem = context.getProcessMgr().getFirstWorkitem(process);
		if (workitem != null) {
			final StringBuilder sb = new StringBuilder();
			// sb.append("$Actions.loc('").append(AbstractMVCPage.uriFor(MyWorklistPage.class))
			// .append("');");
			cParameter.getSession().setAttribute(WorkitemBean.workitemId, workitem.getId());
			return sb.toString();
		} else {
			return null;
		}
	}
}
