package com.tansun.ider.dao.nonfinance.mapper;

import java.util.List;

import com.tansun.ider.model.InstallAccountBean;
import com.tansun.ider.model.vo.X5630VO;

/**
 * InstallTransAndBaseAccountMapper
 * 
 * @author huangyayun
 * @version V1.0
 */
public interface InstallTransAndBaseAccountMapper {


    /**
     * 查询信贷账户
     * 
     * @param installAccountBean
     * @return
     * @throws Exception
     */
    public List<X5630VO> selectCoreInstallmentTransAccts(InstallAccountBean installAccountBean) throws Exception;
    public Integer countBySqlBuilder(InstallAccountBean installAccountBean) throws Exception;
    public List<X5630VO> selectCoreInstallmentTransAcctsForList(InstallAccountBean installAccountBean) throws Exception;
    public Integer countBySqlBuilderForList(InstallAccountBean installAccountBean) throws Exception;
    public List<X5630VO> selectInstallTransAcctsForChildList(InstallAccountBean installAccountBean) throws Exception;
    public Integer countBySqlBuilderForChildList(InstallAccountBean installAccountBean) throws Exception;

    Integer countBySqlBuilderForNotQuotaChildList(InstallAccountBean installAccountBean) throws Exception;
    public List<X5630VO> selectInstallTransAcctsForNotQuotaChildList(InstallAccountBean installAccountBean) throws Exception;
}