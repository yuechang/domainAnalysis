/*
 * Copyright (c) 2016, 2025, HHLY and/or its affiliates. All rights reserved.
 * HHLY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package com.yc.util;

/**
 * @ClassName: ExcelUtils
 * @Description: Excel工具类
 * @author Yue Chang
 * @date 2018年4月22日 下午10:34:43
 * @since 1.0
 */
import com.yc.entity.ModuleEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @ClassName: ExcelUtils
 * @Description: 写入Excel文件的方法（写表头，写数据）
 * @author Yue Chang
 * @date 2018年4月22日 下午11:15:40
 * @since 1.0
 */
public class ExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 将List集合数据写入excel（单个sheet）,拼加模式
     * @param filePath 文件路径
     * @param excelTitle 文件表头
     * @param moduleEntityList 要写入的数据集合
     * @param sheetName sheet名称
     */
    public static void writeModuleEntityListToExcel(String filePath, String[] excelTitle,
                                                    List<ModuleEntity> moduleEntityList,
                                                    String sheetName) throws IOException, InvalidFormatException {
        if(CollectionUtils.isEmpty(moduleEntityList))
            return;

        Workbook workbook = null;
        // 标识位，用于标识sheet的行号
        int rowIndex = 0;
        File file = new File(filePath);
        // 如果此文件已存在，则读取文件来创建workbook
        if (file.exists()){
            workbook = WorkbookFactory.create(new FileInputStream(file));
            //
            rowIndex = workbook.getSheetAt(0).getLastRowNum();
        }

        if (workbook == null){
            if (filePath.toLowerCase().endsWith("xls") || filePath.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook();
            } else{
                logger.debug("invalid file name,should be xls or xlsx");
                return;
            }
        }

        // 如果为首次写入，创建sheet和表头
        if(rowIndex == 0){
            Sheet sheet = workbook.createSheet(sheetName);
            //写表头数据
            Row titleRow = sheet.createRow(rowIndex);
            for (int i = 0; i < excelTitle.length; i++) {
                //创建表头单元格,填值
                titleRow.createCell(i).setCellValue(excelTitle[i]);
            }
        }

        //遍历数据集，将其写入excel中
        try{
            rowIndex++;
            //循环写入主表数据
            Iterator<ModuleEntity> moduleEntityIter = moduleEntityList.iterator();
            while(moduleEntityIter.hasNext()){
                ModuleEntity moduleEntity = moduleEntityIter.next();
                // 获得sheet
                Sheet sheet = workbook.getSheetAt(0);
                Row row = sheet.createRow(rowIndex);
                //create sheet coluum(单元格)
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(moduleEntity.getName());
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(moduleEntity.getAvail());
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(moduleEntity.getTld());
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(moduleEntity.getPrice());
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(moduleEntity.getPreRegisterPrice());
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(moduleEntity.getType());
                rowIndex++;
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            //logger.info(filePath + "写入文件成功>>>>>>>>>>>");
        } catch (IOException e) {
            logger.info("写入文件失败，",e);
        }
    }

    /**
     * 读取Excel2003的主表数据 （单个sheet）
     * @param filePath
     * @return
     */
    @SuppressWarnings("unused")
    private static List<ModuleEntity> readFromXLS2003(String filePath) {
        File excelFile = null;// Excel文件对象
        InputStream is = null;// 输入流对象
        String cellStr = null;// 单元格，最终按字符串处理
        List<ModuleEntity> moduleEntityList = new ArrayList<>();// 返回封装数据的List
        ModuleEntity moduleEntity = null;// 每一个雇员信息对象
        try {
            excelFile = new File(filePath);
            is = new FileInputStream(excelFile);// 获取文件输入流
            HSSFWorkbook workbook2003 = new HSSFWorkbook(is);// 创建Excel2003文件对象
            HSSFSheet sheet = workbook2003.getSheetAt(0);// 取出第一个工作表，索引是0
            // 开始循环遍历行，表头不处理，从1开始
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow(i);// 获取行对象
                moduleEntity = new ModuleEntity();// 实例化Student对象
                if (row == null) {// 如果为空，不处理
                    continue;
                }
                // 循环遍历单元格
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    HSSFCell cell = row.getCell(j);// 获取单元格对象
                    if (cell == null) {// 单元格为空设置cellStr为空串
                        cellStr = "";
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {// 对布尔值的处理
                        cellStr = String.valueOf(cell.getBooleanCellValue());
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {// 对数字值的处理
                        cellStr = cell.getNumericCellValue() + "";
                    } else {// 其余按照字符串处理
                        cellStr = cell.getStringCellValue();
                    }
                    // 下面按照数据出现位置封装到bean中
                    if (j == 0) {
                        moduleEntity.setName(cellStr);
                    } else if (j == 1) {
                        int avail = cellStr == null ? 1 : Integer.valueOf(cellStr);
                        moduleEntity.setAvail(avail);
                    } else if (j == 2) {
                        moduleEntity.setTld(cellStr);
                    } else if (j == 3) {
                        double price = cellStr == null ? 0.0 : Double.valueOf(cellStr);
                        moduleEntity.setPrice(price);
                    } else if(j == 4){
                        double preRegisterPrice = cellStr == null ? 0.0 : Double.valueOf(cellStr);
                        moduleEntity.setPreRegisterPrice(preRegisterPrice);
                    }else {
                        moduleEntity.setType(cellStr);
                    }
                }
                moduleEntityList.add(moduleEntity);// 数据装入List
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {// 关闭文件流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return moduleEntityList;
    }

    /**
     * 读取Excel2003的表头
     * @param filePath 需要读取的文件路径
     * @return
     */
    public static String[] readHeaderFromXLS2003(String filePath){
        String[] excelTitle = null;
        FileInputStream is = null;
        try{
            File excelFile = new File(filePath);
            is = new FileInputStream(excelFile);
            HSSFWorkbook workbook2003 = new HSSFWorkbook(is);
            //循环读取工作表
            for (int i = 0; i < workbook2003.getNumberOfSheets(); i++) {
                HSSFSheet hssfSheet = workbook2003.getSheetAt(i);
                //*************获取表头是start*************
                HSSFRow sheetRow = hssfSheet.getRow(i);
                excelTitle = new String[sheetRow.getLastCellNum()];
                for (int k = 0; k < sheetRow.getLastCellNum(); k++) {
                    HSSFCell hssfCell = sheetRow.getCell(k);
                    excelTitle[k] = hssfCell.getStringCellValue();
//		            	System.out.println(excelTitle[k] + " ");
                }
                //*************获取表头end*************
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {// 关闭文件流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return excelTitle;
    }

    //主函数
    public static void main(String[] args) throws IOException, InvalidFormatException {
		/*
		List<ModuleEntity> moduleEntityList = readFromXLS2003("D:\\employee.xls");
		String[] excelTitle = readHeaderFromXLS2003("D:\\employee.xls");
		*/

        List<ModuleEntity> moduleEntityList = new ArrayList<>();
        ModuleEntity moduleEntity = new ModuleEntity();
        moduleEntity.setAvail(1);
        moduleEntity.setName("a6c.net");
        moduleEntity.setTld("net");
        moduleEntityList.add(moduleEntity);

        writeModuleEntityListToExcel("D:\\moduleEntityList.xlsx",
                ModuleEntity.ATTRIBUTES_TITLE,moduleEntityList,ModuleEntity.DOMAIN_INFO);
    }
}