/**
 * 
 */
package com.tansun.ider.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Desc:反射公共处理类
 * @Author wt
 * @Date 2018年5月24日 下午3:37:40
 */
public class ClassUtil {

    /**
     * 将一个类查询方式加入map（属性值为int型时，0时不加入， 属性值为String型或Long时为null和""不加入）
     * 
     */
    public static Map<String, Object> setConditionMap(Map<String, Object> map ,Object obj) {
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (getValueByFieldName(fieldName, obj) != null){
                map.put(fieldName, getValueByFieldName(fieldName, obj));
            }
        }

        return map;

    }

    /**
     * 根据属性名获取该类此属性的值
     * 
     * @param fieldName
     * @param object
     * @return
     */
    private static Object getValueByFieldName(String fieldName, Object object) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        try {
            Method method = object.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(object, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }

    }
    
	/**
	 * 把vo类中String类型的参数的null值或"NULL","null"转化成空字符串
	 * @param obj
	 * @return
	 */
	public static Object getReflectObjectTransString(Object obj) throws Exception{
		Class<? extends Object> classz = obj.getClass();
		Field[] fields=classz.getDeclaredFields();
		for(Field field:fields){
			if(String.class == field.getType()){
				// 属性参数值首字母转成大写
				char[] cs=(field.getName()).toCharArray();
			    cs[0]-=32;
				String methodGetName = "get"+String.valueOf(cs);
				String methodSetName = "set"+String.valueOf(cs);
				Method getMethod = classz.getMethod(methodGetName, null);
				Method setMethod = classz.getMethod(methodSetName, String.class);
				Object value = getMethod.invoke(obj, null);
				if(null == value || "NULL".equals((String)value) || "null".equals((String)value))
				{
					setMethod.invoke(obj, "");
				}
			}
		}	
		return obj;
	}

}
