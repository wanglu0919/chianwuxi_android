package com.wuxi.app.fragment.homepage.informationcenter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wuxi.app.fragment.BaseSlideFragment;
import com.wuxi.app.fragment.commonfragment.ContentListFragment;
import com.wuxi.app.fragment.commonfragment.MenuItemMainFragment;
import com.wuxi.app.util.Constants.FragmentName;
import com.wuxi.domain.Content;

/**
 * 
 * @author wanglu 泰得利通 内容列表菜单
 * 
 */
@SuppressLint("HandlerLeak")
public class InforContentListFragment extends ContentListFragment {

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1,
			int position, long arg3) {

		Content content = (Content) adapterView.getItemAtPosition(position);
		if (super.parentItem != null) {
			if (parentItem.getName().equals("热点专题")) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("content", content);

				BaseSlideFragment baseSlideFragment = (BaseSlideFragment) getArguments()
						.get(MenuItemMainFragment.ROOTFRAGMENT_KEY);
				
				baseSlideFragment.slideLinstener.replaceFragment(null, -1, FragmentName.HOTTOPICCONTENTFRAGMENT, bundle);//跳转

			}

		}

	}
}