package gxx;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * 中间内容类
 * Create by Gxx
 * Time: 2013-10-08 23:35
 */
public class CenterPanel extends JPanel implements BaseInterface
{
    /**
     * 表格
     */
    JTable table;

    /**
     * 日志文本框
     */
    JTextArea loggerTextArea;

    /**
     * 构造函数
     */
    public CenterPanel()
    {
        JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        loggerTextArea = new JTextArea();
        loggerTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        loggerTextArea.setLineWrap(true);
        loggerTextArea.setText("");

        JPanel loggerPanel = new JPanel();
        JScrollPane loggerScrollPane=new JScrollPane(loggerTextArea);
        loggerPanel.setLayout(new GridLayout(1,1));//加上这句
        loggerPanel.add(loggerScrollPane);

        table = new JTable(100, EXCEL_TITLE.length);
        table.setBorder(new LineBorder(Color.BLACK));
        setTableColumnWidth();
        JPanel pane = new JPanel();
        //pane.add(table.getTableHeader(), BorderLayout.NORTH);
        pane.add(table);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        scrollPane.setViewportView(pane);

        tab.addTab("数据装载", scrollPane);
        tab.addTab("操作日志", loggerPanel);
        tab.setPreferredSize(new Dimension(1050, 600));
        this.add(tab);
        this.setEnabled(true);
    }

    /**
     * 设置表格列宽度
     */
    private void setTableColumnWidth() {
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(2);
        column.setPreferredWidth(140);
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(60);
        column = table.getColumnModel().getColumn(4);
        column.setPreferredWidth(60);
        column = table.getColumnModel().getColumn(5);
        column.setPreferredWidth(180);
        column = table.getColumnModel().getColumn(6);
        column.setPreferredWidth(60);
        column = table.getColumnModel().getColumn(7);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(8);
        column.setPreferredWidth(250);
        column = table.getColumnModel().getColumn(9);
        column.setPreferredWidth(200);
        column = table.getColumnModel().getColumn(10);
        column.setPreferredWidth(150);
        column = table.getColumnModel().getColumn(11);
        column.setPreferredWidth(120);
        column = table.getColumnModel().getColumn(12);
        column.setPreferredWidth(120);
        column = table.getColumnModel().getColumn(13);
        column.setPreferredWidth(120);
        column = table.getColumnModel().getColumn(14);
        column.setPreferredWidth(110);
        column = table.getColumnModel().getColumn(15);
        column.setPreferredWidth(300);
    }
}
