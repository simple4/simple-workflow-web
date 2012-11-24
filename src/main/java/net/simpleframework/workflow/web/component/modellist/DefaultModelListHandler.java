package net.simpleframework.workflow.web.component.modellist;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.workflow.engine.EProcessModelStatus;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.engine.participant.IParticipantModel;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultModelListHandler extends AbstractDbTablePagerHandler implements
		IModelListHandler {
	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
		return context.getModelMgr().getModelList();
	}

	@Override
	public String jsProcessListAction(final ProcessModelBean processModel) {
		return null;
	}

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultDbTablePagerSchema() {

			@Override
			public KVMap getRowData(final ComponentParameter cParameter, final Object dataObject) {
				final ProcessModelBean processModel = (ProcessModelBean) dataObject;
				final KVMap rowData = new KVMap();
				rowData.add("title",
						new LinkElement(processModel).setOnclick(jsProcessListAction(processModel)));
				final IParticipantModel participantMgr = context.getParticipantMgr();
				rowData.add("userText", participantMgr.getUser(processModel.getUserId()));
				rowData.add("createDate", processModel.getCreateDate());
				final EProcessModelStatus status = processModel.getStatus();
				rowData.add("status", ModelListUtils.getStatusIcon(cParameter, status) + status);
				rowData.add("action", AbstractTablePagerSchema.ACTIONc);
				return rowData;
			}
		};
	}
}
