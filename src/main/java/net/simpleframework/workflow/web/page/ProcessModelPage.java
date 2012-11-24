package net.simpleframework.workflow.web.page;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.t1.ResizedTemplatePage;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.web.component.modellist.DefaultModelListHandler;
import net.simpleframework.workflow.web.component.modellist.ModelListBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ProcessModelPage extends ResizedTemplatePage {
	public static IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addImportCSS(getCssResourceHomePath(pParameter) + "/process_mgr.css");

		addComponentBean(pParameter, "processModelMgr", ModelListBean.class).setContainerId(
				"idProcessModelList").setHandleClass(Model.class);
	}

	@Override
	protected String toHtml(final PageParameter pParameter, final Map<String, Object> variables,
			final String variable) throws IOException {
		return "<div class='ProcessModelPage'><div id='idProcessModelList'></div></div>";
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		return super.getNavigationBar(pParameter).append(new LinkElement("#(ProcessModelPage.0)"));
	}

	public String jsProcessListAction(final ProcessModelBean processModel) {
		return "$Actions.loc('"
				+ AbstractMVCPage.uriFor(ProcessPage.class, ProcessModelBean.modelId + "="
						+ processModel.getId()) + "');";
	}

	public static class Model extends DefaultModelListHandler {

		@Override
		public String jsProcessListAction(final ProcessModelBean processModel) {
			return AbstractMVCPage.get(ProcessModelPage.class).jsProcessListAction(processModel);
		}
	}
}
