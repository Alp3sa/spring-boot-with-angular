package com.example.demo.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment;

public class FicheroUtils {
    private static Pattern keyPattern = Pattern.compile("([^(]+)\\(([^)]+)\\)");
    private final String ERROR = "Error en FicheroUtils";
    
    protected String horizontalAlignment;
    protected String indentation;
    protected String verticalAlignment;
    protected String fontFamily;
    protected String fontWeight;
    protected String fontSize;
    protected String underlined;
    protected String fontColor;
    protected String borderStyle;
    protected String borderColor;
    protected String backgroundColor;
    
    
    public void initFormatVars(List<String> campo, boolean isBody) {
        int i = 10;
        if (isBody) {
            i = 30;
        }
        horizontalAlignment = campo != null ? campo.get(i++) : null;
        indentation = campo != null ? campo.get(i++) : null;
        verticalAlignment = campo != null ? campo.get(i++) : null;
        fontFamily = campo != null ? campo.get(i++) : null;
        fontWeight = campo != null ? campo.get(i++) : null;
        fontSize = campo != null ? campo.get(i++) : null;
        underlined = campo != null ? campo.get(i++) : null;
        fontColor = campo != null ? campo.get(i++) : null;
        borderStyle = campo != null ? campo.get(i++) : null;
        borderColor = campo != null ? campo.get(i++) : null;
        backgroundColor = campo != null ? campo.get(i) : null;
    }
    
    public void modificaFormatoDeCabeceras(List<String> formato) {
        horizontalAlignment = formato.get(10);
        indentation = formato.get(11);
        verticalAlignment = formato.get(12);
        fontFamily = formato.get(13);
        fontWeight = formato.get(14);
        fontSize = formato.get(15);
        underlined = formato.get(16);
        fontColor = formato.get(17);
        borderStyle = formato.get(18);
        borderColor = formato.get(19);
        backgroundColor = formato.get(20);
    }
    
    public static void prepararDirectorio(String folderTemplate, String root, String template) {
        folderTemplate = folderTemplate.replace("/", "\\\\");
        template = template.replace("/", "\\\\");
        root = root.replace("\\", "\\\\");

        // Crea la carpeta del fichero si no existe
        File folder = new File(root + "\\app\\views\\" + folderTemplate);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // Borra el fichero si existe
        File f = new File(root + "\\app\\views\\" + template);
        if (f.exists()) {
            if(f.delete()) {
                System.out.println("El fichero "+template+" fue borrado correctamente.");
            }
            else {
                System.out.println("No se pudo eliminar  "+template+".");
            }
        }
    }

