package com.example.chinesejoke.adapter;

import java.util.List;

import com.example.chinesejoke.R;
import com.example.chinesejoke.entity.Joke;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	Context context;
	List<Joke> jokeList;

	public CustomAdapter(Context context, List<Joke> jokeList) {
		super();
		this.context = context;
		this.jokeList = jokeList;
	}

	@Override
	public int getCount() {
		return jokeList.size();
	}

	@Override
	public Object getItem(int position) {
		return jokeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewHolder;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			view = View.inflate(context, R.layout.listview_item, null);
			viewHolder.listview_item_title = (TextView) view.findViewById(R.id.listview_item_title_id);
			viewHolder.listview_item_subContent = (TextView) view.findViewById(R.id.listview_item_content_id);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		Joke joke = jokeList.get(position);
		
		viewHolder.listview_item_title.setText(joke.getJokeTitle());
		viewHolder.listview_item_subContent.setText(joke.getJokeContent());

		return view;
	}

	class ViewHolder {
		TextView listview_item_title;
		TextView listview_item_subContent;
	}
	

}
