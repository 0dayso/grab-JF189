package gxx;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 基础工具类
 * Create by Gxx
 * Time: 2013-10-09 00:46
 */
public class BaseUtils implements BaseInterface
{
    /**
     * 日志文件输出
     */
    public static Logger logger = Logger.getLogger(BaseUtils.class);

    /**
     * 是否有数正在抓取中，如果有请稍后再试
     */
    public static boolean isGrabIng = false;

    /**
     * 初始化界面风格(当前系统的风格)
     */
    public static void initLookAndFeel()
    {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验Excel是否正确
     * @param path
     */
    public static void checkExcel(String path, GrabSN189 grabSN189) throws Exception
    {
        loggerOut(grabSN189, "载入Excel路径:[" + path + "]");
        HSSFWorkbook workbook;
        HSSFSheet sheet;

        // 1 装载文件看是否有异常
        try {
            workbook = new HSSFWorkbook(new FileInputStream(path));
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
            loggerOut(grabSN189, "装载文件异常");
            throw new RuntimeException("装载文件异常：" + path);
        }

        // 2 判是否有数据
        int totalRowNum = sheet.getLastRowNum();
        loggerOut(grabSN189, "文件行数=" + (totalRowNum+1) + "(包括抬头)");
        if(totalRowNum <= 0)
        {
            throw new RuntimeException("该文件无数据");
        }

        // 3 校验第一行数据是否一致
        HSSFRow row = sheet.getRow(0);
        int totalCellNum = row.getLastCellNum();
        loggerOut(grabSN189, "文件列树=" + totalCellNum);
        if(EXCEL_TITLE.length != totalCellNum)//比较个数
        {
            alertWarnMessage("该文件与模板不一致，请重新载入文件！");
            return;
        }

        int index = 0;
        while (index < EXCEL_TITLE.length)//比较是否一致
        {
            try{
                if(!EXCEL_TITLE[index].equals(getHSSFCellValue(row.getCell(index++))))
                {
                    alertWarnMessage("该文件与模板不一致，请重新载入文件！");
                    return;
                }
            } catch (Exception e)
            {
                alertWarnMessage("该文件与模板不一致，请重新载入文件！");
                return;
            }
        }

        /**
         * 4 读取数据到table中
         */
        readExcel2Table(sheet,grabSN189);

        /**
         * 5 逐行数据校验 todo
         */
        for(int i=0;i<grabSN189.inputDtoList.size();i++)
        {
            InputDto dto = (InputDto)grabSN189.inputDtoList.get(i);
            if(dto.isValidate)
            {
                if((null == dto.shouJiHaoMa) || "".equals(dto.shouJiHaoMa) ||
                        (null == dto.shouJiMiMa) || "".equals(dto.shouJiMiMa))
                {
                    dto.isValidate = false;
                    dto.errorMsg = "手机号和手机密码不能为空！";
                } else
                {
                    dto.isValidate = true;
                }
            }

            if(dto.isValidate)
            {
                int count = 0;
                if(StringUtils.isNotBlank(dto.getUnConditionNum()))
                {
                    count ++;
                }
                if(StringUtils.isNotBlank(dto.getUnAnswerNum()))
                {
                    count ++;
                }
                if(StringUtils.isNotBlank(dto.getCallBusyNum()))
                {
                    count ++;
                }
                if(count > 1)
                {
                    dto.isValidate = false;
                    dto.errorMsg = "呼叫转移号码不能设置大于1个！";
                }
            }
            // 得到DefaultTableModel对象
            DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

            // 判入参是否校验通过
            if(!dto.isValidate)
            {
                tableModel.setValueAt(dto.errorMsg, i+1, EXCEL_TITLE.length-1);
            }
        }
    }

    /**
     * 读取数据到table中
     * @param sheet
     * @param grabSN189
     */
    private static void readExcel2Table(HSSFSheet sheet, GrabSN189 grabSN189)
    {
        // 0 初始化inputDtoList
        grabSN189.inputDtoList = new ArrayList();

        // 1 清空所有行 并 初始化头一行
        clearAllAndInitTitle(grabSN189.table);

        // 2 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

        // 2 逐行新增+修改table+放入inputDtoList
        int totalRowNum = sheet.getLastRowNum();
        for(int i=0;i<totalRowNum;i++)
        {
            boolean isRowError = false;
            tableModel.addRow(EMPTY_STRING_ARRAY);
            HSSFRow tempRow = sheet.getRow(i+1);
            for(int j=0;j<tempRow.getLastCellNum();j++)
            {
                try
                {
                    String value = getHSSFCellValue(tempRow.getCell(j));
                    grabSN189.table.setValueAt(value,i+1, j);
                } catch (Exception e)
                {
                    isRowError = true;
                    grabSN189.table.setValueAt("",i+1, j);
                }
            }

            InputDto dto;
            try
            {
                dto = new InputDto(getHSSFCellValue(tempRow.getCell(0)), getHSSFCellValue(tempRow.getCell(1)),
                        "是".equals(getHSSFCellValue(tempRow.getCell(2))), "是".equals(getHSSFCellValue(tempRow.getCell(10))),
                        getHSSFCellValue(tempRow.getCell(11)), getHSSFCellValue(tempRow.getCell(12)), getHSSFCellValue(tempRow.getCell(13)));
            } catch (Exception e)
            {
                isRowError = true;
                dto = new InputDto("", "", false, false, "", "", "");
            }

            if(isRowError)
            {
                dto.isValidate = false;
                dto.errorMsg = "该行数据载入失败！";
            }
            grabSN189.inputDtoList.add(dto);
        }
    }

    /**
     * 清空所有行 并 初始化头一行
     * @param table
     */
    public static void clearAllAndInitTitle(JTable table)
    {
        // 1 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // 2 清空所有行 并 保留头一行
        for(int i=tableModel.getRowCount()-1;i>=1;i--)
        {
            tableModel.removeRow(i);
        }

        // 3 设置头一行的Title
        for(int i=0;i<EXCEL_TITLE.length;i++)
        {
            tableModel.setValueAt(EXCEL_TITLE[i], 0, i);
        }
    }

    /**
     * 全部抓取
     * @param grabSN189
     */
    public static void grabAll(GrabSN189 grabSN189)
    {
        // 判是否暂停
        if(grabSN189.isPause)
        {
            alertErrorMessage("抓取已暂停，请恢复！");
            return;
        }

        // 判网络是不是通的
        if(!isNetWorkLinked())
        {
            alertErrorMessage("当前网络不通，请检查网络是否连接！");
            return;
        }

        // 判是否加载过Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("当前未加载Excel，请加载Excel！");
            return;
        }

        // 判是否有数据正在加载中
        if(isGrabIng)
        {
            alertWarnMessage("有数正在抓取中，请稍后再试！");
            return;
        }

        // 设置成有数据正在加载中
        setGrabIngTrue(grabSN189);

        loggerOut(grabSN189, "全部抓取开始");
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        for(int i=0;i<grabSN189.inputDtoList.size();i++)
        {
            // 判是否暂停
            if(grabSN189.isPause)
            {
                loggerOut(grabSN189, "暂停抓取");
                break;
            }
            loggerOutWithNoLine(grabSN189, "全部抓取，开始抓取第" + (i+1) + "行数据 ");
            InputDto dto = (InputDto)grabSN189.inputDtoList.get(i);
            boolean isSuccess = grabInputDto(grabSN189, dto, i+1);
            count ++;
            if(isSuccess)
            {
                successCount++;
            } else
            {
                failCount++;
            }
            loggerOut(grabSN189, "结果抓取" + (isSuccess?"成功":"失败"));
        }
        loggerOut(grabSN189, "全部抓取结束，一共抓取条" + count + "数据，成功" + successCount + "条，失败" + failCount + "条");

        // 设置成没有数据正在加载中
        setGrabIngFalse(grabSN189);
    }

