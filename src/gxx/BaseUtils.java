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
 * ����������
 * Create by Gxx
 * Time: 2013-10-09 00:46
 */
public class BaseUtils implements BaseInterface
{
    /**
     * ��־�ļ����
     */
    public static Logger logger = Logger.getLogger(BaseUtils.class);

    /**
     * �Ƿ���������ץȡ�У���������Ժ�����
     */
    public static boolean isGrabIng = false;

    /**
     * ��ʼ��������(��ǰϵͳ�ķ��)
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
     * У��Excel�Ƿ���ȷ
     * @param path
     */
    public static void checkExcel(String path, GrabSN189 grabSN189) throws Exception
    {
        loggerOut(grabSN189, "����Excel·��:[" + path + "]");
        HSSFWorkbook workbook;
        HSSFSheet sheet;

        // 1 װ���ļ����Ƿ����쳣
        try {
            workbook = new HSSFWorkbook(new FileInputStream(path));
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
            loggerOut(grabSN189, "װ���ļ��쳣");
            throw new RuntimeException("װ���ļ��쳣��" + path);
        }

        // 2 ���Ƿ�������
        int totalRowNum = sheet.getLastRowNum();
        loggerOut(grabSN189, "�ļ�����=" + (totalRowNum+1) + "(����̧ͷ)");
        if(totalRowNum <= 0)
        {
            throw new RuntimeException("���ļ�������");
        }

        // 3 У���һ�������Ƿ�һ��
        HSSFRow row = sheet.getRow(0);
        int totalCellNum = row.getLastCellNum();
        loggerOut(grabSN189, "�ļ�����=" + totalCellNum);
        if(EXCEL_TITLE.length != totalCellNum)//�Ƚϸ���
        {
            alertWarnMessage("���ļ���ģ�岻һ�£������������ļ���");
            return;
        }

        int index = 0;
        while (index < EXCEL_TITLE.length)//�Ƚ��Ƿ�һ��
        {
            try{
                if(!EXCEL_TITLE[index].equals(getHSSFCellValue(row.getCell(index++))))
                {
                    alertWarnMessage("���ļ���ģ�岻һ�£������������ļ���");
                    return;
                }
            } catch (Exception e)
            {
                alertWarnMessage("���ļ���ģ�岻һ�£������������ļ���");
                return;
            }
        }

        /**
         * 4 ��ȡ���ݵ�table��
         */
        readExcel2Table(sheet,grabSN189);

        /**
         * 5 ��������У�� todo
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
                    dto.errorMsg = "�ֻ��ź��ֻ����벻��Ϊ�գ�";
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
                    dto.errorMsg = "����ת�ƺ��벻�����ô���1����";
                }
            }
            // �õ�DefaultTableModel����
            DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

            // ������Ƿ�У��ͨ��
            if(!dto.isValidate)
            {
                tableModel.setValueAt(dto.errorMsg, i+1, EXCEL_TITLE.length-1);
            }
        }
    }

    /**
     * ��ȡ���ݵ�table��
     * @param sheet
     * @param grabSN189
     */
    private static void readExcel2Table(HSSFSheet sheet, GrabSN189 grabSN189)
    {
        // 0 ��ʼ��inputDtoList
        grabSN189.inputDtoList = new ArrayList();

        // 1 ��������� �� ��ʼ��ͷһ��
        clearAllAndInitTitle(grabSN189.table);

        // 2 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

        // 2 ��������+�޸�table+����inputDtoList
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
                        "��".equals(getHSSFCellValue(tempRow.getCell(2))), "��".equals(getHSSFCellValue(tempRow.getCell(10))),
                        getHSSFCellValue(tempRow.getCell(11)), getHSSFCellValue(tempRow.getCell(12)), getHSSFCellValue(tempRow.getCell(13)));
            } catch (Exception e)
            {
                isRowError = true;
                dto = new InputDto("", "", false, false, "", "", "");
            }

