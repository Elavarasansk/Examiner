package org.web.vexamine.dao.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.ManagerInfo;

/**
 * The Interface ManagerInfoRepository.
 */
@Transactional
public interface ManagerInfoRepository extends JpaRepository<ManagerInfo, Long>, JpaSpecificationExecutor<ManagerInfo> {


	/**
	 * Count by user authority info user credentials mail id.
	 *
	 * @param mailId the mail id
	 * @return the long
	 */
	public Long countByUserAuthorityInfoUserCredentialsMailId(String mailId);

	/**
	 * Delete by user authority info user credentials mail id.
	 *
	 * @param mailId the mail id
	 */
	public void deleteByUserAuthorityInfoUserCredentialsMailId(String mailId);

	/**
	 * Find by user authority info user credentials mail id.
	 *
	 * @param mailId the mail id
	 * @return the list
	 */
	public List<ManagerDetails> findByUserAuthorityInfoUserCredentialsMailId(String mailId);

	/**
	 * Find by user authority info user credentials mail id and question bank question bank name containing ignore case.
	 *
	 * @param mailId the mail id
	 * @param questionBankName the question bank name
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<ManagerDetails> findByUserAuthorityInfoUserCredentialsMailIdAndQuestionBankQuestionBankNameContainingIgnoreCase(String mailId,String questionBankName, Pageable pageable);

	/**
	 * Find by question bank question bank name containing ignore case.
	 *
	 * @param questionBankName the question bank name
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<ManagerDetails> findByQuestionBankQuestionBankNameContainingIgnoreCase(String questionBankName,
			Pageable pageable);

	/**
	 * Find by id not in and user authority info user credentials mail id.
	 *
	 * @param idList the id list
	 * @param mailId the mail id
	 * @return the list
	 */
	public List<ManagerInfo> findByIdNotInAndUserAuthorityInfoUserCredentialsMailId(List<Long> idList ,String mailId);

	/**
	 * Find distinct by user authority info user credentials mail id in.
	 *
	 * @param mailIdList the mail id list
	 * @return the list
	 */
	public List<ManagerInfo> findDistinctByUserAuthorityInfoUserCredentialsMailIdIn(List<String> mailIdList);

	/**
	 * Find by user authority info user credentials mail id in.
	 *
	 * @param mailIdList the mail id list
	 * @return the list
	 */
	public List<ManagerDetails> findByUserAuthorityInfoUserCredentialsMailIdIn(List<String> mailIdList);

	/**
	 * Find by company containing ignore case and user authority info user role id and user authority info user credentials mail id containing ignore case.
	 *
	 * @param company the company
	 * @param id the id
	 * @param mailId the mail id
	 * @return the list
	 */
	public List<ManagerDetails> findByCompanyContainingIgnoreCaseAndUserAuthorityInfoUserRoleIdAndUserAuthorityInfoUserCredentialsMailIdContainingIgnoreCase
	(String company, Long id, String mailId);

	/**
	 * The Interface ManagerDetails.
	 */
	interface ManagerDetails{
		UserAuthorityInfo getUserAuthorityInfo();
		QuestionBank getQuestionBank();
		String getCompany();
		interface QuestionBank{
			String getQuestionBankName();
		}		
		interface UserAuthorityInfo{
			UserCredentials getUserCredentials();
			interface UserCredentials {
				String getMailId();
			}
		}
	}

}