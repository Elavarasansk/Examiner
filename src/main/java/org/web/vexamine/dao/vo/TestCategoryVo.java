package org.web.vexamine.dao.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Instantiates a new test category vo.
 *
 * @param id the id
 * @param category the category
 * @param subCategory the sub category
 * @param suggestLimit the suggest limit
 * @param subCategoryList the sub category list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCategoryVo {	

	private Long id;

	private String category;

	private String subCategory;
	
	private Integer suggestLimit;

	private List<String> subCategoryList;
}
