package com.tansun.ider.web.actionTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tansun.ider.util.NonFinanceRunnableAuth;
import com.tansun.ider.web.controller.PCController;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NonFinanceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
public class NonFinanceApplicationTest {

	@Autowired
	private PCController pCController;

	@SuppressWarnings("static-access")
//	@Test
	public void Test() throws Exception {
		Map<String, Object> bodyMap = new HashMap<String, Object>();
//		Integer strNum = 0000;
		for (int i = 0; i < 1200; i++) {
			String strNum1 = String.format("%04d", i);
			bodyMap.put("idNumber", "11011019742075" + strNum1);
			String str = String.format("%04d", i);
			bodyMap.put("mediaUserName", "测试用户" + str); //
			bodyMap.put("customerName", "测试用户" + str); //
			bodyMap.put("institutionId", "100010001");   //1000102
			bodyMap.put("idType", "1");
			bodyMap.put("city", "");
			bodyMap.put("customerType", "1");
			bodyMap.put("billDay", "05");
			bodyMap.put("branchNumber", "");
			bodyMap.put("dateOfBirth", "1980-05-01");
			bodyMap.put("mobilePhone", "13842354561");
			bodyMap.put("zipCode", "065700");
			bodyMap.put("homePhone", "010-7817898");
			bodyMap.put("companyPhone", "345-45454345");
			bodyMap.put("contactPhone", "13432434322");
			bodyMap.put("contactName", "城要");
			bodyMap.put("memberSince", "");
			bodyMap.put("customerSource", "");
			bodyMap.put("behaviorScore", "");
			bodyMap.put("customerLevel", "01");
			bodyMap.put("annualIncome", "100000");
			bodyMap.put("sexCode", "1");
			bodyMap.put("residencyStatus", "5");
			bodyMap.put("maritalStatus", "0");
			bodyMap.put("occupationCode", "01");
			bodyMap.put("postRankCode", "13");
			bodyMap.put("titleCode", "");
			bodyMap.put("periodOfOccupation", "");
			bodyMap.put("hobby", "");
			bodyMap.put("guarantorFlag", "");
			bodyMap.put("marketerCode", "2312311");
			bodyMap.put("socialSecurityId", "");
			bodyMap.put("socialSecurityNumber", "");
			bodyMap.put("coBrandedNo", "");
			bodyMap.put("mainSupplyIndicator", "1");
			bodyMap.put("mediaObjectCode", "MODM00001");
			bodyMap.put("applicationNumber", "");
			bodyMap.put("formatCode", "01");
			bodyMap.put("businessProgramNo", "");
			bodyMap.put("creditLimit", "60000");
			bodyMap.put("pricingTag", "");
			bodyMap.put("pricingObject", "");
			bodyMap.put("pricingObjectCode", "");
			bodyMap.put("custTagEffectiveDate", "");
			bodyMap.put("custTagExpirationDate", "");
			bodyMap.put("EXCHANGE-PAYMENT-FLAG", "Y");
			bodyMap.put("directDebitStatus", "");
			bodyMap.put("directDebitMode", "");
			bodyMap.put("directDebitBankNo", "");
			bodyMap.put("directDebitAccountNo", "");
			bodyMap.put("applicationStaffNo", "");
			bodyMap.put("embosserName", "1");
			bodyMap.put("requestCardMaking", "1");
			bodyMap.put("type", "2");
			bodyMap.put("creditNodeNo", "003");
			bodyMap.put("currencyCode", "156");
			bodyMap.put("permLimit", "60000");
			bodyMap.put("paymentMark", "1");
			Map<String, Object> bodyMap1 = new HashMap<String, Object>();
			bodyMap1.put("type", "1");
			bodyMap1.put("contactAddress", "北京朝阳");
			bodyMap1.put("contactPostCode", "100020");
			bodyMap1.put("contactMobilePhone", "010-100020");
			bodyMap1.put("city", "北京");
			List<Map> list = new ArrayList<Map>();
			list.add(bodyMap1);
			bodyMap.put("coreCoreCustomerAddrs", list); //
			int istr = Integer.parseInt(strNum1);
			if (istr < 1200) {
				bodyMap.put("productObjectCode", "MODP00020");
			} else {
				bodyMap.put("productObjectCode", "MODP00002");
			}
			bodyMap.put("creditType", "P");
			/*bodyMap.put("limitEffectvDate", "2019-01-01");
			bodyMap.put("limitExpireDate", "2099-01-01");*/
			bodyMap.put("operatorId", "admin1");
			String eventId = "BSS.AD.01.9001";
			Map<String, Object> headerMap = new HashMap<String, Object>();
			String serviceAddr = "http://10.6.10.183:8070/ider-gns2/creditauditService/BSS.AD.01.9001";
			NonFinanceRunnableAuth nonFinanceRunnable = new NonFinanceRunnableAuth(eventId, headerMap, bodyMap,
					pCController, serviceAddr);
			Thread threadAuth = new Thread(nonFinanceRunnable);
			threadAuth.sleep(2000);
			threadAuth.setDaemon(true);
			threadAuth.start();
		}

	}

}
