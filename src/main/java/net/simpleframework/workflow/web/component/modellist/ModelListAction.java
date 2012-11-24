package net.simpleframework.workflow.web.component.modellist;

import net.simpleframework.common.Convert;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.EProcessModelStatus;
import net.simpleframework.workflow.engine.IProcessModelManager;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.web.component.AbstractListAction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelListAction extends AbstractListAction {

	public IForward deleteModel(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		context.getModelMgr().delete(cParameter.getParameter(ProcessModelBean.modelId));
		jsRefreshAction(cParameter, js);
		return js;
	}

	public IForward optSave(final ComponentParameter cParameter) {
		final JavascriptForward js = new JavascriptForward();
		final IProcessModelManager modelMgr = context.getModelMgr();
		final ProcessModelBean processModel = modelMgr.getBean(cParameter
				.getParameter(ProcessModelBean.modelId));
		modelMgr.setStatus(processModel,
				Convert.toEnum(EProcessModelStatus.class, cParameter.getParameter("model_status")));
		jsRefreshAction(cParameter, js);
		js.append("$Actions['ml_opt_window'].close();");
		return js;
	}
}
