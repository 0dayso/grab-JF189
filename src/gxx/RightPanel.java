package gxx;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 右边内容类
 * Create by Gxx
 * Time: 2013-10-09 00:28
 */
class RightPanel extends JPanel
{
    /**
     * 工具类
     */
    private GrabSN189 grabSN189;

    /**
     * 文件路径
     */
    public String filePath;

    /**
     * 构造函数
     */
    public RightPanel(final GrabSN189 grabSN189)
    {
        this.grabSN189 = grabSN189;

        this.setLayout(new GridLayout(16, 1));

        JButton loadButton = new JButton("载入Excel");
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
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "信息", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        add(loadButton);

        JButton grabButton = new JButton("全部抓取");
        grabButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new GrabThread(GrabThread.GRAB_ALL, grabSN189)).start();
            }
        });
        add(grabButton);

        JButton batchButton = new JButton("批量抓取");
        batchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new GrabThread(GrabThread.GRAB_BATCH, grabSN189)).start();
            }
        });
        add(batchButton);

        JButton singleButton = new JButton("单条抓取");
        singleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new GrabThread(GrabThread.GRAB_SINGLE, grabSN189)).start();
            }
        });
        add(singleButton);

        final JButton switchButton = new JButton("暂停");
        switchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(grabSN189.isPause)
                {
                    switchButton.setText("暂停");
                    grabSN189.isPause = !grabSN189.isPause;
                } else
                {
                    BaseUtils.setGrabIngFalse(grabSN189);
                    switchButton.setText("恢复");
                    grabSN189.isPause = !grabSN189.isPause;
                }
            }
        });
        add(switchButton);

        JButton saveButton = new JButton("输出操作结果");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    BaseUtils.saveExcel(grabSN189, filePath);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "输出操作结果失败！", "信息", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton);

        JButton clearLogButton = new JButton("清空日志");
        clearLogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabSN189.loggerTextArea.setText(StringUtils.EMPTY);
            }
        });
        add(clearLogButton);

        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.LIGHT_GRAY, Color.blue));
    }
}
