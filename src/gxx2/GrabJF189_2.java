package gxx2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * 抓取jf.189.cn
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-30 16:59
 */
public class GrabJF189_2 extends JFrame implements BaseInterface_2 {

    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕分辨率大小
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());//getScreenInsets是指获得屏幕的 insets
        WINDOW_START_X = 20;
        WINDOW_START_Y = 20;
        WINDOW_LEN = screen.width-40;
        WINDOW_HEI = screen.height-insets.bottom-40;
    }

    // 方框大小
    public int WINDOW_START_X;
    public int WINDOW_START_Y;
    public int WINDOW_LEN;
    public int WINDOW_HEI;

    /**
     * 表格
     */
    JTable table;

    /**
     * 中间的框
     */
    CenterPanel_2 centerPanel;

    /**
     * 右边菜单
     */
    RightPanel_2 rightPanel;

    /**
     * 日志文本框
     */
    JTextArea loggerTextArea;

    /**
     * excel数据每行对应的输入数据集合
     */
    java.util.List inputDtoList = new ArrayList();

    /**
     * 每次打开软件一个批次，每次成功自动保存到文件，不怕断电
     * 批次号：
     */
    String fetch = "_" + DateUtil.getNowDate() + "_" + DateUtil.getNowTime();

    /**
     * 构造函数
     */
    public GrabJF189_2() {
        // 初始化界面风格
        BaseUtils_2.initLookAndFeel();

        // 中间内容类
        centerPanel = new CenterPanel_2(this);
        table = centerPanel.table;
        loggerTextArea = centerPanel.loggerTextArea;

        // 右边内容类
        rightPanel = new RightPanel_2(this);

        // 当前容器
        Container c = this.getContentPane();

        // 布置内容
        c.add(centerPanel, BorderLayout.WEST);
        c.add(rightPanel, BorderLayout.EAST);

        // 窗口事件监听
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
                System.exit(0);
            }
        });

        // 设置抬头
        setTitle(TITLE);

        // 设置隐藏上方条状
        //setUndecorated(true);

        // 设置位置
        setLocation(WINDOW_START_X, WINDOW_START_Y);

        // 设置窗口大小
        setSize(WINDOW_LEN, WINDOW_HEI);

        // 展示
        show();
    }

    /**
     * main函数
     * @param params
     * @throws Exception
     */
    public static void main(String[] params) throws Exception
    {
        new GrabJF189_2();
    }
}
