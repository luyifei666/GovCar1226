package com.clfsjkj.govcar.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 
 * JsonUtils
 * @author Owen
 * @since 2013-12-30 下午2:57:05
 * 
 */
public class JsonUtils {

	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

    public final static String MESSAGE = "message";
    public final static String RESULT = "result";

	/**
	 * json 转化为JavaBean
	 * 
	 * JavaBean 成员变量与json的key保持一致，并实现其getter和setter方法
	 * 
	 * e.g. VersonEntity entity = (VersonEntity)
	 * JsonUtils.jsonToBean(obj.toString(), VersonEntity.class);
	 * 
	 * class VersonEntity{
	 * 
	 * private int id ;
	 * 
	 * public void setId(int id){ this.id = id ; } public int getId(){ return
	 * this.id ; }
	 * 
	 * }
	 * 
	 * 
	 * @param jsonStr Json字符串
	 * @param cl 类名
	 */
	public static Object jsonToBean(String jsonStr, Class<?> cl) {
		Object obj = null;
		try {
			if (gson != null) {
				obj = gson.fromJson(jsonStr, cl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 将json格式转换成list对象
	 * 
	 * List<JavaBean> 成员变量与json的key保持一致，并实现其getter和setter方法
	 * 
	 * List<ProjectEntity> pList = new ArrayList<ProjectEntity>();
	 * java.lang.reflect.Type type = new TypeToken<List<ProjectEntity>>() {
	 * }.getType(); for (JSONArray array : jsonArrays) {
	 * pList.addAll((List<ProjectEntity>) JsonUtils.jsonToList(array.toString(),
	 * type)); }
	 * 
	 * @param jsonStr json字符串
	 */
	public static List<?> jsonToList(String jsonStr, Type type) {
		List<?> objList = null;
		if (gson != null) {
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}

	public static Object jsonToBean(String jsonStr, Type type) {
		Object obj = null;
		if (gson != null) {
			obj = gson.fromJson(jsonStr, type);
		}
		return obj;
	}

    public static boolean isSuccess(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            JSONObject json;
            try {
                json = new JSONObject(jsonStr);
                return json.getBoolean(RESULT);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    public static String getMessage(String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            JSONObject json;
            try {
                json = new JSONObject(jsonStr);
                return json.getString(MESSAGE);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return "";
    }

	public static String toJson(Object obj) {
		String res = null;
		if (gson != null) {
			res = gson.toJson(obj);
		}
		return res;
	}
}
