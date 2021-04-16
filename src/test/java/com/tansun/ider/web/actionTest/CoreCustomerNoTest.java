package com.tansun.ider.web.actionTest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.dao.issue.CoreCustomerNumberRuleDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerNumberRule;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerNumberRuleSqlBuilder;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NonFinanceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
public class CoreCustomerNoTest {

	@Autowired
	private CoreCustomerNumberRuleDao coreCustomerNumberRuleDao;

//	@Test
	public void Test() throws Exception {
		CoreCustomerNumberRuleSqlBuilder coreCustomerNumberRuleSqlBuilder = new CoreCustomerNumberRuleSqlBuilder();
		coreCustomerNumberRuleSqlBuilder.andSeqTypeNotEqualTo("F");
		List<CoreCustomerNumberRule> listCoreCustomerNumberRule = coreCustomerNumberRuleDao
				.selectListBySqlBuilder(coreCustomerNumberRuleSqlBuilder);
		for (CoreCustomerNumberRule coreCustomerNumberRule : listCoreCustomerNumberRule) {
			CoreCustomerNumberRuleSqlBuilder coreCustomerNumberRuleSqlBuilderStr = new CoreCustomerNumberRuleSqlBuilder();
			coreCustomerNumberRuleSqlBuilderStr.andCustomerNoEqualTo(coreCustomerNumberRule.getCustomerNo());
			coreCustomerNumberRuleSqlBuilderStr.andSeqTypeEqualTo("F");
			CoreCustomerNumberRule coreCustomerNumberRuleDD = coreCustomerNumberRuleDao.selectBySqlBuilder(coreCustomerNumberRuleSqlBuilderStr);
			if (coreCustomerNumberRuleDD ==null) {
				CoreCustomerNumberRule coreCustomerNumberRuleF = new CoreCustomerNumberRule();
				coreCustomerNumberRuleF.setCustomerNo(coreCustomerNumberRule.getCustomerNo());
				coreCustomerNumberRuleF.setId(RandomUtil.getUUID());
				coreCustomerNumberRuleF.setNextSeqNo(1);
				coreCustomerNumberRuleF.setSeqType("F");
				coreCustomerNumberRuleF.setVersion(1);
				coreCustomerNumberRuleDao.insert(coreCustomerNumberRuleF);
			}
			Thread.sleep(100);
		}

	}

}
