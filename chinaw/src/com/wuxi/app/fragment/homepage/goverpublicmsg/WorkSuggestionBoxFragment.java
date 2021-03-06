package com.wuxi.app.fragment.homepage.goverpublicmsg;

import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.R;
import com.wuxi.app.dialog.LoginDialog;
import com.wuxi.app.engine.WorkSuggestionService;
import com.wuxi.app.util.CacheUtil;
import com.wuxi.app.util.Constants;
import com.wuxi.app.util.SystemUtil;
import com.wuxi.domain.MailBoxParameterItem;
import com.wuxi.domain.MenuItem;
import com.wuxi.domain.WorkSuggestionBoxWrapper;
import com.wuxi.exception.NODataException;
import com.wuxi.exception.NetException;


/**
 * 工作意见邮箱  Fragment 布局
 * @author 杨宸 智佳
 * */

public class WorkSuggestionBoxFragment extends BaseFragment implements OnClickListener{
	protected View view;
	protected LayoutInflater mInflater;
	private Context context;
	private MenuItem parentMenuItem;
	private WorkSuggestionBoxWrapper boxWrapper;
	private List<MailBoxParameterItem> parameterItems;

	private static final int DATA__LOAD_SUCESS = 0;
	private static final int DATA_LOAD_ERROR = 1;
	private static final int DATA_SUBMIT_SUCCESS = 2;
	private static final int DATA_SUBMIT_FAILED = 3;
	protected static final int CHANNELCONTENT_ID=R.id.govermsg_custom_content;

	private ProgressBar processBar;
	private LinearLayout layout;
	private RelativeLayout submit_layout;

	private ImageButton submit_btn;
	private ImageButton cancel_btn;

	private LoginDialog loginDialog;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String tip = "";
			if (msg.obj != null) {
				tip = msg.obj.toString();
			}
			switch (msg.what) {
			case DATA__LOAD_SUCESS:
				processBar.setVisibility(View.INVISIBLE);
				showLayout();
				break;
			case DATA_LOAD_ERROR:
				processBar.setVisibility(View.INVISIBLE);
				Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
				break;
			case DATA_SUBMIT_SUCCESS:
				processBar.setVisibility(View.INVISIBLE);
				Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
				break;
			case DATA_SUBMIT_FAILED:
				processBar.setVisibility(View.INVISIBLE);
				Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show();
				break;

			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.worksuggestionbox_layout, null);
		mInflater = inflater;
		context = getActivity();

		initLayout();
		return view;

	}

	public void initLayout(){
		loginDialog=new LoginDialog(context);
		if(!loginDialog.checkLogin()){
			loginDialog.showDialog();
		}
		processBar=(ProgressBar)view.findViewById(R.id.worksuggestbox_progressbar);
		submit_layout=(RelativeLayout)view.findViewById(R.id.worksuggestbox_submit_layout);

		processBar.setVisibility(View.VISIBLE);
		loadData();
	}

	private void loadData(){

		if (boxWrapper!=null&&CacheUtil.get(boxWrapper.getId()) != null) {// 从缓存中查找子菜单
			boxWrapper = (WorkSuggestionBoxWrapper) CacheUtil.get(boxWrapper.getId());
			processBar.setVisibility(View.INVISIBLE);
			showLayout();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {

				WorkSuggestionService boxService = new WorkSuggestionService(context);
				try {
					boxWrapper = boxService.getBoxWrapper(Constants.Urls.GOVERMSG_WORKSUGGESTIONBOX_LAYOUT_URL);
					if (boxWrapper != null) {
						handler.sendEmptyMessage(DATA__LOAD_SUCESS);
						CacheUtil.put(boxWrapper.getId(), boxWrapper);// 放入缓存
					}
					else{
						Message msg = handler.obtainMessage();
						msg.obj = "暂无信息";
						msg.what = DATA_LOAD_ERROR;
						handler.sendMessage(msg);
					}
				} catch (NetException e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = e.getMessage();
					msg.what = DATA_LOAD_ERROR;
					handler.sendMessage(msg);
				} catch (NODataException e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = e.getMessage();
					msg.what = DATA_LOAD_ERROR;
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = e.getMessage();
					msg.what = DATA_LOAD_ERROR;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	public void showLayout(){
		parameterItems=boxWrapper.getParameters();
		if(parameterItems!=null){
			layout = (LinearLayout) view.findViewById(R.id.worksuggestbox_layout);
			for(MailBoxParameterItem item:parameterItems){
				//根据InputType先判断单行文本还是多行文本
				LinearLayout subLayout = null ;
				if(item.getInputType().equals("SingleTextBox")){
					subLayout = (LinearLayout) mInflater.inflate(
							R.layout.worksuggestionbox_singleline_layout, null).findViewById(R.id.worksuggestionbox_singletxtbox_layout);

				}
				else if(item.getInputType().equals("MultiTextBox")){
					subLayout = (LinearLayout) mInflater.inflate(
							R.layout.worksuggestionbox_mutilline_layout, null).findViewById(R.id.worksuggestionbox_mutiltxtbox_layout);
				}
				if(subLayout!=null){
					TextView title_tv=(TextView)subLayout.getChildAt(0);
					EditText content_et=(EditText)subLayout.getChildAt(1);

					title_tv.setText(item.getAlias()+":");
					content_et.setText(item.getValueList());

					layout.addView(subLayout);
				}		
			}
		}
		submit_layout.setVisibility(View.VISIBLE);
		initView();
	}


	public void initView(){
		submit_btn=(ImageButton)view.findViewById(R.id.worksuggestbox_imgbutton_submit);
		cancel_btn=(ImageButton)view.findViewById(R.id.worksuggestbox_imgbutton_cancel);
		submit_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);

	}
	public void setParentMenuItem(MenuItem parentMenuItem) {
		this.parentMenuItem = parentMenuItem;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.worksuggestbox_imgbutton_submit:
			if(loginDialog.checkLogin()){
				String access_token=SystemUtil.getAccessToken(context);
				try {
					submitMail(access_token);
				} catch (NetException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NODataException e) {
					e.printStackTrace();
				}
			}
			else{
				loginDialog.showDialog();
			}

			break;
		case R.id.worksuggestbox_imgbutton_cancel:
			break;
		}
	}

	public void submitMail(final String access_token) throws NetException, JSONException, NODataException{
		processBar.setVisibility(View.VISIBLE);
		if(!judgeIsLegal()){
			new Thread(new Runnable() {

				@Override
				public void run() {
					WorkSuggestionService workSuggestionService=new WorkSuggestionService(context);
					try {
						boolean success=false;
						success=workSuggestionService.submitMailBox(access_token,boxWrapper);
						if(success){
							handler.sendEmptyMessage(DATA_SUBMIT_SUCCESS);
						}
						else{
							handler.sendEmptyMessage(DATA_SUBMIT_FAILED);
						}
					} catch (NetException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (NODataException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 *判断输入情况是否合法 
	 * */
	public boolean judgeIsLegal(){
		boolean submitError=false;
		int index=0;
		for(MailBoxParameterItem item:boxWrapper.getParameters()){
			LinearLayout subLayout = (LinearLayout) layout.getChildAt(index);
			EditText content_et=(EditText)subLayout.getChildAt(1);

			item.setValueList(content_et.getText().toString());

			//对必填选项进行空值判断
			if(item.getRequiredForm()==1){
				if (content_et.getText().toString().equals("")) {
					Toast.makeText(context, item.getAlias()+"  不能为空！", Toast.LENGTH_SHORT).show();
					submitError=true;
					break;
				}
			}

			index++;
		}
		return submitError;
	}
}
