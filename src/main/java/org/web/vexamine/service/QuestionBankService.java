package org.web.vexamine.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.web.vexamine.constants.VexamineTestConstants;
import org.web.vexamine.dao.entity.QuestionAnswer;
import org.web.vexamine.dao.entity.QuestionBank;
import org.web.vexamine.dao.entity.TestCategory;
import org.web.vexamine.dao.repository.QuestionAnswerRepo;
import org.web.vexamine.dao.repository.QuestionBankRepo;
import org.web.vexamine.dao.repository.QuestionBankRepo.QuestionBankName;
import org.web.vexamine.dao.repository.TestCategoryRepo;
import org.web.vexamine.dao.repository.specification.QuestionBankSpecification;
import org.web.vexamine.dao.vo.QuestionBankFilterVo;
import org.web.vexamine.dao.vo.QuestionBankVo;
import org.web.vexamine.utils.CommonFunctions;
import org.web.vexamine.utils.QuestionAnswerConstants;
import org.web.vexamine.utils.constants.QAConstants;
import org.web.vexamine.utils.storage.CookieSessionStorage;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class QuestionBankService.
 */
@Service
@Slf4j
public class QuestionBankService {

	@Autowired
	private QuestionBankRepo questionBankRepo;

	@Autowired
	private QuestionAnswerRepo questionAnswerRepo;

	@Autowired
	private TestCategoryRepo testCategoryRepo;

	@Value("classpath:template/questionbank_template.xls")
	Resource qbtemplateResource;

	/**
	 * Adds the question bank.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the question bank
	 */
	public QuestionBank addQuestionBank(QuestionBankVo questionBankVo) {
		if (checkQuestionBankNameExists(questionBankVo)) {
			throw new DuplicateKeyException("QuestionBankName - \"" + questionBankVo.getQuestionBankName() + "\""
					+ " already exists." + " So QuestionBankName cannot be registered");
		}
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
				: StringUtils.EMPTY;
		QuestionBank questionBank = QuestionBank.builder().build();
		BeanUtils.copyProperties(questionBankVo, questionBank);
		questionBank.setCreateUser(sessionUser);
		questionBank.setUpdateUser(sessionUser);
		return questionBankRepo.save(questionBank);
	}

	/**
	 * Gets the question bank count.
	 *
	 * @return the question bank count
	 */
	public long getQuestionBankCount() {
		return questionBankRepo.count();
	}

	/**
	 * Delete question bank.
	 *
	 * @param id the id
	 */
	public void deleteQuestionBank(Long id) {
		questionBankRepo.deleteById(id);
	}

	/**
	 * Find all question bank.
	 *
	 * @return the list
	 */
	public List<String> findAllQuestionBank() {
		List<QuestionBank> questionBankList = questionBankRepo.findAll();
		return questionBankList.stream().map(QuestionBank::getQuestionBankName).collect(Collectors.toList());

	}

