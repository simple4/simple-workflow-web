package net.simpleframework.workflow.web.page;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.common.html.element.SpanElement;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.struct.TabButton;
import net.simpleframework.mvc.template.struct.TabButtons;
import net.simpleframework.mvc.template.t1.ResizedLCTemplatePage;
import net.simpleframework.workflow.engine.EWorkitemStatus;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.web.component.worklist.DefaultWorklistHandler;
import net.simpleframework.workflow.web.component.worklist.WorklistUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyWorklistPage extends ResizedLCTemplatePage {
	public static IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);
		addImportCSS(getCssResourceHomePath(pParameter) + "/my_worklist.css");
	}

	@Override
	protected String toHtml(final PageParameter pParameter,
			final Class<? extends AbstractMVCPage> pageClass, final Map<String, Object> variables,
			final String variable) throws IOException {
		if ("content_left".equals(variable)) {
			return "<div id='idMyWorklistTree' style='padding: 6px;'></div>";
		} else if ("content_center".equals(variable)) {
			return "<div class='MyWorklistPage_Center' style='padding: 6px;'><div id='idMyWorklistPage'></div></div>";
		}
		return null;
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		return super.getNavigationBar(pParameter).append(new LinkElement("#(MyWorklistPage.0)"));
	}

	@Override
	protected TabButtons getTabButtons(final PageParameter pParameter) {
		return TabButtons.of(
				new TabButton("#(MyWorklistPage.0)", AbstractMVCPage.uriFor(MyWorklistPage.class)),
				new TabButton("#(MyWorklistPage.1)", AbstractMVCPage.uriFor(InitiateItemPage.class)));
	}

	public static class MyWorklist extends DefaultWorklistHandler {

		@Override
		public String getTitle(final ComponentParameter cParameter) {
			final StringBuilder sb = new StringBuilder();
			final EWorkitemStatus status = WorklistUtils.getWorkitemStatus(cParameter);
			if (status != null) {
				sb.append(new LinkElement($m("MyWorklistPage.0"))
						.setOnclick("$Actions['myWorklist']('status=false');"));
			} else {
				sb.append($m("MyWorklistPage.0"));
			}
			if (status != null) {
				sb.append(SpanElement.NAV).append(status);
			}
			return sb.toString();
		}
	}
}
