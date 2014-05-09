package gxx;

/**
 * 抓取线程
 * Create by Gxx
 * Time: 2013-10-10 01:10
 */
public class GrabThread extends Thread
{
    /**
     * 全部抓取
     */
    public static final int GRAB_ALL = 1;

    /**
     * 批量抓取
     */
    public static final int GRAB_BATCH = 2;

    /**
     * 单笔抓取
     */
    public static final int GRAB_SINGLE = 3;

    /**
     * 抓取类型 1 全部抓取 2 批量抓取 3 单笔抓取
     */
    public int type;

    /**
     * 工具类
     */
    GrabSN189 grabSN189;

    /**
     * 构造函数
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
