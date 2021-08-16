package org.web.vexamine.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.dao.entity.Role;
import org.web.vexamine.dao.repository.RoleRepository;
import org.web.vexamine.dao.vo.RoleVo;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class RoleService.
 */
@Service
public class RoleService {

	/** The role repo. */
	@Autowired
	private RoleRepository roleRepo;

	/**
	 * Gets the role list.
	 *
	 * @return the role list
	 */
	public List<Role> getRoleList(){
		return roleRepo.findAll();   
	}

	/**
	 * Save role.
	 *
	 * @param roleForm the role form
	 * @return the role
	 */
	public Role saveRole(RoleVo roleForm) {
		String sessionUser = null;
		if(!ObjectUtils.isEmpty(CookieSessionStorage.get())) {
			sessionUser = CookieSessionStorage.get().getUserName();
		}

		Role roleBuilder = Role.builder()
				.createUser(sessionUser)
				.updateUser(sessionUser)
				.build();
		BeanUtils.copyProperties(roleForm, roleBuilder);
		return roleRepo.save(roleBuilder);
	}

	/**
	 * Removes the role.
	 *
	 * @param roleId the role id
	 */
	public void removeRole(long roleId){
		roleRepo.deleteById(roleId);   
	}

}
