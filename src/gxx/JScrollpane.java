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

        JLabel label1=new JLabel(new ImageIcon("C:\\Documents and Settings\\Administrator\\桌面\\psb.jpg"));
        JPanel panel1=new JPanel();
        panel1.add(label1);
        scrollPane=new JScrollPane(panel1);
        JPanel panel2=new JPanel(new GridLayout(3,1));
        JButton b=new JButton("显示水平滚动轴");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("不要显示水平滚动轴");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("适时显示水平滚动轴");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("显示垂直滚动轴");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("不要显示垂直滚动轴");
        b.addActionListener(this);
        panel2.add(b);
        b=new JButton("适时显示垂直滚动轴");
        b.addActionListener(this);
        panel2.add(b);
        f.add(panel2,BorderLayout.WEST);
        f.add(scrollPane,BorderLayout.CENTER);

        f.setLocationRelativeTo(null);
        f.setSize(new Dimension(550,320));
        f.setVisible(true);
    }
    /*控制JScollPane中是否显示滚动轴的六个常量
    *HORIZONTAL_SCROLLBAR_ALWAYS：总是显示水平滚动轴
    *HORIZONTAL_SCROLLBAR_AS_NEEDED:需要的时候，显示水平滚动轴
    *HORIZONTAL_SCROLLBAR_NEVER:从来不显示水平滚动轴
    *VERTICAL_SCROLLBAR_ALWAYS:总是显示垂直滚动轴
    *VERTICAL_SCROLLBAR_AS_NEEDED：需要的时候，显示垂直滚动轴
    *VERTICAL_SCROLLBAR_NEVER:从来不显示垂直滚动轴
    */
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("显示水平滚动轴"))

            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        if(e.getActionCommand().equals("不要显示水平滚动轴"))
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        if(e.getActionCommand().equals("适时显示水平滚动轴"))
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        if(e.getActionCommand().equals("显示垂直滚动轴"))
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        if(e.getActionCommand().equals("不要显示垂直滚动轴"))
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        if(e.getActionCommand().equals("适时显示垂直滚动轴"))
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.revalidate();//重新载入JScrollPane
    }
    public static void main(String[] args){
        new JScrollpane();
    }
}