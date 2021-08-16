package org.web.vexamine.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.web.vexamine.dao.entity.TestCategory;

/**
 * The Interface TestCategoryRepo.
 */
@Transactional
public interface TestCategoryRepo extends JpaRepository<TestCategory, Long>, JpaSpecificationExecutor<TestCategory> {

  /**
   * Exists by category containing ignore case and sub category containing ignore case.
   *
   * @param category the category
   * @param subCategory the sub category
   * @return true, if successful
   */
  public boolean existsByCategoryContainingIgnoreCaseAndSubCategoryContainingIgnoreCase(String category,
      String subCategory);

  /**
   * Find by category containing ignore case.
   *
   * @param category the category
   * @return the optional
   */
  public Optional<List<TestCategory>> findByCategoryContainingIgnoreCase(String category);
  
  /**
   * Find by category.
   *
   * @param category the category
   * @return the list
   */
  public List<TestCategory> findByCategory(String category);

  /**
   * Find by category and sub category containing ignore case.
   *
   * @param category the category
   * @param Subcategory the subcategory
   * @param pageable the pageable
   * @return the optional
   */
  public Optional<List<TestCategory>> findByCategoryAndSubCategoryContainingIgnoreCase(String category,
      String Subcategory, Pageable pageable);

  /**
   * Find by sub category containing ignore case.
   *
   * @param Subcategory the subcategory
   * @param pageable the pageable
   * @return the optional
   */
  public Optional<List<TestCategory>> findBySubCategoryContainingIgnoreCase(String Subcategory, Pageable pageable);

  /**
   * Find by category containing ignore case.
   *
   * @param category the category
   * @param pageable the pageable
   * @return the optional
   */
  public Optional<List<TestCategory>> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

  /**
   * Find by category and sub category.
   *
   * @param category the category
   * @param Subcategory the subcategory
   * @return the test category
   */
  public TestCategory findByCategoryAndSubCategory(String category, String Subcategory);

}