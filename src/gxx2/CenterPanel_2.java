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
 * 中间内容类
 * Create by Gxx
 * Time: 2013-10-08 23:35
 */
public class CenterPanel_2 extends JPanel implements BaseInterface_2
{
    /**
     * 日志记录器
     */
    static Logger logger = Logger.getLogger(CenterPanel_2.class);

    /**
     * 外框
     */
    JTabbedPane tab;

    /**
     * 表格
     */
    JTable table;

    /**
     * 成功表格
     */
    JTable successTable;

    /**
     * 组件
     */
    JPanel panel;

    /**
     * 日志文本框
     */
    JTextArea loggerTextArea;

    /**
     * 帮助框
     */
    JPanel help;

    /**
     * 抓取类
     */
    GrabJF189_2 grabJF189;

    /**
     * 各个组件
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
    int successCount = 0;//所有用户的成功总数总和 用户算行数
    int selectRow = 0;//选择用户行数
    boolean isAlreadyBrowse = false;//只允许弹出两次到浏览器，第一次登录成功弹出，第二次发送手机验证码成功弹出

    /**
     * 构造函数
     */
    public CenterPanel_2(GrabJF189_2 grabJF189)
    {
        this.grabJF189 = grabJF189;

        //外框
        tab = new JTabbedPane(JTabbedPane.TOP);
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        //组件
        panel = new JPanel();
        JPanel pane = new JPanel();
        //pane.add(table.getTableHeader(), BorderLayout.NORTH);
        pane.add(panel);

        setPanel();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        scrollPane.setViewportView(pane);

        //表格
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

        //表格
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

        //日志输入框
        loggerTextArea = new JTextArea();
        loggerTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        loggerTextArea.setLineWrap(true);
        loggerTextArea.setText("");

        JPanel loggerPanel = new JPanel();
        JScrollPane loggerScrollPane=new JScrollPane(loggerTextArea);
        loggerPanel.setLayout(new GridLayout(1, 1));//加上这句
        loggerPanel.add(loggerScrollPane);

        //帮助
        help = new JPanel();
        JPanel helpPane = new JPanel();
        //pane.add(table.getTableHeader(), BorderLayout.NORTH);
        helpPane.add(help);

        setHelpPanel();

        JScrollPane helpScrollPane = new JScrollPane();
        helpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        helpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) ;
        helpScrollPane.setViewportView(helpPane);

        tab.addTab("数据装载", tableScrollPane);
        tab.addTab("兑换积分", scrollPane);
        tab.addTab("成功记录", successTableScrollPane);
        tab.addTab("操作日志", loggerPanel);
        tab.addTab("帮助", helpScrollPane);
        tab.setPreferredSize(new Dimension(1050, 600));
        this.add(tab);
        this.setEnabled(true);

