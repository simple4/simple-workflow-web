package net.simpleframework.workflow.web.component.action.complete;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkitemCompleteBean extends AbstractComponentBean {

	public WorkitemCompleteBean(final PageDocument pageDocument, final XmlElement element) {
		super(pageDocument, element);
	}

	public WorkitemCompleteBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	@Override
	public boolean isRunImmediately() {
		return false;
	}

	@Override
	public String getHandleClass() {
		return StringUtils.text(super.getHandleClass(),
				DefaultWorkitemCompleteHandler.class.getName());
	}
}
