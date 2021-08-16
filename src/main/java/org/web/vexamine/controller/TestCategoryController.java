package org.web.vexamine.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.vexamine.dao.entity.TestCategory;
import org.web.vexamine.dao.vo.TestCategoryVo;
import org.web.vexamine.service.TestCategoryService;

/**
 * The Class TestCategoryController.
 */
@RestController
@RequestMapping("/test/category")
public class TestCategoryController {

	@Autowired
	private TestCategoryService testCategoryService;

	/**
	 * Adds the category.
	 *
	 * @param testCategoryVo
	 *            the test category vo
	 * @return the test category
	 */
	@PostMapping("/add")
	public TestCategory addCategory(@RequestBody TestCategoryVo testCategoryVo) {
		return testCategoryService.addCategory(testCategoryVo);
	}

	/**
	 * Delete category.
	 *
	 * @param id
	 *            the id
	 */
	@DeleteMapping("/delete/{id}")
	public void deleteCategory(@PathVariable(value = "id") long id) {
		testCategoryService.deleteCategory(id);
	}

	/**
	 * Gets the category count.
	 *
	 * @return the category count
	 */
	@GetMapping("/count")
	public Long getCategoryCount() {
		return testCategoryService.getCategoryCount();
	}

	/**
	 * Search category.
	 *
	 * @param testCategoryVo
	 *            the test category vo
	 * @return the list
	 */
	@PostMapping("/find/category")
	public List<TestCategory> searchCategory(@RequestBody TestCategoryVo testCategoryVo) {
		return testCategoryService.searchCategory(testCategoryVo);
	}

	/**
	 * Search sub category.
	 *
	 * @param testCategoryVo
	 *            the test category vo
	 * @return the list
	 */
	@PostMapping("/find/subcategory")
	public List<TestCategory> searchSubCategory(@RequestBody TestCategoryVo testCategoryVo) {
		return testCategoryService.searchSubCategory(testCategoryVo);
	}

	/**
	 * Search sub category by name.
	 *
	 * @param testCategoryVo
	 *            the test category vo
	 * @return the list
	 */
	@PostMapping("/find/subcategory/name")
	public List<TestCategory> searchSubCategoryByName(@RequestBody TestCategoryVo testCategoryVo) {
		return testCategoryService.searchSubCategoryByName(testCategoryVo);
	}

	/**
	 * Search sub category by name.
	 *
	 * @return the map
	 */
	@PostMapping("/find/all")
	public Map<String, List<String>> searchSubCategoryByName() {
		return testCategoryService.findAllCategory();
	}

	/**
	 * Gets the sub category and category.
	 *
	 * @return the sub category and category
	 */
	@GetMapping("/groups")
	public Map<String, Set<String>> getSubCategoryAndCategory() {
		return testCategoryService.findAndGroupByCategory();
	}

	/**
	 * Gets the category.
	 *
	 * @param testCategoryVo
	 *            the test category vo
	 * @return the category
	 */
	@PostMapping("filter/group")
	public Map<String, Set<String>> getCategory(@RequestBody TestCategoryVo testCategoryVo) {
		return testCategoryService.filterAndGroupByCategory(testCategoryVo);
	}

	/**
	 * Persist category in batch.
	 *
	 * @param testCategoryVo
	 *            the test category vo
	 * @return the map
	 */
	@PostMapping("add/batch")
	public Map<String, Set<String>> persistCategoryInBatch(@RequestBody TestCategoryVo testCategoryVo) {
		testCategoryService.addCategoryBatch(testCategoryVo);
		return getSubCategoryAndCategory();
	}

	/**
	 * Check category existence.
	 *
	 * @param category
	 *            the category
	 * @return the string
	 */
	@PostMapping("check/exists/{category}")
	public String checkCategoryExistence(@PathVariable(value = "category") String category) {
		testCategoryService.checkAndThrowIfCategoryExists(category);
		return "Success";
	}

}
