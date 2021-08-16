package org.web.vexamine.dao.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.TestResult;

/**
 * The Interface TestResultRepository.
 */
@Transactional
public interface TestResultRepository extends JpaRepository<TestResult, Long> {


	/**
	 * The Interface TestResultInfo.
	 */
	interface TestResultInfo {
		
		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		Long getId();
		
		/**
		 * Gets the question answer.
		 *
		 * @return the question answer
		 */
		QuestionAnswer getQuestionAnswer();
		
		/**
		 * Gets the valid answer.
		 *
		 * @return the valid answer
		 */
		Boolean getValidAnswer();			
		
		/**
		 * Gets the status.
		 *
		 * @return the status
		 */
		String getStatus();		
		
		/**
		 * Gets the answer.
		 *
		 * @return the answer
		 */
		String getAnswer();
		
		/**
		 * Gets the time taken.
		 *
		 * @return the time taken
		 */
		Timestamp getTimeTaken();
		
		/**
		 * The Interface QuestionAnswer.
		 */
		interface QuestionAnswer{			  
			
			/**
			 * Gets the question.
			 *
			 * @return the question
			 */
			String getQuestion();			
			
			/**
			 * Gets the answer.
			 *
			 * @return the answer
			 */
			String getAnswer();
		}
	}
	
	/**
	 * Find by test assignment id.
	 *
	 * @param testId the test id
	 * @return the optional
	 */
	public Optional<List<TestResult>> findByTestAssignmentId(Long testId); 
	
	/**
	 * Find by test assignment id order by id.
	 *
	 * @param testId the test id
	 * @return the optional
	 */
	public Optional<List<TestResult>> findByTestAssignmentIdOrderById(Long testId);
	
	/**
	 * Find by test assignment id and question answer question type order by id.
	 *
	 * @param testId the test id
	 * @param questionType the question type
	 * @return the optional
	 */
	public Optional<List<TestResult>> findByTestAssignmentIdAndQuestionAnswerQuestionTypeOrderById(Long testId,int questionType);
	
	
	public Optional<List<TestResult>> findByTestAssignmentIdInAndQuestionAnswerQuestionTypeOrderById(List<Long> testIdList,int questionType);


	
}