package net.simpleframework.workflow.web;

import java.awt.Dimension;
import java.util.Map;

import net.simpleframework.common.ObjectUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.template.AbstractTemplatePage;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.IWorkflowForm;
import net.simpleframework.workflow.engine.ProcessBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.engine.WorkitemBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractWorkflowFormPage extends AbstractTemplatePage implements
		IWorkflowForm {
	public static IWorkflowContext context = WorkflowContextFactory.get();

	private String url;

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		getPageBean().setHandleClass(getClass().getName()).setHandleMethod("doPageLoad");
	}

	public void doPageLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final WorkitemBean workitem = getWorkitem(getWorkitemId(pParameter));
		if (!workitem.isReadMark()) {
			context.getWorkitemMgr().readMark(workitem, false);
		}
		onLoad(pParameter, dataBinding, selector, workitem);
	}

	protected abstract void onLoad(final PageParameter pParameter,
			final Map<String, Object> dataBinding, final PageSelector selector,
			final WorkitemBean workitem);

	@Override
	public String getFormForward() {
		if (url == null) {
			url = AbstractMVCPage.uriFor(getClass());
		}
		return url;
	}

	@Override
	public void bindVariables(final KVMap variables) {
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public Dimension getSize() {
		return null;
	}

	protected void updateProcessTitle(final WorkitemBean workitem, final String title) {
		final ProcessBean process = getProcess(workitem);
		if (!ObjectUtils.objectEquals(title, process.getTitle())) {
			context.getProcessMgr().setProcessTitle(process, title);
		}
	}

	protected ProcessBean getProcess(final WorkitemBean workitem) {
		return context.getActivityMgr()
				.getProcessBean(context.getWorkitemMgr().getActivity(workitem));
	}

	protected Object getWorkitemId(final PageRequestResponse rRequest) {
		return rRequest.getParameter(WorkitemBean.workitemId);
	}

	protected WorkitemBean getWorkitem(final Object id) {
		return context.getWorkitemMgr().getBean(id);
	}
}