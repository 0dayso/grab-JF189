package gxx2;

/**
 * 基础类接口
 * Create by Gxx
 * Time: 2013-10-09 03:00
 */
public interface BaseInterface_2
{
    /**
     * 配置有效期
     */
    public static final String CONFIG_DEAD_LINE = "dead_line";
    /**
     * 返回结果中是否成功，可能的值：true，false
     */
    public static final String KEY_IS_SUCCESS = "isSuccess";
    /**
     * 返回结果中返回字符串
     */
    public static final String KEY_RESP_STR = "respStr";
    /**
     * 返回结果中返回header的集合
     */
    public static final String KEY_RESPONSE_HEADER_MAP = "responseHeaderMap";
    /**
     * 返回结果中返回cookie的数组
     */
    public static final String KEY_RESPONSE_COOKIES = "responseCookies";

    /**
     * 验证码图片地址
     */
    public static final String KEY_CHECK_IMG_ROUTE = "checkImgRoute";

    /**
     * spring web flow流(lt)
     */
    public static final String KEY_SPRING_WEB_FLOW = "springWebFlow";

    /**
     * 产品列表
     */
    public static final String KEY_MONEY_GOODS_LIST = "moneyGoodsList";

    /**
     * 订单列表
     */
    public static final String KEY_ORDER_LIST = "orderList";

    /**
     * 用户姓名
     */
    public static final String KEY_USER_NAME = "userName";

    /**
     * 积分
     */
    public static final String KEY_JI_FEN = "jiFen";
    /**
     * 弹出页面地址1 ：识别用户
     */
    public static final String KEY_BROWSE_URL1 = "browseUrl1";
    /**
     * 弹出页面地址2 ：识别订单
     */
    public static final String KEY_BROWSE_URL2 = "browseUrl2";

    /**
     * 工具Title
     */
    public static final String TITLE = "智能网页机器人";

    /**
     * EXCEL的第一行数据
     */
    public static final String[] EXCEL_TITLE = {"登录方式","用户名","密码","姓名","剩余积分","成功笔数","备注"};

    /**
     * EXCEL的第一行数据
     */
    public static final String[] DETAIL_EXCEL_TITLE = {"序号","手机号","用户名","产品ID","产品名称","单笔积分",
            "笔数","订单号","结果","卡号","卡密","有效期"};

