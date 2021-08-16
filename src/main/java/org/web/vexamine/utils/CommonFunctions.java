package org.web.vexamine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;

/**
 * The Class CommonFunctions.
 */
public class CommonFunctions {

	/**
	 * Random generate.
	 *
	 * @param min the min
	 * @param max the max
	 * @return the long
	 */
	public static long randomGenerate(int min,int max)
	{
		return min + (long)(Math.random() * ((max - min) + 1));
	}

	/**
	 * Gets the random list.
	 *
	 * @param length the length
	 * @return the random list
	 */
	public static Set<Long> getRandomList(int length){
		Random rd = new Random();
		Set<Long> randomList = new HashSet<>();
		while(randomList.size() != length) {
			randomList.add((long)rd.nextInt(length));
		}
		return randomList;
	}

	/**
	 * Gets the random list.
	 *
	 * @param count the count
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param qaIds the qa ids
	 * @return the random list
	 */
	public static Set<Long> getRandomList(int count, int lowerBound, int upperBound, List<Long> qaIds){
		Set<Long> randomList = new HashSet<>();
		while(randomList.size() != count) {
			int rand = (int)randomGenerate(lowerBound, upperBound);
			if(qaIds.contains((long)rand)) {
				randomList.add((long)rand);
			}
		}
		return randomList;
	}
	
	/**
	 * File downloader.
	 *
	 * @param response the response
	 * @param inputStream the resource file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void fileDownloader(HttpServletResponse response, InputStream inputStream) throws IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=");
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
		inputStream.close();
	}
}
