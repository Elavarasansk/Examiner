package org.web.vexamine.utils;

import java.awt.Color;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.PieDataset;
import org.springframework.core.io.Resource;
import org.web.vexamine.constants.VexamineTestConstants;

import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * The Class CustomizePieChart.
 */
//Specify the package name for customizerClass property in the report.jrxml file.
public class CustomizePieChart extends JRAbstractChartCustomizer  {

	/**
	 * Gets the jasper report.
	 *
	 * @return the jasper report
	 * @throws Exception the exception
	 */
	public JasperReport getJasperReport(Resource mainReportJrxml) throws Exception {
			//InputStream in  = this.getClass().getClassLoader().getResourceAsStream(VexamineTestConstants.MAIN_REPORT_JRXML);
			JasperReport jasperReport  = JasperCompileManager.compileReport(JRXmlLoader.load(mainReportJrxml.getInputStream()));
			//in.close();
			return jasperReport;		
	}

	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		// Check the type of plot
		Plot plot = chart.getPlot();
		if (plot instanceof PiePlot) {
			PiePlot piePlot = (PiePlot) plot;   
			Map<String, Color> colorMap = new HashMap<>();
			colorMap.put(VexamineTestConstants.RIGHT_ANSWER,Color.GREEN);
			colorMap.put(VexamineTestConstants.WRONG_ANSWER, Color.RED);     
			PieDataset dataset = piePlot.getDataset();   
			//Assign color to each section of pie chart based on value of key
			for (int i = 0; i < dataset.getItemCount(); i++) {				
				setSectionColor(piePlot, dataset.getKey(i),VexamineTestConstants.RIGHT_ANSWER,colorMap);
				setSectionColor(piePlot, dataset.getKey(i),VexamineTestConstants.WRONG_ANSWER,colorMap);
			}

		}

	}

	/**
	 * Sets the section color.
	 *
	 * @param piePlot the pie plot
	 * @param key the key
	 * @param parterName the parter name
	 * @param colorMap the color map
	 */
	private void setSectionColor(PiePlot piePlot, Comparable key, String parterName, Map<String, Color> colorMap) {			
		if(key.toString().contains(parterName)) {
			piePlot.setSectionPaint(key, colorMap.get(parterName));
		}  					
	}
}
