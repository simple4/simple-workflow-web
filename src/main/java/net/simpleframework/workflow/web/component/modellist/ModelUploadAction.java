package net.simpleframework.workflow.web.component.modellist;

import java.io.IOException;
import java.io.InputStreamReader;

import net.simpleframework.common.ID;
import net.simpleframework.mvc.AbstractUrlForward;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.component.ComponentHandleException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.submit.AbstractSubmitHandler;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;
import net.simpleframework.workflow.engine.IWorkflowContext;
import net.simpleframework.workflow.engine.WorkflowContextFactory;
import net.simpleframework.workflow.schema.ProcessDocument;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelUploadAction extends AbstractSubmitHandler {
	public static final IWorkflowContext context = WorkflowContextFactory.get();

	@Override
	public AbstractUrlForward submit(final ComponentParameter cParameter) {
		final IMultipartFile multipartFile = getMultipartFile(cParameter, "ml_upload");
		final ID loginId = ((IPagePermissionHandler) context.getParticipantMgr())
				.getLoginId(cParameter);
		try {
			final ProcessDocument document = new ProcessDocument(new InputStreamReader(
					multipartFile.getInputStream()));
			context.getModelMgr().addModel(loginId, document);
			return AbstractUrlForward
					.componentUrl(ModelListBean.class, "/jsp/model_upload_result.jsp");
		} catch (final IOException e) {
			throw ComponentHandleException.of(e);
		}
	}
}
