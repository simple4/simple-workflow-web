package net.simpleframework.workflow.web.component.activitylist;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.workflow.engine.ActivityBean;
import net.simpleframework.workflow.engine.EActivityStatus;
import net.simpleframework.workflow.engine.EWorkitemStatus;
import net.simpleframework.workflow.engine.IActivityManager;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.ProcessBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.engine.WorkitemBean;
import net.simpleframework.workflow.engine.participant.IParticipantModel;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultActivityListHandler extends AbstractDbTablePagerHandler implements
		IActivityListHandler {

	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		return ((KVMap) super.getFormParameters(cParameter)).add(ProcessBean.processId,
				cParameter.getParameter(ProcessBean.processId));
	}

	@Override
	public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
		if ("pageItems".equals(beanProperty)) {
			return Convert.toInt(cParameter.getRequestAttr("pageItems"), Integer.MAX_VALUE);
		}
		return super.getBeanProperty(cParameter, beanProperty);
	}

	@Override
	public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
		final ProcessBean processBean = context.getProcessMgr().getBean(
				cParameter.getParameter(ProcessBean.processId));
		final IDataQuery<?> qs = context.getActivityMgr().getActivities(processBean);
		cParameter.setRequestAttr("pageItems", qs.getCount());
		return qs;
	}

	private String getParticipants(final ActivityBean activity, final EWorkitemStatus status) {
		final StringBuilder sb = new StringBuilder();
		final IDataQuery<WorkitemBean> qs = context.getWorkitemMgr().getWorkitemList(activity);
		final IParticipantModel participantMgr = context.getParticipantMgr();
		WorkitemBean item;
		int i = 0;
		while ((item = qs.next()) != null) {
			if (status != null && status != item.getStatus()) {
				continue;
			}
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("<span class='participants2' params='").append(WorkitemBean.workitemId)
					.append("=").append(item.getId()).append("'>")
					.append(participantMgr.getUser(item.getUserId())).append("</span>");
			i++;
		}
		return sb.toString();
	}

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultDbTablePagerSchema() {
			@Override
			public Map<String, Object> getRowData(final ComponentParameter cParameter,
					final Object dataObject) {
				final ActivityBean activity = (ActivityBean) dataObject;
				final KVMap rowData = new KVMap();
				final IActivityManager activityMgr = context.getActivityMgr();
				rowData.add("tasknode", activityMgr.taskNode(activity));
				final ActivityBean pre = activityMgr.getPreActivity(activity);
				if (pre != null) {
					rowData.add("previous", activityMgr.taskNode(pre));
				}
				rowData.add("participants", getParticipants(activity, null));
				rowData.add("participants2", getParticipants(activity, EWorkitemStatus.complete));
				rowData.add("createDate", activity.getCreateDate());
				rowData.add("completeDate", activity.getCompleteDate());

				final EActivityStatus status = activity.getStatus();
				rowData.add("status", ActivityListUtils.getStatusIcon(cParameter, status) + status);

				rowData.add("action", AbstractTablePagerSchema.ACTIONc);
				return rowData;
			}
		};
	}
}
