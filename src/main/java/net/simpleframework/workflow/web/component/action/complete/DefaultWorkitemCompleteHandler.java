package net.simpleframework.workflow.web.component.action.complete;

import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.WorkitemComplete;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultWorkitemCompleteHandler extends AbstractComponentHandler implements
		IWorkitemCompleteHandler {

	@Override
	public void complete(final ComponentParameter cParameter, final WorkitemComplete workitemComplete) {
		workitemComplete.complete(HttpUtils.map(cParameter.request));
	}

	@Override
	public String jsCompleteCallback(final ComponentParameter cParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("$Actions['workflowFormWindow'].close();");
		sb.append("var wl = $Actions['myWorklist']; if (wl) wl.refresh();");
		return sb.toString();
	}
}
