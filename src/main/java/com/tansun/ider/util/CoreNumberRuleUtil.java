package com.tansun.ider.util;


/**
 * 卡号算法生成
 * ClassName:CoreNumberRuleUtil 
 * desc:
 * @author wt
 * @date 2018年4月19日 下午2:58:59
 */
public class CoreNumberRuleUtil {
    //计算生成次数
    private static int i = 0;

    /***
     * 获取当前系统时间戳 并截取 
     * @return
     */
    public synchronized static String getUnixTime() {
        try {
            //线程同步执行，休眠10毫秒 防止卡号重复
            Thread.sleep(10);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        //算法每使用一次加一,超过一百取余
        i++ ;
        i = i > 100 ? i % 10 : i;
        //最后一位随机数,同一时间就不会重复
        return ((System.currentTimeMillis() / 100) + "").substring(3) + (i % 10);
    }

    /**
     * 卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
            || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i-- , j++ ) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

}