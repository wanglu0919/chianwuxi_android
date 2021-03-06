/**
 * 
 */
package com.wuxi.app.fragment.homepage.mygoverinteractpeople;

import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.MainTabActivity;
import com.wuxi.app.R;
import com.wuxi.app.activity.homepage.mygoverinteractpeople.GIP12345MayorMailContentActivity;
import com.wuxi.app.engine.LetterService;
import com.wuxi.app.util.Constants;
import com.wuxi.app.util.LogUtil;
import com.wuxi.domain.LetterWrapper;
import com.wuxi.domain.LetterWrapper.Letter;
import com.wuxi.exception.NODataException;
import com.wuxi.exception.NetException;

/**
 * 政民互动 12345来信办理平台 建议咨询投诉列表 界面碎片
 * 
 * @author 智佳 罗森
 * 
 */
public class GIP12345ComplaintListFragment extends BaseFragment {

	protected static final String TAG = "GIP12345ComplaintListFragment";

	private View view = null;
	private Context context = null;

	private ListView mListView;
	private ProgressBar list_pb;
	private LetterWrapper letterWrapper;
	private List<LetterWrapper.Letter> letters;

	private static final int DATA__LOAD_SUCESS = 0;
	private static final int DATA_LOAD_ERROR = 1;

	private int startIndex = 0; // 获取话题的起始坐标
	private int endIndex = 20; // 获取话题的结束坐标

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String tip = "";

			if (msg.obj != null) {
				tip = msg.obj.toString();
			}
			switch (msg.what) {
			case DATA__LOAD_SUCESS:
				list_pb.setVisibility(View.INVISIBLE);
				showLettersList();
				break;
			case DATA_LOAD_ERROR:
				Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.complaint_list_layout, null);
		context = getActivity();

		initLayout();

		return view;
	}

	/**
	 * 初始化布局控件
	 */
	private void initLayout() {
		mListView = (ListView) view
				.findViewById(R.id.gip_12345_complaint_listView);
		list_pb = (ProgressBar) view
				.findViewById(R.id.gip_12345_complaint_listView_pb);

		list_pb.setVisibility(View.VISIBLE);
		loadData();
	}

	/**
	 * 加载数据
	 */
	public void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				LetterService letterService = new LetterService(context);
				try {
					letterWrapper = letterService.getLetterLitstWrapper(
							Constants.Urls.SUGGESTLETTER_URL, startIndex,
							endIndex);
					if (null != letterWrapper) {
						letters = letterWrapper.getData();

						handler.sendEmptyMessage(DATA__LOAD_SUCESS);

					} else {
						Message message = handler.obtainMessage();
						message.obj = "error";
						handler.sendEmptyMessage(DATA_LOAD_ERROR);
					}

				} catch (NetException e) {
					LogUtil.i(TAG, "出错");
					e.printStackTrace();
					Message message = handler.obtainMessage();
					message.obj = e.getMessage();
					handler.sendEmptyMessage(DATA_LOAD_ERROR);

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NODataException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 显示列表
	 */
	public void showLettersList() {
		
		LettersListViewAdapter adapter = new LettersListViewAdapter(
				);
		if (letters == null || letters.size() == 0) {
			Toast.makeText(context, "对不起，暂无建议咨询投诉信息", Toast.LENGTH_SHORT).show();
		} else {
			mListView.setAdapter(adapter);
			mListView.setOnItemClickListener(adapter);
		}
	}

	/**
	 * 列表适配器
	 * 
	 * @author 智佳 罗森
	 * 
	 */
	public class LettersListViewAdapter extends BaseAdapter implements
			OnItemClickListener {

		/**
		 * @方法： LettersListViewAdapter
		 * @描述：
		 * @param fragment
		 */
		public LettersListViewAdapter() {
			
		}

		@Override
		public int getCount() {
			return letters.size();
		}

		@Override
		public Object getItem(int position) {
			return letters.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			public TextView title_text;
			public TextView code_text;
			public TextView type_text;
			public TextView depname_text;
			public TextView answerDate_text;
			public TextView readCount_text;
			public TextView appraise_text;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.gip_12345_complaint_listview_item, null);

				viewHolder = new ViewHolder();

				viewHolder.title_text = (TextView) convertView
						.findViewById(R.id.gip_12345_complaint_tilte);
				viewHolder.code_text = (TextView) convertView
						.findViewById(R.id.gip_12345_complaint_code);
				viewHolder.type_text = (TextView) convertView
						.findViewById(R.id.gip_12345_complaint_type);
				viewHolder.depname_text = (TextView) convertView
						.findViewById(R.id.gip_12345_complaint_depname);
				viewHolder.answerDate_text = (TextView) convertView
						.findViewById(R.id.gip_12345_complaint_answerDate);
				viewHolder.readCount_text = (TextView) convertView
						.findViewById(R.id.gip_12345_complaint_readcount);
				viewHolder.appraise_text = (TextView) convertView
						.findViewById(R.id.gip_12345_complaint_appraise);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.title_text.setText(letters.get(position).getTitle());
			viewHolder.code_text.setText(letters.get(position).getCode());
			viewHolder.type_text.setText(letters.get(position).getType());
			viewHolder.depname_text.setText(letters.get(position).getDepname());
			
			viewHolder.answerDate_text.setText(letters.get(position)
					.getAnswerdate());
			
			viewHolder.readCount_text.setText(String.valueOf(letters.get(
					position).getReadcount()));
			viewHolder.appraise_text.setText(letters.get(position)
					.getAppraise());

			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View arg1,
				int position, long arg3) {
			Letter letter = (Letter) adapterView.getItemAtPosition(position);

			Intent intent = new Intent(getActivity(), GIP12345MayorMailContentActivity.class);
			intent.putExtra("letter", letter);
			MainTabActivity.instance.addView(intent);
		}

	}

}
