package gxx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * ��http://www.sn.189.cn/ץȡ���ݹ�����
 * =======================================================================
 * ��EXCEL��ȡ���ݣ�������http://www.sn.189.cn/��ץȡ����
 * ���룺
 * �û��������룬����������ת�ƺ��룬��Ӧ�����ת�ƺ��룬��æʱ����ת�ƺ���
 * ������
 * 1.��ѯ��ͨ���
 * 2.��ѯ�������
 * 3.������ѯ
 * 4.�ײͲ�ѯ
 * 5.����ת��
 * =======================================================================
 * Create by Gxx
 * Time: 2013-10-08 23:17
 */
public class GrabSN189 extends JFrame implements BaseInterface
{
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

    // �Ƿ���ͣ
    boolean isPause = false;
    boolean grabType = false;

    /**
     * ���
     */
    JTable table;

    /**
     * �ұ߲˵�
     */
    RightPanel rightPanel;

    /**
     * �·�������
     */
    BottomPanel bottomPanel;

    /**
     * ��־�ı���
     */
    JTextArea loggerTextArea;

    /**
     * excel����ÿ�ж�Ӧ���������ݼ���
     */
    java.util.List inputDtoList;

    /**
     * ���캯��
     */
    public GrabSN189()
    {
        // ��ʼ��������
        BaseUtils.initLookAndFeel();

        // �˵���
        //MyMenu myMenu = new MyMenu(this);

        // �м�������
        CenterPanel centerPanel = new CenterPanel();
        table = centerPanel.table;
        loggerTextArea = centerPanel.loggerTextArea;

        // �ұ�������
        rightPanel = new RightPanel(this);

        // �·�������
        bottomPanel = new BottomPanel();

        // ��ǰ����
        Container c = this.getContentPane();

        // ���ò˵�
        //this.setJMenuBar(myMenu);

        // ��������
        c.add(centerPanel, BorderLayout.WEST);
        c.add(bottomPanel, BorderLayout.SOUTH);
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

        // �����ʾ
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * main����
     * @param params
     * @throws Exception
     */
    public static void main(String[] params) throws Exception
    {
        new GrabSN189();
    }
}
