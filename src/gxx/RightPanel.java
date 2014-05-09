package gxx;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * �ұ�������
 * Create by Gxx
 * Time: 2013-10-09 00:28
 */
class RightPanel extends JPanel
{
    /**
     * ������
     */
    private GrabSN189 grabSN189;

    /**
     * �ļ�·��
     */
    public String filePath;

    /**
     * ���캯��
     */
    public RightPanel(final GrabSN189 grabSN189)
    {
        this.grabSN189 = grabSN189;

        this.setLayout(new GridLayout(16, 1));

        JButton loadButton = new JButton("����Excel");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser file = new JFileChooser();
                int result = file.showOpenDialog(new JPanel());
                if (result == file.APPROVE_OPTION) {
                    filePath = file.getSelectedFile().getPath();
                    try {
                        BaseUtils.checkExcel(filePath, grabSN189);
                    } catch (Exception e1) {
                        filePath = StringUtils.EMPTY;
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        add(loadButton);

        JButton grabButton = new JButton("ȫ��ץȡ");
        grabButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new GrabThread(GrabThread.GRAB_ALL, grabSN189)).start();
            }
        });
        add(grabButton);

        JButton batchButton = new JButton("����ץȡ");
        batchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new GrabThread(GrabThread.GRAB_BATCH, grabSN189)).start();
            }
        });
        add(batchButton);

        JButton singleButton = new JButton("����ץȡ");
        singleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new GrabThread(GrabThread.GRAB_SINGLE, grabSN189)).start();
            }
        });
        add(singleButton);

        final JButton switchButton = new JButton("��ͣ");
        switchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(grabSN189.isPause)
                {
                    switchButton.setText("��ͣ");
                    grabSN189.isPause = !grabSN189.isPause;
                } else
                {
                    BaseUtils.setGrabIngFalse(grabSN189);
                    switchButton.setText("�ָ�");
                    grabSN189.isPause = !grabSN189.isPause;
                }
            }
        });
        add(switchButton);

        JButton saveButton = new JButton("����������");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    BaseUtils.saveExcel(grabSN189, filePath);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "����������ʧ�ܣ�", "��Ϣ", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton);

        JButton clearLogButton = new JButton("�����־");
        clearLogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabSN189.loggerTextArea.setText(StringUtils.EMPTY);
            }
        });
        add(clearLogButton);

        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.LIGHT_GRAY, Color.blue));
    }
}
