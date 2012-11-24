package net.simpleframework.workflow.web.remote;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.IForwardCallback.IJsonForwardCallback;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;
import net.simpleframework.workflow.engine.IProcessModelManager;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.schema.ProcessDocument;
import net.simpleframework.workflow.schema.ProcessNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelerRemotePage extends AbstractWorkflowRemotePage {
	/**
	 * 设计器登录验证
	 * 
	 * @param pageParameter
	 * @return
	 */
	public IForward login(final PageParameter pParameter) {
		final String login = pParameter.getParameter("login");
		final String password = pParameter.getParameter("password");
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				((IPagePermissionHandler) context.getParticipantMgr()).login(pParameter, login,
						password, null);
				json.put("jsessionid", pParameter.getSession().getId());
			}
		});
	}

	@Override
	public String getRole(final PageParameter pParameter) {
		return "login".equals(getMethod(pParameter)) ? IPermissionHandler.sj_anonymous
				: IPermissionHandler.sj_all_account;
	}

	/**
	 * 获取所有模型
	 * 
	 * @param pageParameter
	 * @return
	 */
	public IForward models(final PageParameter pParameter) {
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				final ArrayList<Map<?, ?>> models = new ArrayList<Map<?, ?>>();
				ProcessModelBean pm;
				final IDataQuery<ProcessModelBean> query = context.getModelMgr().getModelList();
				while ((pm = query.next()) != null) {
					models.add(new KVMap().add("text", pm.toString()).add("id", pm.getId()).map());
				}
				json.put("models", models);
			}
		});
	}

	/**
	 * 新建模型
	 * 
	 * @param pageParameter
	 * @return
	 */
	public IForward newModel(final PageParameter pParameter) {
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				final IProcessModelManager pmm = context.getModelMgr();
				final ProcessDocument document = new ProcessDocument();
				final ProcessNode processNode = document.getProcessNode();
				processNode.setName(parameter(pParameter, "name"));
				processNode.setDescription(parameter(pParameter, "description"));
				final ProcessModelBean model = pmm.addModel(
						((IPagePermissionHandler) context.getParticipantMgr()).getLoginId(pParameter),
						document);
				json.put("id", model.getId().getValue());
				json.put("text", processNode.toString());
			}
		});
	}

	public IForward model(final PageParameter pParameter) {
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				final IProcessModelManager modelMgr = context.getModelMgr();
				final ProcessModelBean pm = modelMgr.getBean(pParameter.getParameter("id"));
				json.put("doc", modelMgr.getProcessDocument(pm).toString());
			}
		});
	}

	public IForward deleteModel(final PageParameter pParameter) {
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				context.getModelMgr().delete(pParameter.getParameter("id"));
			}
		});
	}

	public IForward saveModel(final PageParameter pParameter) {
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				final IProcessModelManager pmm = context.getModelMgr();
				final ProcessModelBean bean = pmm.getBean(pParameter.getParameter("id"));
				final String doc = parameter(pParameter, "doc");
				pmm.updateModel(bean,
						((IPagePermissionHandler) context.getParticipantMgr()).getLoginId(pParameter),
						doc.toCharArray());
				json.put("result", Boolean.TRUE);
			}
		});
	}
}