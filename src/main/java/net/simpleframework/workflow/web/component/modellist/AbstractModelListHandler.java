package net.simpleframework.workflow.web.component.modellist;

import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractModelListHandler extends AbstractDbTablePagerHandler {

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new ModelTablePagerSchema();
	}

	protected class ModelTablePagerSchema extends DefaultDbTablePagerSchema {
	}
}
