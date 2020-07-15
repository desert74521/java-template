package com.test.platform.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.baomidou.mybatisplus.annotation.TableName;

public class CommonUtil {
	public static String getClassTableNameByModel(Class<?> cls) {
		Annotation tb = cls.getAnnotation(TableName.class);
		Method[] met = tb.annotationType().getDeclaredMethods(); 
		for(Method me : met ){  
            if(!me.isAccessible()){  
                me.setAccessible(true);  
            }  
            try {  
            	if("value".equals(me.getName())) {
            		return (String) me.invoke(tb);  
            	}
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                e.printStackTrace();  
            }  
        }
		return null;
	}
}