    public XSSFCellStyle estiloCabeceraPorDefecto(XSSFWorkbook workbook, Font headerFont) {
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER);
        headerCellStyle.setAlignment(org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER);
        return headerCellStyle;
    }

    public XSSFCellStyle modificarEstilo(XSSFWorkbook workbook, String cellValue) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        establecerAlineacion(cellStyle, cellValue);

        // Establecer color de fondo
        if (backgroundColor != null && !"".equals(backgroundColor)) {
            cellStyle.setFillPattern(org.apache.poi.ss.usermodel.CellStyle.SOLID_FOREGROUND);
            int[] c = hex2RgbInt(backgroundColor);
            XSSFColor targetColor = new XSSFColor(new java.awt.Color(c[0], c[1], c[2]));
            cellStyle.setFillForegroundColor(targetColor);
        }

        XSSFFont customFont = crearTipoDeFuente(workbook, fontWeight, fontFamily,
                fontSize, fontColor);

        if (underlined != null) {
            byte customUnderlined = 0;
            try {
                customUnderlined = (byte) Font.class.getDeclaredField("U_"+underlined).get(Font.class);
            } catch (Exception e) {
                System.out.println("Error al establecer subrayado.");
            }
            customFont.setUnderline(customUnderlined);
        }

        cellStyle.setFont(customFont);

        // Establecer borde
        if (borderStyle != null && !borderStyle.isEmpty() && !"none".equals(borderStyle)) {
            try {
                cellStyle
                        .setBorderTop((short) (((STBorderStyle.Enum) STBorderStyle.class
                                .getDeclaredField(borderStyle.toUpperCase())
                                .get(STBorderStyle.class)).intValue() - 1));
                cellStyle
                        .setBorderRight((short) (((STBorderStyle.Enum) STBorderStyle.class
                                .getDeclaredField(borderStyle.toUpperCase())
                                .get(STBorderStyle.class)).intValue() - 1));
                cellStyle
                        .setBorderBottom((short) (((STBorderStyle.Enum) STBorderStyle.class
                                .getDeclaredField(borderStyle.toUpperCase())
                                .get(STBorderStyle.class)).intValue() - 1));
                cellStyle
                        .setBorderLeft((short) (((STBorderStyle.Enum) STBorderStyle.class
                                .getDeclaredField(borderStyle.toUpperCase())
                                .get(STBorderStyle.class)).intValue() - 1));

                if (borderColor != null && !"#000000".equals(borderColor)) {
                    int[] c = hex2RgbInt(borderColor);
                    XSSFColor targetColor = new XSSFColor(new java.awt.Color(c[0], c[1], c[2]));
                    cellStyle.setBorderColor(BorderSide.TOP, targetColor);
                    cellStyle.setBorderColor(BorderSide.RIGHT, targetColor);
                    cellStyle.setBorderColor(BorderSide.BOTTOM, targetColor);
                    cellStyle.setBorderColor(BorderSide.LEFT, targetColor);
                }
            } catch (Exception e) {
                System.out.println("Error al establecer el borde del campo.");
            }
        }

        return cellStyle;
    }
    
    public Cell modificaFormatoDeCampos(int a3, Cell listCell, String cellValue, XSSFWorkbook workbook, List format) {
        try {
            boolean defaultFormatApply = applyDefaultFormat(format, a3);
            XSSFCellStyle listCellStyle = null;
            if (defaultFormatApply) {
                initFormatVars(format,true);
            }
            listCellStyle = modificarEstilo(workbook, cellValue);
            listCell.setCellStyle(listCellStyle);          
        } catch (Exception e) {
            System.out.println(ERROR + ".modificaFormatoDeCampos: ");
            e.printStackTrace();
        }
        return listCell;
    }
    
    public void establecerAlineacion(XSSFCellStyle cellStyle, String cellValue) {
        if("ALIGN_CENTER".equals(horizontalAlignment)) {
            cellStyle.setAlignment(org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER);
        }
        else if ("ALIGN_LEFT".equals(horizontalAlignment)) {
            cellStyle.setAlignment(org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT);
            establecerSangria(cellStyle);
        }
        else if ("ALIGN_RIGHT".equals(horizontalAlignment)) {
            cellStyle.setAlignment(org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT);
            establecerSangria(cellStyle);
        }
        else if ("ALIGN_JUSTIFY".equals(horizontalAlignment)) {
            cellStyle.setAlignment(org.apache.poi.ss.usermodel.CellStyle.ALIGN_JUSTIFY);
        }
        else {
            cellStyle.setAlignment(org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT);
        }
        
        if (verticalAlignment!=null && !"TOP".equals(verticalAlignment)) {
            try {
                cellStyle.setVerticalAlignment((short) (((STVerticalAlignment.Enum) STVerticalAlignment.class.getDeclaredField(verticalAlignment.toUpperCase()).get(STVerticalAlignment.class)).intValue()-1));
            } catch (Exception e) {
                System.out.println("Error al establecer alineación vertical.");
            }
        }
        else if (cellValue!=null && cellValue.length() > 40) {
            cellStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.CellStyle.VERTICAL_TOP);
        } else {
            cellStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER);
        }
    }
    
    public void establecerSangria(XSSFCellStyle cellStyle) {
        if (indentation!=null) {
            cellStyle.setIndention(Short.parseShort(indentation));
        }
    }

    public XSSFFont crearTipoDeFuente(XSSFWorkbook workbook, String fontWeight, String fontFamily,
            String fontSize, String fontColor) {
        XSSFFont customFont = workbook.createFont();

        // Establecer el peso de la fuente
        if (fontWeight != null && !"".equals(fontWeight)) {
            if (fontWeight.contains("bold")) {
                customFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            }
            if (fontWeight.contains("italic")) {
                customFont.setItalic(true);
            }
        }

        // Establece la familia de la fuente
        if (fontFamily != null && !"".equals(fontFamily)) {
            customFont.setFontName(fontFamily);
        } else {
            customFont.setFontName("Serif");
        }

        // Establece el tamaño de la fuente
        if (fontSize != null) {
            try {
                customFont.setFontHeightInPoints(Short.valueOf(fontSize));
            } catch (Exception e) {
                customFont.setFontHeightInPoints((short) 11);
            }
        }

        if (fontColor != null) {
            int[] c = hex2RgbInt(fontColor);
            XSSFColor targetColor = new XSSFColor(new java.awt.Color(c[0], c[1], c[2]));
            customFont.setColor(targetColor);
        }

        return customFont;
    }

    public int[] hex2RgbInt(String colorStr) {
        int[] val = new int[3];
        val[0] = Integer.parseInt(colorStr.substring(1, 3), 16);
        val[1] = Integer.parseInt(colorStr.substring(3, 5), 16);
        val[2] = Integer.parseInt(colorStr.substring(5, 7), 16);
        return val;
    }
    
    public void resizeColumnsTitleBased(int sheetIndex, int titleIndex, List<Sheet> listSheets) {
        listSheets.get(sheetIndex).autoSizeColumn(titleIndex);
        if (listSheets.get(sheetIndex).getColumnWidth(titleIndex) < 4000) {
            listSheets.get(sheetIndex).setColumnWidth(titleIndex, 4000);
        }
    }
    
    public List<String> ordenaAlfabeticamenteColumnasDeListas(Object[] listOfFields) {
        List<String> listOfFieldsIndex = new ArrayList();
        for (int a4 = 0; a4 < listOfFields.length; a4++) {
            listOfFieldsIndex.add(listOfFields[a4].toString());
        }
        Collections.sort(listOfFieldsIndex);
        return listOfFieldsIndex;
    }
    
    public String comprobarYaml(String enumValue, Object o) {
        LinkedHashMap<Object, Map<?, ?>> objects = (LinkedHashMap<Object, Map<?, ?>>) o;
        for (Map.Entry<Object, Map<?, ?>> entry : objects.entrySet()) {
            Matcher matcher = keyPattern.matcher(entry.getKey().toString().trim());
            if (matcher.matches() && "TableKeyValue".equals(matcher.group(1))) {
                String id = matcher.group(2);
                if (id.substring(id.lastIndexOf('-') + 1, id.length()).equals(enumValue)) {
                    Map<String, Object> m = (Map) entry.getValue();
                    return m.get("value").toString();
                }
            }
        }
        return null;
    }
    
    public static boolean applyDefaultFormat(List format, int sheetIndex) {
        return Boolean.TRUE.equals((Boolean) ((List) ((List) format.get(0)).get(sheetIndex)).get(1));
    }
}