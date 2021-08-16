package org.web.vexamine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.entity.Role;
import org.web.vexamine.dao.vo.RoleVo;
import org.web.vexamine.service.RoleService;


/**
 * The Class RoleController.
 */
@RestController
@RequestMapping("/role/")
public class RoleController {

	@Autowired
	private RoleService roleService;

    /**
     * Gets the role list.
     *
     * @return the role list
     */
    @GetMapping
    public List<Role> getRoleList(){
        return roleService.getRoleList();   
    }
    
    /**
     * Adds the role.
     *
     * @param roleForm the role form
     * @return the role
     */
    @PostMapping
    public Role addRole(@RequestBody RoleVo roleForm){
        return roleService.saveRole(roleForm);   
    }
    
    /**
     * Delete role.
     *
     * @param roleId the role id
     */
    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable(value="roleId") long roleId) {
        roleService.removeRole(roleId);
    }
    
}
