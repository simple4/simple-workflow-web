package net.simpleframework.workflow.web.component.worklist;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorklistBean extends TablePagerBean {

	public WorklistBean(final PageDocument pageDocument, final XmlElement element) {
		super(pageDocument, element);
		setShowLineNo(true);
	}

	public WorklistBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	@Override
	public String getJspPath() {
		return ComponentUtils.getResourceHomePath(WorklistBean.class) + "/jsp/worklist.jsp";
	}

	@Override
	public String getHandleClass() {
		return StringUtils.text(super.getHandleClass(), DefaultWorklistHandler.class.getName());
	}
}
