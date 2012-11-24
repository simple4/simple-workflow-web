package net.simpleframework.workflow.web.component.worklist;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.coll.ParameterMap;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumnMap;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;
import net.simpleframework.workflow.engine.ActivityBean;
import net.simpleframework.workflow.engine.EWorkitemStatus;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.IWorkitemManager;
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
public class DefaultWorklistHandler extends AbstractDbTablePagerHandler implements IWorklistHandler {

	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public Object getBeanProperty(final ComponentParameter cParameter, final String beanProperty) {
		String title;
		if ("title".equals(beanProperty) && (title = getTitle(cParameter)) != null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(title);
			wrapNavImage(cParameter, sb);
			return sb.toString();
		}
		return super.getBeanProperty(cParameter, beanProperty);
	}

	@Override
	public String getTitle(final ComponentParameter cParameter) {
		return null;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		return ((KVMap) super.getFormParameters(cParameter)).add(WorklistUtils.STATUS,
				cParameter.getParameter(WorklistUtils.STATUS));
	}

	@Override
	public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
		final EWorkitemStatus status = WorklistUtils.getWorkitemStatus(cParameter);
		final ID userId = ((IPagePermissionHandler) context.getParticipantMgr())
				.getLoginId(cParameter);
		final IWorkitemManager workitemMgr = context.getWorkitemMgr();
		if (status != null) {
			return workitemMgr.getWorkitemList(userId, status);
		} else {
			return workitemMgr.getWorkitemList(userId);
		}
	}

	@Override
	public String jsWorkflowFormAction(final WorkitemBean workitemBean) {
		final StringBuilder sb = new StringBuilder();
		sb.append("$Actions['workflowFormWindow']('").append(WorkitemBean.workitemId);
		sb.append("=").append(workitemBean.getId()).append("');");
		return sb.toString();
	}

	private final ParameterMap usersMap = new ParameterMap();

	private String getUserTo(final ActivityBean activity) {
		final String key = "to_" + activity.getId();
		String userTo = usersMap.get(key);
		if (userTo == null) {
			final StringBuilder sb = new StringBuilder();
			final IWorkitemManager workitemMgr = context.getWorkitemMgr();
			final IParticipantModel participantMgr = context.getParticipantMgr();
			final IDataQuery<ActivityBean> qs = context.getActivityMgr().getNextActivities(activity);
			ActivityBean activity2;
			int i = 0;
			while ((activity2 = qs.next()) != null) {
				WorkitemBean workitem;
				final IDataQuery<WorkitemBean> qs2 = workitemMgr.getWorkitemList(activity2);
				while ((workitem = qs2.next()) != null) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(participantMgr.getUser(workitem.getUserId()));
					i++;
				}
			}
			if (sb.length() > 0) {
				usersMap.put(key, userTo = sb.toString());
			}
		}
		return userTo;
	}

	private String getUserFrom(ActivityBean activity) {
		activity = context.getActivityMgr().getPreActivity(activity);
		if (activity == null) {
			return null;
		}
		final String key = "from_" + activity.getId();
		String userFrom = usersMap.get(key);
		if (userFrom == null) {
			final StringBuilder sb = new StringBuilder();
			final IDataQuery<WorkitemBean> qs = context.getWorkitemMgr().getWorkitemList(activity,
					EWorkitemStatus.complete);
			final IParticipantModel participantMgr = context.getParticipantMgr();
			WorkitemBean workitem;
			int i = 0;
			while ((workitem = qs.next()) != null) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(participantMgr.getUser(workitem.getUserId()));
				i++;
			}
			if (sb.length() > 0) {
				usersMap.put(key, userFrom = sb.toString());
			}
		}
		return userFrom;
	}

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultDbTablePagerSchema() {
			@Override
			public Map<String, Object> getRowData(final ComponentParameter cParameter,
					final Object dataObject) {
				final WorkitemBean workitemBean = (WorkitemBean) dataObject;
				final ActivityBean activity = context.getWorkitemMgr().getActivity(workitemBean);
				final KVMap rowData = new KVMap();
				try {
					String title = StringUtils.text(context.getActivityMgr().getProcessBean(activity)
							.getTitle(), $m("DefaultWorklistHandle.0"));
					if (!context.getWorkitemMgr().isFinalStatus(workitemBean)) {
						title = new LinkElement(title).setOnclick(jsWorkflowFormAction(workitemBean))
								.toString();
					}
					if (EWorkitemStatus.running == workitemBean.getStatus()
							&& !workitemBean.isReadMark()) {
						title = "<strong>" + title + "</strong>";
						rowData.add(
								"icon",
								"<img style='vertical-align: middle;' src='"
										+ ComponentUtils.getCssResourceHomePath(cParameter)
										+ "/images/unread.png'>");
					}
					rowData.add("title", title);
					rowData.add("activity", context.getActivityMgr().taskNode(activity));
					final String userFrom = getUserFrom(activity);
					if (userFrom != null) {
						rowData.add("userFrom", userFrom);
					}
					final String userTo = getUserTo(activity);
					if (userTo != null) {
						rowData.add("userTo", userTo);
					}
					rowData.add("createDate", workitemBean.getCreateDate());
					rowData.add("completeDate", workitemBean.getCompleteDate());
					rowData.add("action", AbstractTablePagerSchema.ACTIONc);
				} catch (final Exception e) { // 此处装载工作列表，需要catch掉
					log.warn(e);
				}
				return rowData;
			}

			@Override
			public TablePagerColumnMap getTablePagerColumns(final ComponentParameter cParameter) {
				final TablePagerColumnMap columns = new TablePagerColumnMap().addAll(super
						.getTablePagerColumns(cParameter));
				final EWorkitemStatus status = WorklistUtils.getWorkitemStatus(cParameter);
				if (EWorkitemStatus.running == status) {
					columns.remove("userTo");
					columns.remove("completeDate");
				}
				return columns;
			}
		};
	}

	@Override
	public MenuItems getContextMenu(final ComponentParameter cParameter, final MenuBean menuBean,
			final MenuItem menuItem) {
		final EWorkitemStatus status = WorklistUtils.getWorkitemStatus(cParameter);
		final MenuItems items = MenuItems.of();
		if (status == EWorkitemStatus.complete) {
			items.add(MenuItem.of(menuBean, $m("DefaultWorklistHandle.1"), null).setJsSelectCallback(
					"$pager_action(item).retake();"));
		} else if (status == EWorkitemStatus.running) {
			items.add(MenuItem.of(menuBean, $m("DefaultWorklistHandle.3"), null).setJsSelectCallback(
					"$pager_action(item).readMark();"));
			items.add(new MenuItem(menuBean).setTitle("-"));
			items.add(MenuItem.of(menuBean, $m("DefaultWorklistHandle.4"), null).setJsSelectCallback(
					"$pager_action(item).fallback();"));
			items.add(MenuItem.sep(menuBean));
			items.add(MenuItem.of(menuBean, $m("DefaultWorklistHandle.2"), null).setJsSelectCallback(
					"$pager_action(item).delegate();"));
		}
		return items;
	}
}