package com.tansun.ider.web.actionTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.util.CacheUtils;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NonFinanceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
public class UpdateRedisKeyTest {

	@Autowired
	private RedisTemplate redisTemplate;

//	@Test
	public void Test() throws Exception {
		// BSS.IQ.03.0020
//		String eventNo = "BSS.IQ.03.0020";
//		String key = Constant.PARAMS_FLAG + eventNo;
//		redisTemplate.opsForHash().delete(CoreEventActivityRel.class.getName(), key);
//		CoreArtifactInstanRel coreArtifactInstanRel = new CoreArtifactInstanRel();
//		redisTemplate.delete(coreArtifactInstanRel.getClass());core_artifact_element_rel  
		CacheUtils.getInstance().deleteMap(CoreEventActivityRel.class);
	}

}
