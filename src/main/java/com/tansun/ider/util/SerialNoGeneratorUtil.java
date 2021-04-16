package com.tansun.ider.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.service.business.common.Constant;

/**
 * ClassName: MasterUtil <br/>
 * Function: ADD FUNCTION. <br/>
 * Reason: ADD REASON(可选). <br/>
 * date: 2018年3月19日 下午4:52:21 <br/>
 * 
 * @author dengjunwen
 * @since JDK 1.8
 */
public class SerialNoGeneratorUtil {

    private static SerialNoGeneratorUtil serialNoGeneratorUtil;
    /** 默认返回成功码值 */
    public final static int MAX_SERIAL_VALUE = 99999;

    private static Object synclock = new Object();

    private SerialNoGeneratorUtil() {
    }

    /**
     * 返回全局唯一实例 Description: <br/>
     * 
     * @return
     * @author dengjunwen
     * @since JDK 1.8
     */
    public static SerialNoGeneratorUtil getInstance() {
        if (serialNoGeneratorUtil == null) {
            // 考虑到高并发的情况
            synchronized (synclock) {
                if (serialNoGeneratorUtil == null) {
                	serialNoGeneratorUtil = new SerialNoGeneratorUtil();
                }
            }
        }
        return serialNoGeneratorUtil;
    }

    /**
     * 获取事件全局流水号
     * 
     * @return
     */
    /*public String getGlobalEventNo() {
        JedisCluster jedisCluster = SpringUtil.getBean(JedisCluster.class);
        Long id = jedisCluster.hincrBy(Constant.RedisKey, Constant.CORE_GLOBAL_INCREMENT_KEY, 1);
        String strId = "";
        if (id > MAX_SERIAL_VALUE) {
            strId = (id % 100000) + "";
        } else {
            strId = id.toString();
        }
        FastDateFormat seqDateFormat = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
        String currentTime = seqDateFormat.format(System.currentTimeMillis());
        String serialNo = String.format("%05d", Long.parseLong(strId));
        String globalEventNo = currentTime + serialNo;
        return globalEventNo;
    }*/

    /**
     * 获取顺序号规则中的下一顺序号
     * 
     * @param key
     * @return
     */
    public Long getCustomerNumberRuleOrderNumber(String key) {
        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        Long id = stringRedisTemplate.boundValueOps(Constant.CORE_CUSTOMR_NUMBER_RULE + key).increment(1);
        return id;
    }

    /**
     * 获取随机生成编号 sssss
     * 
     * @param key
     * @return
     */
    public String getProductUnitCode(String key) {
        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        Long id = stringRedisTemplate.boundValueOps(Constant.CORE_CUSTOMR_NUMBER_SSSSS).increment(1);
        String strId = "";
        Long temp = 99999L;
        if (id > temp) {
            strId = (id % 100000) + "";
        } else {
            strId = id.toString();
        }
        String serialId = String.format("%05d", Long.parseLong(strId));
        return serialId.toString();
    }

    /**
     * 生成序列号规则内容
     * 
     * @param key
     * @return
     */
    public String getMediaNum(String key) {
        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        Long id = stringRedisTemplate.boundValueOps(Constant.CORE_MEDIA_NUMBER).increment(1);
        String strId = "";
        Long temp = 999999999L;
        if (id > temp) {
            strId = (id % 1000000000) + "";
        } else {
            strId = id.toString();
        }
        String serialId = String.format("%08d", Long.parseLong(strId));
        return serialId.toString();
    }

    /**
     * 获取下一版本号
     * 
     * @return
     */
    public static String getNextTableVersionNumber(String key) {
        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        Long id = stringRedisTemplate.boundValueOps(Constant.CORE_TABLE + key).increment(1);
        return id.toString();
    }

}
