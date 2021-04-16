package com.tansun.ider.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 文件操作
 * 
 * @author admin
 * @version 1.0
 */
public class CSVUtils {

	/**
	 * 生成为CVS文件
	 * 
	 * @param exportData
	 *            源数据List
	 * @param map
	 *            csv文件的列表头map
	 * @param outPutPath
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath, String fileName) {
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		BufferedReader br = null;
		try {
			File file = new File(outPutPath);
			if (!file.exists()) {
				file.mkdir();
			}
			// 定义文件名格式并创建
			// csvFile = new File(outPutPath+fileName+".csv");
			// csvFile = File.createTempFile(fileName, ".csv", new
			// File(outPutPath));
			csvFile = new File(outPutPath + fileName + ".csv");
			if (!csvFile.exists()) {
				csvFile.createNewFile();
				System.out.println("csvFile：" + csvFile);
				// UTF-8使正确读取分隔符","
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"),
						1024);
				System.out.println("csvFileOutputStream：" + csvFileOutputStream);
				// 写入文件头部
				for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
					java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
					csvFileOutputStream.write((String) propertyEntry.getValue() != null
							? new String(((String) propertyEntry.getValue()).getBytes("GBK"), "GBK") : "");
					if (propertyIterator.hasNext()) {
						csvFileOutputStream.write(",");
					}
					System.out.println(new String(((String) propertyEntry.getValue()).getBytes("GBK"), "GBK"));
				}
				csvFileOutputStream.write("\r\n");
				// 写入文件内容
				for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
					Object row = (Object) iterator.next();
					for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
						java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
						csvFileOutputStream.write((String) BeanUtils.getProperty(row,
								((String) propertyEntry.getKey()) != null ? (String) propertyEntry.getKey() : ""));
						if (propertyIterator.hasNext()) {
							csvFileOutputStream.write(",");
						}
					}
					if (iterator.hasNext()) {
						csvFileOutputStream.write("\r\n");
					}
				}
				csvFileOutputStream.flush();
			} else {
				// br = new BufferedReader(new FileReader(csvFile));
				// String line = "";
				// String everyLine = "";
				// List<String> allString = new ArrayList<>();
				// while ((line = br.readLine()) != null) // 读取到的内容给line变量
				// {
				// everyLine = line;
				// System.out.println(everyLine);
				// allString.add(everyLine);
				// }
				// System.out.println("csv表格中所有行数：" + allString.size());
				// csvFileOutputStream = new BufferedWriter(new
				// OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"),
				// 1024);
				// csvFileOutputStream.newLine();
				csvFileOutputStream = new BufferedWriter(new FileWriter(csvFile, true)); // 附加
				// 添加新的数据行
				csvFileOutputStream.write("\r\n");
				for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
					Object row = (Object) iterator.next();
					for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
						java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
						csvFileOutputStream.write((String) BeanUtils.getProperty(row,
								((String) propertyEntry.getKey()) != null ? (String) propertyEntry.getKey() : ""));
						if (propertyIterator.hasNext()) {
							csvFileOutputStream.write(",");
						}
					}
					if (iterator.hasNext()) {
						csvFileOutputStream.write("\r\n");
					}
				}
				csvFileOutputStream.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
			    if(csvFileOutputStream != null){
			        csvFileOutputStream.close();
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return csvFile;
	}

	/**
	 * 下载文件
	 * 
	 * @param response
	 * @param csvFilePath
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @throws IOException
	 */
	public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName)
			throws IOException {
		response.setContentType("application/csv;charset=GBK");
		response.setHeader("Content-Disposition",
				"attachment;  filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
		// URLEncoder.encode(fileName, "GBK")

		InputStream in = null;
		try {
			in = new FileInputStream(csvFilePath);
			int len = 0;
			byte[] buffer = new byte[1024];
			response.setCharacterEncoding("GBK");
			OutputStream out = response.getOutputStream();
			while ((len = in.read(buffer)) > 0) {
				// out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
				// });
				out.write(buffer, 0, len);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}


	/**
	 * 测试数据
	 * 
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		List exportData = new ArrayList<Map>();
		Map row1 = new LinkedHashMap<String, String>();
		row1.put("1", "11");
		row1.put("2", "12");
		row1.put("3", "13");
		row1.put("4", "14");
		exportData.add(row1);
		row1 = new LinkedHashMap<String, String>();
		row1.put("1", "21");
		row1.put("2", "22");
		row1.put("3", "23");
		row1.put("4", "24");
		exportData.add(row1);
		LinkedHashMap map = new LinkedHashMap();
		map.put("1", "第一列");
		map.put("2", "第二列");
		map.put("3", "第三列");
		map.put("4", "第四列");

		String path = "c:/export/";
		String fileName = "文件导出";
		File file = CSVUtils.createCSVFile(exportData, map, path, fileName);
		String fileName2 = file.getName();
		System.out.println("文件名称：" + fileName2);
	}
}
