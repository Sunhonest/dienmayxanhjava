package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelExporter {
    
    public static void exportTableToExcel(JTable table, String title, String fileName) {
        exportTableToExcel(table, title, fileName, null);
    }
    
    public static void exportTableToExcel(JTable table, String title, String fileName, String[] summaryInfo) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String defaultFileName = fileName + "_" + timestamp + ".xlsx";
            fileChooser.setSelectedFile(new java.io.File(defaultFileName));
            
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
            fileChooser.setFileFilter(filter);
            
            int userSelection = fileChooser.showSaveDialog(null);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                    fileToSave = new java.io.File(filePath);
                }
                
                exportToExcel(table, title, fileToSave, summaryInfo);
                
                JOptionPane.showMessageDialog(null, 
                    "Xuất Excel thành công!\nFile đã được lưu tại: " + fileToSave.getAbsolutePath(), 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi xuất Excel: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void exportToExcel(JTable table, String title, java.io.File file, String[] summaryInfo) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle numberStyle = createNumberStyle(workbook);
        CellStyle summaryStyle = createSummaryStyle(workbook);
        
        int currentRow = 0;
        
        Row titleRow = sheet.createRow(currentRow++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title.toUpperCase());
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, table.getColumnCount() - 1));
        titleRow.setHeight((short) (25 * 20));
        
        currentRow++;
        Row dateRow = sheet.createRow(currentRow++);
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        dateCell.setCellStyle(dataStyle);
        
        currentRow++;
        
        Row headerRow = sheet.createRow(currentRow++);
        headerRow.setHeight((short) (20 * 20));
        for (int i = 0; i < table.getColumnCount(); i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(table.getColumnName(i));
            headerCell.setCellStyle(headerStyle);
        }
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            Row dataRow = sheet.createRow(currentRow++);
            dataRow.setHeight((short) (18 * 20));
            for (int j = 0; j < model.getColumnCount(); j++) {
                Cell dataCell = dataRow.createCell(j);
                Object value = model.getValueAt(i, j);
                
                if (value != null) {
                    String strValue = value.toString();
                    if (isNumber(strValue)) {
                        try {
                            double numValue = parseNumber(strValue);
                            dataCell.setCellValue(numValue);
                            dataCell.setCellStyle(numberStyle);
                        } catch (Exception e) {
                            dataCell.setCellValue(strValue);
                            dataCell.setCellStyle(dataStyle);
                        }
                    } else {
                        dataCell.setCellValue(strValue);
                        dataCell.setCellStyle(dataStyle);
                    }
                } else {
                    dataCell.setCellValue("");
                    dataCell.setCellStyle(dataStyle);
                }
            }
        }
        
        if (summaryInfo != null && summaryInfo.length > 0) {
            currentRow++;
            
            Row summaryTitleRow = sheet.createRow(currentRow++);
            Cell summaryTitleCell = summaryTitleRow.createCell(0);
            summaryTitleCell.setCellValue("THÔNG TIN TỔNG KẾT");
            summaryTitleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 0, table.getColumnCount() - 1));
            
            for (String info : summaryInfo) {
                if (info != null && !info.trim().isEmpty()) {
                    Row summaryRow = sheet.createRow(currentRow++);
                    Cell summaryCell = summaryRow.createCell(0);
                    summaryCell.setCellValue(info);
                    summaryCell.setCellStyle(summaryStyle);
                    sheet.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 0, table.getColumnCount() - 1));
                }
            }
        }
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) < 2500) {
                sheet.setColumnWidth(i, 2500);
            }
            if (sheet.getColumnWidth(i) > 8000) {
                sheet.setColumnWidth(i, 8000);
            }
        }
        
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
        
        workbook.close();
    }
    
    private static boolean isNumber(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        String cleanStr = str.replaceAll("[,\\s]", "");
        try {
            Double.parseDouble(cleanStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static double parseNumber(String str) {
        return Double.parseDouble(str.replaceAll("[,\\s]", ""));
    }
    
    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorders(style);
        return style;
    }
    
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorders(style);
        return style;
    }
    
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(style);
        return style;
    }
    
    private static CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("#,##0"));
        setBorders(style);
        return style;
    }
    
    private static CellStyle createSummaryStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.DARK_GREEN.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorders(style);
        return style;
    }
    
    private static void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    }
}