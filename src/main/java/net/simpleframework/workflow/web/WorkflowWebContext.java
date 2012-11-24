package net.simpleframework.workflow.web;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ctx.ModuleFunction;
import net.simpleframework.ctx.ModuleFunctions;
import net.simpleframework.mvc.ctx.WebModuleFunction;
import net.simpleframework.workflow.engine.impl.WorkflowContext;
import net.simpleframework.workflow.web.page.MyWorklistPage;
import net.simpleframework.workflow.web.page.ProcessModelPage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkflowWebContext extends WorkflowContext {

	@Override
	public ModuleFunctions getFunctions(final ModuleFunction parent) {
		if (parent == null) {
			return ModuleFunctions
					.of(new WebModuleFunction(MyWorklistPage.class).setName(
							"simple-workflow-MyWorklistPage").setText($m("WorkflowWebContext.0"))).append(
							new WebModuleFunction(ProcessModelPage.class).setName(
									"simple-workflow-ProcessModelPage").setText($m("WorkflowWebContext.1")));
		}
		return null;
	}
}
