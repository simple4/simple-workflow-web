package net.simpleframework.workflow.web.component.worklist;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.ITablePagerHandler;
import net.simpleframework.workflow.engine.WorkitemBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IWorklistHandler extends ITablePagerHandler {

	/**
	 * 定义我的工作列表的标题
	 * 
	 * @param compParameter
	 * @return
	 */
	String getTitle(ComponentParameter cParameter);

	/**
	 * 定义打开表单的js
	 * 
	 * @param workitemBean
	 * @return
	 */
	String jsWorkflowFormAction(WorkitemBean workitemBean);
}
