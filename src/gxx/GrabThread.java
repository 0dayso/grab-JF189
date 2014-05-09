package gxx;

/**
 * ץȡ�߳�
 * Create by Gxx
 * Time: 2013-10-10 01:10
 */
public class GrabThread extends Thread
{
    /**
     * ȫ��ץȡ
     */
    public static final int GRAB_ALL = 1;

    /**
     * ����ץȡ
     */
    public static final int GRAB_BATCH = 2;

    /**
     * ����ץȡ
     */
    public static final int GRAB_SINGLE = 3;

    /**
     * ץȡ���� 1 ȫ��ץȡ 2 ����ץȡ 3 ����ץȡ
     */
    public int type;

    /**
     * ������
     */
    GrabSN189 grabSN189;

    /**
     * ���캯��
     * @param type
     * @param grabSN189
     */
    public GrabThread(int type, GrabSN189 grabSN189)
    {
        this.type = type;
        this.grabSN189 = grabSN189;
    }

    @Override
    public void run()
    {
        if(GRAB_ALL == type)
        {
            BaseUtils.grabAll(grabSN189);
        } else if(GRAB_BATCH == type)
        {
            BaseUtils.grabBatch(grabSN189);
        } else {
            BaseUtils.grabSingle(grabSN189);
        }
    }
}
