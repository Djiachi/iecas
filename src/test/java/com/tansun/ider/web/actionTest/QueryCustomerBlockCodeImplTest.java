package com.tansun.ider.web.actionTest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tansun.ider.model.CustomerContrlViewBean;
import com.tansun.ider.service.impl.QueryCustomerBlockCodeImpl;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NonFinanceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
public class QueryCustomerBlockCodeImplTest {

	@Autowired
	private QueryCustomerBlockCodeImpl queryCustomerBlockCodeImpl;
	
//	@Test
	public void QueryCustomerContrlView() {
		List<CustomerContrlViewBean> listCoreCustomerContrlView = null;
		try {
			listCoreCustomerContrlView = queryCustomerBlockCodeImpl.queryCoreCustomerContrlView("443000068350", "MODCI0101",
					"2019-07-24", "2019-07-28","yyyy-MM-dd");
			System.out.println(listCoreCustomerContrlView.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
