package gxx2;

import gxx.HttpClientUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * �м�������
 * Create by Gxx
 * Time: 2013-10-08 23:35
 */
public class CenterPanel_2 extends JPanel implements BaseInterface_2
{
    /**
     * ��־��¼��
     */
    static Logger logger = Logger.getLogger(CenterPanel_2.class);

    /**
     * ���
     */
    JTabbedPane tab;

    /**
     * ���
     */
    JTable table;

    /**
     * �ɹ����
     */
    JTable successTable;

    /**
     * ���
     */
    JPanel panel;

    /**
     * ��־�ı���
     */
    JTextArea loggerTextArea;

    /**
     * ������
     */
    JPanel help;

    /**
     * ץȡ��
     */
    GrabJF189_2 grabJF189;

    /**
     * �������
     */
    String springWebFlow = StringUtils.EMPTY;
    Cookie[] cookies = new Cookie[]{};
    boolean loadCheckSuccess = false;
    MoneyGoodsList moneyGoodsList = new MoneyGoodsList();
    String orderId = StringUtils.EMPTY;
    String viewState = StringUtils.EMPTY;
    boolean msgSendSuccess = false;
    MoneyGoods moneyGoods = new MoneyGoods();
    int buyNum = 0;
    String mobile = StringUtils.EMPTY;
    String userName = StringUtils.EMPTY;
    String jf = StringUtils.EMPTY;
    List<Order> orderList = new ArrayList<Order>();
    String outIp = StringUtils.EMPTY;
    int successCount = 0;//�����û��ĳɹ������ܺ� �û�������
    int selectRow = 0;//ѡ���û�����
    boolean isAlreadyBrowse = false;//ֻ���������ε����������һ�ε�¼�ɹ��������ڶ��η����ֻ���֤��ɹ�����

    /**
     * ���캯��
     */
    public CenterPanel_2(GrabJF189_2 grabJF189)
    {
        this.grabJF189 = grabJF189;

        //���
        tab = new JTabbedPane(JTabbedPane.TOP);
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        //���
        panel = new JPanel();
        JPanel pane = new JPanel();
        //pane.add(table.getTableHeader(), BorderLayout.NORTH);
        pane.add(panel);

        setPanel();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        scrollPane.setViewportView(pane);

        //���
        table = new JTable(100, EXCEL_TITLE.length);
        table.setBorder(new LineBorder(Color.BLACK));
        setTableColumnWidth();
        JPanel tablePane = new JPanel();
        //pane.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePane.add(table);

        JScrollPane tableScrollPane = new JScrollPane();
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        tableScrollPane.setViewportView(tablePane);

        //���
        successTable = new JTable(100, 11);
        successTable.setBorder(new LineBorder(Color.BLACK));
        BaseUtils_2.clearSuccessAllAndInitTitle(successTable);

        setSuccessTableColumnWidth();
        JPanel successTablePane = new JPanel();
        //pane.add(table.getTableHeader(), BorderLayout.NORTH);
        successTablePane.add(successTable);

        JScrollPane successTableScrollPane = new JScrollPane();
        successTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        successTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        successTableScrollPane.setViewportView(successTablePane);

        //��־�����
        loggerTextArea = new JTextArea();
        loggerTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        loggerTextArea.setLineWrap(true);
        loggerTextArea.setText("");

        JPanel loggerPanel = new JPanel();
        JScrollPane loggerScrollPane=new JScrollPane(loggerTextArea);
        loggerPanel.setLayout(new GridLayout(1, 1));//�������
        loggerPanel.add(loggerScrollPane);

        //����
        help = new JPanel();
        JPanel helpPane = new JPanel();
        //pane.add(table.getTableHeader(), BorderLayout.NORTH);
        helpPane.add(help);

        setHelpPanel();

        JScrollPane helpScrollPane = new JScrollPane();
        helpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        helpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        helpScrollPane.setViewportView(helpPane);

        tab.addTab("����װ��", tableScrollPane);
        tab.addTab("�һ�����", scrollPane);
        tab.addTab("�ɹ���¼", successTableScrollPane);
        tab.addTab("������־", loggerPanel);
        tab.addTab("����", helpScrollPane);
        tab.setPreferredSize(new Dimension(1050, 600));
        this.add(tab);
        this.setEnabled(true);

        //ˢ�½���
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                refreshUI();
            }
        });

        //ˢ�½���
        helpScrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                refreshUI();
            }
        });
    }

    Button getGoodsButton = new Button();

    Label goodsLabel = new Label();
    JComboBox goodsComboBox = new JComboBox();
    Button chooseGoodsButton = new Button();
    Label pageLabel = new Label();
    TextField page = new TextField("", 10);
    Button changePageButton = new Button();

    Label userNameLabel = new Label();
    TextField userNameTextField = new TextField("", 10);
    Label passwordLabel = new Label();
    TextField passwordTextField = new TextField("", 10);

    Label provinceLabel = new Label();
    JComboBox provinceComboBox = new JComboBox();
    Label cityLabel = new Label();
    JComboBox cityComboBox = new JComboBox();

    Label checkLabel = new Label();
    TextField check = new TextField("", 10);
    JLabel checkImgLabel=new JLabel();
    JPanel checkImgPanel = new JPanel();
    Button loginButton = new Button();
    Button changeCheckImgButton = new Button();

    Label userLabel = new Label();
    TextField buyNumTextField = new TextField("", 5);
    Button makeOrderButton = new Button();
    Button smsCheckButton = new Button();
    Label smsLabel = new Label();
    TextField smsTextField = new TextField("", 20);
    Button payOrderButton = new Button();

    Button reGetGoods = new Button();
    Button reLogin = new Button();

    public void setPanel(){

        //panel.setLayout(new FlowLayout());
        panel.setLayout(new GridLayout(2, 1));
        JPanel upPanel = new JPanel();
        upPanel.setLayout(new FlowLayout());
        panel.add(upPanel);
        JPanel downPanel = new JPanel();
        downPanel.setLayout(new FlowLayout());
        panel.add(downPanel);

        getGoodsButton.setLabel("��ȡ��Ʒ");
        upPanel.add(getGoodsButton);

        goodsLabel.setText("��Ʒ��Ϣ��");
        upPanel.add(goodsLabel);
        upPanel.add(goodsComboBox);
        chooseGoodsButton.setLabel("ѡ��");
        upPanel.add(chooseGoodsButton);
        upPanel.add(pageLabel);
        upPanel.add(page);
        changePageButton.setLabel("��ת");
        upPanel.add(changePageButton);

        userNameLabel.setText("�û�����");
        upPanel.add(userNameLabel);
        userNameTextField.setText("");
        upPanel.add(userNameTextField);
        passwordLabel.setText("���룺");
        upPanel.add(passwordLabel);
        passwordTextField.setText("");
        upPanel.add(passwordTextField);

        provinceLabel.setText("ʡ��");
        upPanel.add(provinceLabel);
        upPanel.add(provinceComboBox);
        cityLabel.setText("�У�");
        upPanel.add(cityLabel);
        upPanel.add(cityComboBox);

        checkLabel.setText("��֤�룺");
        upPanel.add(checkLabel);
        upPanel.add(check);
        checkImgPanel.add(checkImgLabel, new Integer(Integer.MIN_VALUE));
        upPanel.add(checkImgPanel);
        changeCheckImgButton.setLabel("ˢ��");
        upPanel.add(changeCheckImgButton);
        loginButton.setLabel("�ύ");
        upPanel.add(loginButton);

        userLabel.setText("");
        upPanel.add(userLabel);
        buyNumTextField.setText("1");
        upPanel.add(buyNumTextField);
        makeOrderButton.setLabel("���ɶ���");
        downPanel.add(makeOrderButton);
        smsCheckButton.setLabel("������֤��");
        downPanel.add(smsCheckButton);
        smsLabel.setText("�ֻ���֤�룺");
        downPanel.add(smsLabel);
        downPanel.add(smsTextField);
        payOrderButton.setLabel("��");
        downPanel.add(payOrderButton);

        reGetGoods.setLabel("������Ʒ");
        downPanel.add(reGetGoods);
        reLogin.setLabel("���µ�¼");
        downPanel.add(reLogin);

        //��ʼ��ʡ��Ϣ
        initProvinceComboBox();

        //��ʾ��ȡ��Ʒ�������
        showGetGoodsComps();
        //Ӱ��ѡ���Ʒ�������
        hideGoodsComps();
        //Ӱ�ص�½�������
        hideLoginComps();
        //Ӱ��֧���������
        hidePayComps();
        //Ӱ�ظ�����Ʒ��ť
        reGetGoods.setVisible(false);
        //Ӱ�����µ�¼��ť
        reLogin.setVisible(false);

        //������Ʒ��ť
        reGetGoods.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //���������Ʒ��ť
                clickReGetGoods();
            }
        });
        //���µ�¼��ť
        reLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //������µ�¼��ť
               clickReLogin();
            }
        });
        //��ȡ��Ʒ��ť
        getGoodsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(grabJF189.inputDtoList.size() == 0){
                    BaseUtils_2.alertWarnMessage("������Excel��");
                    return;
                }
                //��ȡ�ֽ��Ʒ�б�
                Map result = BaseUtils_2.getMoneyGoodsListByPage(grabJF189, 1);
                boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if (!isSuccess) {
                    return;
                }
                moneyGoodsList = (MoneyGoodsList) result.get(KEY_MONEY_GOODS_LIST);
                //����Ʒ�б�
                fillGoodsComboBox();
                //Ӱ�ػ�ȡ��Ʒ�������
                hideGetGoodsComps();
                //��ʾѡ���Ʒ�������
                showGoodsComps();
            }
        });

        chooseGoodsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //��ȡѡ���Ʒid
                String chooseGoodsValue = goodsComboBox.getSelectedItem().toString();
                String indexStr = "ID:[";
                int index = chooseGoodsValue.indexOf(indexStr);
                chooseGoodsValue = chooseGoodsValue.substring(index + indexStr.length());
                indexStr = "]";
                index = chooseGoodsValue.indexOf(indexStr);
                String goodsId = chooseGoodsValue.substring(0, index);
                for(MoneyGoods moneyGoods1 : moneyGoodsList.moneyGoodsList){
                    if(moneyGoods1.getGoodsId().equals(goodsId)){
                        moneyGoods = moneyGoods1;
                    }
                }

                //Ӱ��ѡ���Ʒ�������
                hideGoodsComps();
                //��ʾ��½�������
                showLoginComps();
                // ������֤��
                loadCheckImg();
                //��ʾ������Ʒ��ť
                reGetGoods.setVisible(true);
                //Ӱ�����µ�¼��ť
                reLogin.setVisible(false);
            }
        });
        changePageButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //��ȡ�ֽ��Ʒ�б�
                Map result = BaseUtils_2.getMoneyGoodsListByPage(grabJF189, Integer.parseInt(page.getText()));
                boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if (!isSuccess) {
                    return;
                }
                moneyGoodsList = (MoneyGoodsList) result.get(KEY_MONEY_GOODS_LIST);
                //����Ʒ�б�
                fillGoodsComboBox();
            }
        });
        provinceComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                /**
                 * ������ֵ�任������ᴥ�����Σ�
                 * 2���任ǰ�Ĵ����¼�
                 * 1���任��Ĵ����¼�
                 */
                if(1 == itemEvent.getStateChange()){
                    fillCityComboBox();
                }
            }
        });
        //��½��ť
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!loadCheckSuccess) {
                    String warnMessage = "δ�ɹ�����ͼƬ��֤��";
                    BaseUtils_2.alertWarnMessage(warnMessage);
                    return;
                }
                Map result;
                try {
                    //�ύ��½
                    result = login();
                } catch (Exception e) {
                    String errorMessage = "��½�쳣�������µ�½��";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if (!isSuccess) {
                    String errorMessage = "��½ʧ�ܣ������µ�½��";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String) result.get(KEY_RESP_STR);//���Ƿ�ɹ�

                //�õ���ת��ַ
                Map headerMap = (Map) result.get(KEY_RESPONSE_HEADER_MAP);//����ͷ
                String jumpUrl = (String) headerMap.get("Location");
                //��ת
                result = BaseUtils_2.jump(grabJF189, jumpUrl);
                BaseUtils_2.browse((String)result.get(KEY_BROWSE_URL1));
                isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if (!isSuccess) {
                    return;
                }
                cookies = (Cookie[]) result.get(KEY_RESPONSE_COOKIES);//����ͷ

                //��ȡ��½��Ϣ
                try{
                    refreshLoginInfo(cookies);
                } catch (Exception e){
                    String errorMessage = "��¼�ɹ������ǻ�ȡ��½��Ϣʧ�ܣ����µ�½...";
                    logger.error(errorMessage, e);
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    try{
                        refreshLoginInfo(cookies);
                    } catch (Exception e1){
                        errorMessage = "��¼�ɹ������ǻ�ȡ��½��Ϣʧ�ܣ������µ�½��";
                        logger.error(errorMessage, e1);
                        BaseUtils_2.loggerOut(grabJF189, errorMessage);
                        BaseUtils_2.alertErrorMessage(errorMessage);
                        return;
                    }
                }

                //Ӱ�ص�½�������
                hideLoginComps();
                //��ʾ֧���������
                showPayComps();
                //��ʾ������Ʒ��ť
                reGetGoods.setVisible(true);
                //��ʾ���µ�¼��ť
                reLogin.setVisible(true);
            }
        });
        //ˢ����֤��
        changeCheckImgButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // ������֤��
                loadCheckImg();
            }
        });
        //���ɶ���
        makeOrderButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //��������
                buyNum = Integer.parseInt(buyNumTextField.getText());
                /**
                 * 1 ��ȡviewState
                 */
                String url = "http://jf.189.cn/duihuan/MakeOrder.aspx?ID=" + moneyGoods.getGoodsId() +
                        "&Num=" + buyNum + "&PayType=0";
                logger.info("1 ��ȡviewState=>url=" + url);
                Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);
                //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
                boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if(!isSuccess)
                {
                    String errorMessage = "���ɶ���ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
                logger.info("+++++++++++��������" + webContent);

                viewState = BaseUtils_2.getViewStateFromMakeOrder(webContent);
                logger.info(viewState);

                /**
                 * 2 �ύ���ɶ���
                 */
                logger.info("2 �ύ���ɶ���=>url=" + url);
                PostMethod method = new PostMethod(url);
                //����
                method.addParameter("__VIEWSTATE", viewState);
                method.addParameter("txtBuy", StringUtils.EMPTY + buyNum);
                method.addParameter("txtContectName", "");
                method.addParameter("txtContectTel", "");
                method.addParameter("DropDownList1", "0");
                method.addParameter("txtAddRess", "");
                method.addParameter("txtSMS", "");
                method.addParameter("txtEmail", "");
                method.addParameter("Button1", "??????");
                method.addParameter("lbl_provice", "?????");
                method.addParameter("lbl_city", "");
                method.addParameter("lbl_area", "");
                method.addParameter("lbl_area_code", "");
                method.addParameter("lbl_city_code", "");
                method.addParameter("hdUseType", "");
                method.addParameter("hdSelectAddr", "");
                method.addParameter("giftTYPE", "2");
                method.addParameter("GOODSSTORE", "5559");
                //�������
                try {
                    result = HttpClientUtils.connect(method, "GBK", cookies);//���ؽ��
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
                isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�

                if(!isSuccess)
                {
                    String errorMessage = "���ɶ���ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
                logger.info("+++++++++++��������" + webContent);

                /**
                 * 3.��ö����� �� ��ת��ַ ����ת
                 */
                url = BaseUtils_2.getOrderUrl(webContent);
                logger.info("3 ��ö����� �� ��ת��ַ ����ת=>url=" + url);
                result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);
                //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
                isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if(!isSuccess)
                {
                    String errorMessage = "��ö����Ų���ת��ַʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
                logger.info("+++++++++++��������" + webContent);
                viewState = BaseUtils_2.getViewStateFromMakeOrder(webContent);
                logger.info(viewState);

                orderId = url.substring(url.indexOf("OrderID=") + "OrderID=".length());//������

                String message = "���ɶ����ɹ���������:[" + orderId + "]��";
                BaseUtils_2.loggerOut(grabJF189, message);
                BaseUtils_2.alertMessage(message);
            }
        });
        //�����ֻ���֤��
        smsCheckButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(StringUtils.EMPTY.equals(orderId)){
                    String warmMessage = "�������ɶ�����";
                    BaseUtils_2.loggerOut(grabJF189, warmMessage);
                    BaseUtils_2.alertWarnMessage(warmMessage);
                    return;
                }
                /**
                 * 4.��ȡ��֤��
                 * "1" : "����������ѷ�����" + �ֻ��� + "������15�����������֤��"
                 * "2" : "����������ȡ̫Ƶ����һ���Ӻ�����ط���"
                 * ���� : "��������뷢��ʧ�ܣ����Ժ����ԣ�"
                 */
                String url = "http://jf.189.cn/XNDH/ajaxManager.aspx";
                logger.info("4.��ȡ��֤��=>url=" + url);
                logger.info("�����ţ�" + orderId);
                PostMethod method = new PostMethod(url);
                //����
                InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(selectRow-1);
                if(LOGIN_TYPE_MB.equals(dto.getLoginType())){
                    method.addParameter("Type", "CreateRndCode");
                } else if(LOGIN_TYPE_PH.equals(dto.getLoginType())){
                    method.addParameter("Type", "CreateIVRCode");
                } else if(LOGIN_TYPE_NET.equals(dto.getLoginType())){
                    method.addParameter("Type", "CreateRndCode");
                } else if(LOGIN_TYPE_CARD.equals(dto.getLoginType())){
                    method.addParameter("Type", "CreateRndCode");
                } else {
                    throw new RuntimeException("��Ч�ĵ�¼��ʽ��");
                }
                method.addParameter("orderid", orderId);
                Map result = new HashMap();
                //�������
                try {
                    result = HttpClientUtils.connect(method, "GBK", cookies);//���ؽ��
                } catch (Exception e) {
                    e.printStackTrace();
                    result.put(KEY_IS_SUCCESS, false);
                }
                //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
                boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if(!isSuccess)
                {
                    String errorMessage = "��ȡ��֤��ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
                logger.info("+++++++++++��������" + webContent);
                //�ж��ŷ��ͳɹ�
                if("1".equals(webContent) || "true".equalsIgnoreCase(webContent)){
                    msgSendSuccess = true;
                    String message = "������֤�뷢�ͳɹ���";
                    BaseUtils_2.loggerOut(grabJF189, message);
                    BaseUtils_2.alertMessage(message);

                    url = "http://jf.189.cn/XNDH/DummyOrderPay.aspx?OrderID=" + orderId;
                    //ֻ���������ε����������һ�ε�¼�ɹ�����(Ϊ������������ֻ���)���ڶ��η����ֻ���֤��ɹ�����
                    if(!isAlreadyBrowse){
                        BaseUtils_2.browse(url);
                        isAlreadyBrowse = true;
                    }
                } else {
                    msgSendSuccess = false;
                    String errorMessage = "������֤�뷢��ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                }
            }
        });

        //�򵥰�ť
        payOrderButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(StringUtils.EMPTY.equals(orderId)){
                    String warnMessage = "�������ɶ�����";
                    BaseUtils_2.loggerOut(grabJF189, warnMessage);
                    BaseUtils_2.alertWarnMessage(warnMessage);
                    return;
                }
                if(!msgSendSuccess){
                    String warnMessage = "����δ���ͳɹ���";
                    BaseUtils_2.loggerOut(grabJF189, warnMessage);
                    BaseUtils_2.alertWarnMessage(warnMessage);
                    return;
                }
                /**
                 * 5.�ύ
                 */
                String url = "http://jf.189.cn/XNDH/DummyOrderPay.aspx?OrderID=" + orderId;
                logger.info("5.�ύ=>url=" + url);
                PostMethod method = new PostMethod(url);
                //����
                method.addParameter("__VIEWSTATE", viewState);//viewState
                method.addParameter("HidInputVoucher", "0");
                method.addParameter("HidInputIntegral", StringUtils.EMPTY + moneyGoods.getPrice() * buyNum);//����
                method.addParameter("HidInputMoney", "0");
                method.addParameter("cbkIntegral", "on");
                method.addParameter("hidpw", smsTextField.getText());//��֤�� todo ����������
                method.addParameter("hidmac", DateUtil.getDateTime(new Date()) + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                method.addParameter("hiddisk", DateUtil.getDateTime(new Date()) + "cccccccc==");
                method.addParameter("hidcpu", DateUtil.getDateTime(new Date()) + "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd==");
                method.addParameter("submitOrder.x", "0");
                method.addParameter("submitOrder.y", "0");

                /**
                 * __VIEWSTATE	/wEPDwUKLTg1Mjk3NTUzMQ8WCB4Nb3JkZXJfdHlwZV9ucgUBMB4KSXNQcm92aW5jZQUBMB4IdG90YWxwYXkC9AMeEW9yZGVyTmVlZE1peE1vbmV5ZhYMAgEPFgIeBFRleHQFATBkAgMPFgIfBAUEMTQ3NWQCBQ8WAh8EBQM1MDBkAgkPFgIfBAUOJzAyOS04NTI1MjMwMSdkAgsPFgIfBAUHNzYzODk4M2QCDQ9kFhQCAQ8PFgIfBAUBMmRkAgkPDxYCHwQFATBkZAILDxYCHwQFBzc2Mzg5ODNkAg0PFgIfBAXBAjxhIGNsYXNzPSJmbCIgaHJlZj0iL2R1aWh1YW4vcHJvZHVjdHNJbmZvLmFzcHg/SUQ9NTAzMjAiIHRhcmdldD0iX2JsYW5rIj48aW1nIHNyYz0iaHR0cDovLzExNi4yMjguNTUuMTQyL1Blck1lcmNoYW50L2NvbW1vZGl0eS8yMDE0MDEwMi8yMDE0MDEwMjA5MzcxMHMuanBnIiAgb25lcnJvcj10aGlzLnNyYz0naW1hZ2VzL25vLmpwZycgYm9yZGVyPScwJyB3aWR0aD0iODAiIGhlaWdodD0iODAiIC8+PC9hPjxhIGhyZWY9Ii9kdWlodWFuL3Byb2R1Y3RzSW5mby5hc3B4P0lEPTUwMzIwIiB0YXJnZXQ9Il9ibGFuayI+MTE4ODjlhYXlgLzljaE15YWDPC9hPjxiciAvPmQCDw8WAh8EBQ41MDDnp6/liIYrMOWFg2QCEQ8WAh8EBQExZAITDxYCHwQFCeacquaUr+S7mGQCFQ8WAh8EBQM1MDBkAhcPZBYGAgEPFgIeB1Zpc2libGVnFgICBQ8WAh8EBQEwZAIDDxYEHgVjbGFzcwUIc2VsZWN0ZWQfBWcWCgIBDw8WAh8EBQM1MDBkZAIDDxYCHghkaXNhYmxlZGRkAgUPFgIfBAUEMTQ3NWQCBw8PFgIfBAUDNTAwZGQCCQ8WAh8FZ2QCBQ8WAh8FZ2QCGw9kFgQCAQ8WAh8FaBYCAgEPEGRkFgFmZAIFDxYCHwVnZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAgULY2JrSW50ZWdyYWwFC3N1Ym1pdE9yZGVy
                 HidInputVoucher	0
                 HidInputIntegral	500
                 HidInputMoney	0
                 cbkIntegral	on
                 hidpw	lbXiWW6hl6KXxeTuezqX7w==
                 hidmac	rh6qD393AW54hFWbU+J1HMLb6Hx8yWxVNCKNAnbJ9i4nPV4BY5l4jDKsawCR2fY+whdzM9UWJrYE1a6jE8SrWfv6E+ygJZQksygm9fLbHBpg98XsCB7Q0GGLa5PAagCE
                 hiddisk	9WsfkbNVYbjZmcCHzCivFw==
                 hidcpu	+iJLIEzAJ5FIWQ60gSSfQowdSrPC7bvZJXBFHvlGLr87zPN4Ew373hm36SEaZsXZwxeHRb7ZZQAFWAZYttrxmw==
                 submitOrder.x	0
                 submitOrder.y	0
                 */
                Map result = new HashMap();
                //�������
                try {
                    result = HttpClientUtils.connect(method, "GBK", cookies);//���ؽ��
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
                boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if(!isSuccess)
                {
                    String errorMessage = "��ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
                logger.info("+++++++++++��������" + webContent);

                //�򵥺� ���ܳɹ�ʧ�� ˢ�µ�½��Ϣ Ȼ���ٲ�ѯ�������
                try{
                    refreshLoginInfo(cookies);
                } catch (Exception e){
                    String errorMessage = "ˢ���û�ʣ�����ʧ�ܣ�������ѯ���������";
                    logger.error(errorMessage, e);
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                }

                try {
                    //��ѯ�������
                    result = BaseUtils_2.orderDetail(orderId, cookies);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "��ѯ����[" + orderId + "]���ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }

                isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
                if(!isSuccess)
                {
                    String errorMessage = "��ѯ����[" + orderId + "]���ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
                logger.info("+++++++++++��������" + webContent);
                BaseUtils_2.loggerOut(grabJF189, webContent);//todo

                boolean isOrderSuccess = webContent.indexOf("�ɹ�") > -1;
                String cardNo = StringUtils.EMPTY;
                String password = StringUtils.EMPTY;
                String deadLine = StringUtils.EMPTY;
                if(isOrderSuccess){
                    String indexStr = "<li><b>���ţ�</b><span>";
                    int index = webContent.indexOf(indexStr);
                    while (index > 0){
                        webContent = webContent.substring(index + indexStr.length());
                        indexStr = "</span><b>���룺</b><span>";
                        index = webContent.indexOf(indexStr);
                        cardNo = webContent.substring(0, index);
                        webContent = webContent.substring(index + indexStr.length());
                        indexStr = "</span><b>��Ч�ڣ�</b><span>";
                        index = webContent.indexOf(indexStr);
                        password = webContent.substring(0, index);
                        webContent = webContent.substring(index + indexStr.length());
                        indexStr = "</span><b></b><span>";
                        index = webContent.indexOf(indexStr);
                        deadLine = webContent.substring(0, index);
                        //�������
                        Order order = new Order(mobile, userName, moneyGoods.getGoodsId(), moneyGoods.getGoodsName(),
                                moneyGoods.getPrice(), buyNum, orderId, isOrderSuccess, cardNo, password, deadLine);
                        orderList.add(order);

                        //��ӳɹ���¼�������
                        successCount ++;
                        BaseUtils_2.putSuccessTable(successTable, order, successCount) ;

                        indexStr = "<li><b>���ţ�</b><span>";
                        index = webContent.indexOf(indexStr);
                    }
                }

                if(isOrderSuccess){
                    InputDto_2 inputDto = BaseUtils_2.getInputDtoByMobile(mobile, grabJF189);
                    int dtoSuccessCount;
                    try{
                        dtoSuccessCount = Integer.parseInt(inputDto.getSuccessCount());
                    } catch (Exception e){
                        dtoSuccessCount = 0;
                    }
                    // �õ�DefaultTableModel����
                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    tableModel.setValueAt(StringUtils.EMPTY + (dtoSuccessCount+1), selectRow, 5);
                    inputDto.setSuccessCount(StringUtils.EMPTY + (dtoSuccessCount + 1));

                    String message = "����[" + orderId + "]֧���ɹ���";
                    BaseUtils_2.loggerOut(grabJF189, message);
                    BaseUtils_2.alertMessage(message);

                    try {
                        BaseUtils_2.saveExcel(grabJF189);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null, "����������ʧ�ܣ�", "��Ϣ", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String errorMessage = "����[" + orderId + "]֧��ʧ�ܣ�";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                }
                //��ոñʶ���
                orderId = StringUtils.EMPTY;
                for(Order order1 : orderList){
                    logger.info(order1);
                }
            }
        });
    }

    public void setHelpPanel(){
        help.setLayout(new GridLayout(5, 1));
        JPanel help1 = new JPanel();
        help1.setLayout(new GridLayout(13, 1));
        Label label1 = new Label("�ƽ������֤��̳�");
        label1.setAlignment(1);
        help1.add(label1);
        Label label2 = new Label("0.���ܳ����ƽ�ԭ�򣺶�����֤����Ҫ��ҳ����ؼ����ܲ�������������ɣ�");
        help1.add(label2);
        Label label3 = new Label("1.��Ҫ��װChrome���������������ΪĬ�����������Ϊ���ػ����̳�ҳ�棬" +
                "ʵʱ�޸ķ���˵�js�ļ�����ü��ܺ�Ķ�����֤�룬chrome����������");
        help1.add(label3);
        Label label4 = new Label("2.����Excel��ѡ���û�����ȡ��Ʒ��ѡ���Ʒ������ͼƬ��֤�룬�ύ");
        help1.add(label4);
        Label label5 = new Label("3.�ɹ���½�󣬻��Զ�����Chromeҳ�浽��½���ҳ�棬��ʱ��Ҫ�ر��������" +
                "��������Ự����");
        help1.add(label5);
        Label label6 = new Label("4.���빺����������ɶ��������Ͷ�����֤��");
        help1.add(label6);
        Label label7 = new Label("5.�ɹ����Ͷ�����֤��󣬻��Զ�����Chromeҳ�浽����ҳ��");
        help1.add(label7);
        Label label8 = new Label("6.��Chrome����ҳ���ϣ�����������ֱ�������յ��Ķ�����֤�룬" +
                "Ȼ�����Ա��Ҽ�������Ԫ��(N)������ͼ1");
        help1.add(label8);
        Label label9 = new Label("7.����ô�[src=\"Common/scripts/js/dummyPay.js\"]������ͼ2");
        help1.add(label9);
        Label label10 = new Label("8.��dummyPay.js��ͼ����21����������һ��js���룬Ȼ��Ctrl s ���棬�ٵ��" +
                "���Ͻǲ����˳�������ͼ3");
        help1.add(label10);
        TextField textField1 = new TextField(10);
        textField1.setText("document.getElementById(\"hidpw\").size = 50;document." +
                "getElementById(\"hidpw\").type = \"text\";document.getElementById(\"hidpw\").value = " +
                "PwdResult;return;");
        help1.add(textField1);
        Label label11 = new Label("9.���ȷ����ť�������һ���������������Ϊ���ܺ���ֻ���֤�룬�����������" +
                "�ֻ���֤����У�����򵥣�����ͼ4");
        help1.add(label11);
        Label label12 = new Label("10.���Chromeҳ�����ţ�����ҳ����Թرգ��´����ɶ����������ֻ���֤���ֱ" +
                "�������Chromeҳ��������λ�ֻ���֤����ȷ����ť���ɵõ��µļ�����֤�룬���浯����ҳ����Թر�");
        help1.add(label12);
        help.add(help1);

        /**
         * �������� ͼƬ��������
         * Icon icon4 = new ImageIcon("C:\\Users\\sky\\Desktop\\1-4.jpg");
         */
        JLabel label13 = new JLabel();
        Icon icon1 = new ImageIcon(BaseUtils_2.getSameDirWithLib() + "images\\1.jpg");
        label13.setIcon(icon1);
        label13.setBounds(0, 0, icon1.getIconWidth(), icon1.getIconHeight());
        help.add(label13);

        JLabel label14 = new JLabel();
        Icon icon2 = new ImageIcon(BaseUtils_2.getSameDirWithLib() + "images\\2.jpg");
        label14.setIcon(icon2);
        label14.setBounds(0, 0, icon2.getIconWidth(), icon2.getIconHeight());
        help.add(label14);

        JLabel label15 = new JLabel();
        Icon icon3 = new ImageIcon(BaseUtils_2.getSameDirWithLib() + "images\\3.jpg");
        label15.setIcon(icon3);
        label15.setBounds(0, 0, icon3.getIconWidth(), icon3.getIconHeight());
        help.add(label15);

        JLabel label16 = new JLabel();
        Icon icon4 = new ImageIcon(BaseUtils_2.getSameDirWithLib() + "images\\4.jpg");
        label16.setIcon(icon4);
        label16.setBounds(0, 0, icon4.getIconWidth(), icon4.getIconHeight());
        help.add(label16);
    }

    /**
     * ������֤��
     */
    public void loadCheckImg() {
        loadCheckSuccess = false;
        InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(selectRow-1);
        Map result = BaseUtils_2.loadCheckImg(dto);
        loadCheckSuccess = (Boolean)result.get(KEY_IS_SUCCESS);
        if(!loadCheckSuccess){
            String errorMessage = "������֤��ʧ�ܣ�";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return;
        } else {
            String checkImgRoute = (String)result.get(KEY_CHECK_IMG_ROUTE);
            springWebFlow = (String)result.get(KEY_SPRING_WEB_FLOW);
            cookies = (Cookie[])result.get(KEY_RESPONSE_COOKIES);

            Icon icon = new ImageIcon(checkImgRoute);
            checkImgLabel.setIcon(icon);
            checkImgLabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        }
    }

    /**
     * �ύ��½
     */
    public Map login() throws Exception {
        String userNameStr = userNameTextField.getText();
        String passwordStr = passwordTextField.getText();
        String checkStr = check.getText();
        InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(selectRow-1);
        if(LOGIN_TYPE_MB.equals(dto.getLoginType())){
            return BaseUtils_2.loginMB(grabJF189, userNameStr, passwordStr, checkStr, springWebFlow, cookies);
        } else if(LOGIN_TYPE_PH.equals(dto.getLoginType())){
            return BaseUtils_2.loginPH(grabJF189, userNameStr, passwordStr, checkStr, springWebFlow, cookies);
        } else if(LOGIN_TYPE_NET.equals(dto.getLoginType())){
            return BaseUtils_2.loginNet(grabJF189, userNameStr, passwordStr, checkStr, springWebFlow, cookies);
        } else if(LOGIN_TYPE_CARD.equals(dto.getLoginType())){
            return BaseUtils_2.loginCard(grabJF189, userNameStr, passwordStr, checkStr, springWebFlow, cookies);
        } else {
            throw new RuntimeException("��Ч�ĵ�¼��ʽ��");
        }
    }

    /**
     * ����Ʒ�б�
     */
    public void fillGoodsComboBox() {
        pageLabel.setText("��ǰҳ:[" + moneyGoodsList.page + "]�ܹ�ҳ��:[" +
                moneyGoodsList.pageCount + "]ѡ��ҳ��:");
        goodsComboBox.removeAllItems();
        for(MoneyGoods moneyGoods : moneyGoodsList.moneyGoodsList){
            goodsComboBox.addItem("ID:[" + moneyGoods.getGoodsId() + "]����:[" + moneyGoods.getPrice() +
                    "]��Ʒ:[" + moneyGoods.getGoodsName() + "]");
        }
    }

    /**
     * Ӱ�ػ�ȡ��Ʒ�������
     */
    public void hideGetGoodsComps(){
        getGoodsButton.setVisible(false);
    }

    /**
     * ��ʾ��ȡ��Ʒ�������
     */
    public void showGetGoodsComps(){
        getGoodsButton.setVisible(true);
    }

    /**
     * Ӱ�ص�½�������
     */
    public void hideLoginComps(){
        userNameLabel.setVisible(false);
        userNameTextField.setVisible(false);
        passwordLabel.setVisible(false);
        passwordTextField.setVisible(false);
        provinceLabel.setVisible(false);
        provinceComboBox.setVisible(false);
        cityLabel.setVisible(false);
        cityComboBox.setVisible(false);
        checkLabel.setVisible(false);
        check.setVisible(false);
        checkImgLabel.setVisible(false);
        checkImgPanel.setVisible(false);
        loginButton.setVisible(false);
        changeCheckImgButton.setVisible(false);
    }

    /**
     * ��ʾ��½�������
     */
    public void showLoginComps(){
        userNameLabel.setVisible(true);
        userNameTextField.setVisible(true);
        passwordLabel.setVisible(true);
        passwordTextField.setVisible(true);

        provinceLabel.setVisible(true);
        provinceComboBox.setVisible(true);
        cityLabel.setVisible(true);
        cityComboBox.setVisible(true);

        InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(selectRow-1);
        if(LOGIN_TYPE_MB.equals(dto.getLoginType()) || LOGIN_TYPE_SLYH.equals(dto.getLoginType()) ||
                LOGIN_TYPE_TWYH.equals(dto.getLoginType())){
            provinceLabel.setVisible(false);
            provinceComboBox.setVisible(false);
            cityLabel.setVisible(false);
            cityComboBox.setVisible(false);
        } else if(LOGIN_TYPE_CARD.equals(dto.getLoginType())){
            cityLabel.setVisible(false);
            cityComboBox.setVisible(false);
        }

        checkLabel.setVisible(true);
        check.setVisible(true);
        checkImgLabel.setVisible(true);
        checkImgPanel.setVisible(true);
        loginButton.setVisible(true);
        changeCheckImgButton.setVisible(true);
    }

    /**
     * Ӱ��ѡ���Ʒ�������
     */
    public void hideGoodsComps(){
        goodsLabel.setVisible(false);
        goodsComboBox.setVisible(false);
        chooseGoodsButton.setVisible(false);
        pageLabel.setVisible(false);
        page.setVisible(false);
        changePageButton.setVisible(false);
    }

    /**
     * ��ʾѡ���Ʒ�������
     */
    public void showGoodsComps(){
        goodsLabel.setVisible(true);
        goodsComboBox.setVisible(true);
        chooseGoodsButton.setVisible(true);
        pageLabel.setVisible(true);
        page.setVisible(true);
        changePageButton.setVisible(true);
    }

    /**
     * Ӱ��֧���������
     */
    public void hidePayComps(){
        userLabel.setVisible(false);
        buyNumTextField.setVisible(false);
        makeOrderButton.setVisible(false);
        smsCheckButton.setVisible(false);
        smsLabel.setVisible(false);
        smsTextField.setVisible(false);
        payOrderButton.setVisible(false);
    }

    /**
     * ��ʾ֧���������
     */
    public void showPayComps(){
        userLabel.setVisible(true);
        buyNumTextField.setVisible(true);
        makeOrderButton.setVisible(true);
        smsCheckButton.setVisible(true);
        smsLabel.setVisible(true);
        smsTextField.setVisible(true);
        payOrderButton.setVisible(true);
    }

    /**
     * ���ñ���п��
     */
    private void setTableColumnWidth() {
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);
        column = table.getColumnModel().getColumn(2);
        column.setPreferredWidth(140);
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(60);
        column = table.getColumnModel().getColumn(4);
        column.setPreferredWidth(60);
    }

    /**
     * ���óɹ����������
     */
    public void setSuccessTableColumnWidth(){
        TableColumn column = successTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(80);
        column = successTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(50);
        column = successTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(50);
        column = successTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(200);
        column = successTable.getColumnModel().getColumn(4);
        column.setPreferredWidth(60);
        column = successTable.getColumnModel().getColumn(5);
        column.setPreferredWidth(60);
        column = successTable.getColumnModel().getColumn(6);
        column.setPreferredWidth(60);
        column = successTable.getColumnModel().getColumn(7);
        column.setPreferredWidth(60);
        column = successTable.getColumnModel().getColumn(8);
        column.setPreferredWidth(100);
        column = successTable.getColumnModel().getColumn(9);
        column.setPreferredWidth(100);
        column = successTable.getColumnModel().getColumn(10);
        column.setPreferredWidth(100);
    }

    /**
     * ˢ�µ�½��Ϣ ��ҪΪ����ʾʵʱ����
     * @param cookies
     */
    public void refreshLoginInfo(Cookie[] cookies){
        Map result = BaseUtils_2.getLoginInfo(grabJF189, cookies);
        boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if (!isSuccess) {
            return;
        }
        mobile = userNameTextField.getText();
        userName = (String)result.get(KEY_USER_NAME);
        jf = (String)result.get(KEY_JI_FEN);
        userLabel.setText("�ֻ��ţ�[" + mobile + "],�û�������[" + userName + "],���֣�[" + jf +
                "],��Ʒ���ƣ�[" + moneyGoods.getGoodsName() + "],���֣�[" + moneyGoods.getPrice() + "]*������");

        // �õ�DefaultTableModel����
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setValueAt(userName, selectRow, 3);
        tableModel.setValueAt(jf, selectRow, 4);
        InputDto_2 inputDto = BaseUtils_2.getInputDtoByMobile(mobile, grabJF189);
        inputDto.setUserName(userName);
        inputDto.setJf(StringUtils.EMPTY + jf);
    }

    /**
     * ������������ ˢ�½���
     */
    public void refreshUI(){
        int si = tab.getSelectedIndex();
        if(si > 1){
            tab.setSelectedIndex(si - 1);
            tab.setSelectedIndex(si);
        } else if(si >= 0) {
            tab.setSelectedIndex(si + 1);
            tab.setSelectedIndex(si);
        }
    }

    /**
     * ��ʼ��ʡ��Ϣ
     */
    public void initProvinceComboBox(){
        provinceComboBox.removeAllItems();
        for(String province : provinceArray){
            provinceComboBox.addItem(province);
        }
        fillCityComboBox();
    }

    /**
     * ��������Ϣ
     */
    public void fillCityComboBox(){
        cityComboBox.removeAllItems();
        String province = provinceComboBox.getSelectedItem().toString();
        String indexStr = ",";
        int index = province.indexOf(indexStr);
        String provinceNum = province.substring(0, index);
        String provinceName = province.substring(index + indexStr.length());
        String cityStr = cityArray[Integer.parseInt(provinceNum) - 1];
        String[] array = cityStr.split("\\|");
        for(String city : array){
            cityComboBox.addItem(city);
        }
    }

    /**
     * ���������Ʒ��ť
     */
    public void clickReGetGoods(){
        //��ʾ��ȡ��Ʒ�������
        showGetGoodsComps();
        //Ӱ��ѡ���Ʒ�������
        hideGoodsComps();
        //Ӱ�ص�½�������
        hideLoginComps();
        //Ӱ��֧���������
        hidePayComps();
        //Ӱ�ظ�����Ʒ��ť
        reGetGoods.setVisible(false);
        //Ӱ�����µ�¼��ť
        reLogin.setVisible(false);
    }

    /**
     * ������µ�¼��ť
     */
    public void clickReLogin(){
        //���û�в�Ʒ�б�
        if(moneyGoodsList.moneyGoodsList.size() == 0){
            //���������Ʒ��ť
            clickReGetGoods();
            return;
        }
        //Ӱ��ѡ���Ʒ�������
        hideGoodsComps();
        //��ʾ��½�������
        showLoginComps();
        // ������֤��
        loadCheckImg();
        //Ӱ��֧���������
        hidePayComps();
        //��ʾ������Ʒ��ť
        reGetGoods.setVisible(true);
        //Ӱ�����µ�¼��ť
        reLogin.setVisible(false);
    }
}