	/**
	 * Search question bank by category.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the list
	 */
	public List<QuestionBank> searchQuestionBankByCategory(QuestionBankVo questionBankVo) {
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, questionBankVo.getSuggestLimit());
		Optional<List<QuestionBank>> questionBankList = questionBankRepo
				.findByTestCategoryIdAndQuestionBankNameContainingIgnoreCase(questionBankVo.getCategoryId(),
						questionBankVo.getQuestionBankName(), pageable);
		return questionBankList.isPresent() ? questionBankList.get() : Collections.emptyList();
	}

	/**
	 * Search question bank by name.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the list
	 */
	public List<String> searchQuestionBankByName(QuestionBankVo questionBankVo) {
		Pageable pageable = PageRequest.of(VexamineTestConstants.INITIAL, questionBankVo.getSuggestLimit());
		List<QuestionBankName> questionBankList = questionBankRepo
				.findByQuestionBankNameContainingIgnoreCase(questionBankVo.getQuestionBankName(), pageable);
		List<String> list = questionBankList.stream().map(QuestionBankName::getQuestionBankName)
				.collect(Collectors.toList());
		return list;
	}

	/**
	 * Gets the all question bank.
	 *
	 * @param questionBankVo the question bank vo
	 * @return the all question bank
	 */
	public Map<String, Object> getAllQuestionBank(QuestionBankVo questionBankVo) {
		Pageable pageable = PageRequest.of(questionBankVo.getOffset(), questionBankVo.getOffset(),
				Sort.by(VexamineTestConstants.QUESTION_BANK_NAME));
		Page<QuestionBank> page = questionBankRepo.findAll(pageable);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(VexamineTestConstants.VALUE, page.getContent());
		resultMap.put(VexamineTestConstants.COUNT, page.getTotalElements());
		return resultMap;
	}

	/**
	 * Gets the question bank.
	 *
	 * @param id the id
	 * @return the question bank
	 */
	public QuestionBank getQuestionBank(Long id) {
		Optional<QuestionBank> questionBankList = questionBankRepo.findById(id);
		return questionBankList.isPresent() ? questionBankList.get() : null;
	}

	/**
	 * Gets the question bank.
	 *
	 * @param questionBankName the question bank name
	 * @return the question bank
	 */
	public QuestionBank getQuestionBank(String questionBankName) {
		return questionBankRepo.findByQuestionBankNameContainingIgnoreCase(questionBankName);
	}

	/**
	 * Gets the question bank list.
	 *
	 * @param questionList the question list
	 * @return the question bank list
	 */
	public List<QuestionBank> getQuestionBankList(List<String> questionList) {
		Optional<List<QuestionBank>> questionBankList = questionBankRepo.findByQuestionBankNameIn(questionList);
		return questionBankList.isPresent() ? questionBankList.get() : null;
	}

	/**
	 * Check question bank name exists.
	 *
	 * @param questionBankVo the question bank vo
	 * @return true, if successful
	 */
	private boolean checkQuestionBankNameExists(QuestionBankVo questionBankVo) {
		return questionBankRepo.existsByQuestionBankNameContainingIgnoreCase(questionBankVo.getQuestionBankName());
	}

	/**
	 * Gets the qb template.
	 *
	 * @param response the response
	 * @return the qb template
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void getQbTemplate(HttpServletResponse response) throws IOException{
		InputStream qbTemplateFile = qbtemplateResource.getInputStream();
		CommonFunctions.fileDownloader(response, qbTemplateFile);
	}

	/**
	 * Upload question bank.
	 *
	 * @param file the file
	 * @param category the category
	 * @param subCategory the sub category
	 * @param qbName the qb name
	 */
	public void uploadQuestionBank(MultipartFile file, String category, String subCategory, String qbName) {
		TestCategory testCategory = testCategoryRepo.findByCategoryAndSubCategory(category, subCategory);
		QuestionBank questionBank = validateQb(testCategory, qbName);
		validateQuestionsFromFile(file, questionBank);
	}

	/**
	 * Validate qb.
	 *
	 * @param testCategory the test category
	 * @param qbName the qb name
	 * @return the question bank
	 */
	private QuestionBank validateQb(TestCategory testCategory, String qbName) {
		QuestionBank questionBank = questionBankRepo.findByTestCategoryIdAndQuestionBankName(testCategory.getId(), qbName);
		if(!ObjectUtils.isEmpty(questionBank)) {
			Log.error(String.format("Already exists. Please upload new qb", qbName));
			throw new DataIntegrityViolationException(String.format("Already exists. Please upload new qb", qbName));	
		}

		String userName = CookieSessionStorage.get().getUserName();
		questionBank = QuestionBank.builder()
				.testCategory(testCategory)
				.questionBankName(qbName)
				.createUser(userName)
				.createDate(new Date())
				.build();
		return questionBankRepo.save(questionBank);
	}

	/**
	 * Validate questions from file.
	 *
	 * @param qbFile the qb file
	 * @param questionBank the question bank
	 */
	private void validateQuestionsFromFile(MultipartFile qbFile, QuestionBank questionBank) {
		File qbTemplateFile = new File(System.getProperty("java.io.tmpdir")+File.separator+qbFile.getName()
		+System.currentTimeMillis()+FilenameUtils.getExtension(qbFile.getOriginalFilename()));
		try {
			qbFile.transferTo(qbTemplateFile);
			Workbook qbWorkbook = WorkbookFactory.create(qbTemplateFile);

			for(Sheet workSheet: qbWorkbook) {
				String sheetName = workSheet.getSheetName();
				switch(sheetName) {
				case "MultipleChoice":
					processMultiChoiceQa(workSheet, questionBank);
					break;

				case "EitherOr":
					processEitherOrQa(workSheet, questionBank);
					break;

				case "Descriptive":
					processDescriptiveQa(workSheet, questionBank);
					break;
				}
			}
			qbWorkbook.close();
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}finally{
			FileUtils.deleteQuietly(qbTemplateFile);
		}
	}

	/**
	 * Process multi choice qa.
	 *
	 * @param multiChoiceSheet the multi choice sheet
	 * @param questionBank the question bank
	 */
	private void processMultiChoiceQa(Sheet multiChoiceSheet, QuestionBank questionBank) {
		String userName = CookieSessionStorage.get().getUserName();
		DataFormatter dataFormat = new DataFormatter();
		List<QuestionAnswer> qaList = new ArrayList<>();

		int lastRowIndex = multiChoiceSheet.getLastRowNum();
		for(int curRow=QAConstants.START_ROW; curRow<=lastRowIndex; curRow++) {
			Row qaRow = multiChoiceSheet.getRow(curRow);
			Cell questionCell = qaRow.getCell(QAConstants.QUESTION_COL);
			String question = dataFormat.formatCellValue(questionCell);
			if(StringUtils.isEmpty(question)) {
				log.warn("Encountered an empty questoin in the template. So terminating");
				break;
			}

			Cell answerCell = qaRow.getCell(QAConstants.ANSWER_COL);
			Cell optionACell = qaRow.getCell(QAConstants.OPTION_A_COL);
			Cell optionBCell = qaRow.getCell(QAConstants.OPTION_B_COL);
			Cell optionCCell = qaRow.getCell(QAConstants.OPTION_C_COL);
			Cell optionDCell = qaRow.getCell(QAConstants.OPTION_D_COL);

			String answer = dataFormat.formatCellValue(answerCell).trim();
			String optionA = dataFormat.formatCellValue(optionACell).trim();
			String optionB = dataFormat.formatCellValue(optionBCell).trim();
			String optionC = dataFormat.formatCellValue(optionCCell).trim();
			String optionD = dataFormat.formatCellValue(optionDCell).trim();

			String verifyAnswer = answer.toLowerCase();

			if(!verifyAnswer.equals(optionA.toLowerCase()) && !verifyAnswer.equals(optionB.toLowerCase())
					&& !verifyAnswer.equals(optionC.toLowerCase()) && !verifyAnswer.equals(optionD.toLowerCase())) {
				log.error(String.format("Answer{%s},doesn't matches any options {%s,%s,%s,%s}--- question{%s}", answer, optionA, optionB, optionC, optionD, question));
				continue;
			}

			QuestionAnswer qaBuilder = QuestionAnswer.builder()
					.question(question)
					.answer(answer)
					.mcqOption1(optionA)
					.mcqOption2(optionB)
					.mcqOption3(optionC)
					.mcqOption4(optionD)
					.questionBank(questionBank)
					.questionType(2)
					.createDate(new Date())
					.createUser(userName)
					.build();
			qaList.add(qaBuilder);
		}
		questionAnswerRepo.saveAll(qaList);
	}

	/**
	 * Process either or qa.
	 *
	 * @param eitherOrSheet the either or sheet
	 * @param questionBank the question bank
	 */
	private void processEitherOrQa(Sheet eitherOrSheet, QuestionBank questionBank) {
		String userName = CookieSessionStorage.get().getUserName();
		DataFormatter dataFormat = new DataFormatter();
		List<QuestionAnswer> qaList = new ArrayList<>();

		int lastRowIndex = eitherOrSheet.getLastRowNum();
		for(int curRow=QAConstants.START_ROW; curRow<=lastRowIndex; curRow++) {
			Row qaRow = eitherOrSheet.getRow(curRow);
			Cell questionCell = qaRow.getCell(QAConstants.QUESTION_COL);
			String question = dataFormat.formatCellValue(questionCell);
			if(StringUtils.isEmpty(question)) {
				log.warn("Encountered an empty questoin in the template. So terminating");
				break;
			}

			Cell answerCell = qaRow.getCell(QAConstants.ANSWER_COL);
			Cell optionACell = qaRow.getCell(QAConstants.OPTION_A_COL);
			Cell optionBCell = qaRow.getCell(QAConstants.OPTION_B_COL);

			String answer = dataFormat.formatCellValue(answerCell).trim();
			String optionA = dataFormat.formatCellValue(optionACell).trim();
			String optionB = dataFormat.formatCellValue(optionBCell).trim();

			String verifyAnswer = answer.toLowerCase();

			if(!verifyAnswer.equals(optionA.toLowerCase()) && !verifyAnswer.equals(optionB.toLowerCase())) {
				log.error(String.format("Answer{%s},doesn't matches any option a{%s,%s}--- question{%s}", answer, optionA, optionB, question));
				continue;
			}

			QuestionAnswer qaBuilder = QuestionAnswer.builder()
					.question(question)
					.answer(answer)
					.choiceOption1(optionA)
					.choiceOption2(optionB)
					.questionBank(questionBank)
					.questionType(1)
					.createDate(new Date())
					.createUser(userName)
					.build();
			qaList.add(qaBuilder);
		}
		questionAnswerRepo.saveAll(qaList);
	}

	/**
	 * Process descriptive qa.
	 *
	 * @param eitherOrSheet the either or sheet
	 * @param questionBank the question bank
	 */
	private void processDescriptiveQa(Sheet eitherOrSheet, QuestionBank questionBank) {
		String userName = CookieSessionStorage.get().getUserName();
		DataFormatter dataFormat = new DataFormatter();
		List<QuestionAnswer> qaList = new ArrayList<>();

		int lastRowIndex = eitherOrSheet.getLastRowNum();
		for(int curRow=QAConstants.START_ROW; curRow<=lastRowIndex; curRow++) {
			Row qaRow = eitherOrSheet.getRow(curRow);
			Cell questionCell = qaRow.getCell(QAConstants.QUESTION_COL);
			String question = dataFormat.formatCellValue(questionCell);
			if(StringUtils.isEmpty(question)) {
				log.warn("Encountered an empty questoin in the template. So terminating");
				break;
			}

			Cell answerCell = qaRow.getCell(QAConstants.ANSWER_COL);
			String answer = dataFormat.formatCellValue(answerCell).trim();

			QuestionAnswer qaBuilder = QuestionAnswer.builder()
					.question(question)
					.answer(answer)
					.questionBank(questionBank)
					.questionType(0)
					.createDate(new Date())
					.createUser(userName)
					.build();
			qaList.add(qaBuilder);
		}
		questionAnswerRepo.saveAll(qaList);
	}

	/**
	 * Adds the question bank bulk.
	 *
	 * @param file the file
	 * @param category the category
	 * @param subCategory the sub category
	 * @param qbName the qb name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void addQuestionBankBulk(MultipartFile file, String category, String subCategory, String qbName)
			throws IOException {
		String sessionUser = !ObjectUtils.isEmpty(CookieSessionStorage.get()) ? CookieSessionStorage.get().getUserName()
				: StringUtils.EMPTY;
		TestCategory testCategory = testCategoryRepo.findByCategoryAndSubCategory(category, subCategory);
		QuestionBank questionBank = questionBankRepo.findByQuestionBankNameContainingIgnoreCase(qbName);
		if (Objects.isNull(questionBank)) {
			questionBank = QuestionBank.builder().build();
			questionBank.setTestCategory(testCategory);
			questionBank.setQuestionBankName(qbName);
			questionBank.setCreateUser(sessionUser);
			questionBank.setUpdateUser(sessionUser);
			questionBankRepo.save(questionBank);
		}
		File qbTemplateFile = new File(System.getProperty("java.io.tmpdir")+File.separator+file.getName()
		+System.currentTimeMillis()+FilenameUtils.getExtension(file.getOriginalFilename()));
		try {
			file.transferTo(qbTemplateFile);
			readQBTemplate(qbTemplateFile, questionBank, sessionUser);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			FileUtils.deleteQuietly(qbTemplateFile);

		}
	}

	/**
	 * Read QB template.
	 *
	 * @param qbTemplateFile the qb template file
	 * @param questionBank the question bank
	 * @param sessionUser the session user
	 * @throws EncryptedDocumentException the encrypted document exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void readQBTemplate(File qbTemplateFile, QuestionBank questionBank, String sessionUser)
			throws EncryptedDocumentException, IOException {
		Workbook qbWorkbook = WorkbookFactory.create(qbTemplateFile);
		Sheet qbSheet = qbWorkbook.getSheetAt(0);
		List<QuestionAnswer> questionAnswerList = new ArrayList<>();
		for (int counter = QuestionAnswerConstants.START_ROW; counter <= qbSheet.getLastRowNum(); counter++) {
			QuestionAnswer questionAnswer = QuestionAnswer.builder().build();
			if (Objects.nonNull(qbSheet.getRow(counter))) {
				String questionType = qbSheet.getRow(counter).getCell(QuestionAnswerConstants.QUESTION_TYPE_COLUMN)
						.getStringCellValue();
				switch (questionType) {
				case "Declarative":
					prepareDeclarativeVo(qbSheet.getRow(counter), 0, questionAnswer);
					questionAnswerList.add(questionAnswer);
					break;
				case "Two-choice":
					prepareTwoChoiceVo(qbSheet.getRow(counter), 1, questionAnswer);
					questionAnswerList.add(questionAnswer);
					break;
				case "Multiple-choice":
					prepareMultipleChoiceVo(qbSheet.getRow(counter), 2, questionAnswer);
					questionAnswerList.add(questionAnswer);
					break;
				default:
					break;
				}
			}
		}
		qbWorkbook.close();
		questionAnswerList.stream().forEach(rec -> {
			rec.setQuestionBank(questionBank);
			rec.setCreateUser(sessionUser);
			rec.setUpdateUser(sessionUser);
		});
		questionAnswerRepo.saveAll(questionAnswerList);
	}

	/**
	 * Prepare multiple choice vo.
	 *
	 * @param row the row
	 * @param questionType the question type
	 * @param questionAnswer the question answer
	 */
	public void prepareMultipleChoiceVo(Row row, int questionType, QuestionAnswer questionAnswer) {
		questionAnswer.setQuestion(row.getCell(QuestionAnswerConstants.QUESTION_COLUMN).toString());
		questionAnswer.setMcqOption1(row.getCell(QuestionAnswerConstants.OPTION_ONE_COLUMN).toString());
		questionAnswer.setMcqOption2(row.getCell(QuestionAnswerConstants.OPTION_TWO_COLUMN).toString());
		questionAnswer.setMcqOption3(row.getCell(QuestionAnswerConstants.OPTION_THREE_COLUMN).toString());
		questionAnswer.setMcqOption4(row.getCell(QuestionAnswerConstants.OPTION_FOUR_COLUMN).toString());
		questionAnswer.setAnswer(row.getCell(QuestionAnswerConstants.ANSWER_COLUMN).toString());
		questionAnswer.setQuestionType(questionType);
	}

	/**
	 * Prepare two choice vo.
	 *
	 * @param row the row
	 * @param questionType the question type
	 * @param questionAnswer the question answer
	 */
	public void prepareTwoChoiceVo(Row row, int questionType, QuestionAnswer questionAnswer) {
		questionAnswer.setQuestion(row.getCell(QuestionAnswerConstants.QUESTION_COLUMN).toString());
		questionAnswer.setChoiceOption1(row.getCell(QuestionAnswerConstants.OPTION_ONE_COLUMN).toString());
		questionAnswer.setChoiceOption2(row.getCell(QuestionAnswerConstants.OPTION_TWO_COLUMN).toString());
		questionAnswer.setAnswer(row.getCell(QuestionAnswerConstants.ANSWER_COLUMN).toString());
		questionAnswer.setQuestionType(questionType);
	}

	/**
	 * Prepare declarative vo.
	 *
	 * @param row the row
	 * @param questionType the question type
	 * @param questionAnswer the question answer
	 */
	public void prepareDeclarativeVo(Row row, int questionType, QuestionAnswer questionAnswer) {
		questionAnswer.setQuestion(row.getCell(QuestionAnswerConstants.QUESTION_COLUMN).toString());
		questionAnswer.setAnswer(row.getCell(QuestionAnswerConstants.ANSWER_COLUMN).toString());
		questionAnswer.setQuestionType(questionType);
	}

	/**
	 * Find question bank group.
	 *
	 * @return the map
	 */
	public Map<String, Set<String>> findQuestionBankGroup() {
		List<QuestionBank> qbList = questionBankRepo.findAll();
		Map<String, Set<String>> qbGroup = qbList.stream().collect(Collectors.groupingBy(QuestionBank ->
		QuestionBank.getTestCategory().getCategory()+"-"+QuestionBank.getTestCategory().getSubCategory(), 
		Collectors.mapping(QuestionBank::getQuestionBankName, Collectors.toSet())));
		return qbGroup;
	}

	/**
	 * Filter and fetch question bank.
	 *
	 * @param qbFilter the qb filter
	 * @return the list
	 */
	public List<QuestionBank> filterAndFetchQuestionBank(QuestionBankFilterVo qbFilter) {
		QuestionBankSpecification qbSpec = new QuestionBankSpecification(qbFilter);
		return questionBankRepo.findAll(qbSpec);
	}

	public List<Map<String,Object>> findQuestionBankCascade() {
		List<Map<String,Object>> selectionList = new ArrayList<>();		
		List<QuestionBank> qbList = questionBankRepo.findAll();		
		Map<Object, Set<QuestionBank>> dataMap = qbList.stream().collect(Collectors.groupingBy(QuestionBank -> Arrays.asList(
				QuestionBank.getTestCategory().getCategory(),QuestionBank.getTestCategory().getSubCategory()), 
				Collectors.mapping(QuestionBank->QuestionBank, Collectors.toSet())));	
		for(Map.Entry<Object, Set<QuestionBank>> entry  : dataMap.entrySet()) {
			List<String> keysList  = (List<String>) entry.getKey(); 
			String category = keysList.get(0);
			String subCategory = keysList.get(1);
			List<Map<String,Object>> questionBankList = entry.getValue().stream()
					.map(data->{
						Map<String,Object> map = new HashMap<>();
						map.put("value", data.getId());
						map.put("label", data.getQuestionBankName());
						return map;
					}).collect(Collectors.toList());
			Map<String,Object> subData = new HashMap<>();
			subData.put("value", subCategory);
			subData.put("label", subCategory);
			subData.put("children", questionBankList);	
			Map<String,Object> resultMap = new HashMap<>();
			resultMap.put("value", category);
			resultMap.put("label", category);	    			
			Map<String, Object> filterMap = selectionList.stream().filter(record -> record.get("label").equals((category)))
					.findAny().orElse(null);
			if(MapUtils.isNotEmpty(filterMap)) {
				selectionList.removeIf(record -> record.get("label").equals((category)));
				List<Map<String,Object>> childrenList = new ArrayList<>();
				childrenList.addAll((List<Map<String,Object>>) filterMap.get("children"));
				childrenList.add(subData);     
				resultMap.put("children", childrenList);
			}else {			
				resultMap.put("children", Arrays.asList(subData));	
			}
			selectionList.add(resultMap);
		}			
		return selectionList;
	}


}