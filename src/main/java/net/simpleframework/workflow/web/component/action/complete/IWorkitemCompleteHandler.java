package net.simpleframework.workflow.web.component.action.complete;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;
import net.simpleframework.workflow.engine.WorkitemComplete;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IWorkitemCompleteHandler extends IComponentHandler {

	/**
	 * 工作项完成的触发动作
	 * 
	 * @param compParameter
	 * @param workitemComplete
	 */
	void complete(ComponentParameter cParameter, WorkitemComplete workitemComplete);

	String jsCompleteCallback(ComponentParameter cParameter);
}