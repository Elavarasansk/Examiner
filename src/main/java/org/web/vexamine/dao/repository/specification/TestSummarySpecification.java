package org.web.vexamine.dao.repository.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.TestSummary;
import org.web.vexamine.dao.vo.TestSummaryVo;
import org.web.vexamine.utils.storage.CookieSessionStorage;

/**
 * The Class TestSummarySpecification.
 */
@Transactional
public class TestSummarySpecification  implements Specification<TestSummary> {

	private TestSummaryVo testSummaryVo;

	/**
	 * Instantiates a new test summary specification.
	 *
	 * @param testSummaryVo the test summary vo
	 */
	public TestSummarySpecification(TestSummaryVo testSummaryVo) {
		super();
		this.testSummaryVo = testSummaryVo;
	}

	@Override
	public Predicate toPredicate(Root<TestSummary> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder critbuilder) {
		String createdUser = CookieSessionStorage.get().getUserName();
		Predicate predicate = critbuilder.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();
		Path<Object> testAssignRoot = root.get("testAssignment");
		Path<Object> qbRoot = testAssignRoot.get("questionBank");
		Path<Object> userAuthRoot = testAssignRoot.get("userAuthorityInfo");		
		Expression<String> questionRoot = qbRoot.get("questionBankName");
		Expression<String> categoryRoot = qbRoot.get("testCategory").get("category");
		Expression<String> subCategoryRoot = qbRoot.get("testCategory").get("subCategory");
		Expression<String> mailRoot = userAuthRoot.get("userCredentials").get("mailId");		
		Expression<String> testdateRoot = testAssignRoot.get("testStartTime");
		Expression<String> questionsCountRoot = testAssignRoot.get("questionsCount");
		Expression<String> createUser = testAssignRoot.get("createUser");
		Expression<String> createDate = root.get(VexamineTestConstants.CREATE_DATE);
		Expression<String> sortKey =null;
		if(StringUtils.isNotEmpty(testSummaryVo.getSortKey())) {	
			switch(testSummaryVo.getSortKey()) {
			case VexamineTestConstants.TEST_DATE:
				sortKey = testdateRoot;
				break;			
			case "questionTaken":
				sortKey = questionsCountRoot;
				break;
			default:
				 sortKey = root.get(testSummaryVo.getSortKey());
				break;
			}
		}
		if(StringUtils.isNotEmpty(testSummaryVo.getMailIdName())) {
			expressions.add(critbuilder.like(critbuilder.lower(mailRoot), containsJoiner(testSummaryVo.getMailIdName())));
		}
		if(StringUtils.isNotEmpty(testSummaryVo.getQuestionBankName())) {
			expressions.add(critbuilder.equal(questionRoot, testSummaryVo.getQuestionBankName()));
		}	
		if(StringUtils.isNotEmpty(testSummaryVo.getCategory())) {
			expressions.add(critbuilder.equal(categoryRoot, testSummaryVo.getCategory()));
		}
		if(StringUtils.isNotEmpty(testSummaryVo.getSubCategory())) {
			expressions.add(critbuilder.equal(subCategoryRoot, testSummaryVo.getSubCategory()));
		}
		if(StringUtils.isNotEmpty(testSummaryVo.getSortKey())) {			
			if( testSummaryVo.getSortType().equals(VexamineTestConstants.ASCEND)) {
				criteriaQuery.orderBy(critbuilder.asc(sortKey));
			}else {
				criteriaQuery.orderBy(critbuilder.desc(sortKey));
			}
		}else {
			criteriaQuery.orderBy(critbuilder.desc(createDate));
		}
		expressions.add(critbuilder.equal(userAuthRoot.get("userRole").get("type"), VexamineTestConstants.CANDIDATE));
		expressions.add(critbuilder.equal(createUser, createdUser));

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