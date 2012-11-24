package net.simpleframework.workflow.web.component.modellist;

import net.simpleframework.mvc.component.ui.pager.ITablePagerHandler;
import net.simpleframework.workflow.engine.ProcessModelBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IMyModelListHandler extends ITablePagerHandler {

	String jsStartProcessAction(ProcessModelBean processModel);
}
