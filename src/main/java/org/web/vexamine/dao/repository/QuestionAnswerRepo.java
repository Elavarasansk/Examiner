package org.web.vexamine.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.QuestionAnswer;

/**
 * The Interface QuestionAnswerRepo.
 */
@Transactional
public interface QuestionAnswerRepo extends JpaRepository<QuestionAnswer, Long>, JpaSpecificationExecutor<QuestionAnswer>  {
	
	/**
	 * Delete by question bank id.
	 *
	 * @param id the id
	 */
	public void deleteByQuestionBankId(Long id);
	
	/**
	 * Find by question bank id and id in.
	 *
	 * @param id the id
	 * @param idList the id list
	 * @return the optional
	 */
	public Optional<List<QuestionAnswer>> findByQuestionBankIdAndIdIn(Long id, List<Long> idList);
	
	/**
	 * Find by question bank id.
	 *
	 * @param id the id
	 * @param pageable the pageable
	 * @return the optional
	 */
	public Optional<List<QuestionAnswer>> findByQuestionBankId(Long id, Pageable pageable);
	
	/**
	 * Find by question bank id.
	 *
	 * @param qbId the qb id
	 * @return the optional
	 */
	public Optional<List<QuestionAnswer>> findByQuestionBankId(Long qbId);
	
	/**
	 * Find by question bank id order by id desc.
	 *
	 * @param qbId the qb id
	 * @return the optional
	 */
	public Optional<List<QuestionAnswer>> findByQuestionBankIdOrderByIdDesc(Long qbId);
	
	/**
	 * Find by question containing ignore case.
	 *
	 * @param question the question
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<QuestionAnswer> findByQuestionContainingIgnoreCase(String question, Pageable pageable);
	
	/**
	 * Count by question bank id.
	 *
	 * @param questionBankId the question bank id
	 * @return the long
	 */
	public Long countByQuestionBankId(Long questionBankId);
	
	/**
	 * Count by question bank question bank name.
	 *
	 * @param questionBankName the question bank name
	 * @return the long
	 */
	public Long countByQuestionBankQuestionBankName(String questionBankName);
	
}