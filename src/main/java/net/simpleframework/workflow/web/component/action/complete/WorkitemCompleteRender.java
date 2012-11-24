package net.simpleframework.workflow.web.component.action.complete;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.workflow.engine.WorkitemBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkitemCompleteRender extends ComponentJavascriptRender {

	public WorkitemCompleteRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cParameter) {
		final String workitemId = cParameter.getParameter(WorkitemBean.workitemId);
		final StringBuilder sb = new StringBuilder();
		sb.append("var dc = function() { $Loading.hide(); };");
		sb.append("$Loading.show();");
		final StringBuilder params = new StringBuilder();
		params.append(WorkitemCompleteUtils.BEAN_ID).append("=").append(cParameter.hashId())
				.append("&").append(WorkitemBean.workitemId).append("=").append(workitemId);
		sb.append("var params=\"").append(params).append("\";");
		ComponentRenderUtils.appendParameters(sb, cParameter, "params");
		sb.append("params = params.addParameter(arguments[0]);");
		sb.append("new Ajax.Request('")
				.append(ComponentUtils.getResourceHomePath(WorkitemCompleteBean.class))
				.append("/jsp/workitem_complete.jsp', {");
		sb.append("postBody: params,");
		sb.append("onComplete: function(req) {");
		sb.append("try {");
		sb.append("var json = req.responseText.evalJSON();");
		sb.append("var err = json['exception']; if (err) { $error(err); return; }");
		sb.append("var rt = json['responseText'];");
		sb.append("if (rt) { new $UI.AjaxRequest(null, rt, '")
				.append(cParameter.getBeanProperty("name")).append("', false); }");
		sb.append("if (json['transitionManual']) { (function() { $Actions['transitionManualWindow']('");
		sb.append(params).append("'); }).defer(); }");
		sb.append("else if (json['participantManual']) { (function() { $Actions['participantManualWindow']('");
		sb.append(params).append("'); }).defer(); }");

		final IWorkitemCompleteHandler hdl = (IWorkitemCompleteHandler) cParameter
				.getComponentHandler();
		final String jsCallback = hdl.jsCompleteCallback(cParameter);
		if (StringUtils.hasText(jsCallback)) {
			sb.append("else {").append(jsCallback).append("}");
		}
		sb.append("} finally { dc(); }");
		sb.append("}, onException: dc, onFailure: dc");
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cParameter, sb.toString());
	}
}