        //刷新界面
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                refreshUI();
            }
        });

        //刷新界面
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

        getGoodsButton.setLabel("获取产品");
        upPanel.add(getGoodsButton);

        goodsLabel.setText("产品信息：");
        upPanel.add(goodsLabel);
        upPanel.add(goodsComboBox);
        chooseGoodsButton.setLabel("选择");
        upPanel.add(chooseGoodsButton);
        upPanel.add(pageLabel);
        upPanel.add(page);
        changePageButton.setLabel("跳转");
        upPanel.add(changePageButton);

        userNameLabel.setText("用户名：");
        upPanel.add(userNameLabel);
        userNameTextField.setText("");
        upPanel.add(userNameTextField);
        passwordLabel.setText("密码：");
        upPanel.add(passwordLabel);
        passwordTextField.setText("");
        upPanel.add(passwordTextField);

        provinceLabel.setText("省：");
        upPanel.add(provinceLabel);
        upPanel.add(provinceComboBox);
        cityLabel.setText("市：");
        upPanel.add(cityLabel);
        upPanel.add(cityComboBox);

        checkLabel.setText("验证码：");
        upPanel.add(checkLabel);
        upPanel.add(check);
        checkImgPanel.add(checkImgLabel, new Integer(Integer.MIN_VALUE));
        upPanel.add(checkImgPanel);
        changeCheckImgButton.setLabel("刷新");
        upPanel.add(changeCheckImgButton);
        loginButton.setLabel("提交");
        upPanel.add(loginButton);

        userLabel.setText("");
        upPanel.add(userLabel);
        buyNumTextField.setText("1");
        upPanel.add(buyNumTextField);
        makeOrderButton.setLabel("生成订单");
        downPanel.add(makeOrderButton);
        smsCheckButton.setLabel("发送验证码");
        downPanel.add(smsCheckButton);
        smsLabel.setText("手机验证码：");
        downPanel.add(smsLabel);
        downPanel.add(smsTextField);
        payOrderButton.setLabel("买单");
        downPanel.add(payOrderButton);

        reGetGoods.setLabel("更换产品");
        downPanel.add(reGetGoods);
        reLogin.setLabel("重新登录");
        downPanel.add(reLogin);

        //初始化省信息
        initProvinceComboBox();

        //显示获取产品部分组件
        showGetGoodsComps();
        //影藏选择产品部分组件
        hideGoodsComps();
        //影藏登陆部分组件
        hideLoginComps();
        //影藏支付部分组件
        hidePayComps();
        //影藏更换产品按钮
        reGetGoods.setVisible(false);
        //影藏重新登录按钮
        reLogin.setVisible(false);

        //更换产品按钮
        reGetGoods.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //点击更换产品按钮
                clickReGetGoods();
            }
        });
        //重新登录按钮
        reLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //点击重新登录按钮
               clickReLogin();
            }
        });
        //获取产品按钮
        getGoodsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(grabJF189.inputDtoList.size() == 0){
                    BaseUtils_2.alertWarnMessage("请载入Excel！");
                    return;
                }
                //获取现金产品列表
                Map result = BaseUtils_2.getMoneyGoodsListByPage(grabJF189, 1);
                boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//判是否成功
                if (!isSuccess) {
                    return;
                }
                moneyGoodsList = (MoneyGoodsList) result.get(KEY_MONEY_GOODS_LIST);
                //填充产品列表
                fillGoodsComboBox();
                //影藏获取产品部分组件
                hideGetGoodsComps();
                //显示选择产品部分组件
                showGoodsComps();
            }
        });

        chooseGoodsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //获取选择产品id
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

                //影藏选择产品部分组件
                hideGoodsComps();
                //显示登陆部分组件
                showLoginComps();
                // 加载验证码
                loadCheckImg();
                //显示更换产品按钮
                reGetGoods.setVisible(true);
                //影藏重新登录按钮
                reLogin.setVisible(false);
            }
        });
        changePageButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //获取现金产品列表
                Map result = BaseUtils_2.getMoneyGoodsListByPage(grabJF189, Integer.parseInt(page.getText()));
                boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//判是否成功
                if (!isSuccess) {
                    return;
                }
                moneyGoodsList = (MoneyGoodsList) result.get(KEY_MONEY_GOODS_LIST);
                //填充产品列表
                fillGoodsComboBox();
            }
        });
        provinceComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                /**
                 * 下拉框值变换，这里会触发两次：
                 * 2：变换前的触发事件
                 * 1：变换后的触发事件
                 */
                if(1 == itemEvent.getStateChange()){
                    fillCityComboBox();
                }
            }
        });
        //登陆按钮
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!loadCheckSuccess) {
                    String warnMessage = "未成功加载图片验证码";
                    BaseUtils_2.alertWarnMessage(warnMessage);
                    return;
                }
                Map result;
                try {
                    //提交登陆
                    result = login();
                } catch (Exception e) {
                    String errorMessage = "登陆异常，请重新登陆！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//判是否成功
                if (!isSuccess) {
                    String errorMessage = "登陆失败，请重新登陆！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String) result.get(KEY_RESP_STR);//判是否成功

                //得到跳转地址
                Map headerMap = (Map) result.get(KEY_RESPONSE_HEADER_MAP);//返回头
                String jumpUrl = (String) headerMap.get("Location");
                //跳转
                result = BaseUtils_2.jump(grabJF189, jumpUrl);
                BaseUtils_2.browse((String)result.get(KEY_BROWSE_URL1));
                isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//判是否成功
                if (!isSuccess) {
                    return;
                }
                cookies = (Cookie[]) result.get(KEY_RESPONSE_COOKIES);//返回头

                //获取登陆信息
                try{
                    refreshLoginInfo(cookies);
                } catch (Exception e){
                    String errorMessage = "登录成功，但是获取登陆信息失败，重新登陆...";
                    logger.error(errorMessage, e);
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    try{
                        refreshLoginInfo(cookies);
                    } catch (Exception e1){
                        errorMessage = "登录成功，但是获取登陆信息失败，请重新登陆！";
                        logger.error(errorMessage, e1);
                        BaseUtils_2.loggerOut(grabJF189, errorMessage);
                        BaseUtils_2.alertErrorMessage(errorMessage);
                        return;
                    }
                }

                //影藏登陆部分组件
                hideLoginComps();
                //显示支付部分组件
                showPayComps();
                //显示更换产品按钮
                reGetGoods.setVisible(true);
                //显示重新登录按钮
                reLogin.setVisible(true);
            }
        });
        //刷新验证码
        changeCheckImgButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 加载验证码
                loadCheckImg();
            }
        });
        //生成订单
        makeOrderButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //购买数量
                buyNum = Integer.parseInt(buyNumTextField.getText());
                /**
                 * 1 获取viewState
                 */
                String url = "http://jf.189.cn/duihuan/MakeOrder.aspx?ID=" + moneyGoods.getGoodsId() +
                        "&Num=" + buyNum + "&PayType=0";
                logger.info("1 获取viewState=>url=" + url);
                Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);
                //返回结果 可以不带cookie 但是返回会带cookie
                boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
                if(!isSuccess)
                {
                    String errorMessage = "生成订单失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String)result.get(KEY_RESP_STR);//网页内容
                logger.info("+++++++++++返回内容" + webContent);

                viewState = BaseUtils_2.getViewStateFromMakeOrder(webContent);
                logger.info(viewState);

                /**
                 * 2 提交生成订单
                 */
                logger.info("2 提交生成订单=>url=" + url);
                PostMethod method = new PostMethod(url);
                //参数
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
                //结果集合
                try {
                    result = HttpClientUtils.connect(method, "GBK", cookies);//返回结果
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //返回结果 可以不带cookie 但是返回会带cookie
                isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功

                if(!isSuccess)
                {
                    String errorMessage = "生成订单失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                webContent = (String)result.get(KEY_RESP_STR);//网页内容
                logger.info("+++++++++++返回内容" + webContent);

                /**
                 * 3.获得订单号 和 跳转地址 并跳转
                 */
                url = BaseUtils_2.getOrderUrl(webContent);
                logger.info("3 获得订单号 和 跳转地址 并跳转=>url=" + url);
                result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);
                //返回结果 可以不带cookie 但是返回会带cookie
                isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
                if(!isSuccess)
                {
                    String errorMessage = "获得订单号并跳转地址失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                webContent = (String)result.get(KEY_RESP_STR);//网页内容
                logger.info("+++++++++++返回内容" + webContent);
                viewState = BaseUtils_2.getViewStateFromMakeOrder(webContent);
                logger.info(viewState);

                orderId = url.substring(url.indexOf("OrderID=") + "OrderID=".length());//订单号

                String message = "生成订单成功，订单号:[" + orderId + "]！";
                BaseUtils_2.loggerOut(grabJF189, message);
                BaseUtils_2.alertMessage(message);
            }
        });
        //发送手机验证码
        smsCheckButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(StringUtils.EMPTY.equals(orderId)){
                    String warmMessage = "请先生成订单！";
                    BaseUtils_2.loggerOut(grabJF189, warmMessage);
                    BaseUtils_2.alertWarnMessage(warmMessage);
                    return;
                }
                /**
                 * 4.获取验证码
                 * "1" : "短信随机码已发送至" + 手机号 + "。请在15分钟内完成验证。"
                 * "2" : "短信随机码获取太频繁，一分钟后才能重发！"
                 * 其他 : "短信随机码发送失败，请稍后再试！"
                 */
                String url = "http://jf.189.cn/XNDH/ajaxManager.aspx";
                logger.info("4.获取验证码=>url=" + url);
                logger.info("订单号：" + orderId);
                PostMethod method = new PostMethod(url);
                //参数
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
                    throw new RuntimeException("无效的登录方式！");
                }
                method.addParameter("orderid", orderId);
                Map result = new HashMap();
                //结果集合
                try {
                    result = HttpClientUtils.connect(method, "GBK", cookies);//返回结果
                } catch (Exception e) {
                    e.printStackTrace();
                    result.put(KEY_IS_SUCCESS, false);
                }
                //返回结果 可以不带cookie 但是返回会带cookie
                boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//判是否成功
                if(!isSuccess)
                {
                    String errorMessage = "获取验证码失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String)result.get(KEY_RESP_STR);//网页内容
                logger.info("+++++++++++返回内容" + webContent);
                //判短信发送成功
                if("1".equals(webContent) || "true".equalsIgnoreCase(webContent)){
                    msgSendSuccess = true;
                    String message = "短信验证码发送成功！";
                    BaseUtils_2.loggerOut(grabJF189, message);
                    BaseUtils_2.alertMessage(message);

                    url = "http://jf.189.cn/XNDH/DummyOrderPay.aspx?OrderID=" + orderId;
                    //只允许弹出两次到浏览器，第一次登录成功弹出(为了让浏览器保持缓存)，第二次发送手机验证码成功弹出
                    if(!isAlreadyBrowse){
                        BaseUtils_2.browse(url);
                        isAlreadyBrowse = true;
                    }
                } else {
                    msgSendSuccess = false;
                    String errorMessage = "短信验证码发送失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                }
            }
        });

        //买单按钮
        payOrderButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(StringUtils.EMPTY.equals(orderId)){
                    String warnMessage = "请先生成订单！";
                    BaseUtils_2.loggerOut(grabJF189, warnMessage);
                    BaseUtils_2.alertWarnMessage(warnMessage);
                    return;
                }
                if(!msgSendSuccess){
                    String warnMessage = "短信未发送成功！";
                    BaseUtils_2.loggerOut(grabJF189, warnMessage);
                    BaseUtils_2.alertWarnMessage(warnMessage);
                    return;
                }
                /**
                 * 5.提交
                 */
                String url = "http://jf.189.cn/XNDH/DummyOrderPay.aspx?OrderID=" + orderId;
                logger.info("5.提交=>url=" + url);
                PostMethod method = new PostMethod(url);
                //参数
                method.addParameter("__VIEWSTATE", viewState);//viewState
                method.addParameter("HidInputVoucher", "0");
                method.addParameter("HidInputIntegral", StringUtils.EMPTY + moneyGoods.getPrice() * buyNum);//积分
                method.addParameter("HidInputMoney", "0");
                method.addParameter("cbkIntegral", "on");
                method.addParameter("hidpw", smsTextField.getText());//验证码 todo 这里是明文
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
                //结果集合
                try {
                    result = HttpClientUtils.connect(method, "GBK", cookies);//返回结果
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //返回结果 可以不带cookie 但是返回会带cookie
                boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
                if(!isSuccess)
                {
                    String errorMessage = "买单失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                String webContent = (String)result.get(KEY_RESP_STR);//网页内容
                logger.info("+++++++++++返回内容" + webContent);

                //买单好 不管成功失败 刷新登陆信息 然后再查询订单结果
                try{
                    refreshLoginInfo(cookies);
                } catch (Exception e){
                    String errorMessage = "刷新用户剩余积分失败，继续查询订单结果！";
                    logger.error(errorMessage, e);
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                }

                try {
                    //查询订单结果
                    result = BaseUtils_2.orderDetail(orderId, cookies);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "查询订单[" + orderId + "]结果失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }

                isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
                if(!isSuccess)
                {
                    String errorMessage = "查询订单[" + orderId + "]结果失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                    return;
                }
                webContent = (String)result.get(KEY_RESP_STR);//网页内容
                logger.info("+++++++++++返回内容" + webContent);
                BaseUtils_2.loggerOut(grabJF189, webContent);//todo

                boolean isOrderSuccess = webContent.indexOf("成功") > -1;
                String cardNo = StringUtils.EMPTY;
                String password = StringUtils.EMPTY;
                String deadLine = StringUtils.EMPTY;
                if(isOrderSuccess){
                    String indexStr = "<li><b>卡号：</b><span>";
                    int index = webContent.indexOf(indexStr);
                    while (index > 0){
                        webContent = webContent.substring(index + indexStr.length());
                        indexStr = "</span><b>密码：</b><span>";
                        index = webContent.indexOf(indexStr);
                        cardNo = webContent.substring(0, index);
                        webContent = webContent.substring(index + indexStr.length());
                        indexStr = "</span><b>有效期：</b><span>";
                        index = webContent.indexOf(indexStr);
                        password = webContent.substring(0, index);
                        webContent = webContent.substring(index + indexStr.length());
                        indexStr = "</span><b></b><span>";
                        index = webContent.indexOf(indexStr);
                        deadLine = webContent.substring(0, index);
                        //订单结果
                        Order order = new Order(mobile, userName, moneyGoods.getGoodsId(), moneyGoods.getGoodsName(),
                                moneyGoods.getPrice(), buyNum, orderId, isOrderSuccess, cardNo, password, deadLine);
                        orderList.add(order);

                        //添加成功记录到表格中
                        successCount ++;
                        BaseUtils_2.putSuccessTable(successTable, order, successCount) ;

                        indexStr = "<li><b>卡号：</b><span>";
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
                    // 得到DefaultTableModel对象
                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    tableModel.setValueAt(StringUtils.EMPTY + (dtoSuccessCount+1), selectRow, 5);
                    inputDto.setSuccessCount(StringUtils.EMPTY + (dtoSuccessCount + 1));

                    String message = "订单[" + orderId + "]支付成功！";
                    BaseUtils_2.loggerOut(grabJF189, message);
                    BaseUtils_2.alertMessage(message);

                    try {
                        BaseUtils_2.saveExcel(grabJF189);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null, "输出操作结果失败！", "信息", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String errorMessage = "订单[" + orderId + "]支付失败！";
                    BaseUtils_2.loggerOut(grabJF189, errorMessage);
                    BaseUtils_2.alertErrorMessage(errorMessage);
                }
                //清空该笔订单
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
        Label label1 = new Label("破解短信验证码教程");
        label1.setAlignment(1);
        help1.add(label1);
        Label label2 = new Label("0.不能程序破解原因：短信验证码需要网页密码控件加密产生不能随机生成！");
        help1.add(label2);
        Label label3 = new Label("1.需要安装Chrome浏览器，并且设置为默认浏览器，因为加载积分商城页面，" +
                "实时修改服务端的js文件来获得加密后的短信验证码，chrome可以做到！");
        help1.add(label3);
        Label label4 = new Label("2.载入Excel，选择用户，获取产品，选择产品，输入图片验证码，提交");
        help1.add(label4);
        Label label5 = new Label("3.成功登陆后，会自动弹出Chrome页面到登陆后的页面，暂时不要关闭浏览器，" +
                "让浏览器会话保持");
        help1.add(label5);
        Label label6 = new Label("4.输入购买个数，生成订单，发送短信验证码");
        help1.add(label6);
        Label label7 = new Label("5.成功发送短信验证码后，会自动弹出Chrome页面到订单页面");
        help1.add(label7);
        Label label8 = new Label("6.在Chrome订单页面上，短信随机码框直接输入收到的短信验证码，" +
                "然后在旁边右键点击审查元素(N)，如下图1");
        help1.add(label8);
        Label label9 = new Label("7.点击该处[src=\"Common/scripts/js/dummyPay.js\"]，如下图2");
        help1.add(label9);
        Label label10 = new Label("8.在dummyPay.js视图中在21行下新增下一行js代码，然后Ctrl s 保存，再点击" +
                "右上角擦擦退出，如下图3");
        help1.add(label10);
        TextField textField1 = new TextField(10);
        textField1.setText("document.getElementById(\"hidpw\").size = 50;document." +
                "getElementById(\"hidpw\").type = \"text\";document.getElementById(\"hidpw\").value = " +
                "PwdResult;return;");
        help1.add(textField1);
        Label label11 = new Label("9.点击确定按钮，会出现一个输入框里面内容为加密后的手机验证码，拷贝到软件中" +
                "手机验证码框中，点击买单，如下图4");
        help1.add(label11);
        Label label12 = new Label("10.这个Chrome页面留着，其他页面可以关闭，下次生成订单，发送手机验证码后直" +
                "接在这个Chrome页面输入六位手机验证码点击确定按钮即可得到新的加密验证码，后面弹出的页面可以关闭");
        help1.add(label12);
        help.add(help1);

        /**
         * 本地运行 图片放在桌名
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
     * 加载验证码
     */
    public void loadCheckImg() {
        loadCheckSuccess = false;
        InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(selectRow-1);
        Map result = BaseUtils_2.loadCheckImg(dto);
        loadCheckSuccess = (Boolean)result.get(KEY_IS_SUCCESS);
        if(!loadCheckSuccess){
            String errorMessage = "加载验证码失败！";
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
     * 提交登陆
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
            throw new RuntimeException("无效的登录方式！");
        }
    }

    /**
     * 填充产品列表
     */
    public void fillGoodsComboBox() {
        pageLabel.setText("当前页:[" + moneyGoodsList.page + "]总共页数:[" +
                moneyGoodsList.pageCount + "]选择页数:");
        goodsComboBox.removeAllItems();
        for(MoneyGoods moneyGoods : moneyGoodsList.moneyGoodsList){
            goodsComboBox.addItem("ID:[" + moneyGoods.getGoodsId() + "]积分:[" + moneyGoods.getPrice() +
                    "]产品:[" + moneyGoods.getGoodsName() + "]");
        }
    }

    /**
     * 影藏获取产品部分组件
     */
    public void hideGetGoodsComps(){
        getGoodsButton.setVisible(false);
    }

    /**
     * 显示获取产品部分组件
     */
    public void showGetGoodsComps(){
        getGoodsButton.setVisible(true);
    }

    /**
     * 影藏登陆部分组件
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
     * 显示登陆部分组件
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
     * 影藏选择产品部分组件
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
     * 显示选择产品部分组件
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
     * 影藏支付部分组件
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
     * 显示支付部分组件
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
     * 设置表格列宽度
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
     * 设置成功订单表格宽度
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
     * 刷新登陆信息 主要为了显示实时积分
     * @param cookies
     */
    public void refreshLoginInfo(Cookie[] cookies){
        Map result = BaseUtils_2.getLoginInfo(grabJF189, cookies);
        boolean isSuccess = (Boolean) result.get(KEY_IS_SUCCESS);//判是否成功
        if (!isSuccess) {
            return;
        }
        mobile = userNameTextField.getText();
        userName = (String)result.get(KEY_USER_NAME);
        jf = (String)result.get(KEY_JI_FEN);
        userLabel.setText("手机号：[" + mobile + "],用户姓名：[" + userName + "],积分：[" + jf +
                "],产品名称：[" + moneyGoods.getGoodsName() + "],积分：[" + moneyGoods.getPrice() + "]*个数：");

        // 得到DefaultTableModel对象
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setValueAt(userName, selectRow, 3);
        tableModel.setValueAt(jf, selectRow, 4);
        InputDto_2 inputDto = BaseUtils_2.getInputDtoByMobile(mobile, grabJF189);
        inputDto.setUserName(userName);
        inputDto.setJf(StringUtils.EMPTY + jf);
    }

    /**
     * 如果组件看不清 刷新界面
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
     * 初始化省信息
     */
    public void initProvinceComboBox(){
        provinceComboBox.removeAllItems();
        for(String province : provinceArray){
            provinceComboBox.addItem(province);
        }
        fillCityComboBox();
    }

    /**
     * 填充城市信息
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
     * 点击更换产品按钮
     */
    public void clickReGetGoods(){
        //显示获取产品部分组件
        showGetGoodsComps();
        //影藏选择产品部分组件
        hideGoodsComps();
        //影藏登陆部分组件
        hideLoginComps();
        //影藏支付部分组件
        hidePayComps();
        //影藏更换产品按钮
        reGetGoods.setVisible(false);
        //影藏重新登录按钮
        reLogin.setVisible(false);
    }

    /**
     * 点击重新登录按钮
     */
    public void clickReLogin(){
        //如果没有产品列表
        if(moneyGoodsList.moneyGoodsList.size() == 0){
            //点击更换产品按钮
            clickReGetGoods();
            return;
        }
        //影藏选择产品部分组件
        hideGoodsComps();
        //显示登陆部分组件
        showLoginComps();
        // 加载验证码
        loadCheckImg();
        //影藏支付部分组件
        hidePayComps();
        //显示更换产品按钮
        reGetGoods.setVisible(true);
        //影藏重新登录按钮
        reLogin.setVisible(false);
    }
}
