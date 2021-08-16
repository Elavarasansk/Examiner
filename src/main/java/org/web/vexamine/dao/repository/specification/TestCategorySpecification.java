package org.web.vexamine.dao.repository.specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.TestCategory;
import org.web.vexamine.dao.vo.TestCategoryVo;

/**
 * The Class TestCategorySpecification.
 */
@Transactional
public class TestCategorySpecification implements Specification<TestCategory> {

	private TestCategoryVo testCategoryVo;

	/**
	 * Instantiates a new test category specification.
	 *
	 * @param testCategoryVo the test category vo
	 */
	public TestCategorySpecification(TestCategoryVo testCategoryVo) {
		super();
		this.testCategoryVo = testCategoryVo;
	}

	@Override
	public Predicate toPredicate(Root<TestCategory> categoryRoot, CriteriaQuery<?> criteriaQuery, CriteriaBuilder critbuilder) {
		Predicate predicate = critbuilder.conjunction();
		List<Expression<Boolean>> expressions = predicate.getExpressions();
		 
		if(!StringUtils.isEmpty(testCategoryVo.getCategory())) {
			expressions.add(critbuilder.like(critbuilder.upper(categoryRoot.get("category")), containsJoiner(testCategoryVo.getCategory())));
		}
		if(!StringUtils.isEmpty(testCategoryVo.getSubCategory())) {
			expressions.add(critbuilder.like(critbuilder.upper(categoryRoot.get("subCategory")), containsJoiner(testCategoryVo.getSubCategory())));
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
		return String.format("%%%s%%", value.toUpperCase());
	}

}