package net.simpleframework.workflow.web.component.modellist;

import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.ado.ColumnData;
import net.simpleframework.common.ado.EOrder;
import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.ado.query.ListDataObjectQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerHandler;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumnMap;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.InitiateItem;
import net.simpleframework.workflow.engine.InitiateItems;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.schema.ProcessNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyModelListHandler extends AbstractTablePagerHandler implements IMyModelListHandler {

	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
		final ID loginId = ((IPagePermissionHandler) context.getParticipantMgr())
				.getLoginId(cParameter);
		InitiateItems items;
		if (loginId == null || (items = context.getModelMgr().getInitiateItems(loginId)) == null) {
			return DataQueryUtils.nullQuery();
		}
		return new ListDataObjectQuery<InitiateItem>(items);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected int doSort(final ColumnData dbColumn, final Object o1, final Object o2) {
		final InitiateItem item1 = (InitiateItem) o1;
		final InitiateItem item2 = (InitiateItem) o2;
		final String col = dbColumn.getColumnName();
		Comparable c1 = null, c2 = null;
		if ("createDate".equals(col)) {
			c1 = item1.model().getCreateDate();
			c2 = item2.model().getCreateDate();
		}
		if (c1 != null && c2 != null) {
			return dbColumn.getOrder() == EOrder.desc ? c1.compareTo(c2) : c2.compareTo(c1);
		} else {
			return 0;
		}
	}

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultTablePagerSchema() {
			@Override
			public Map<String, Object> getRowData(final ComponentParameter cParameter,
					final Object dataObject) {
				final InitiateItem item = (InitiateItem) dataObject;
				final ProcessModelBean processModel = item.model();
				final ProcessNode processNode = context.getModelMgr().getProcessDocument(processModel)
						.getProcessNode();
				final KVMap rowData = new KVMap();
				rowData.add("title",
						new LinkElement(processNode).setOnclick(jsStartProcessAction(processModel)));
				rowData.add("userText", context.getParticipantMgr().getUser(processModel.getUserId()));
				rowData.add("createDate", processModel.getCreateDate());
				return rowData;
			}

			@Override
			public TablePagerColumnMap getTablePagerColumns(final ComponentParameter cParameter) {
				final TablePagerColumnMap columns = super.getTablePagerColumns(cParameter);
				final TablePagerColumnMap columns2 = new TablePagerColumnMap();
				columns2.add("title", columns.get("title"));
				columns2.add("userText", columns.get("userText"));
				columns2.add("createDate", columns.get("createDate"));
				return columns2;
			}
		};
	}

	@Override
	public String jsStartProcessAction(final ProcessModelBean processModel) {
		return "$Actions['ml_start_process']('" + ProcessModelBean.modelId + "="
				+ processModel.getId() + "');";
	}
}
