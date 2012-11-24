package net.simpleframework.workflow.web.component.modellist;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.PagerUtils;
import net.simpleframework.mvc.component.ui.pager.TablePagerLoaded;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.workflow.web.component.action.startprocess.StartProcessBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelListLoaded extends TablePagerLoaded {

	@Override
	public void beforeComponentRender(final PageParameter pParameter) {
		super.beforeComponentRender(pParameter);
		final ComponentParameter nComponentParameter = PagerUtils.get(pParameter);
		final IComponentHandler hdl = nComponentParameter.getComponentHandler();
		if (hdl instanceof DefaultModelListHandler) {
			// upload
			pParameter.addComponentBean("ml_upload_window", WindowBean.class)
					.setUrl("model_upload.jsp").setTitle($m("ModelLoaded.2")).setHeight(190);

			// opt window
			pParameter.addComponentBean("ajax_ml_opt", AjaxRequestBean.class).setUrlForward(
					"model_opt.jsp");
			pParameter.addComponentBean("ml_opt_window", WindowBean.class)
					.setContentRef("ajax_ml_opt").setTitle($m("model_list_menu.1")).setHeight(210);

			// delete action
			pParameter.addComponentBean("ml_delete_model", AjaxRequestBean.class)
					.setConfirmMessage($m("ModelLoaded.1")).setHandleMethod("deleteModel")
					.setHandleClass(ModelListAction.class);
		} else {
			pParameter.addComponentBean("ml_start_process", StartProcessBean.class)
					.setConfirmMessage($m("ModelLoaded.0")).setHandleClass(MyStartProcessHandler.class);
		}
	}
}
