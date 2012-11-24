package net.simpleframework.workflow.web.component;

import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.pager.PagerUtils;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.WorkflowContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractListAction extends DefaultAjaxRequestHandler {
	public static final IWorkflowContext context = WorkflowContextFactory.get();

	protected JavascriptForward jsRefreshAction(final ComponentParameter cParameter,
			final JavascriptForward js) {
		js.append("$Actions['").append(PagerUtils.get(cParameter).getBeanProperty("name"))
				.append("'].refresh();");
		return js;
	}
}
