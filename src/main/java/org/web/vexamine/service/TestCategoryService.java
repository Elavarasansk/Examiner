package org.web.vexamine.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.TestCategory;
import org.web.vexamine.dao.repository.TestCategoryRepo;
import org.web.vexamine.dao.repository.specification.TestCategorySpecification;
import org.web.vexamine.dao.vo.TestCategoryVo;
import org.web.vexamine.utils.storage.CookieSessionStorage;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class TestCategoryService.
 */
@Service

/** The Constant log. */
@Slf4j
public class TestCategoryService {

	@Autowired
	private TestCategoryRepo testCategoryRepo;

	/**
	 * Adds the category.
	 *
	 * @param testCategoryVo the test category vo
	 * @return the test category
	 */
	public TestCategory addCategory(TestCategoryVo testCategoryVo) {
		if (checkCategoryExists(testCategoryVo)) {
			throw new DuplicateKeyException("Category - \"" + testCategoryVo.getCategory() + "\"" + " Sub category - \""
					+ testCategoryVo.getSubCategory() + "\"" + " already exists." + " So Category cannot be registered");
		}
		String sessionUser  = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? 
				CookieSessionStorage.get().getUserName() : StringUtils.EMPTY;	
				TestCategory testCategory = TestCategory.builder().build();
				BeanUtils.copyProperties(testCategoryVo, testCategory);
				testCategory.setCreateUser(sessionUser);
				testCategory.setUpdateUser(sessionUser);
				return testCategoryRepo.save(testCategory);
	}

	/**
	 * Delete category.
	 *
	 * @param id the id
	 */
	public void deleteCategory(Long id) {
		testCategoryRepo.deleteById(id);
	}

	/**
	 * Gets the category count.
	 *
	 * @return the category count
	 */
	public long getCategoryCount() {
		return testCategoryRepo.count();
	}

	/**
	 * Search category.
	 *
	 * @param testCategoryVo the test category vo
	 * @return the list
	 */
	public List<TestCategory> searchCategory(TestCategoryVo testCategoryVo) {
		if (StringUtils.isEmpty(testCategoryVo.getCategory())) {
			return Collections.emptyList();
		}
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, testCategoryVo.getSuggestLimit());
		Optional<List<TestCategory>> searchList = testCategoryRepo
				.findByCategoryContainingIgnoreCase(testCategoryVo.getCategory(), pageable);
		return searchList.isPresent() ? searchList.get() : Collections.emptyList();
	}

	/**
	 * Search sub category.
	 *
	 * @param testCategoryVo the test category vo
	 * @return the list
	 */
	public List<TestCategory> searchSubCategory(TestCategoryVo testCategoryVo) {
		if (StringUtils.isEmpty(testCategoryVo.getSubCategory())) {
			return Collections.emptyList();
		}
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, testCategoryVo.getSuggestLimit());
		Optional<List<TestCategory>> searchList = testCategoryRepo.findByCategoryAndSubCategoryContainingIgnoreCase(
				testCategoryVo.getCategory(), testCategoryVo.getSubCategory(), pageable);
		return searchList.isPresent() ? searchList.get() : Collections.emptyList();
	}

	/**
	 * Search sub category by name.
	 *
	 * @param testCategoryVo the test category vo
	 * @return the list
	 */
	public List<TestCategory> searchSubCategoryByName(TestCategoryVo testCategoryVo) {
		if (StringUtils.isEmpty(testCategoryVo.getCategory()) || StringUtils.isEmpty(testCategoryVo.getSubCategory())) {
			return Collections.emptyList();
		}
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, testCategoryVo.getSuggestLimit());
		Optional<List<TestCategory>> searchList = testCategoryRepo
				.findBySubCategoryContainingIgnoreCase(testCategoryVo.getSubCategory(), pageable);
		return searchList.isPresent() ? searchList.get() : Collections.emptyList();

	}

	/**
	 * Check category exists.
	 *
	 * @param testCategoryVo the test category vo
	 * @return true, if successful
	 */
	private boolean checkCategoryExists(TestCategoryVo testCategoryVo) {
		return testCategoryRepo.existsByCategoryContainingIgnoreCaseAndSubCategoryContainingIgnoreCase(
				testCategoryVo.getCategory(), testCategoryVo.getSubCategory());
	}

	/**
	 * Find all category.
	 *
	 * @return the map
	 */
	public Map<String, List<String>> findAllCategory() {
		List<TestCategory> testCategoryList = testCategoryRepo.findAll();
		Map<String, List<String>> categoryTypeMap = new HashMap<String, List<String>>();

		categoryTypeMap.put("Category",
				testCategoryList.stream().map(TestCategory::getCategory).collect(Collectors.toList()));
		categoryTypeMap.put("SubCategory",
				testCategoryList.stream().map(TestCategory::getSubCategory).collect(Collectors.toList()));

		return categoryTypeMap;
	}

	/**
	 * Find and group by category.
	 *
	 * @return the map
	 */
	public Map<String, Set<String>> findAndGroupByCategory() {
		List<TestCategory> testCategoryList = testCategoryRepo.findAll();
		Map<String, Set<String>> categorySubCategoryMap = testCategoryList.stream().collect(Collectors.groupingBy(TestCategory::getCategory,
				Collectors.mapping(TestCategory::getSubCategory, Collectors.toSet())));
		return categorySubCategoryMap;
	}

	/**
	 * Filter and group by category.
	 *
	 * @param testCategoryVo the test category vo
	 * @return the map
	 */
	public Map<String, Set<String>> filterAndGroupByCategory(TestCategoryVo testCategoryVo) {
		TestCategorySpecification searchSpec = new TestCategorySpecification(testCategoryVo);
		List<TestCategory> testCategoryList = testCategoryRepo.findAll(searchSpec);
		Map<String, Set<String>> categorySubCategoryMap = testCategoryList.stream().collect(Collectors.groupingBy(TestCategory::getCategory,
				Collectors.mapping(TestCategory::getSubCategory, Collectors.toSet())));
		return categorySubCategoryMap;
	}

	/**
	 * Adds the category batch.
	 *
	 * @param testCategoryVo the test category vo
	 */
	public void addCategoryBatch(TestCategoryVo testCategoryVo) {
		String userName = CookieSessionStorage.get().getUserName();
		List<TestCategory> testCategoryList = new ArrayList<>();
		String categoryName = testCategoryVo.getCategory();

		List<String> subCategoryList = testCategoryRepo.findByCategory(categoryName).stream()
				.map(TestCategory::getSubCategory).collect(Collectors.toList());

		for(String newSubCat: testCategoryVo.getSubCategoryList()) {
			
			if(subCategoryList.contains(newSubCat)) {
				log.warn(String.format("Category & Sub-Category - %s//%s Already exists.", categoryName));
				continue;
			}
			
			TestCategory testCategory = TestCategory.builder()
					.category(categoryName)
					.subCategory(newSubCat)
					.createDate(new Date())
					.createUser(userName)
					.build();
			testCategoryList.add(testCategory);
		}
		testCategoryRepo.saveAll(testCategoryList);
	}

	/**
	 * Check and throw if category exists.
	 *
	 * @param categoryName the category name
	 */
	public void checkAndThrowIfCategoryExists(String categoryName) {
		List<TestCategory> existingCategory = testCategoryRepo.findByCategory(categoryName);
		if(!CollectionUtils.isEmpty(existingCategory)) {
			Log.error(String.format("Category - %s Already exists. Please add new", categoryName));
			throw new DataIntegrityViolationException(String.format("Category - %s Already exists. Please add new", categoryName));	
		}
	}

}
