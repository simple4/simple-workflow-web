package net.simpleframework.workflow.web.component.processlist;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.workflow.engine.EProcessStatus;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.ProcessBean;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultProcessListHandler extends AbstractDbTablePagerHandler implements
		IProcessListHandler {

	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final ProcessModelBean processModel = context.getModelMgr().getBean(
					cParameter.getParameter(ProcessModelBean.modelId));
			if (processModel != null) {
				final StringBuilder sb = new StringBuilder();
				sb.append(processModel);
				wrapNavImage(cParameter, sb);
				return sb.toString();
			}
		}
		return super.getBeanProperty(cParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		return ((KVMap) super.getFormParameters(cParameter)).add(ProcessModelBean.modelId,
				cParameter.getParameter(ProcessModelBean.modelId));
	}

	@Override
	public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
		return context.getProcessMgr().getProcessList(
				context.getModelMgr().getBean(cParameter.getParameter(ProcessModelBean.modelId)));
	}

	@Override
	public String jsActivityListAction(final ProcessBean processBean) {
		return "$Actions['activitylist_window']('" + ProcessBean.processId + "="
				+ processBean.getId() + "');";
	}

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultDbTablePagerSchema() {
			@Override
			public Map<String, Object> getRowData(final ComponentParameter cParameter,
					final Object dataObject) {
				final ProcessBean processBean = (ProcessBean) dataObject;
				final KVMap rowData = new KVMap();
				rowData.add(
						"title",
						new LinkElement(StringUtils.text(processBean.getTitle(),
								$m("DefaultWorklistHandle.0")))
								.setOnclick(jsActivityListAction(processBean)));
				rowData.add("userText", context.getParticipantMgr().getUser(processBean.getUserId()));

				rowData.add("createDate", processBean.getCreateDate());
				rowData.add("completeDate", processBean.getCompleteDate());

				final EProcessStatus status = processBean.getStatus();
				rowData.add("status", ProcessListUtils.getStatusIcon(cParameter, status) + status);

				rowData.add("action", AbstractTablePagerSchema.ACTIONc);
				return rowData;
			}
		};
	}
}
