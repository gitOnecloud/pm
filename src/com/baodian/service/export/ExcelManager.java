package com.baodian.service.export;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Service;

@Service("excelManager")
public class ExcelManager {
	
	public int HEAD = 0, LEFT = 0;//表头
	public int CELL = 1, CENTER = 1;//内容
	public int RIGHT = 2;
	
	private HSSFCellStyle createCellStyle(HSSFWorkbook workbook, int type, int align) {
		HSSFCellStyle cellstyle = workbook.createCellStyle();
		switch(type) {
			case 0:
				HSSFFont font = workbook.createFont();
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);//粗体显示
				cellstyle.setFont(font);//设置字体
				cellstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);//填充格式
				cellstyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);//填充颜色
				break;
		}
		cellstyle.setBorderBottom(CellStyle.BORDER_THIN);//下边框
		cellstyle.setBorderLeft(CellStyle.BORDER_THIN);//左边框
		cellstyle.setBorderRight(CellStyle.BORDER_THIN);//右边框
		cellstyle.setBorderTop(CellStyle.BORDER_THIN);//上边框
		cellstyle.setAlignment(CellStyle.ALIGN_CENTER);//左右居中
		switch(align) {
			case 0:
				cellstyle.setAlignment(CellStyle.ALIGN_LEFT);//左对齐
				break;
			case 1:
				cellstyle.setAlignment(CellStyle.ALIGN_CENTER);//居中对齐
				break;
			case 2:
				cellstyle.setAlignment(CellStyle.ALIGN_RIGHT);//右对齐
				break;
		
		}
		cellstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//上下居中
		return cellstyle;
	}
	/**
	 * 生成excel
	 * @param rowHeight 行高，主要是表头高和单元格高  单位为20倍磅，即1磅=20
	 * @param columnWidth 列宽 单位为250~280倍字符
	 * @param head 表头
	 * @param headType 表头样式
	 * @param cells 内容
	 * @param cellType 内容样式
	 */
	public InputStream createExcel(short[] rowHeight, int[] columnWidth,
			String[] head, int[] headType, List<String[]> cells, int[] cellType) throws IOException {
		
		HSSFWorkbook workbook = new HSSFWorkbook();//创建工作表
		HSSFSheet sheet = workbook.createSheet();//创建表格
		for(int i=0; i<columnWidth.length; i++) {
			sheet.setColumnWidth(i, columnWidth[i]);//设置列宽
		}
		Map<Integer, HSSFCellStyle> cellStyleMap = new HashMap<Integer, HSSFCellStyle>();//表格样式
		for(int i : headType) {//标题样式
			if(! cellStyleMap.containsKey(HEAD*100 + i)) {
				cellStyleMap.put(HEAD*100 + i, createCellStyle(workbook, HEAD, i));
			}
		}
		for(int i : cellType) {//内容样式
			if(! cellStyleMap.containsKey(CELL*100 + i)) {
				cellStyleMap.put(CELL*100 + i, createCellStyle(workbook, CELL, i));
			}
		}
		
		//输出表头
		HSSFRow row = sheet.createRow(0);//创建第一行
		row.setHeight(rowHeight[0]);//设置行高
		HSSFCell cell;//单元格
		for(int i=0; i<head.length; i++) {
			cell = row.createCell(i);//创建单元格
			cell.setCellValue(head[i]);//设置值
			cell.setCellStyle(cellStyleMap.get(HEAD*100 + headType[i]));//设置样式
		}
		//输出内容
		for(String[] column : cells) {
			row = sheet.createRow(sheet.getLastRowNum()+1);//创建行
			row.setHeight(rowHeight[1]);//设置行高
			for(int i=0; i<column.length; i++) {
				cell = row.createCell(i);//创建单元格
				cell.setCellValue(column[i]);//设置值
				cell.setCellStyle(cellStyleMap.get(CELL*100 + cellType[i]));//设置样式
			}
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		workbook.write(output);
		
		byte[] ba = output.toByteArray();
		InputStream excelStream = new ByteArrayInputStream(ba);
		output.flush();
		output.close();
		return excelStream;
	}

}
