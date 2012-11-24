package net.simpleframework.workflow.web.component.action.startprocess;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestUtils;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;
import net.simpleframework.workflow.engine.IProcessModelManager;
import net.simpleframework.workflow.engine.InitiateItem;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class StartProcessUtils {

	public static final String BEAN_ID = "startprocess_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static InitiateItem getInitiateItem(final PageRequestResponse rRequest) {
		final IProcessModelManager modelMgr = WorkflowContextFactory.get().getModelMgr();
		ProcessModelBean processModel = null;
		final String modelId = rRequest.getParameter(ProcessModelBean.modelId);
		if (StringUtils.hasText(modelId)) {
			processModel = modelMgr.getBean(modelId);
		}
		if (processModel == null) {
			processModel = modelMgr.getProcessModelByName(rRequest.getParameter("modelName"));
		}
		return processModel != null ? modelMgr.getInitiateItems(
				((IPagePermissionHandler) WorkflowContextFactory.get().getParticipantMgr())
						.getLoginId(rRequest)).get(processModel) : null;
	}

	public static void doStartProcess(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		final ComponentParameter cParameter = get(request, response);
		final KVMap kv = new KVMap();
		final InitiateItem initiateItem = getInitiateItem(cParameter);
		if (initiateItem == null) {
			kv.add("exception", $m("StartProcessUtils.0"));
		} else {
			try {
				((IStartProcessHandler) cParameter.getComponentHandler()).doInit(cParameter,
						initiateItem);
				initiateItem.doTransitions();

				final boolean transitionManual = initiateItem.isTransitionManual();
				final boolean initiateRoles = initiateItem.getInitiateRoles().size() > 1;
				if (transitionManual || initiateRoles) {
					kv.add(
							"responseText",
							UrlForward.getResponseText(cParameter,
									ComponentUtils.getResourceHomePath(StartProcessBean.class)
											+ "/jsp/start_process_route.jsp"));
					kv.add("transitionManual", transitionManual);
					kv.add("initiateRoles", initiateRoles);
				} else {
					final String confirmMessage = (String) cParameter.getBeanProperty("confirmMessage");
					if (StringUtils.hasText(confirmMessage)) {
						kv.add(
								"responseText",
								UrlForward.getResponseText(cParameter,
										ComponentUtils.getResourceHomePath(StartProcessBean.class)
												+ "/jsp/start_process_route2.jsp"));
						kv.add("confirmMessage", confirmMessage);
					} else {
						final String jsCallback = jsStartProcessCallback(cParameter, initiateItem);
						if (StringUtils.hasText(jsCallback)) {
							kv.add("jsCallback", jsCallback);
						}
					}
				}
			} catch (final Throwable th) {
				kv.add("exception", AjaxRequestUtils.createException(th));
			}
		}
		final Writer out = cParameter.getResponseWriter();
		out.write(kv.toJSON());
		out.flush();
	}

	static String jsStartProcessCallback(final ComponentParameter nComponentParameter,
			final InitiateItem initiateItem) {
		final IStartProcessHandler hdl = (IStartProcessHandler) nComponentParameter
				.getComponentHandler();
		return hdl.jsStartProcessCallback(nComponentParameter, WorkflowContextFactory.get()
				.getProcessMgr().startProcess(initiateItem));
	}
}
