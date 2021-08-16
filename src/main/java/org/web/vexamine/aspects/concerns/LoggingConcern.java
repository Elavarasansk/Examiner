package org.web.vexamine.aspects.concerns;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class LoggingConcern.
 *
 * @author vairavanu
 */
@Aspect
@Component("LoggingConcern")
@Slf4j
public class LoggingConcern {

	/**
	 * Log controller calls.
	 */
	@Pointcut("execution(* org.web.vexamine.*.*.*(..))")
	private void logControllerCalls() {}


	/**
	 * Respository calls.
	 */
	@Pointcut("execution(* org.web.vexamine.dao.repository.*.*(..))")
	private void respositoryCalls() {}

	/**
	 * Respo method initiation.
	 *
	 * @param jp the jp
	 */
	@Before("respositoryCalls()")
	public void respoMethodInitiation(JoinPoint jp) {
		String className = jp.getSignature().getDeclaringTypeName();
		String methodName = jp.getSignature().getName();
		Object[] arguments = jp.getArgs();
		log.info(String.format("Repository Call initated on-----> %s::%s Parameters {%s} <----------", className, methodName, arguments.length>0? arguments[0]:""));
	}
	
	/**
	 * Log method initiation.
	 *
	 * @param jp the jp
	 */
	@Before("logControllerCalls()")
	public void logMethodInitiation(JoinPoint jp) {
		String className = jp.getSignature().getDeclaringTypeName();
		String methodName = jp.getSignature().getName();
		log.info(String.format("Method Execution Started on -----> %s::%s <----------", className, methodName));
	}
	
	/**
	 * Log method error.
	 *
	 * @param jp the jp
	 */
	@AfterThrowing("logControllerCalls()")
	public void logMethodError(JoinPoint jp) {
		String className = jp.getSignature().getDeclaringTypeName();
		String methodName = jp.getSignature().getName();
		Object[] arguments = jp.getArgs();
		log.error(String.format("Exception Error Occured on-----> %s::%s Parameters {%s} <----------", className, methodName, arguments.length>0? arguments[0]:""));
	}
	
	/**
	 * Log method completion.
	 *
	 * @param jp the jp
	 */
	@AfterReturning("logControllerCalls()")
	public void logMethodCompletion(JoinPoint jp) {
		String className = jp.getSignature().getDeclaringTypeName();
		String methodName = jp.getSignature().getName();
		log.info(String.format("Method Execution Successfull on -----> %s::%s <----------", className, methodName));
	}
	
}
