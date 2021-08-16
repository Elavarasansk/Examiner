package org.web.vexamine.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.Role;

/**
 * The Interface RoleRepository.
 */
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	/**
	 * Find by type.
	 *
	 * @param roleName the role name
	 * @return the role
	 */
	Role findByType(String roleName);

}
