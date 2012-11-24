package net.simpleframework.workflow.web.page;

import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.struct.TabButtons;
import net.simpleframework.mvc.template.t1.ResizedTemplatePage;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.web.component.modellist.ModelListBean;
import net.simpleframework.workflow.web.component.modellist.MyModelListHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class InitiateItemPage extends ResizedTemplatePage {
	public static IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);
		addImportCSS(getCssResourceHomePath(pParameter) + "/my_worklist.css");

		addComponentBean(pParameter, "initiateItemMgr", ModelListBean.class).setContainerId(
				"idInitiateItemPage").setHandleClass(MyModelListHandler.class);
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		// uriFor(ModelerRemotePage.class)
		return super.getNavigationBar(pParameter).append(new LinkElement("#(MyWorklistPage.1)"));
	}

	@Override
	protected TabButtons getTabButtons(final PageParameter pParameter) {
		return get(MyWorklistPage.class).getTabButtons(pParameter);
	}
}