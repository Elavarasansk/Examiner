package org.web.vexamine.dao.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.TestResult;

/**
 * The Interface ReportGeneratorRepository.
 */
@Transactional
public interface ReportGeneratorRepository extends JpaRepository<TestResult, Long> {

/**
 * Find by test assignment id.
 *
 * @param testId the test id
 * @return the list
 */
public List<TestResultInfo> findByTestAssignmentId(Long testId); 

	/**
	 * The Interface TestResultInfo.
	 */
	interface TestResultInfo {
		Long getId();
		QuestionAnswer getQuestionAnswer();
		Boolean getValidAnswer();			
		String getStatus();		
		String getAnswer();
		Timestamp getTimeTaken();
		interface QuestionAnswer{			  
			String getQuestion();			
			String getAnswer();
		}

	}
	
	
}