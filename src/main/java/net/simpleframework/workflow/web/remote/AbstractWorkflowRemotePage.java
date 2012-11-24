package net.simpleframework.workflow.web.remote;

import java.util.Properties;

import net.simpleframework.common.ThrowableUtils;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.mvc.IForwardCallback.IJsonForwardCallback;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.template.AbstractTemplatePage;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.WorkflowContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractWorkflowRemotePage extends AbstractTemplatePage {
	public static IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);
	}

	@Override
	public String getRole(final PageParameter pParameter) {
		return IPermissionHandler.sj_all_account;
	}

	protected String parameter(final PageParameter pParameter, final String key) {
		return HttpUtils.toLocaleString(pParameter.getParameter(key), "utf-8");
	}

	protected void copyTo(final PageParameter pParameter, final Properties properties,
			final String... keys) {
		if (keys == null) {
			return;
		}
		for (final String key : keys) {
			properties.setProperty(key, parameter(pParameter, key));
		}
	}

	@Override
	public JsonForward doJsonForward(final IJsonForwardCallback callback) {
		JsonForward json;
		try {
			json = super.doJsonForward(callback);
		} catch (final Throwable e) {
			json = new JsonForward();
			json.put("error", ThrowableUtils.getThrowableMessage(e));
		}
		return json;
	}
}
