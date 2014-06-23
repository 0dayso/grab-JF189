package gxx2;

/**
 * ������ӿ�
 * Create by Gxx
 * Time: 2013-10-09 03:00
 */
public interface BaseInterface_2
{
    /**
     * ������Ч��
     */
    public static final String CONFIG_DEAD_LINE = "dead_line";
    /**
     * ���ؽ�����Ƿ�ɹ������ܵ�ֵ��true��false
     */
    public static final String KEY_IS_SUCCESS = "isSuccess";
    /**
     * ���ؽ���з����ַ���
     */
    public static final String KEY_RESP_STR = "respStr";
    /**
     * ���ؽ���з���header�ļ���
     */
    public static final String KEY_RESPONSE_HEADER_MAP = "responseHeaderMap";
    /**
     * ���ؽ���з���cookie������
     */
    public static final String KEY_RESPONSE_COOKIES = "responseCookies";

    /**
     * ��֤��ͼƬ��ַ
     */
    public static final String KEY_CHECK_IMG_ROUTE = "checkImgRoute";

    /**
     * spring web flow��(lt)
     */
    public static final String KEY_SPRING_WEB_FLOW = "springWebFlow";

    /**
     * ��Ʒ�б�
     */
    public static final String KEY_MONEY_GOODS_LIST = "moneyGoodsList";

    /**
     * �����б�
     */
    public static final String KEY_ORDER_LIST = "orderList";

    /**
     * �û�����
     */
    public static final String KEY_USER_NAME = "userName";

    /**
     * ����
     */
    public static final String KEY_JI_FEN = "jiFen";
    /**
     * ����ҳ���ַ1 ��ʶ���û�
     */
    public static final String KEY_BROWSE_URL1 = "browseUrl1";
    /**
     * ����ҳ���ַ2 ��ʶ�𶩵�
     */
    public static final String KEY_BROWSE_URL2 = "browseUrl2";

    /**
     * ����Title
     */
    public static final String TITLE = "������ҳ������";

    /**
     * EXCEL�ĵ�һ������
     */
    public static final String[] EXCEL_TITLE = {"��¼��ʽ","�û���","����","����","ʣ�����","�ɹ�����","��ע"};

    /**
     * EXCEL�ĵ�һ������
     */
    public static final String[] DETAIL_EXCEL_TITLE = {"���","�ֻ���","�û���","��ƷID","��Ʒ����","���ʻ���",
            "����","������","���","����","����","��Ч��"};