    /**
     * 省市信息
     */
    public static final String[] provinceArray = new String[]{"01,北京","02,上海","03,天津","04,重庆","05,河北","06,山西","07,内蒙古","08,辽宁","09,吉林","10,黑龙江","11,江苏","12,浙江","13,安徽","14,福建","15,江西","16,山东","17,河南","18,湖北","19,湖南","20,广东","21,广西","22,海南","23,四川","24,贵州","25,云南","26,西藏","27,陕西","28,甘肃","29,青海","30,宁夏","31,新疆"};//,"32,台湾","33,香港","34,澳门"
    public static final String[] cityArray = new String[]{"北京,010","上海,021","天津,022","重庆,023","石家庄,0311|保定,0312|张家口,0313|承德,0314|唐山,0315|廊坊,0316|沧州,0317|衡水,0318|秦皇岛,0335|邯郸,0310|邢台,0319","太原,0351|大同,0352|阳泉,0353|晋中,0354|长治,0355|晋城,0356|临汾,0357|运城,0359|忻州,0350|朔州,0349|吕梁,0358","呼和浩特,0471|呼伦贝尔,0470|包头,0472|乌海,0473|乌兰察布盟,0474|通辽,0475|赤峰,0476|鄂尔多斯,0477|巴彦淖尔,0478|锡林郭勒盟,0479|兴安盟,0482|阿拉善盟,0483","沈阳,024|大连,0411|鞍山,0412|抚顺,024|本溪,0414|丹东,0415|锦州,0416|营口,0417|阜新,0418|辽阳,0419|铁岭,024|朝阳,0421|盘锦,0427|葫芦岛,0429","长春,0431|吉林,0432|延边自治州,0433|四平,0434|通化,0435|白城,0436|辽源,0437|松原,0438|白山,0439","哈尔滨,0451|齐齐哈尔,0452|牡丹江,0453|佳木斯,0454|绥化,0455|黑河,0456|大兴安岭,0457|伊春,0458|大庆,0459|七台河,0464|鸡西,0467|鹤岗,0468|双鸭山,0469","南京,025|无锡,0510|徐州,0516|常州,0519|苏州,0512|南通,0513|连云港,0518|淮安,0517|盐城,0515|扬州,0514|镇江,0511|泰州,0523|宿迁,0527","杭州,0571|湖州,0572|金华,0579|宁波,0574|舟山,0580|丽水,0578|嘉兴,0573|绍兴,0575|台州,0576|温州,0577|衢州,0570","合肥,551|蚌埠,552|芜湖,553|淮南,554|马鞍山,555|安庆,556|宿州,557|阜阳,558|黄山,559|滁州,550|淮北,561|铜陵,562|宣城,563|六安,564|巢湖,565|池州,566|亳州,560","福州,0591|厦门,0592|宁德,0593|莆田,0594|泉州,0595|漳州,0596|龙岩,0597|三明,0598|南平,0599","南昌,0791|九江,0792|上饶,0793|抚州,0794|宜春,0795|吉安,0796|赣州,0797|景德镇,0798|萍乡,0799|新余,0790|鹰潭,0701","济南,0531|青岛,0532|淄博,0533|德州,0534|烟台,0535|潍坊,0536|济宁,0537|泰安,0538|临沂,0539|威海,0631|枣庄,0632|日照,0633|莱芜,0634|聊城,0635|荷泽,0530|滨州,0543|东营,0546","商丘,0370|郑州,0371|安阳,0372|新乡,0373|许昌,0374|平顶山,0375|信阳,0376|南阳,0377|开封,0378|洛阳,0379|焦作,0391|鹤壁,0392|濮阳,0393|周口,0394|漯河,0395|驻马店,0396|三门峡,0398","武汉,027|襄樊,0710|黄冈,0713|宜昌,0717|孝感,0712|鄂州,0711|咸宁,0715|十堰,0719|荆门,0724|黄石,0714|随州,0722|恩施,0718|仙桃,0728|天门,0728|潜江,027|神农架,0719|荆州,0716","长沙,0731|岳阳,0730|湘潭,0731|株洲,0731|衡阳,0734|郴州,0735|常德,0736|益阳,0737|娄底,0738|邵阳,0739|湘西自治州,0743|张家界,0744|怀化,0745|永州,0746","广州,020|深圳,0755|珠海,0756|汕头,0754|韶关,0751|河源,0762|梅州,0753|惠州,0752|汕尾,0660|东莞,0769|中山,0760|江门,0750|佛山,0757|阳江,0662|湛江,0759|茂名,0668|肇庆,0758|清远,0763|潮州,0768|揭阳,0663|云浮,0766","南宁,0771|崇左,0771|柳州,0772|来宾,0772|桂林,0773|梧州,0774|贺州,0774|玉林,0775|贵港,0775|百色,0776|钦州,0777|河池,0778|北海,0779|防城港,0770","海口,0898|三亚,0899|琼海,0898|东方,0890|文昌,0898|儋州,0890|万宁,0898|通什,0899|琼山,0898","成都,028|攀枝花,0812|自贡,0813|绵阳,0816|南充,0817|达州,0818|遂宁,0825|广安,0826|巴中,0827|泸州,0830|宜宾,0831|资阳,0832|内江,0832|乐山,0833|眉山,0833|凉山州,0834|雅安,0835|甘孜州,0836|阿坝州,0837|德阳,0838|广元,0839","贵阳,0851|遵义,0852|安顺,0853|黔南,0854|黔东南,0855|铜仁,0856|毕节,0857|六盘水,0858|黔西南,0859","昆明,0871|大理,0872|红河,0873|玉溪,0877|曲靖,0874|版纳,0691|丽江,0888|迪庆,0887|普洱,0879|楚雄,0878|德宏,0692|昭通,0870|文山,0876|保山,0875|怒江,0886|临沧,0883","拉萨,0891|日喀则,0892|山南,0893|林芝,0894|昌都,0895|那曲,0896|阿里,0897","西安,029|铜川,0919|宝鸡,0917|咸阳,0910|渭南,0913|延安,0911|榆林,0912|商洛,0914|安康,0915|汉中,0916","兰州,0931|临夏,0930|定西,0932|平凉,0933|庆阳,0934|武威,0935|张掖,0936|酒泉,0937|天水,0938|陇南,0939|甘南,0941|白银,0943|金昌,0945|嘉峪关,0947","西宁,0971|海东,0972|海南,0974|海西,0977|海北,0970|黄南,0973|果洛,0975|玉树,0976|格尔木,0979","银川,0951|石嘴山,0952|吴忠,0953|固原,0954|中卫,0955","乌鲁木齐,0991|昌吉,0994|吐鲁番,0995|哈密,0902|奎屯,0992|克拉玛依,0990|石河子,0993|阿克苏,0997|塔城,0901|博州,0909|和田,0903|喀什,0998|伊犁,0999|阿勒泰,0906|巴州,0996|克州,0908"};

    /**
     * 登录类型
     */
    public static final String LOGIN_TYPE_MB = "手机";
    public static final String LOGIN_TYPE_PH = "固话";
    public static final String LOGIN_TYPE_NET = "宽带";
    public static final String LOGIN_TYPE_CARD = "会员卡";
    public static final String LOGIN_TYPE_SLYH = "商旅用户";
    public static final String LOGIN_TYPE_TWYH = "他网用户";

    /**
     * 新增空一行
     */
    public static final String[] EMPTY_STRING_ARRAY = {"","","","",""};

    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     * 手机登录
     */
    public static final String LOGIN_URL = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerMB";

    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     * 固话登录
     */
    public static final String LOGIN_URL_FOR_GUHUA = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerPH";

    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     * 宽带登录
     */
    public static final String LOGIN_URL_FOR_NET = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerNET";

    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     * 会员卡登录
     */
    public static final String LOGIN_URL_FOR_CARD = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerCARD";

    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     * 商旅用户登录
     */
    public static final String LOGIN_URL_FOR_SLUSER = "https://sso.jf.189.cn//sso/login?" +
            "service=http://jf.189.cn/SelfHelp/OrderDetail.aspx?S=0";

    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     * 他网用户登录
     */
    public static final String LOGIN_URL_FOR_ONUSER = "https://sso.jf.189.cn//sso/login?" +
            "service=http://jf.189.cn/SelfHelp/OrderDetail.aspx?S=0";

    /**
     * 图片地址
     */
    public static final String IMAGE_URL = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?rand=";

    /**
     * 判网络是不是通的 访问得URL
     */
    public static final String[] CHECK_NET_WORK_LINKED_URLS = new String[]{"http://www.baidu.com", "http://www.google.cn"};

    /**
     * 获取登录信息方式
     */
    public static final String LOGIN_MESSAGE_TYPE_1 = "方式一(错误率低)";
    public static final String LOGIN_MESSAGE_TYPE_2 = "方式二(一定错误率)";
}
