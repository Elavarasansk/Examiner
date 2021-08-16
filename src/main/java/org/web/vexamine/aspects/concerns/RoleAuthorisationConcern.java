package org.web.vexamine.aspects.concerns;

import javax.security.sasl.AuthenticationException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.web.vexamine.aspects.annotations.RoleAuthorisation;
import org.web.vexamine.dao.entity.UserAuthorityInfo;
import org.web.vexamine.dao.entity.UserCredentials;
import org.web.vexamine.dao.repository.UserAuthorityInfoRepository;
import org.web.vexamine.dao.repository.UserCredentialRepository;
import org.web.vexamine.utils.VexamineConstants;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class RoleAuthorisationConcern.
 * @author vairavanu
 */
@Aspect
@Component("RoleAuthorisationConcern")
public class RoleAuthorisationConcern {

	@Autowired
	private UserCredentialRepository userCredentialRepo;

	@Autowired
	private UserAuthorityInfoRepository userAuthorityInfoRepo;

	/**
	 * On user login.
	 *
	 * @param joinPoint the join point
	 * @param roleAuthorisation the role authorisation
	 * @throws Throwable the throwable
	 */
	@Before(value = "@annotation(roleAuthorisation)")
	public void onUserLogin(JoinPoint joinPoint, RoleAuthorisation roleAuthorisation) throws Throwable {
		String sessionUser = getSessionUser();

		UserCredentials userCredentials = userCredentialRepo.findByMailId(sessionUser);
		
		if(ObjectUtils.isEmpty(userCredentials)) {
			throw new AuthenticationException("User Doesnot exists. Please Login");
		}
		
		
		UserAuthorityInfo userAuthorityInfo= userAuthorityInfoRepo.findByUserCredentialsId(userCredentials.getId());
		String userRole = userAuthorityInfo.getUserRole().getType();

		if(roleAuthorisation.type().equals(RoleAuthorisation.RoleCategories.SUPER_ADMIN)) {
			if(!userRole.equals(RoleAuthorisation.RoleCategories.SUPER_ADMIN)) {
				throw new AuthenticationException("Trying to Perform super-admin level operations with invalid previleges");
			}

		} else if (roleAuthorisation.type().equals(RoleAuthorisation.RoleCategories.MANAGER)) {
			if(!userRole.equals(RoleAuthorisation.RoleCategories.MANAGER)) {
				throw new AuthenticationException("Trying to Perform manager level operations with invalid previleges");
			}

		} else if (roleAuthorisation.type().equals(RoleAuthorisation.RoleCategories.CANDIDATE)) {
			if(!userRole.equals(RoleAuthorisation.RoleCategories.CANDIDATE)) {
				throw new AuthenticationException("Trying to Perform candidate operations with invalid previleges");
			}
		}
	}


	/**
	 * Gets the session user.
	 *
	 * @return the session user
	 */
	private String getSessionUser() {
		String sessionUser = VexamineConstants.SYSTEM_USER;
		if(!ObjectUtils.isEmpty(CookieSessionStorage.get())) {
			sessionUser = CookieSessionStorage.get().getUserName();
			if(StringUtils.isEmpty(sessionUser)) {
				sessionUser = VexamineConstants.SYSTEM_USER;
			}
		}
		return sessionUser;
	}



}
