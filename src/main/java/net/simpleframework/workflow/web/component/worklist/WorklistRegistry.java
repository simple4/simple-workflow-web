package net.simpleframework.workflow.web.component.worklist;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ui.pager.TablePagerRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(WorklistRegistry.worklist)
@ComponentBean(WorklistBean.class)
@ComponentResourceProvider(WorklistResourceProvider.class)
public class WorklistRegistry extends TablePagerRegistry {
	public static final String worklist = "wf_worklist";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pParameter,
			final Object component) {
		final WorklistBean worklist = (WorklistBean) super.createComponentBean(pParameter, component);
		return worklist;
	}
}