    /**
     * 批量抓取
     * @param grabSN189
     */
    public static void grabBatch(GrabSN189 grabSN189)
    {
        // 判是否暂停
        if(grabSN189.isPause)
        {
            alertErrorMessage("抓取已暂停，请恢复！");
            return;
        }

        // 判网络是不是通的
        if(!isNetWorkLinked())
        {
            alertErrorMessage("当前网络不通，请检查网络是否连接！");
            return;
        }

        // 判是否加载过Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("当前未加载Excel，请加载Excel！");
            return;
        }

        int[] selectRows = grabSN189.table.getSelectedRows();
        if(0 == selectRows.length)
        {
            alertWarnMessage("请批量选择数据!");
            return;
        }

        // 判是否有数据正在加载中
        if(isGrabIng)
        {
            alertWarnMessage("有数正在抓取中，请稍后再试！");
            return;
        }

        // 设置成有数据正在加载中
        setGrabIngTrue(grabSN189);

        loggerOut(grabSN189, "批量抓取开始");
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        for(int i=0;i<selectRows.length;i++)
        {
            if(0 == selectRows[i])
            {
                alertMessage("忽略选择第一行抬头数据!");
                continue;
            }

            // 判是否暂停
            if(grabSN189.isPause)
            {
                loggerOut(grabSN189, "暂停抓取");
                break;
            }

            loggerOutWithNoLine(grabSN189, "批量抓取，开始抓取第" + selectRows[i] + "行数据 ");
            InputDto dto = (InputDto)grabSN189.inputDtoList.get(selectRows[i]-1);
            boolean isSuccess = grabInputDto(grabSN189, dto, selectRows[i]);
            count ++;
            if(isSuccess)
            {
                successCount++;
            } else
            {
                failCount++;
            }
            loggerOut(grabSN189, "结果抓取" + (isSuccess?"成功":"失败"));
        }
        loggerOut(grabSN189, "批量抓取结束，一共抓取条" + count + "数据，成功" + successCount + "条，失败" + failCount + "条");

