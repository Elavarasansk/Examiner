package org.web.vexamine.dao.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.TestAssignment;


/**
 * The Interface TestAssignmentRepository.
 */
@Transactional
public interface TestAssignmentRepository extends JpaRepository<TestAssignment, Long> , JpaSpecificationExecutor<TestAssignment> {

	/**
	 * Find by status.
	 *
	 * @param status the status
	 * @return the list
	 */
	public List<TestAssignment> findByStatus(String status);

	/**
	 * Exists by user authority info id in and status in.
	 *
	 * @param userAuthId the user auth id
	 * @param statusList the status list
	 * @return true, if successful
	 */
	public boolean existsByUserAuthorityInfoIdInAndStatusIn(List<Long> userAuthId,List<String> statusList);
	
	public List<TestAssignment> findByIdInAndStatus(List<Long> assigneeList,String status);
	
	/**
	 * Exists by user authority info id in and question bank name and status in.
	 *
	 * @param userAuthId the user auth id
	 * @param questionBankName the question bank name
	 * @param statusList the status list
	 * @return true, if successful
	 */
	public boolean existsByUserAuthorityInfoIdInAndQuestionBankQuestionBankNameAndStatusIn(List<Long> userAuthId,String questionBankName,List<String> statusList);

	public boolean existsByUserAuthorityInfoIdInAndQuestionBankIdAndStatusIn(List<Long> userAuthId,Long questionBankId,List<String> statusList);

	/**
	 * Find by user authority info id in and status in.
	 *
	 * @param userAuthId the user auth id
	 * @param statusList the status list
	 * @return the list
	 */
	public List<TestAssignment> findByUserAuthorityInfoIdInAndStatusIn(List<Long> userAuthId,List<String> statusList);

	/**
	 * Count by user authority info id.
	 *
	 * @param id the id
	 * @return the long
	 */
	public Long countByUserAuthorityInfoId(Long id);

	/**
	 * Update expired status.
	 *
	 * @param assigneeList the assignee list
	 */
	@Modifying( clearAutomatically = true )
	@Query(value="UPDATE vexamine_test_assignment SET status = 'Expired' , expired = true WHERE id IN (?1) AND status = 'New' ",nativeQuery = true)
	public void updateExpiredStatus(List<Long> assigneeList);

	/**
	 * Update reject status.
	 *
	 * @param assigneeList the assignee list
	 * @return the int
	 */
	@Modifying( clearAutomatically = true )
	@Query(value="UPDATE vexamine_test_assignment SET status = 'Rejected' , expired = true WHERE id IN (?1) AND status = 'New' ",nativeQuery = true)
	public int updateRejectStatus(List<Long> assigneeList);
	
	/**
	 * Find by user authority info id order by create date desc.
	 *
	 * @param id the id
	 * @return the list
	 */
	public List<TestAssignment> findByUserAuthorityInfoIdOrderByCreateDateDesc(long id);

	/**
	 * Find by user authority info id order by allowed start date desc.
	 *
	 * @param id the id
	 * @return the list
	 */
	public List<TestAssignment> findByUserAuthorityInfoIdOrderByAllowedStartDateDesc(long id);

	/**
	 * Find by user authority info id order by expiration time.
	 *
	 * @param id the id
	 * @return the list
	 */
	public List<TestAssignment> findByUserAuthorityInfoIdOrderByExpirationTime(long id);

	/**
	 * Find candidate specific.
	 *
	 * @param authId the auth id
	 * @param name the name
	 * @return the list
	 */
	@Query(value="select * from vexamine_test_assignment where user_auth_id= ?1 and " + 
			"(question_bank_id in (select id from vexamine_question_bank where (LOWER(question_bank_name) like LOWER(?2) or " + 
			" category_id in (select id from vexamine_test_category where (LOWER(category) like LOWER(?2) or LOWER(sub_category) like LOWER(?2))))))" + 
			" ORDER BY CASE status WHEN 'New' THEN 1 WHEN 'Inprogress' THEN 2 WHEN 'Completed' THEN 3 WHEN 'Expired' THEN 4  WHEN 'Rejected' THEN 5 ELSE 6  END ,"+
		    " create_date DESC",
			nativeQuery=true)
	public List<TestAssignment> findCandidateSpecific(long authId, String name);

	/**
	 * Find by user authority info user credentials mail id containing ignore case or question bank question bank name containing ignore case and status in.
	 *
	 * @param searchText the search text
	 * @param searchText2 the search text 2
	 * @param statusList the status list
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<TestAssignmentDetails> findByUserAuthorityInfoUserCredentialsMailIdContainingIgnoreCaseOrQuestionBankQuestionBankNameContainingIgnoreCaseAndStatusIn(
			String searchText, String searchText2, List<String> statusList, Pageable pageable);

	/**
	 * Find by question bank question bank name and expiration time.
	 *
	 * @param questionBankName the question bank name
	 * @param expirationDate the expiration date
	 * @return the list
	 */
	public List<TestAssignment> findByQuestionBankQuestionBankNameAndExpirationTime(String questionBankName, Timestamp expirationDate);

	/**
	 * The Interface TestAssignmentDetails.
	 */
	interface TestAssignmentDetails {

		QuestionBank getQuestionBank();
		Long getId();
		Integer getQuestionsCount();
		Boolean getInviteSent();
		String getStatus();
		Boolean getExpired();
		Timestamp getExpirationTime();
		Timestamp getTestStartTime();
		Timestamp getTestEndTime();
		UserAuthorityInfo getUserAuthorityInfo();    	

		interface UserAuthorityInfo{
			UserCredentials getUserCredentials();
			interface UserCredentials {
				String getMailId();
			}
		}

		/**
		 * The Interface QuestionBank.
		 */
		interface QuestionBank{

			String getQuestionBankName();
			TestCategory getTestCategory();
			interface TestCategory{
				String getCategory();
				String getSubCategory();
			}
		}

	}
}
