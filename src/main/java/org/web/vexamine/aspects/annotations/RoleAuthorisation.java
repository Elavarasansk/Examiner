package org.web.vexamine.aspects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface RoleAuthorisation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RoleAuthorisation {
	
	/**
	 * Type.
	 *
	 * @return the role categories
	 */
	RoleCategories type();
	
	/**
	 * The Enum RoleCategories.
	 */
	public enum RoleCategories {
		CANDIDATE,
		MANAGER,
		SUPER_ADMIN
	}

}
