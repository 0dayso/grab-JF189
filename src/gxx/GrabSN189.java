package gxx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 从http://www.sn.189.cn/抓取数据工具类
 * =======================================================================
 * 从EXCEL读取数据，逐条向http://www.sn.189.cn/，抓取数据
 * 输入：
 * 用户名，密码，无条件呼叫转移号码，无应答呼叫转移号码，遇忙时呼叫转移号码
 * 操作：
 * 1.查询普通余额
 * 2.查询其他余额
 * 3.流量查询
 * 4.套餐查询
 * 5.呼叫转移
 * =======================================================================
 * Create by Gxx
 * Time: 2013-10-08 23:17
 */
public class GrabSN189 extends JFrame implements BaseInterface
{
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

    // 是否暂停
    boolean isPause = false;
    boolean grabType = false;

    /**
     * 表格
     */
    JTable table;

    /**
     * 右边菜单
     */
    RightPanel rightPanel;

    /**
     * 下方进度条
     */
    BottomPanel bottomPanel;

    /**
     * 日志文本框
     */
    JTextArea loggerTextArea;

    /**
     * excel数据每行对应的输入数据集合
     */
    java.util.List inputDtoList;

    /**
     * 构造函数
     */
    public GrabSN189()
    {
        // 初始化界面风格
        BaseUtils.initLookAndFeel();

        // 菜单类
        //MyMenu myMenu = new MyMenu(this);

        // 中间内容类
        CenterPanel centerPanel = new CenterPanel();
        table = centerPanel.table;
        loggerTextArea = centerPanel.loggerTextArea;

        // 右边内容类
        rightPanel = new RightPanel(this);

        // 下方进度条
        bottomPanel = new BottomPanel();

        // 当前容器
        Container c = this.getContentPane();

        // 设置菜单
        //this.setJMenuBar(myMenu);

        // 布置内容
        c.add(centerPanel, BorderLayout.WEST);
        c.add(bottomPanel, BorderLayout.SOUTH);
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

        // 最大化显示
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * main函数
     * @param params
     * @throws Exception
     */
    public static void main(String[] params) throws Exception
    {
        new GrabSN189();
    }
}
