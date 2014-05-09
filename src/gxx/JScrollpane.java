package gxx;

/**
 * Create by Gxx
 * Time: 2013-10-09 00:07
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JScrollpane implements ActionListener{
    JScrollPane scrollPane;
    public JScrollpane(){
        JFrame f=new JFrame("JScrollpane1");
        //Container contentPane=f.getContentPane();

        JLabel label1=new JLabel(new ImageIcon("C:\\Documents and Settings\\Administrator\\����\\psb.jpg"));
        JPanel panel1=new JPanel();
        panel1.add(label1);
        scrollPane=new JScrollPane(panel1);
        JPanel panel2=new JPanel(new GridLayout(3,1));
        JButton b=new JButton("��ʾˮƽ������");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("��Ҫ��ʾˮƽ������");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("��ʱ��ʾˮƽ������");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("��ʾ��ֱ������");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("��Ҫ��ʾ��ֱ������");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("��ʱ��ʾ��ֱ������");
        b.addActionListener(this);
        panel2.add(b);
        f.add(panel2,BorderLayout.WEST);
        f.add(scrollPane,BorderLayout.CENTER);

        f.setLocationRelativeTo(null);
        f.setSize(new Dimension(550,320));
        f.setVisible(true);
    }
    /*����JScollPane���Ƿ���ʾ���������������
    *HORIZONTAL_SCROLLBAR_ALWAYS��������ʾˮƽ������
    *HORIZONTAL_SCROLLBAR_AS_NEEDED:��Ҫ��ʱ����ʾˮƽ������
    *HORIZONTAL_SCROLLBAR_NEVER:��������ʾˮƽ������
    *VERTICAL_SCROLLBAR_ALWAYS:������ʾ��ֱ������
    *VERTICAL_SCROLLBAR_AS_NEEDED����Ҫ��ʱ����ʾ��ֱ������
    *VERTICAL_SCROLLBAR_NEVER:��������ʾ��ֱ������
    */
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("��ʾˮƽ������"))

            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        if(e.getActionCommand().equals("��Ҫ��ʾˮƽ������"))
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        if(e.getActionCommand().equals("��ʱ��ʾˮƽ������"))
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        if(e.getActionCommand().equals("��ʾ��ֱ������"))
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        if(e.getActionCommand().equals("��Ҫ��ʾ��ֱ������"))
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        if(e.getActionCommand().equals("��ʱ��ʾ��ֱ������"))
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.revalidate();//��������JScrollPane
    }
    public static void main(String[] args){
        new JScrollpane();
    }
}