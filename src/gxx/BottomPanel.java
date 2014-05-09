package gxx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ½ø¶ÈÌõ
 * Create by Gxx
 * Time: 2013-10-09 00:46
 */
public class BottomPanel extends JPanel
{
    private JProgressBar pb;
    int counter;

    public BottomPanel()
    {
        pb = new JProgressBar();
        pb.setPreferredSize(new Dimension(680, 20));
        Timer time = new Timer(1, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                counter++;
                pb.setValue(counter);
                Timer t = (Timer) e.getSource();
                if (counter == pb.getMaximum())
                {
                    t.stop();
                    counter = 0;
                    t.start();
                }
            }
        });
        time.start();
        pb.setStringPainted(true);
        pb.setMinimum(0);
        pb.setMaximum(1000);
        pb.setBackground(Color.white);
        pb.setForeground(Color.gray);
        this.add(pb);
        this.setVisible(false);
    }

    public void showCanSee()
    {
        this.counter = 0;
        this.setVisible(true);
    }

    public void showCanNotSee()
    {
        this.setVisible(false);
    }

    public void setProcessBar(BoundedRangeModel rangeModel)
    {
        pb.setModel(rangeModel);
    }
}