        // 设置成没有数据正在加载中
        setGrabIngFalse(grabSN189);
    }

    /**
     * 单条抓取
     * @param grabSN189
     */
    public static void grabSingle(GrabSN189 grabSN189)
    {
        // 判是否暂停
        if(grabSN189.isPause)
        {
            alertErrorMessage("抓取已暂停，请恢复！");
            return;
        }

        // 判网络是不是通的
        if(!isNetWorkLinked())
        {
            alertErrorMessage("当前网络不通，请检查网络是否连接！");
            return;
        }

        // 判是否加载过Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("当前未加载Excel，请加载Excel！");
            return;
        }

        int[] selectRows = grabSN189.table.getSelectedRows();
        if(0 == selectRows.length)
        {
            alertWarnMessage("请选择一行数据！");
            return;
        } else if(1 < selectRows.length)
        {
            alertWarnMessage("只能选择一行数据！");
            return;
        }

        int selectRow = grabSN189.table.getSelectedRow();
        if(0 == selectRow)
        {
            alertWarnMessage("不能选择第一行抬头数据！");
            return;
        }

        // 判是否有数据正在加载中
        if(isGrabIng)
        {
            alertWarnMessage("有数正在抓取中，请稍后再试！");
            return;
        }

        // 设置成有数据正在加载中
        setGrabIngTrue(grabSN189);

        loggerOut(grabSN189, "单条抓取，开始抓取第" + selectRow + "行数据");
        InputDto dto = (InputDto)grabSN189.inputDtoList.get(selectRow-1);
        boolean isSuccess = false;
        if(!dto.isValidate())
        {
            // 设置成没有数据正在加载中
            setGrabIngFalse(grabSN189);
            alertErrorMessage(dto.errorMsg);
        } else
        {
            isSuccess = grabInputDto(grabSN189, dto, selectRow);
        }
        loggerOut(grabSN189, "单条抓取结束，抓取" + (isSuccess?"成功":"失败"));

        // 设置成没有数据正在加载中
        setGrabIngFalse(grabSN189);
    }

    /**
     * 抓取某条数据
     * @param grabSN189 为了得到table
     * @param dto 抓取这条数据
     * @param tableRow 这条数据对应表格中的行
     */
    public static boolean grabInputDto(GrabSN189 grabSN189, InputDto dto, int tableRow)
    {
        // 得到DefaultTableModel对象
        DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

        // 判入参是否校验通过
        if(!dto.isValidate())
        {
            tableModel.setValueAt(dto.errorMsg, tableRow, EXCEL_TITLE.length - 1);
            return false;
        }

        // 入参校验通过则 做登陆 获取cookie
        LoginSN189 loginSN189;
        try {
            loginSN189 = new LoginSN189(grabSN189, dto);
        } catch (Exception e)
        {
            tableModel.setValueAt("登陆失败!" + e.getMessage(), tableRow, EXCEL_TITLE.length-1);
            return false;
        }

        boolean isSuccess = true;//是否抓取成功
        // 做登陆 获取cookie 之后 抓取数据
        try {
            loginSN189.process();
        } catch (Exception e) {
            isSuccess = false;
            tableModel.setValueAt("抓取失败!" + e.getMessage(), tableRow, EXCEL_TITLE.length-1);
            //这里不需要continue; 下面要取几次抓取的结果
        }

        if(isSuccess)
        {
            tableModel.setValueAt("抓取成功!", tableRow, EXCEL_TITLE.length-1);
        }

        tableModel.setValueAt(loginSN189.puTongYuE, tableRow, 3);
        tableModel.setValueAt(loginSN189.qiTaYuE, tableRow, 4);
        tableModel.setValueAt(loginSN189.flowQueryPeriod, tableRow, 5);
        tableModel.setValueAt(loginSN189.totalFlow, tableRow, 6);
        tableModel.setValueAt(loginSN189.totalTime, tableRow, 7);
        tableModel.setValueAt(loginSN189.packageName, tableRow, 8);
        tableModel.setValueAt(loginSN189.packageInfo, tableRow, 9);
        tableModel.setValueAt(loginSN189.callDivertSetResult, tableRow, 14);
        return isSuccess;
    }

    /**
     * 保存Excel
     * @param grabSN189
     * @param filePath
     */
    public static void saveExcel(GrabSN189 grabSN189, String filePath) throws Exception
    {
        // 判是否加载过Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("当前未加载Excel，请加载Excel！");
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet;

        // 1 装载文件看是否有异常
        sheet = workbook.createSheet();

        // 2 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

        // 3 从table把数据拷贝到excel中
        for(int i=0;i<tableModel.getRowCount();i++)
        {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0);
            row.getCell(0).setCellValue((String) tableModel.getValueAt(i, 0));
            row.createCell(1);
            row.getCell(1).setCellValue((String) tableModel.getValueAt(i, 1));
            row.createCell(2);
            row.getCell(2).setCellValue((String) tableModel.getValueAt(i, 2));
            row.createCell(3);
            row.getCell(3).setCellValue((String) tableModel.getValueAt(i, 3));
            row.createCell(4);
            row.getCell(4).setCellValue((String) tableModel.getValueAt(i, 4));
            row.createCell(5);
            row.getCell(5).setCellValue((String) tableModel.getValueAt(i, 5));
            row.createCell(6);
            row.getCell(6).setCellValue((String) tableModel.getValueAt(i, 6));
            row.createCell(7);
            row.getCell(7).setCellValue((String) tableModel.getValueAt(i, 7));
            row.createCell(8);
            row.getCell(8).setCellValue((String) tableModel.getValueAt(i, 8));
            row.createCell(9);
            row.getCell(9).setCellValue((String) tableModel.getValueAt(i, 9));
            row.createCell(10);
            row.getCell(10).setCellValue((String) tableModel.getValueAt(i, 10));
            row.createCell(11);
            row.getCell(11).setCellValue((String) tableModel.getValueAt(i, 11));
            row.createCell(12);
            row.getCell(12).setCellValue((String) tableModel.getValueAt(i, 12));
            row.createCell(13);
            row.getCell(13).setCellValue((String) tableModel.getValueAt(i, 13));
            row.createCell(14);
            row.getCell(14).setCellValue((String) tableModel.getValueAt(i, 14));
            row.createCell(15);
            row.getCell(15).setCellValue((String) tableModel.getValueAt(i, 15));
        }

        // 4 保存文件
        String fileName = new File(filePath).getName();
        // 保存在 jar包所在lib目录得同级目录out下 并且文件名加 操作结果.xls结尾
        String path = getJarPath().substring(0, getJarPath().lastIndexOf("lib"))
                + "out\\" + fileName.substring(0, fileName.lastIndexOf(".xls"))  + "操作结果" + ".xls";
        FileOutputStream fOut = new FileOutputStream(path);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        loggerOut(grabSN189, "输出excel文件成功，路径[" + path + "]");

        // 5 提示保存Excel成功
        alertMessage("输出操作结果成功！");
    }

    /**
     * 得到单元格的值
     * @param cell
     * @return
     */
    public static String getHSSFCellValue(HSSFCell cell)
    {
        if(null == cell)
        {
            return StringUtils.EMPTY;
        }
        Object cellValue = null;
//        String[] valueArr = mapvalue.split(",");
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING://字符串类型
                cellValue = cell.getRichStringCellValue().getString();
                if (((String)cellValue).trim().equals("")
                        || ((String)cellValue).trim().length() <= 0) {
                    cellValue = "";
                }
                //cellValue = inputEncode((String)cellValue);
                break;
            case HSSFCell.CELL_TYPE_NUMERIC://数字类型
//                if (valueArr.length == 2 && valueArr[1].equals("date")) {
//                    cellValue = cell.getDateCellValue();;
//                }
//                if (valueArr.length == 2 && valueArr[1].equals("timestamp")) {
//                    Date date = cell.getDateCellValue();
//                    SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String time=format1.format(date);
//                    cellValue= Timestamp.valueOf(time);
//                }else { // 如果长度为2，说明此列为默认字符串类型
                    BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                    // cellValue =big.toEngineeringString();
                    cellValue = big.toString();
//                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                BigDecimal bigula = new BigDecimal(cell
                        .getCachedFormulaResultType());
                // cellValue = bigula.toEngineeringString();
                cellValue = bigula.toString();
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue = "";
                break;
            default:
                break;
        }
        return null == cellValue?"":cellValue.toString();
    }

    /**
     * 提示信息
     * @param message
     */
    public static void alertMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "信息", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 警告信息
     * @param message
     */
    public static void alertWarnMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "信息", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 错误信息
     * @param message
     */
    public static void alertErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "信息", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 将日志写入loggerTextPane
     * @param grabSN189
     * @param string
     */
    public static void loggerOutWithNoLine(GrabSN189 grabSN189, String string)
    {
        grabSN189.loggerTextArea.append(string);
        logger.info(string);
    }

    /**
     * 将日志写入loggerTextPane
     * @param grabSN189
     * @param string
     */
    public static void loggerOut(GrabSN189 grabSN189, String string)
    {
        grabSN189.loggerTextArea.append(string + "\r\n");
        logger.info(string);
    }

    /**
     * 得到jar包路径
     * @return
     */
    public static String getJarPath()
    {
        JarUtil ju = new JarUtil(JarUtil.class);
        return ju.getJarPath() + "/";
    }

    /**
     * 调用JS来加密密码
     * @param pwd
     * @return
     */
    public static String encPwd(String pwd) throws Exception
    {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByExtension("js");
        engine.eval(new FileReader(ENC_PWD_JS_FILE));
        Invocable inv = (Invocable) engine;
        String encPwd = String.valueOf(inv.invokeFunction(ENC_PWD_FUNCTION_NAME, pwd));
        return encPwd;
    }

    /**
     * 判网络是不是通的
     * @return
     */
    public static boolean isNetWorkLinked()
    {
        boolean isNetWorkLinked = false;
        for (int urlsCount = 0; urlsCount < CHECK_NET_WORK_LINKED_URLS.length; urlsCount++) {
            if (canGetHtmlCode(CHECK_NET_WORK_LINKED_URLS[urlsCount])) {
                isNetWorkLinked = true;
                break;
            }
        }
        return isNetWorkLinked;
    }

    /**
     * 判是否能得到HTML内容
     * @param httpUrl
     * @return
     */
    public static boolean canGetHtmlCode(String httpUrl) {
        String htmlCode = "";
        try {
            InputStream in;
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.connect();
            in = connection.getInputStream();
            byte[] buffer = new byte[50];
            in.read(buffer);
            htmlCode = new String(buffer);
        } catch (Exception e) {
        }
        if (htmlCode == null || htmlCode.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 设置成有数据正在加载中
     */
    public static void setGrabIngTrue(GrabSN189 grabSN189)
    {
        isGrabIng = true;
        grabSN189.bottomPanel.showCanSee();
    }

    /**
     * 设置成没有数据正在加载中
     */
    public static void setGrabIngFalse(GrabSN189 grabSN189)
    {
        isGrabIng = false;
        grabSN189.bottomPanel.showCanNotSee();
    }

    /**
     * 判断暂停抓取
     * @param grabSN189
     */
    public static void checkPause(GrabSN189 grabSN189)
    {
        if(grabSN189.isPause)
        {
            BaseUtils.loggerOut(grabSN189, "暂停抓取！");
            throw new RuntimeException("暂停抓取！");
        }
    }
}
