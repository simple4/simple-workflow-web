package net.simpleframework.workflow.web.remote;

import java.util.Properties;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.IForwardCallback.IJsonForwardCallback;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.workflow.engine.ActivityBean;
import net.simpleframework.workflow.engine.IMappingVal;
import net.simpleframework.workflow.engine.IProcessManager;
import net.simpleframework.workflow.engine.ProcessBean;
import net.simpleframework.workflow.engine.remote.IProcessRemote;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SubProcessRemotePage extends AbstractWorkflowRemotePage {

	public IForward startProcess(final PageParameter pParameter) {

		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				final Properties properties = new Properties();
				copyTo(pParameter, properties, IProcessRemote.SERVERURL, IProcessRemote.SUB_ACTIVITYID,
						IProcessRemote.VAR_MAPPINGS);

				// 子流程变量
				final KVMap variables = new KVMap();
				final String[] mappings = StringUtils.split(properties
						.getProperty(IProcessRemote.VAR_MAPPINGS));
				if (mappings != null) {
					for (final String mapping : mappings) {
						variables.add(mapping, parameter(pParameter, mapping));
					}
				}

				final ProcessBean process = context.getProcessMgr().startProcess(
						context.getModelMgr()
								.getProcessModel(parameter(pParameter, IProcessRemote.MODEL)), variables,
						properties, null);
				json.put(IProcessRemote.SUB_PROCESSID, process.getId());
			}
		});
	}

	public IForward checkProcess(final PageParameter pParameter) {
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				final IProcessManager processMgr = context.getProcessMgr();
				final ProcessBean sProcess = processMgr.getBean(parameter(pParameter,
						IProcessRemote.SUB_PROCESSID));
				if (sProcess != null && processMgr.isFinalStatus(sProcess)) {
					processMgr.backToRemote(sProcess);
				}
				json.put("success", Boolean.TRUE);
			}
		});
	}

	public IForward subComplete(final PageParameter pParameter) {
		return doJsonForward(new IJsonForwardCallback() {
			@Override
			public void doAction(final JsonForward json) {
				final ActivityBean nActivity = context.getActivityMgr().getBean(
						parameter(pParameter, IProcessRemote.SUB_ACTIVITYID));
				context.getActivityMgr().subComplete(nActivity, new IMappingVal() {
					@Override
					public Object val(final String mapping) {
						return parameter(pParameter, mapping);
					}
				});
				json.put("success", Boolean.TRUE);
			}
		});
	}
}