package gxx2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * ץȡjf.189.cn
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-30 16:59
 */
public class GrabJF189_2 extends JFrame implements BaseInterface_2 {

    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();//��ȡ��Ļ�ֱ��ʴ�С
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());//getScreenInsets��ָ�����Ļ�� insets
        WINDOW_START_X = 20;
        WINDOW_START_Y = 20;
        WINDOW_LEN = screen.width-40;
        WINDOW_HEI = screen.height-insets.bottom-40;
    }

    // �����С
    public int WINDOW_START_X;
    public int WINDOW_START_Y;
    public int WINDOW_LEN;
    public int WINDOW_HEI;

    /**
     * ���
     */
    JTable table;

    /**
     * �м�Ŀ�
     */
    CenterPanel_2 centerPanel;

    /**
     * �ұ߲˵�
     */
    RightPanel_2 rightPanel;

    /**
     * ��־�ı���
     */
    JTextArea loggerTextArea;

    /**
     * excel����ÿ�ж�Ӧ���������ݼ���
     */
    java.util.List inputDtoList = new ArrayList();

    /**
     * ÿ�δ����һ�����Σ�ÿ�γɹ��Զ����浽�ļ������¶ϵ�
     * ���κţ�
     */
    String fetch = "_" + DateUtil.getNowDate() + "_" + DateUtil.getNowTime();

    /**
     * ���캯��
     */
    public GrabJF189_2() {
        // ��ʼ��������
        BaseUtils_2.initLookAndFeel();

        // �м�������
        centerPanel = new CenterPanel_2(this);
        table = centerPanel.table;
        loggerTextArea = centerPanel.loggerTextArea;

        // �ұ�������
        rightPanel = new RightPanel_2(this);

        // ��ǰ����
        Container c = this.getContentPane();

        // ��������
        c.add(centerPanel, BorderLayout.WEST);
        c.add(rightPanel, BorderLayout.EAST);

        // �����¼�����
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
                System.exit(0);
            }
        });

        // ����̧ͷ
        setTitle(TITLE);

        // ���������Ϸ���״
        //setUndecorated(true);

        // ����λ��
        setLocation(WINDOW_START_X, WINDOW_START_Y);

        // ���ô��ڴ�С
        setSize(WINDOW_LEN, WINDOW_HEI);

        // չʾ
        show();
    }

    /**
     * main����
     * @param params
     * @throws Exception
     */
    public static void main(String[] params) throws Exception
    {
        new GrabJF189_2();
    }
}
