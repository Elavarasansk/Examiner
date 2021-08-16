package org.web.vexamine.dao.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.QuestionBank;

/**
 * The Interface QuestionBankRepo.
 */
@Transactional
public interface QuestionBankRepo  extends JpaRepository<QuestionBank, Long>, JpaSpecificationExecutor<QuestionBank> {

	/**
	 * Exists by question bank name containing ignore case.
	 *
	 * @param questionBankName the question bank name
	 * @return true, if successful
	 */
	public boolean existsByQuestionBankNameContainingIgnoreCase(String questionBankName);
	
	/**
	 * Find by question bank name containing ignore case.
	 *
	 * @param questionBankName the question bank name
	 * @return the question bank
	 */
	public QuestionBank findByQuestionBankNameContainingIgnoreCase(String questionBankName);

	/**
	 * Find by test category id and question bank name containing ignore case.
	 *
	 * @param categoryId the category id
	 * @param questionBankName the question bank name
	 * @param pageable the pageable
	 * @return the optional
	 */
	public Optional<List<QuestionBank>> findByTestCategoryIdAndQuestionBankNameContainingIgnoreCase(Long categoryId,String questionBankName, Pageable pageable);

	/**
	 * Find by question bank name containing ignore case.
	 *
	 * @param questionBankName the question bank name
	 * @param pageable the pageable
	 * @return the list
	 */
	public List<QuestionBankName> findByQuestionBankNameContainingIgnoreCase(String questionBankName, Pageable pageable);

	/**
	 * Find by question bank name in.
	 *
	 * @param questionBankList the question bank list
	 * @return the optional
	 */
	public Optional<List<QuestionBank>> findByQuestionBankNameIn(List<String>  questionBankList);
	
	/**
	 * The Interface QuestionBankName.
	 */
	interface QuestionBankName {
  		String getQuestionBankName();
		}

	/**
	 * Find by test category id and question bank name.
	 *
	 * @param categoryId the category id
	 * @param questionBankName the question bank name
	 * @return the question bank
	 */
	public QuestionBank findByTestCategoryIdAndQuestionBankName(Long categoryId, String questionBankName);
}