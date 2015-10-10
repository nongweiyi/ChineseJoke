package com.example.chinesejoke.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chinesejoke.entity.Joke;

/*
 * 解析json格式数据，将结果存放在list中
 * 
 */
public class JsonParser {
	public static List<Joke> parseJson(String jsonResult) throws JSONException {
		// 解析最外层
		JSONObject jsonObjectOut = new JSONObject(jsonResult);
		// 说明成功获取数据
		if (0 == jsonObjectOut.getInt("res_code")) {
			// 解析body
			JSONObject jsonObjectBody = jsonObjectOut.getJSONObject("res_body");
			// 获取数组
			JSONArray jsonArray = jsonObjectBody.getJSONArray("JokeList");
			List<Joke> jokeList = new ArrayList<Joke>();
			for (int index = 0; index < jsonArray.length(); index++) {

				JSONObject contentObject = jsonArray.getJSONObject(index);
				String billNo = contentObject.getString("BillNo");
				String jokeTitle = contentObject.getString("JokeTitle");
				String jokeContent = contentObject.getString("JokeContent");
				int type = contentObject.getInt("Type");
				Joke joke = new Joke(billNo, jokeTitle, jokeContent, type);
				jokeList.add(joke);

			}
			return jokeList;
		}
		return null;
	}
}
