package org.web.vexamine.dao.repository.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.TestAssignment;
import org.web.vexamine.dao.vo.TestAssignmentVo;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class TestAssignmentSpecification.
 */
@Transactional
public class TestAssignmentSpecification implements Specification<TestAssignment> {

	private TestAssignmentVo testAssignmentVo;

	/**
	 * Instantiates a new test assignment specification.
	 *
	 * @param testAssignmentVo the test assignment vo
	 */
	public TestAssignmentSpecification(TestAssignmentVo testAssignmentVo) {
		super();
		this.testAssignmentVo = testAssignmentVo;
	}

	@Override
	public Predicate toPredicate(Root<TestAssignment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder critbuilder) {
		String createdUser = CookieSessionStorage.get().getUserName();
		Predicate predicate = critbuilder.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();

		Expression<String> mailRoot =  root.get("userAuthorityInfo").get("userCredentials").get("mailId");
		Expression<String> questionRoot = root.get("questionBank").get("questionBankName");
		Expression<String>  statusRoot=root.get("status");
		Expression<String> createUser =root.get("createUser");


		if(StringUtils.isNotEmpty(testAssignmentVo.getMailId())) {
			expressions.add(critbuilder.like(critbuilder.lower(mailRoot), containsJoiner(testAssignmentVo.getMailId())));
		}
		if(StringUtils.isNotEmpty(testAssignmentVo.getQuestionBankName())) {
			expressions.add(critbuilder.equal(questionRoot,testAssignmentVo.getQuestionBankName()));
		}
		if(StringUtils.isNotEmpty(testAssignmentVo.getStatus())) {
			expressions.add(critbuilder.equal(statusRoot, testAssignmentVo.getStatus()));
		}		
		expressions.add(critbuilder.equal(createUser, createdUser));

		if (StringUtils.isEmpty(testAssignmentVo.getSortKey())) {
			Expression<Object> caseExpression = critbuilder.selectCase()
					.when(critbuilder.equal(statusRoot, "New"), 1)
					.when(critbuilder.equal(statusRoot, "Inprogress"), 2)
					.when(critbuilder.equal(statusRoot, "Completed"), 3)
					.when(critbuilder.equal(statusRoot, "Expired"), 4)
					.when(critbuilder.equal(statusRoot, "Rejected"), 5)
					.otherwise(6);	
			Order order1 = critbuilder.asc(caseExpression);
			Order order2 =critbuilder.desc(createUser);
			criteriaQuery =  criteriaQuery.orderBy(order1,order2);
		}else {
			Order order1;
                if(testAssignmentVo.getSortType().equals(VexamineTestConstants.ASCEND)) {
                	order1 =  critbuilder.asc(root.get(testAssignmentVo.getSortKey()));
                }else {
                	order1 = critbuilder.desc(root.get(testAssignmentVo.getSortKey()));
                } 
			criteriaQuery =  criteriaQuery.orderBy(order1);
		}
		return predicate;
	}

	/**
	 * Contains joiner.
	 *
	 * @param value the value
	 * @return the string
	 */
	private String containsJoiner(String value) {
		return String.format("%%%s%%", value.toLowerCase());
	}

}