package gxx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 菜单类
 * Create by Gxx
 * Time: 2013-10-08 23:28
 */
class MyMenu extends JMenuBar
{
    private GrabSN189 grabSN189;
    private JDialog aboutDialog;

    /**
     * 构造函数
     */
    public MyMenu(final GrabSN189 grabSN189)
    {
        this.grabSN189 = grabSN189;

        JMenu fileMenu = new JMenu("菜单");

        JMenuItem exitMenuItem = new JMenuItem("退出", KeyEvent.VK_E);

        JMenuItem aboutMenuItem = new JMenuItem("关于..", KeyEvent.VK_A);

        fileMenu.add(exitMenuItem);

        fileMenu.add(aboutMenuItem);

        this.add(fileMenu);

        aboutDialog = new JDialog();

        initAboutDialog();

        exitMenuItem.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {

                grabSN189.dispose();

                System.exit(0);
            }

        });

        aboutMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                aboutDialog.show();
            }

        });
    }

    public JDialog get()
    {
        return aboutDialog;
    }

    public void initAboutDialog()
    {

        aboutDialog.setTitle("关于");

        Container con = aboutDialog.getContentPane();

        Icon icon = new ImageIcon("sdmile.gif");

        JLabel aboutLabel = new JLabel("" + "Swing!" + "", icon, JLabel.CENTER);

        con.add(aboutLabel, BorderLayout.CENTER);
        aboutDialog.setSize(450, 225);
        aboutDialog.setLocation(300, 300);
        aboutDialog.addWindowListener(new WindowAdapter()
        {
            public void WindowClosing(WindowEvent e)
            {
                grabSN189.dispose();
            }
        });
    }
}
