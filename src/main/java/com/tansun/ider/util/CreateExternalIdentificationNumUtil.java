package com.tansun.ider.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.service.business.common.Constant;

/**
 * 
 * ClassName:CreateExternalIdentificationNumUtil desc:生成外部识别号Util类
 * 
 * @author wt
 * @date 2018年4月19日 上午11:39:52
 */
public class CreateExternalIdentificationNumUtil {

    private static CreateExternalIdentificationNumUtil externalUtil;

    private static Object synclock = new Object();

    private CreateExternalIdentificationNumUtil() {
    }

    /**
     * 生成外部识别号唯一标识
     * 
     * @return
     */
    public static CreateExternalIdentificationNumUtil getInstance() {
        if (externalUtil == null) {
            // 考虑到高并发的情况
            synchronized (synclock) {
                if (externalUtil == null) {
                    externalUtil = new CreateExternalIdentificationNumUtil();
                }
            }
        }
        return externalUtil;
    }

    /**
     * 根据外部识别号生成 sssssssss
     * 
     * @return
     */
    public String getExternalNo(String binNo) {
        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        Long id = stringRedisTemplate.boundValueOps(Constant.CORE_GLOBAL_INCREMENT_KEY+binNo).increment(1);
        String strId = "";
        Long temp = 99999999L;
        if (id > temp) {
            strId = (id % 100000000) + "";
        } else {
            strId = id.toString();
        }
        String serialNo = String.format("%08d", Long.parseLong(strId));
        return "6"+serialNo;
    }

}
