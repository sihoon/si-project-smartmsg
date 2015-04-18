package com.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ApachePOI36 {

	String sheetName = null;
	int maxCountColumn = 0;
	int limitColumn = 0;
	int limitRow = 0;
	boolean bLimitRow = false;
	int lastRow = 0;
	boolean bLimitColumn = false;
	String[] arrTitle = null;

	public ApachePOI36() {

		sheetName = null;
		maxCountColumn = 0;
		limitColumn = 50;
		limitRow = 500000;
	}

	public void setSheetName(String str) {
		this.sheetName = str;
	}

	public void setlimitColumn(int i) {
		this.limitColumn = i;
	}

	public void setlimitRow(int i) {
		this.limitRow = i;
	}

	public String getSheetName() {
		return this.sheetName;
	}

	public int getlimitColumn() {
		return this.limitColumn;
	}

	public int getlimitRow() {
		return this.limitRow;
	}

	public boolean getbLimitColumn() {
		return this.bLimitColumn;
	}

	public boolean getbLimitRow() {
		return this.bLimitRow;
	}

	public void setTitle(String[] arr) {
		this.arrTitle = arr;
	}

	public int getmaxCountColumn() {
		return this.maxCountColumn;
	}

	public String[][] Read(String filePath) {

		InputStream fin = null;
		Workbook workbook = null;
		try {
			fin = new FileInputStream(filePath);
			workbook = WorkbookFactory.create(fin);
		} catch (IOException ie) {
			System.out.println(ie);
		} catch (InvalidFormatException ins) {
			System.out.println(ins);
		} catch (Exception e) {
			System.out.println(e);
		}

		Sheet sheet = null;
		int countColumn = 0;

		if (sheetName == null)
			sheet = workbook.getSheetAt(0);
		else
			sheet = workbook.getSheet(sheetName);

		lastRow = sheet.getLastRowNum();

		if (lastRow > limitRow) {
			lastRow = limitRow - 1;
			bLimitRow = true;
		}

		String[][] result = new String[lastRow + 1][];

		int countReadColumn = 0;
		DecimalFormat df = new DecimalFormat("#");

		for (int i = 0; i <= lastRow; i++) {

			countReadColumn = 0;
			Row row = sheet.getRow(i);

			if (row == null) {
				result[i] = new String[0];
				continue;
			}
			countColumn = row.getLastCellNum();
			if (countColumn < 0)
				countColumn = 0;
			if (countColumn > limitColumn) {
				countColumn = limitColumn - 1;
				bLimitColumn = true;
			}
			result[i] = new String[countColumn];

			// for (int j = row.getFirstCellNum(); j < countColumn; j++) {
			for (int j = 0; j < countColumn; j++) {
				countReadColumn++;

				Cell cell = row.getCell(j);

				if (cell == null)
					continue;

				int cellType = cell.getCellType();

				switch (cellType) {
				case Cell.CELL_TYPE_NUMERIC: // 0
					double d = cell.getNumericCellValue();
					if (DateUtil.isCellDateFormatted(cell)) {
						// format in form of YYYYMMDD
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyyMMdd", java.util.Locale.KOREA);
						String cellText = formatter.format(DateUtil
								.getJavaDate(d));
						result[i][j] = cellText;
					} else {
						result[i][j] = df.format(d);
					}

					break;
				case Cell.CELL_TYPE_STRING: // 1
					result[i][j] = cell.getRichStringCellValue().getString();
					break;
				case Cell.CELL_TYPE_FORMULA: // 2
					result[i][j] = cell.getCellFormula();
					break;
				case Cell.CELL_TYPE_BLANK: // 3
					result[i][j] = "";
					break;
				case Cell.CELL_TYPE_BOOLEAN: // 4
					result[i][j] = String.valueOf(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_ERROR: // 5
					result[i][j] = String.valueOf(cell.getErrorCellValue());
					break;
				default:
					result[i][j] = "";
					break;
				}
			}
			if (countColumn > this.maxCountColumn)
				this.maxCountColumn = countReadColumn;
		}
		return result;

	}
}
