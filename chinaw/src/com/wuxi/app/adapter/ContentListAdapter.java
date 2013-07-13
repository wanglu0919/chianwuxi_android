package com.wuxi.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wuxi.app.R;
import com.wuxi.domain.Content;

/**
 * 
 * @author wanglu 泰得利通 内容列表适配器
 * 
 */
public class ContentListAdapter extends BaseAdapter {

	private List<Content> contents;
	private Context context;

	public ContentListAdapter(List<Content> contents, Context context) {

		this.contents = contents;
		this.context=context;
	}

	@Override
	public int getCount() {
		return contents.size();
	}

	@Override
	public Object getItem(int position) {
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView title_text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Content content = contents.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.content_list_item_layout, null);
			viewHolder = new ViewHolder();

			viewHolder.title_text = (TextView) convertView
					.findViewById(R.id.content_list_tv_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title_text.setText(content.getTitle());
		return convertView;
	}

	public void addItem(Content content) {
		this.contents.add(content);
	}

}