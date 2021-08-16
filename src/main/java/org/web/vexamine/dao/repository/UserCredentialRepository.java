package org.web.vexamine.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.UserCredentials;

/**
 * The Interface UserCredentialRepository.
 */
@Transactional
public interface UserCredentialRepository extends JpaRepository<UserCredentials, Long> {
	
	/**
	 * Exists by mail id in.
	 *
	 * @param mailList the mail list
	 * @return true, if successful
	 */
	public boolean existsByMailIdIn(List<String> mailList);

	/**
	 * Find by mail id.
	 *
	 * @param mailId the mail id
	 * @return the user credentials
	 */
	UserCredentials findByMailId(String mailId);
	
	List<UserCredentials> findByMailIdIn(List<String> mailIdList);
	
    /**
     * Gets the all mail id.
     *
     * @param mailIdList the mail id list
     * @return the all mail id
     */
    @Query(value= "SELECT mail_id from vexamine_user_credentials WHERE mail_id IN (?1)",nativeQuery = true)
	List<String> getAllMailId(List<String> mailIdList);

	/**
	 * Delete by mail id.
	 *
	 * @param mailId the mail id
	 * @return the long
	 */
	@Modifying
	Long deleteByMailId(String mailId);
	
}