            if(isRowError)
            {
                dto.isValidate = false;
                dto.errorMsg = "������������ʧ�ܣ�";
            }
            grabSN189.inputDtoList.add(dto);
        }
    }

    /**
     * ��������� �� ��ʼ��ͷһ��
     * @param table
     */
    public static void clearAllAndInitTitle(JTable table)
    {
        // 1 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // 2 ��������� �� ����ͷһ��
        for(int i=tableModel.getRowCount()-1;i>=1;i--)
        {
            tableModel.removeRow(i);
        }

        // 3 ����ͷһ�е�Title
        for(int i=0;i<EXCEL_TITLE.length;i++)
        {
            tableModel.setValueAt(EXCEL_TITLE[i], 0, i);
        }
    }

    /**
     * ȫ��ץȡ
     * @param grabSN189
     */
    public static void grabAll(GrabSN189 grabSN189)
    {
        // ���Ƿ���ͣ
        if(grabSN189.isPause)
        {
            alertErrorMessage("ץȡ����ͣ����ָ���");
            return;
        }

        // �������ǲ���ͨ��
        if(!isNetWorkLinked())
        {
            alertErrorMessage("��ǰ���粻ͨ�����������Ƿ����ӣ�");
            return;
        }

        // ���Ƿ���ع�Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("��ǰδ����Excel�������Excel��");
            return;
        }

        // ���Ƿ����������ڼ�����
        if(isGrabIng)
        {
            alertWarnMessage("��������ץȡ�У����Ժ����ԣ�");
            return;
        }

        // ���ó����������ڼ�����
        setGrabIngTrue(grabSN189);

        loggerOut(grabSN189, "ȫ��ץȡ��ʼ");
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        for(int i=0;i<grabSN189.inputDtoList.size();i++)
        {
            // ���Ƿ���ͣ
            if(grabSN189.isPause)
            {
                loggerOut(grabSN189, "��ͣץȡ");
                break;
            }
            loggerOutWithNoLine(grabSN189, "ȫ��ץȡ����ʼץȡ��" + (i+1) + "������ ");
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
            loggerOut(grabSN189, "���ץȡ" + (isSuccess?"�ɹ�":"ʧ��"));
        }
        loggerOut(grabSN189, "ȫ��ץȡ������һ��ץȡ��" + count + "���ݣ��ɹ�" + successCount + "����ʧ��" + failCount + "��");

        // ���ó�û���������ڼ�����
        setGrabIngFalse(grabSN189);
    }

    /**
     * ����ץȡ
     * @param grabSN189
     */
    public static void grabBatch(GrabSN189 grabSN189)
    {
        // ���Ƿ���ͣ
        if(grabSN189.isPause)
        {
            alertErrorMessage("ץȡ����ͣ����ָ���");
            return;
        }

        // �������ǲ���ͨ��
        if(!isNetWorkLinked())
        {
            alertErrorMessage("��ǰ���粻ͨ�����������Ƿ����ӣ�");
            return;
        }

        // ���Ƿ���ع�Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("��ǰδ����Excel�������Excel��");
            return;
        }

        int[] selectRows = grabSN189.table.getSelectedRows();
        if(0 == selectRows.length)
        {
            alertWarnMessage("������ѡ������!");
            return;
        }

        // ���Ƿ����������ڼ�����
        if(isGrabIng)
        {
            alertWarnMessage("��������ץȡ�У����Ժ����ԣ�");
            return;
        }

        // ���ó����������ڼ�����
        setGrabIngTrue(grabSN189);

        loggerOut(grabSN189, "����ץȡ��ʼ");
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        for(int i=0;i<selectRows.length;i++)
        {
            if(0 == selectRows[i])
            {
                alertMessage("����ѡ���һ��̧ͷ����!");
                continue;
            }

            // ���Ƿ���ͣ
            if(grabSN189.isPause)
            {
                loggerOut(grabSN189, "��ͣץȡ");
                break;
            }

            loggerOutWithNoLine(grabSN189, "����ץȡ����ʼץȡ��" + selectRows[i] + "������ ");
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
            loggerOut(grabSN189, "���ץȡ" + (isSuccess?"�ɹ�":"ʧ��"));
        }
        loggerOut(grabSN189, "����ץȡ������һ��ץȡ��" + count + "���ݣ��ɹ�" + successCount + "����ʧ��" + failCount + "��");

        // ���ó�û���������ڼ�����
        setGrabIngFalse(grabSN189);
    }

    /**
     * ����ץȡ
     * @param grabSN189
     */
    public static void grabSingle(GrabSN189 grabSN189)
    {
        // ���Ƿ���ͣ
        if(grabSN189.isPause)
        {
            alertErrorMessage("ץȡ����ͣ����ָ���");
            return;
        }

        // �������ǲ���ͨ��
        if(!isNetWorkLinked())
        {
            alertErrorMessage("��ǰ���粻ͨ�����������Ƿ����ӣ�");
            return;
        }

        // ���Ƿ���ع�Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("��ǰδ����Excel�������Excel��");
            return;
        }

        int[] selectRows = grabSN189.table.getSelectedRows();
        if(0 == selectRows.length)
        {
            alertWarnMessage("��ѡ��һ�����ݣ�");
            return;
        } else if(1 < selectRows.length)
        {
            alertWarnMessage("ֻ��ѡ��һ�����ݣ�");
            return;
        }

        int selectRow = grabSN189.table.getSelectedRow();
        if(0 == selectRow)
        {
            alertWarnMessage("����ѡ���һ��̧ͷ���ݣ�");
            return;
        }

        // ���Ƿ����������ڼ�����
        if(isGrabIng)
        {
            alertWarnMessage("��������ץȡ�У����Ժ����ԣ�");
            return;
        }

        // ���ó����������ڼ�����
        setGrabIngTrue(grabSN189);

        loggerOut(grabSN189, "����ץȡ����ʼץȡ��" + selectRow + "������");
        InputDto dto = (InputDto)grabSN189.inputDtoList.get(selectRow-1);
        boolean isSuccess = false;
        if(!dto.isValidate())
        {
            // ���ó�û���������ڼ�����
            setGrabIngFalse(grabSN189);
            alertErrorMessage(dto.errorMsg);
        } else
        {
            isSuccess = grabInputDto(grabSN189, dto, selectRow);
        }
        loggerOut(grabSN189, "����ץȡ������ץȡ" + (isSuccess?"�ɹ�":"ʧ��"));

        // ���ó�û���������ڼ�����
        setGrabIngFalse(grabSN189);
    }

    /**
     * ץȡĳ������
     * @param grabSN189 Ϊ�˵õ�table
     * @param dto ץȡ��������
     * @param tableRow �������ݶ�Ӧ����е���
     */
    public static boolean grabInputDto(GrabSN189 grabSN189, InputDto dto, int tableRow)
    {
        // �õ�DefaultTableModel����
        DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

        // ������Ƿ�У��ͨ��
        if(!dto.isValidate())
        {
            tableModel.setValueAt(dto.errorMsg, tableRow, EXCEL_TITLE.length - 1);
            return false;
        }

        // ���У��ͨ���� ����½ ��ȡcookie
        LoginSN189 loginSN189;
        try {
            loginSN189 = new LoginSN189(grabSN189, dto);
        } catch (Exception e)
        {
            tableModel.setValueAt("��½ʧ��!" + e.getMessage(), tableRow, EXCEL_TITLE.length-1);
            return false;
        }

        boolean isSuccess = true;//�Ƿ�ץȡ�ɹ�
        // ����½ ��ȡcookie ֮�� ץȡ����
        try {
            loginSN189.process();
        } catch (Exception e) {
            isSuccess = false;
            tableModel.setValueAt("ץȡʧ��!" + e.getMessage(), tableRow, EXCEL_TITLE.length-1);
            //���ﲻ��Ҫcontinue; ����Ҫȡ����ץȡ�Ľ��
        }

        if(isSuccess)
        {
            tableModel.setValueAt("ץȡ�ɹ�!", tableRow, EXCEL_TITLE.length-1);
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
     * ����Excel
     * @param grabSN189
     * @param filePath
     */
    public static void saveExcel(GrabSN189 grabSN189, String filePath) throws Exception
    {
        // ���Ƿ���ع�Excel
        if(StringUtils.isBlank(grabSN189.rightPanel.filePath))
        {
            alertWarnMessage("��ǰδ����Excel�������Excel��");
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet;

        // 1 װ���ļ����Ƿ����쳣
        sheet = workbook.createSheet();

        // 2 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabSN189.table.getModel();

        // 3 ��table�����ݿ�����excel��
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

        // 4 �����ļ�
        String fileName = new File(filePath).getName();
        // ������ jar������libĿ¼��ͬ��Ŀ¼out�� �����ļ����� �������.xls��β
        String path = getJarPath().substring(0, getJarPath().lastIndexOf("lib"))
                + "out\\" + fileName.substring(0, fileName.lastIndexOf(".xls"))  + "�������" + ".xls";
        FileOutputStream fOut = new FileOutputStream(path);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        loggerOut(grabSN189, "���excel�ļ��ɹ���·��[" + path + "]");

        // 5 ��ʾ����Excel�ɹ�
        alertMessage("�����������ɹ���");
    }

    /**
     * �õ���Ԫ���ֵ
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
            case HSSFCell.CELL_TYPE_STRING://�ַ�������
                cellValue = cell.getRichStringCellValue().getString();
                if (((String)cellValue).trim().equals("")
                        || ((String)cellValue).trim().length() <= 0) {
                    cellValue = "";
                }
                //cellValue = inputEncode((String)cellValue);
                break;
            case HSSFCell.CELL_TYPE_NUMERIC://��������
//                if (valueArr.length == 2 && valueArr[1].equals("date")) {
//                    cellValue = cell.getDateCellValue();;
//                }
//                if (valueArr.length == 2 && valueArr[1].equals("timestamp")) {
//                    Date date = cell.getDateCellValue();
//                    SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String time=format1.format(date);
//                    cellValue= Timestamp.valueOf(time);
//                }else { // �������Ϊ2��˵������ΪĬ���ַ�������
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
     * ��ʾ��Ϣ
     * @param message
     */
    public static void alertMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * ������Ϣ
     * @param message
     */
    public static void alertWarnMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "��Ϣ", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * ������Ϣ
     * @param message
     */
    public static void alertErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "��Ϣ", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * ����־д��loggerTextPane
     * @param grabSN189
     * @param string
     */
    public static void loggerOutWithNoLine(GrabSN189 grabSN189, String string)
    {
        grabSN189.loggerTextArea.append(string);
        logger.info(string);
    }

    /**
     * ����־д��loggerTextPane
     * @param grabSN189
     * @param string
     */
    public static void loggerOut(GrabSN189 grabSN189, String string)
    {
        grabSN189.loggerTextArea.append(string + "\r\n");
        logger.info(string);
    }

    /**
     * �õ�jar��·��
     * @return
     */
    public static String getJarPath()
    {
        JarUtil ju = new JarUtil(JarUtil.class);
        return ju.getJarPath() + "/";
    }

    /**
     * ����JS����������
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
     * �������ǲ���ͨ��
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
     * ���Ƿ��ܵõ�HTML����
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
     * ���ó����������ڼ�����
     */
    public static void setGrabIngTrue(GrabSN189 grabSN189)
    {
        isGrabIng = true;
        grabSN189.bottomPanel.showCanSee();
    }

    /**
     * ���ó�û���������ڼ�����
     */
    public static void setGrabIngFalse(GrabSN189 grabSN189)
    {
        isGrabIng = false;
        grabSN189.bottomPanel.showCanNotSee();
    }

    /**
     * �ж���ͣץȡ
     * @param grabSN189
     */
    public static void checkPause(GrabSN189 grabSN189)
    {
        if(grabSN189.isPause)
        {
            BaseUtils.loggerOut(grabSN189, "��ͣץȡ��");
            throw new RuntimeException("��ͣץȡ��");
        }
    }
}
