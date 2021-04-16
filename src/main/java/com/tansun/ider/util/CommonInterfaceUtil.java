package com.tansun.ider.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;

/**
 * 通用工具类（包括对构件获取元件的封装、获取汇率、通用处理方法等）
 *
 * @author dengjunwen
 * @date 2018年10月23日 下午1:48:46
 */
public class CommonInterfaceUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonInterfaceUtil.class);
    // 默认错误码
    public static final String EXTER_DEFAULT_RESPONSE_CODE = "99";
    // 默认错误描述
    public static final String EXTER_DEFAULT_RESPONSE_MSG = "系统异常";
    // 默认优先级
    public static final int EXTER_DEFAULT_PRIOR = 999;


    /**
     * 针对调额类参数使用
     *
     * @param creditNodeNo
     * @param operationMode
     * @param artifactNo
     * @return
     * @throws Exception
     */
    public static Map<String, String> getComponent(String creditNodeNo, String operationMode, String artifactNo) throws Exception {
        EventCommArea eventCommArea = new EventCommArea();
        // 运营模式
        eventCommArea.setEcommOperMode(operationMode);
        // 额度节点编号
        eventCommArea.setEcommAuthCreditNode(creditNodeNo);
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        return artService.getElementByArtifact(artifactNo, eventCommArea);
    }


}
