package org.web.vexamine.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.TestSummary;

/**
 * The Interface TestSummaryRepository.
 */
@Transactional
public interface TestSummaryRepository extends JpaRepository<TestSummary, Long>, JpaSpecificationExecutor<TestSummary> {

	
	/**
	 * Find by test assignment id in order by create date desc.
	 *
	 * @param testAssignmentIdList the test assignment id list
	 * @return the list
	 */
	public List<TestSummary> findByTestAssignmentIdInOrderByCreateDateDesc(List<Long> testAssignmentIdList);

}