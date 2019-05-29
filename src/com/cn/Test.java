package com.cn;

public class Test {
	public static void main(String[]  args){
		String info = WeatherUtils.GetWeatherData("合肥");
		WeatherInfo weatherinfo = WeatherUtils.GetWeather(info);
		System.out.println(weatherinfo.toString());
	}
}
