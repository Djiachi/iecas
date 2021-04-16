package com.tansun.ider.dao.nonfinance.mapper;

import com.tansun.ider.dao.issue.entity.CoreAccountBalanceCheck;
import com.tansun.ider.dao.issue.entity.CoreAccountBalanceCheckKey;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountBalanceCheckSqlBuilder;

import java.util.List;

/**
 * CoreAccountBalanceCheckMapper
 * 
 * @author PG(Auto Generator)
 * @version V2.0
 */
public interface AccountBalanceCheckMapper {

    /**
     * 根据SqlBuilder查询列表，返回值包含大字段栏位
     * 
     * @param coreAccountBalanceCheckSqlBuilder
     * @return
     * @throws Exception
     */
    public List<CoreAccountBalanceCheck> selectBySqlBuilderWithBLOBs(CoreAccountBalanceCheckSqlBuilder coreAccountBalanceCheckSqlBuilder) throws RuntimeException;

    /**
     * 根据SqlBuilder查询列表，返回值不包含大字段栏位
     * 
     * @param coreAccountBalanceCheckSqlBuilder
     * @return
     * @throws Exception
     */
    public List<CoreAccountBalanceCheck> selectBySqlBuilder(CoreAccountBalanceCheckSqlBuilder coreAccountBalanceCheckSqlBuilder) throws RuntimeException;

    /**
     * 根据主键查询单个对象
     * 
     * @param coreAccountBalanceCheckKey
     * @return
     * @throws Exception
     */
    public CoreAccountBalanceCheck selectByPrimaryKey(CoreAccountBalanceCheckKey coreAccountBalanceCheckKey) throws RuntimeException;

    /**
     * 根据SqlBuilder统计记录条数
     * 
     * @param coreAccountBalanceCheckSqlBuilder
     * @return
     * @throws Exception
     */
    public int countBySqlBuilder(CoreAccountBalanceCheckSqlBuilder coreAccountBalanceCheckSqlBuilder) throws RuntimeException;

    /**
     * 单条插入新增记录
     * 
     * @param coreAccountBalanceCheck
     * @return
     * @throws Exception
     */
    public int insert(CoreAccountBalanceCheck coreAccountBalanceCheck) throws RuntimeException;

    /**
     * 批量插入新增记录
     * 
     * @param listCoreAccountBalanceCheck
     * @return
     * @throws Exception
     */
    public int insertUseBatch(List<CoreAccountBalanceCheck> listCoreAccountBalanceCheck) throws RuntimeException;

    /**
     * 根据SqlBuilder更新记录，空值会持久化到数据库
     * 
     * @param coreAccountBalanceCheckSqlBuilder
     * @return
     * @throws Exception
     */
    public int updateBySqlBuilder(CoreAccountBalanceCheckSqlBuilder coreAccountBalanceCheckSqlBuilder) throws RuntimeException;

    /**
     * 根据SqlBuilder更新记录，空值不会持久化到数据库
     * 
     * @param coreAccountBalanceCheckSqlBuilder
     * @return
     * @throws Exception
     */
    public int updateBySqlBuilderSelective(CoreAccountBalanceCheckSqlBuilder coreAccountBalanceCheckSqlBuilder) throws RuntimeException;

    /**
     * 根据主键更新记录，空值会持久化到数据库
     * 
     * @param coreAccountBalanceCheckKey
     * @return
     * @throws Exception
     */
    public int updateByPrimaryKey(CoreAccountBalanceCheckKey coreAccountBalanceCheckKey) throws RuntimeException;

    /**
     * 根据主键更新记录，空值不会持久化到数据库
     * 
     * @param coreAccountBalanceCheckKey
     * @return
     * @throws Exception
     */
    public int updateByPrimaryKeySelective(CoreAccountBalanceCheckKey coreAccountBalanceCheckKey) throws RuntimeException;

    /**
     * 根据SqlBuilder删除记录
     * 
     * @param coreAccountBalanceCheckSqlBuilder
     * @return
     * @throws Exception
     */
    public int deleteBySqlBuilder(CoreAccountBalanceCheckSqlBuilder coreAccountBalanceCheckSqlBuilder) throws RuntimeException;

    /**
     * 根据主键删除记录
     * 
     * @param coreAccountBalanceCheckKey
     * @return
     * @throws Exception
     */
    public int deleteByPrimaryKey(CoreAccountBalanceCheckKey coreAccountBalanceCheckKey) throws RuntimeException;

}