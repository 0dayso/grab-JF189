package gxx2;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * �ұ�������
 * Create by Gxx
 * Time: 2013-10-09 00:28
 */
class RightPanel_2 extends JPanel implements BaseInterface_2
{
    /**
     * ������
     */
    private GrabJF189_2 grabJF189;

    /**
     * �ļ�·��
     */
    public String filePath;

    /**
     * ��Ч��
     */
    Label deadLineLabel = new Label();

    /**
     * ����ip
     */
    Label outIpLabel = new Label();

    /**
     * �Ƿ񵯳�IE
     */
    JButton ieButton;

    /**
     * ���캯��
     */
    public RightPanel_2(final GrabJF189_2 grabJF189)
    {
        this.grabJF189 = grabJF189;

        this.setLayout(new GridLayout(16, 1));

        //��Ч��
        deadLineLabel.setText("��Ч��:" + DateUtil.getCNDate(DateUtil.getDate(
                PropertyUtil.getInstance().getProperty(CONFIG_DEAD_LINE))));
        add(deadLineLabel);

        JButton loadButton = new JButton("����Excel");
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
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        add(loadButton);

        JButton singleButton = new JButton("ѡ���û�");
        singleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(grabJF189.inputDtoList.size() == 0){
                    BaseUtils_2.alertWarnMessage("������Excel��");
                    return;
                }
                int[] selectRows = grabJF189.table.getSelectedRows();
                if(0 == selectRows.length)
                {
                    BaseUtils_2.alertWarnMessage("��ѡ��һ�����ݣ�");
                    return;
                } else if(1 < selectRows.length)
                {
                    BaseUtils_2.alertWarnMessage("ֻ��ѡ��һ�����ݣ�");
                    return;
                }

                int selectRow = grabJF189.table.getSelectedRow();
                if(0 == selectRow)
                {
                    BaseUtils_2.alertWarnMessage("����ѡ���һ��̧ͷ���ݣ�");
                    return;
                }
                InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(selectRow-1);
                if(!dto.isValidate){
                    BaseUtils_2.alertWarnMessage("������������ʧ�ܣ�[" + dto.getErrorMsg() + "]����ѡ���������ݣ�");
                    return;
                }
                grabJF189.centerPanel.selectRow = selectRow;
                grabJF189.centerPanel.userNameTextField.setText(dto.getMobile());
                grabJF189.centerPanel.passwordTextField.setText(dto.getPassword());
                grabJF189.centerPanel.tab.setSelectedIndex(1);
                //������µ�¼��ť
                grabJF189.centerPanel.clickReLogin();
            }
        });
        add(singleButton);

        JButton saveButton = new JButton("����������");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    BaseUtils_2.saveExcel(grabJF189);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "����������ʧ�ܣ�", "��Ϣ", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton);

        ieButton = new JButton("�л������������");
        ieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabJF189.centerPanel.tanIe = !grabJF189.centerPanel.tanIe;
                ieButton.setText("�л����������" + (grabJF189.centerPanel.tanIe?"":"��") + "��");
            }
        });
        //add(ieButton);

        JButton clearLogButton = new JButton("�����־");
        clearLogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grabJF189.loggerTextArea.setText(StringUtils.EMPTY);
            }
        });
        add(clearLogButton);

        JButton resetNetButton = new JButton("��������");
        resetNetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //ˢ������
                BaseUtils_2.refreshNetWork(grabJF189);
                //��ʾ����ip
                showOutIp(grabJF189);
            }
        });
        add(resetNetButton);

        JButton openSaveButton = new JButton("�򿪱����ļ���");
        openSaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String filePath = BaseUtils_2.getSameDirWithLib() + "save";
                String[] cmd = new String[5];
                cmd[0] = "cmd";
                cmd[1] = "/c";
                cmd[2] = "start";
                cmd[3] = " ";
                cmd[4] = filePath;
                try {
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        add(openSaveButton);

        JButton logOutButton = new JButton("�˳�");
        logOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //���µ�½
                grabJF189.centerPanel.clickReLogin();
            }
        });
        add(logOutButton);

        //��ʾ����ip
        showOutIp(grabJF189);
        add(outIpLabel);

        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.LIGHT_GRAY, Color.blue));
    }

    /**
     * ��ʾ����ip
     */
    public void showOutIp(GrabJF189_2 grabJF189){
        String outIp = BaseUtils_2.getIpAddress(grabJF189);
        grabJF189.centerPanel.outIp = outIp;
        outIpLabel.setText("����ip:" + outIp);
    }
}
