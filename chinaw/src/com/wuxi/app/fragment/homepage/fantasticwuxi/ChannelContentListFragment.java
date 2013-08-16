package com.wuxi.app.fragment.homepage.fantasticwuxi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wuxi.app.fragment.commonfragment.ContentDetailFragment;
import com.wuxi.app.fragment.commonfragment.ContentListFragment;
import com.wuxi.app.util.Constants.FragmentName;
import com.wuxi.domain.Content;


/**
 * 
 * @author wanglu 泰得利通
 *	内容列表界面
 */
public class ChannelContentListFragment extends ContentListFragment {

	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		Content content=(Content) adapterView.getItemAtPosition(position);
		Bundle bundle=new Bundle();
		bundle.putSerializable(ContentDetailFragment.CONTENT_KEY, content);
		if(this.channel!=null){
			bundle.putSerializable(ContentDetailFragment.CHANNEL_KEY, channel);
		}
		
		
		this.baseSlideFragment.slideLinstener.replaceFragment(null, -1, FragmentName.WUXICHANNELCONTENTDETAILFRAGMENT, bundle);
		
	}

	
}
