package com.example.chinesejoke.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chinesejoke.entity.Joke;

/*
 * ����json��ʽ���ݣ�����������list��
 * 
 */
public class JsonParser {
	public static List<Joke> parseJson(String jsonResult) throws JSONException {
		// ���������
		JSONObject jsonObjectOut = new JSONObject(jsonResult);
		// ˵���ɹ���ȡ����
		if (0 == jsonObjectOut.getInt("res_code")) {
			// ����body
			JSONObject jsonObjectBody = jsonObjectOut.getJSONObject("res_body");
			// ��ȡ����
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
