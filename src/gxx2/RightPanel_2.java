package gxx2;

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
class RightPanel_2 extends JPanel
{
    /**
     * 工具类
     */
    private GrabJF189_2 grabJF189;

    /**
     * 文件路径
     */
    public String filePath;

    /**
     * 外网ip
     */
    Label outIpLabel = new Label();

    /**
     * 构造函数
     */
    public RightPanel_2(final GrabJF189_2 grabJF189)
    {
        this.grabJF189 = grabJF189;

        this.setLayout(new GridLayout(16, 1));

        JButton loadButton = new JButton("载入Excel");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser file = new JFileChooser();
                int result = file.showOpenDialog(new JPanel());
                if (result == file.APPROVE_OPTION) {
                    filePath = file.getSelectedFile().getPath();
                    try {
                        BaseUtils_2.checkExcel(filePath, grabJF189);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        filePath = StringUtils.EMPTY;
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "信息", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        add(loadButton);

        JButton singleButton = new JButton("选择用户");
        singleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(grabJF189.inputDtoList.size() == 0){
                    BaseUtils_2.alertWarnMessage("请载入Excel！");
                    return;
                }
                int[] selectRows = grabJF189.table.getSelectedRows();
                if(0 == selectRows.length)
                {
                    BaseUtils_2.alertWarnMessage("请选择一行数据！");
                    return;
                } else if(1 < selectRows.length)
                {
                    BaseUtils_2.alertWarnMessage("只能选择一行数据！");
                    return;
                }

                int selectRow = grabJF189.table.getSelectedRow();
                if(0 == selectRow)
                {
                    BaseUtils_2.alertWarnMessage("不能选择第一行抬头数据！");
                    return;
                }
                InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(selectRow-1);
                if(!dto.isValidate){
                    BaseUtils_2.alertWarnMessage("该行数据载入失败：[" + dto.getErrorMsg() + "]，请选择其他数据！");
                    return;
                }
                grabJF189.centerPanel.selectRow = selectRow;
                grabJF189.centerPanel.userNameTextField.setText(dto.getMobile());
                grabJF189.centerPanel.passwordTextField.setText(dto.getPassword());
                grabJF189.centerPanel.tab.setSelectedIndex(1);
                //点击重新登录按钮
                grabJF189.centerPanel.clickReLogin();
            }
        });
        add(singleButton);

        JButton saveButton = new JButton("输出操作结果");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    BaseUtils_2.saveExcel(grabJF189);
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
                grabJF189.loggerTextArea.setText(StringUtils.EMPTY);
            }
        });
        add(clearLogButton);

        JButton resetNetButton = new JButton("重新联网");
        resetNetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //刷新网络
                BaseUtils_2.refreshNetWork(grabJF189);
                //显示外网ip
                showOutIp(grabJF189);
            }
        });
        add(resetNetButton);
        //显示外网ip
        showOutIp(grabJF189);
        add(outIpLabel);

        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.LIGHT_GRAY, Color.blue));
    }

    /**
     * 显示外网ip
     */
    public void showOutIp(GrabJF189_2 grabJF189){
        String outIp = BaseUtils_2.getIpAddress(grabJF189);
        grabJF189.centerPanel.outIp = outIp;
        outIpLabel.setText("外网ip:" + outIp);
    }
}
