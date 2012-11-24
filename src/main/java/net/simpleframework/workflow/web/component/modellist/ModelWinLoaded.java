package net.simpleframework.workflow.web.component.modellist;

import java.util.Map;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelWinLoaded extends DefaultPageHandler {
	public static final IWorkflowContext context = WorkflowContextFactory.get();

	public void optLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final ProcessModelBean processModel = context.getModelMgr().getBean(
				pParameter.getParameter(ProcessModelBean.modelId));
		if (processModel != null) {
			dataBinding.put("model_status", processModel.getStatus().ordinal());
		}
	}
}