    /**
     * ʡ����Ϣ
     */
    public static final String[] provinceArray = new String[]{"01,����","02,�Ϻ�","03,���","04,����","05,�ӱ�","06,ɽ��","07,���ɹ�","08,����","09,����","10,������","11,����","12,�㽭","13,����","14,����","15,����","16,ɽ��","17,����","18,����","19,����","20,�㶫","21,����","22,����","23,�Ĵ�","24,����","25,����","26,����","27,����","28,����","29,�ຣ","30,����","31,�½�"};//,"32,̨��","33,���","34,����"
    public static final String[] cityArray = new String[]{"����,010","�Ϻ�,021","���,022","����,023","ʯ��ׯ,0311|����,0312|�żҿ�,0313|�е�,0314|��ɽ,0315|�ȷ�,0316|����,0317|��ˮ,0318|�ػʵ�,0335|����,0310|��̨,0319","̫ԭ,0351|��ͬ,0352|��Ȫ,0353|����,0354|����,0355|����,0356|�ٷ�,0357|�˳�,0359|����,0350|˷��,0349|����,0358","���ͺ���,0471|���ױ���,0470|��ͷ,0472|�ں�,0473|�����첼��,0474|ͨ��,0475|���,0476|������˹,0477|�����׶�,0478|���ֹ�����,0479|�˰���,0482|��������,0483","����,024|����,0411|��ɽ,0412|��˳,024|��Ϫ,0414|����,0415|����,0416|Ӫ��,0417|����,0418|����,0419|����,024|����,0421|�̽�,0427|��«��,0429","����,0431|����,0432|�ӱ�������,0433|��ƽ,0434|ͨ��,0435|�׳�,0436|��Դ,0437|��ԭ,0438|��ɽ,0439","������,0451|�������,0452|ĵ����,0453|��ľ˹,0454|�绯,0455|�ں�,0456|���˰���,0457|����,0458|����,0459|��̨��,0464|����,0467|�׸�,0468|˫Ѽɽ,0469","�Ͼ�,025|����,0510|����,0516|����,0519|����,0512|��ͨ,0513|���Ƹ�,0518|����,0517|�γ�,0515|����,0514|��,0511|̩��,0523|��Ǩ,0527","����,0571|����,0572|��,0579|����,0574|��ɽ,0580|��ˮ,0578|����,0573|����,0575|̨��,0576|����,0577|����,0570","�Ϸ�,551|����,552|�ߺ�,553|����,554|��ɽ,555|����,556|����,557|����,558|��ɽ,559|����,550|����,561|ͭ��,562|����,563|����,564|����,565|����,566|����,560","����,0591|����,0592|����,0593|����,0594|Ȫ��,0595|����,0596|����,0597|����,0598|��ƽ,0599","�ϲ�,0791|�Ž�,0792|����,0793|����,0794|�˴�,0795|����,0796|����,0797|������,0798|Ƽ��,0799|����,0790|ӥ̶,0701","����,0531|�ൺ,0532|�Ͳ�,0533|����,0534|��̨,0535|Ϋ��,0536|����,0537|̩��,0538|����,0539|����,0631|��ׯ,0632|����,0633|����,0634|�ĳ�,0635|����,0530|����,0543|��Ӫ,0546","����,0370|֣��,0371|����,0372|����,0373|���,0374|ƽ��ɽ,0375|����,0376|����,0377|����,0378|����,0379|����,0391|�ױ�,0392|���,0393|�ܿ�,0394|���,0395|פ���,0396|����Ͽ,0398","�人,027|�差,0710|�Ƹ�,0713|�˲�,0717|Т��,0712|����,0711|����,0715|ʮ��,0719|����,0724|��ʯ,0714|����,0722|��ʩ,0718|����,0728|����,0728|Ǳ��,027|��ũ��,0719|����,0716","��ɳ,0731|����,0730|��̶,0731|����,0731|����,0734|����,0735|����,0736|����,0737|¦��,0738|����,0739|����������,0743|�żҽ�,0744|����,0745|����,0746","����,020|����,0755|�麣,0756|��ͷ,0754|�ع�,0751|��Դ,0762|÷��,0753|����,0752|��β,0660|��ݸ,0769|��ɽ,0760|����,0750|��ɽ,0757|����,0662|տ��,0759|ï��,0668|����,0758|��Զ,0763|����,0768|����,0663|�Ƹ�,0766","����,0771|����,0771|����,0772|����,0772|����,0773|����,0774|����,0774|����,0775|���,0775|��ɫ,0776|����,0777|�ӳ�,0778|����,0779|���Ǹ�,0770","����,0898|����,0899|��,0898|����,0890|�Ĳ�,0898|����,0890|����,0898|ͨʲ,0899|��ɽ,0898","�ɶ�,028|��֦��,0812|�Թ�,0813|����,0816|�ϳ�,0817|����,0818|����,0825|�㰲,0826|����,0827|����,0830|�˱�,0831|����,0832|�ڽ�,0832|��ɽ,0833|üɽ,0833|��ɽ��,0834|�Ű�,0835|������,0836|������,0837|����,0838|��Ԫ,0839","����,0851|����,0852|��˳,0853|ǭ��,0854|ǭ����,0855|ͭ��,0856|�Ͻ�,0857|����ˮ,0858|ǭ����,0859","����,0871|����,0872|���,0873|��Ϫ,0877|����,0874|����,0691|����,0888|����,0887|�ն�,0879|����,0878|�º�,0692|��ͨ,0870|��ɽ,0876|��ɽ,0875|ŭ��,0886|�ٲ�,0883","����,0891|�տ���,0892|ɽ��,0893|��֥,0894|����,0895|����,0896|����,0897","����,029|ͭ��,0919|����,0917|����,0910|μ��,0913|�Ӱ�,0911|����,0912|����,0914|����,0915|����,0916","����,0931|����,0930|����,0932|ƽ��,0933|����,0934|����,0935|��Ҵ,0936|��Ȫ,0937|��ˮ,0938|¤��,0939|����,0941|����,0943|���,0945|������,0947","����,0971|����,0972|����,0974|����,0977|����,0970|����,0973|����,0975|����,0976|���ľ,0979","����,0951|ʯ��ɽ,0952|����,0953|��ԭ,0954|����,0955","��³ľ��,0991|����,0994|��³��,0995|����,0902|����,0992|��������,0990|ʯ����,0993|������,0997|����,0901|����,0909|����,0903|��ʲ,0998|����,0999|����̩,0906|����,0996|����,0908"};

    /**
     * ��¼����
     */
    public static final String LOGIN_TYPE_MB = "�ֻ�";
    public static final String LOGIN_TYPE_PH = "�̻�";
    public static final String LOGIN_TYPE_NET = "���";
    public static final String LOGIN_TYPE_CARD = "��Ա��";
    public static final String LOGIN_TYPE_SLYH = "�����û�";
    public static final String LOGIN_TYPE_TWYH = "�����û�";

    /**
     * ������һ��
     */
    public static final String[] EMPTY_STRING_ARRAY = {"","","","",""};

    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     * �ֻ���¼
     */
    public static final String LOGIN_URL = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerMB";

    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     * �̻���¼
     */
    public static final String LOGIN_URL_FOR_GUHUA = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerPH";

    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     * �����¼
     */
    public static final String LOGIN_URL_FOR_NET = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerNET";

    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     * ��Ա����¼
     */
    public static final String LOGIN_URL_FOR_CARD = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerCARD";

    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     * �����û���¼
     */
    public static final String LOGIN_URL_FOR_SLUSER = "https://sso.jf.189.cn//sso/login?" +
            "service=http://jf.189.cn/SelfHelp/OrderDetail.aspx?S=0";

    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     * �����û���¼
     */
    public static final String LOGIN_URL_FOR_ONUSER = "https://sso.jf.189.cn//sso/login?" +
            "service=http://jf.189.cn/SelfHelp/OrderDetail.aspx?S=0";

    /**
     * ͼƬ��ַ
     */
    public static final String IMAGE_URL = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?rand=";

    /**
     * �������ǲ���ͨ�� ���ʵ�URL
     */
    public static final String[] CHECK_NET_WORK_LINKED_URLS = new String[]{"http://www.baidu.com", "http://www.google.cn"};

    /**
     * ��ȡ��¼��Ϣ��ʽ
     */
    public static final String LOGIN_MESSAGE_TYPE_1 = "��ʽһ(�����ʵ�)";
    public static final String LOGIN_MESSAGE_TYPE_2 = "��ʽ��(һ��������)";
}
