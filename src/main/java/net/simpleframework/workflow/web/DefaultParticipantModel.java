package net.simpleframework.workflow.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.organization.IRole;
import net.simpleframework.organization.IRoleManager;
import net.simpleframework.organization.web.OrganizationPermissionHandler;
import net.simpleframework.workflow.engine.EDelegationSource;
import net.simpleframework.workflow.engine.ProcessModelBean;
import net.simpleframework.workflow.engine.participant.IParticipantModel;
import net.simpleframework.workflow.engine.participant.Participant;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultParticipantModel extends OrganizationPermissionHandler implements
		IParticipantModel {

	@Override
	public Collection<Participant> getRelativeParticipants(final Object user, final Object role,
			final String relative, final KVMap variables) {
		final ArrayList<Participant> participants = new ArrayList<Participant>();
		IRole oRole = getRoleObject(role);
		if (oRole != null) {
			// 获取相对角色，部门
			final IRoleManager roleMgr = context.getRoleMgr();
			oRole = roleMgr.getRoleByText(roleMgr.roleChart(oRole), relative);
			if (oRole != null) {
				final ID roleId = oRole.getId();
				for (final ID userId : users(roleId, variables)) {
					participants.add(new Participant(userId, roleId));
				}
			}
		}
		return participants;
	}

	@Override
	public Iterator<ID> getUsersOfDelegation(final ProcessModelBean processModel,
			final EDelegationSource source, final Map<String, String> filterMap) {
		return null;
	}
}
