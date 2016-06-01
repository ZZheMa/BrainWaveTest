package com.example.sms1_0.ui.activity;

import java.util.ArrayList;

import com.example.brainwave_test.R;
import com.example.sms1_0.adapter.MainPagerAdapter;
import com.example.sms1_0.base.BaseActivity;
import com.example.sms1_0.ui.fragment.ConversationFragment;
import com.example.sms1_0.ui.fragment.GroupFragment;
import com.example.sms1_0.ui.fragment.SearchFragment;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private MainPagerAdapter pagerAdapter;
    private TextView tv_tab_conversation;
    private TextView tv_tab_group;
    private TextView tv_tab_search;
    private ImageView iv_tab_conversation;
    private ImageView iv_tab_group;
    private ImageView iv_tab_search;
    private LinearLayout ll_tab_conversation;
    private LinearLayout ll_tab_group;
    private LinearLayout ll_tab_search;
    private View v_indicator_line;

    private ConversationFragment conversationFragment;
    private GroupFragment groupFragment;
    private SearchFragment searchFragment;
    
    private BluetoothAdapter bluetoothAdapter;

	/**
     * ��ʼ���ؼ�
     */
	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		tv_tab_conversation = (TextView)findViewById(R.id.tv_tab_conversation);
		tv_tab_group = (TextView)findViewById(R.id.tv_tab_group);
		tv_tab_search = (TextView)findViewById(R.id.tv_tab_search);
		iv_tab_conversation = (ImageView)findViewById(R.id.iv_tab_conversation);
		iv_tab_group = (ImageView)findViewById(R.id.iv_tab_group);
		iv_tab_search = (ImageView)findViewById(R.id.iv_tab_search);
		ll_tab_conversation = (LinearLayout)findViewById(R.id.ll_tab_conversation);
		ll_tab_group = (LinearLayout)findViewById(R.id.ll_tab_group);
		ll_tab_search = (LinearLayout)findViewById(R.id.ll_tab_search);
		v_indicator_line = (View)findViewById(R.id.v_indicator_line);
	}

	/**
	 * ��ʼ������
	 */
	@Override
	public void initData() {
		
		//��ȡ����������
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
        conversationFragment = new ConversationFragment();
        groupFragment = new GroupFragment();
        searchFragment = new SearchFragment();
        
        conversationFragment.setBluetoothAdapter(bluetoothAdapter);
        groupFragment.setBluetoothAdapter(bluetoothAdapter);
        searchFragment.setBluetoothAdapter(bluetoothAdapter);
        
		//�����д��仰����ôfragments��ָ��һ��null��null���޷�add�ģ���ָ���쳣
		fragments = new ArrayList<Fragment>();
		fragments.add(conversationFragment);
	 	fragments.add(groupFragment);
	 	fragments.add(searchFragment);
	 		
		pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
		
		viewPager.setAdapter(pagerAdapter);
		
		textLightAndScale();
		computeIndicatorLine();
	}

	
	/**
	 * ��ʼ������
	 */
	@Override
	public void initListener() {

		//ѡ��������
		ll_tab_conversation.setOnClickListener(this);
		ll_tab_group.setOnClickListener(this);
		ll_tab_search.setOnClickListener(this);
		
		//viewpagerҳ�滬������
		// �����ƶ�
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				textLightAndScale();
			}
			@Override
			public void onPageScrolled(int position, float positionOffset, int pixelsOffset) {
				//�������λ��
				int distance = pixelsOffset / 3;
				//λ�Ƽ����������յ�
				ViewPropertyAnimator.animate(v_indicator_line).translationX(distance + position * v_indicator_line.getWidth()).setDuration(0);
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		
	}


	/**
	 * ��ʼ������¼�
	 */
	@Override
	public void processClick(View v) {
		//ѡ��������
		int id = v.getId();
		switch (id) {
		case R.id.ll_tab_conversation :
			viewPager.setCurrentItem(0);  //ΪʲôҪ��viewpager���������Ĵ������ܽ�ҳ���л���
										//��Ϊ����ҳ����ͨ��viewpager����ʾ�ġ�
			break;
		case R.id.ll_tab_group :
			viewPager.setCurrentItem(1);
			break;
		case R.id.ll_tab_search :
			viewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
		
	}
 
	/**
	 * �ı�������ɫ��С
	 */
	public void textLightAndScale() {
		//��ȡ��ǰѡ���
		int item = viewPager.getCurrentItem();
		//�ı�������ɫ
		tv_tab_conversation.setTextColor((item == 0) ? Color.WHITE : 0xff999999);
		tv_tab_group.setTextColor((item == 1) ? Color.WHITE : 0xff999999);
		tv_tab_search.setTextColor((item == 2) ? Color.WHITE : 0xff999999);
		
		//�ı������С
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleX((item == 0) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_conversation).scaleY((item == 0) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleX((item == 1) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_group).scaleY((item == 1) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleX((item == 2) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(tv_tab_search).scaleY((item == 2) ? 1.2f : 1).setDuration(200);
	
		//�ı�ͼƬ��С
		ViewPropertyAnimator.animate(iv_tab_conversation).scaleX((item == 0) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(iv_tab_conversation).scaleY((item == 0) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(iv_tab_group).scaleY((item == 1) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(iv_tab_group).scaleY((item == 1) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(iv_tab_search).scaleY((item == 2) ? 1.2f : 1).setDuration(200);
		ViewPropertyAnimator.animate(iv_tab_search).scaleY((item == 2) ? 1.2f : 1).setDuration(200);
 
		
	}
	
	/**
	 * ����ָʾ�߳���
	 */
	public void computeIndicatorLine() {
		@SuppressWarnings("deprecation")
		int width = getWindowManager().getDefaultDisplay().getWidth();
		v_indicator_line.getLayoutParams().width = width / 3;
	}
	
}




