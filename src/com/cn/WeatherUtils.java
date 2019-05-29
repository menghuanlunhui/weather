package com.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

/**
 * 通过get请求向网站http://wthrcdn.etouch.cn/weather_mini获取某个 城市的天气状况数据，数据格式是Json
 * 
 * @author 22786
 * 
 */
public class WeatherUtils {
	/**
	 * 通过城市名称获取该城市的天气信息
	 * 
	 * @param cityName
	 * @return
	 */
	
	public  static String GetWeatherData(String cityname) {
		StringBuilder sb=new StringBuilder();;
		try {
			String weather_url = "http://wthrcdn.etouch.cn/weather_mini?city="+cityname;
			
			URL url = new URL(weather_url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			GZIPInputStream gzin = new GZIPInputStream(is);
			InputStreamReader isr = new InputStreamReader(gzin, "utf-8"); // 设置读取流的编码格式，自定义编码
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			while((line=reader.readLine())!=null)
				sb.append(line+" ");
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(sb.toString());
		return sb.toString();
		
	}
	
	
	/**
	 * 将JSON格式数据进行解析 ，返回一个weather对象
	 * @param str
	 * @return
	 */
	public static WeatherInfo GetWeather(String weatherInfobyJson){
		JSONObject dataOfJson = JSONObject.fromObject(weatherInfobyJson);
		if(dataOfJson.getInt("status")!=1000)
			return null;
		
		//创建WeatherInfo对象，提取所需的天气信息
		WeatherInfo weatherInfo = new WeatherInfo();
		
		//从json数据中提取数据
		String data = dataOfJson.getString("data");
		
		dataOfJson = JSONObject.fromObject(data);
		weatherInfo.setCityname(dataOfJson.getString("city"));;
		weatherInfo.setAirquality(dataOfJson.getString("wendu"));//温度
		
		//获取预测的天气预报信息
		JSONArray forecast = dataOfJson.getJSONArray("forecast");
		//取得当天的
		JSONObject result=forecast.getJSONObject(0);
		
		weatherInfo.setDate(result.getString("date"));
		
		String high = result.getString("high").substring(2);
		String low  = result.getString("low").substring(2);
		
		weatherInfo.setTemperature(low+"~"+high);
		
		weatherInfo.setWeather(result.getString("type"));
		
		
		
		return weatherInfo;
	}
}