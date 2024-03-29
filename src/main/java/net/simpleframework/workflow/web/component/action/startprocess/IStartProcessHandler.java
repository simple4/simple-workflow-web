package net.simpleframework.workflow.web.component.action.startprocess;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;
import net.simpleframework.workflow.engine.InitiateItem;
import net.simpleframework.workflow.engine.ProcessBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IStartProcessHandler extends IComponentHandler {

	/**
	 * 创建流程之前的初始化逻辑。抛出的异常将被作为创建流程时的错误
	 * 
	 * @param compParameter
	 * @param initiateItem
	 */
	void doInit(ComponentParameter cParameter, InitiateItem initiateItem);

	/**
	 * 创建流程实例
	 * 
	 * @param compParameter
	 * @param initiateItem
	 * @return 返回
	 */
	String jsStartProcessCallback(ComponentParameter cParameter, ProcessBean process);
}
