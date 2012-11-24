package net.simpleframework.workflow.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.WorkitemBean;
import net.simpleframework.workflow.engine.WorkitemComplete;
import net.simpleframework.workflow.web.AbstractWorkflowFormPage;
import net.simpleframework.workflow.web.component.action.complete.WorkitemCompleteBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class MyWorklistForm extends AbstractWorkflowFormPage {

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);
		addHtmlViewVariable(getClass(), "worklist_form");

		addComponentBean(pParameter, "wf_completeAction", WorkitemCompleteBean.class).setSelector(
				"#idWorklistForm");
		addAjaxRequest(pParameter, "wf_saveAction").setHandleMethod("doSaveAction").setSelector(
				"#idWorklistForm");
	}

	@Override
	public void onComplete(final Map<String, String> parameters,
			final WorkitemComplete workitemComplete) {
		onSave(parameters, workitemComplete.getWorkitem());
	}

	protected void onSave(final Map<String, String> parameters, final WorkitemBean workitem) {
	}

	public IForward doSaveAction(final ComponentParameter cParameter) {
		onSave(HttpUtils.map(cParameter.request), getWorkitem(getWorkitemId(cParameter)));
		return new JavascriptForward("$Actions['myWorklist'].refresh(); alert('").append(
				$m("MyWorklistForm.0")).append("');");
	}

	public String buildForm(final PageParameter pParameter) {
		return buildInputHidden(pParameter, WorkitemBean.workitemId);
	}
}
