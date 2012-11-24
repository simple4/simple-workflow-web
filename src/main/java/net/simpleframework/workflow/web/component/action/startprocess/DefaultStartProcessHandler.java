package net.simpleframework.workflow.web.component.action.startprocess;

import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.workflow.engine.InitiateItem;
import net.simpleframework.workflow.engine.ProcessBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultStartProcessHandler extends AbstractComponentHandler implements
		IStartProcessHandler {
	@Override
	public void doInit(final ComponentParameter cParameter, final InitiateItem initiateItem) {
	}

	@Override
	public String jsStartProcessCallback(final ComponentParameter cParameter,
			final ProcessBean process) {
		return null;
	}
}
