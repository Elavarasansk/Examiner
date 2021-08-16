package org.web.vexamine.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.UserAuthorityInfo;

/**
 * The Interface UserAuthorityInfoRepository.
 */
@Transactional
public interface UserAuthorityInfoRepository extends JpaRepository<UserAuthorityInfo, Long> {
	
	/**
	 * Find by user credentials id.
	 *
	 * @param userId the user id
	 * @return the user authority info
	 */
	UserAuthorityInfo findByUserCredentialsId(long userId);
	
	/**
	 * Find by user credentials id in.
	 *
	 * @param userIdList the user id list
	 * @return the list
	 */
	List<UserAuthorityInfo> findByUserCredentialsIdIn(List<Long> userIdList);
	
	/**
	 * Find by user credentials mail id.
	 *
	 * @param mailId the mail id
	 * @return the user authority info
	 */
	UserAuthorityInfo findByUserCredentialsMailId(String mailId);

	/**
	 * Delete by user credentials mail id.
	 *
	 * @param mailId the mail id
	 * @return the long
	 */
	@Modifying
	Long deleteByUserCredentialsMailId(String mailId);

	/**
	 * Find by user credentials user name containing ignore case and user role type.
	 *
	 * @param userName the user name
	 * @param role the role
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<UserAuthorityInfo> findByUserCredentialsUserNameContainingIgnoreCaseAndUserRoleType(String userName,String role, Pageable pageable);

	/**
	 * Find by user credentials mail id containing ignore case and user role type.
	 *
	 * @param userName the user name
	 * @param role the role
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<UserDetails> findByUserCredentialsMailIdContainingIgnoreCaseAndUserRoleType(String userName,String role, Pageable pageable);

	/**
	 * Find by user credentials user name in and user role type.
	 *
	 * @param userIdList the user id list
	 * @param role the role
	 * @return the optional
	 */
	public Optional<List<UserAuthorityInfo>> findByUserCredentialsUserNameInAndUserRoleType(List<String> userIdList,String role);

	/**
	 * Find by user credentials mail id in and user role type.
	 *
	 * @param mailIdList the mail id list
	 * @param role the role
	 * @return the optional
	 */
	public Optional<List<UserAuthorityInfo>> findByUserCredentialsMailIdInAndUserRoleType(List<String> mailIdList,String role);

	/**
	 * Gets the count by user role type.
	 *
	 * @param role the role
	 * @return the count by user role type
	 */
	public Long getCountByUserRoleType(String role);

	/**
	 * The Interface UserDetails.
	 */
	interface UserDetails{
		UserCredentials getUserCredentials();
		interface UserCredentials {
			String getMailId();
		}

	}
}
