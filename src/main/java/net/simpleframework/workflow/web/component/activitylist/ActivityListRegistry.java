package net.simpleframework.workflow.web.component.activitylist;

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
@ComponentName(ActivityListRegistry.activitylist)
@ComponentBean(ActivityListBean.class)
@ComponentResourceProvider(ActivityListResourceProvider.class)
public class ActivityListRegistry extends TablePagerRegistry {
	public static final String activitylist = "wf_activitylist";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pParameter,
			final Object data) {
		final ActivityListBean activitylist = (ActivityListBean) super.createComponentBean(
				pParameter, data);
		return activitylist;
	}
}
