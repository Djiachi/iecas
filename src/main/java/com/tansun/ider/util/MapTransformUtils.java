package com.tansun.ider.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Map Bean转换类
 * 
 * @author wt
 *
 */
public class MapTransformUtils {

	/**
	 * 
	* @Description: 
	* @param map
	* @param beanClass
	* @return
	* @throws Exception
	 */
	public static Object mapToObject2(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null){
			return null;
		}
		Object obj = beanClass.newInstance();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			Method setter = property.getWriteMethod();
			if (setter != null) {
				setter.invoke(obj, map.get(property.getName()));
			}
		}
		return obj;
	}

	/**
	 * Map 转换成 Bean
	 * 
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
		if (map == null){
			return null;
		}
		T obj = beanClass.newInstance();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}
			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}
		return obj;
	}

	/**
	 * Obj 对象转换成Map
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}
		return map;
	}

}
