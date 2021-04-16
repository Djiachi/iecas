package com.tansun.ider.web.actionTest;

import org.springframework.beans.factory.annotation.Autowired;

import com.tansun.ider.service.DateRollService;
import com.tansun.ider.service.business.EventCommArea;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NonFinanceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
public class DateRollServiceImplTest {

	@Autowired
	private DateRollService dateRollServiceImpl;
	
//	@Test
	public void Test() throws Exception {
		EventCommArea eventCommArea = new EventCommArea();
		eventCommArea.setEcommSystemUnitNo("100");
		EventCommArea eventCommArea1 = dateRollServiceImpl.dateRoll(eventCommArea);
		System.out.println(eventCommArea1.toString());
	}


}
