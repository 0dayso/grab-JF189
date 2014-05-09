package gxx2;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class JComboBoxSecond extends JFrame{
    JPanel contentPane;
    JScrollPane jScrollPane1=new JScrollPane();
    Object[][]objContent={{"Tom","Hanks","Java","24","true"},{"Ben","John",
            "Java","25","true"},{"Michael","Fred","Java","26","true"},
            {"Steve","Bell","Java","27","true"},
            {"Jim","Lan","Java","29","true"}
    };
    Object[]objHead={"名字","姓氏","技能","年龄","是否属实"};
    JTable jTable1=new JTable(objContent,objHead);
    JComboBox jComBo=new JComboBox();
    JCheckBox jCheck=new JCheckBox("");
    public JComboBoxSecond(){
        try{
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     *Componentinitialization.
     *
     *@throwsjava.lang.Exception
     */
    private void jbInit() throws Exception{
        contentPane=(JPanel)getContentPane();
        contentPane.setLayout(null);
        setSize(new Dimension(467,401));
        setTitle("FrameTitle");
        jScrollPane1.setBounds(new Rectangle(11,20,442,337));
        jComBo.addItem("Java");
        jComBo.addItem("Orcale");
        jComBo.addItem("Linux");
        jComBo.addItem("Unix");
        jComBo.addItem("C#");

        TableColumn tableCol=jTable1.getColumnModel().getColumn(2);
        tableCol.setResizable(false);
        TableColumn tableColCheck=jTable1.getColumnModel().getColumn(4);
        tableCol.setCellEditor(new DefaultCellEditor(jComBo));
        tableColCheck.setCellEditor(new DefaultCellEditor(jCheck));
        contentPane.add(jScrollPane1);
        jScrollPane1.getViewport().add(jTable1);
    }

    public static void main(String[] parma){
         new JComboBoxSecond();
    }
}