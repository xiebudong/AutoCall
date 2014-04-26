package com.fgc.autocall.ui;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import com.fgc.autocall.R;
import com.fgc.autocall.Tools.Tools;
import com.fgc.autocall.ui.component.ButtonTwoState;
import com.fgc.autocall.ui.component.ButtonTwoState.OnTwoStateSwitchListener;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityMain extends BaseActivity {

	private static final String LOG_TAG = "ActivityMain";
	
	private RelativeLayout mLayoutTitleBar;
	private ButtonTwoState mButtonStartPause;
	
	private MenuDrawer mMenuDrawer;
	private ListView mMenuListView;
	private MenuAdapter mMenuAdapter;
	
	private LinearLayout mLayoutWraning;
	private LinearLayout mLayoutWraningOk;
	private TextView mTextOk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMenuDrawer = MenuDrawer.attach(this);
		mMenuDrawer.setContentView(R.layout.a_main);		
		
		initView();
	}

	private void initView()
	{
		mLayoutTitleBar = (RelativeLayout)findViewById(R.id.title_bar);
		Button btnLeft = (Button)mLayoutTitleBar.findViewById(R.id.title_btn_left);
		btnLeft.setBackgroundResource(R.drawable.selector_btn_menu);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)btnLeft.getLayoutParams();
		lp.leftMargin = Tools.dip2px(this, 8);
		btnLeft.setLayoutParams(lp);
		btnLeft.setOnClickListener(mOnClickListener);
		mButtonStartPause = (ButtonTwoState)mLayoutTitleBar.findViewById(R.id.title_btn_right);
		mButtonStartPause.setTwoStateDrawble(R.drawable.selector_btn_start, R.drawable.selector_btn_pause);
		mButtonStartPause.setOnTwoStateSwitchListener(mOnStatePauseSwitchListener);
		
		mLayoutWraning = (LinearLayout)findViewById(R.id.layout_wraning);
		mLayoutWraningOk = (LinearLayout)findViewById(R.id.layout_warning_ok);
		mLayoutWraningOk.setOnClickListener(mOnClickListener);
		mTextOk = (TextView)findViewById(R.id.text_ok);
		
		initLeftMenu();
	}
	
	  private static final int MENU_INDEX_SETTING = 0;
	  private static final int MENU_INDEX_EXPORT_CALL_RECORD = 1;
	  private static final int MENU_INDEX_CHECK_SIM_CARD_INFO = 2;
	  private static final int MENU_INDEX_ABOUT_US = 3;
    private void initLeftMenu()
    {
		View menuView = getLayoutInflater().inflate(R.layout.left_menu, null);
		mMenuDrawer.setMenuView(menuView);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setMenuSize(Tools.getScreenWidth(this)*2/3+40);
		mMenuDrawer.setDropShadowSize(Tools.dip2px(this, 10));
		
		
		Resources resource = getResources();
		
		
    	List<MenuItem> items = new ArrayList<MenuItem>();
        items.add(MENU_INDEX_SETTING, new MenuItem(resource.getString(R.string.left_menu_setting),
        												true,
        												true,
        												R.drawable.left_menu_setting));
    	items.add(MENU_INDEX_EXPORT_CALL_RECORD, new MenuItem(resource.getString(R.string.left_menu_export),
												 true, 
												 false,
												 R.drawable.left_menu_export));
        items.add(MENU_INDEX_CHECK_SIM_CARD_INFO, new MenuItem(resource.getString(R.string.left_menu_simcard),
												false,
												true,
												R.drawable.left_menu_simcard));
        items.add(MENU_INDEX_ABOUT_US, new MenuItem(resource.getString(R.string.left_menu_about),
        										 true,
        										 true,
        										 R.drawable.left_menu_about_us));
         
    	mMenuListView = (ListView)menuView.findViewById(R.id.list_view_menu);
    	mMenuAdapter = new MenuAdapter(items);
    	mMenuListView.setAdapter(mMenuAdapter);
    	mMenuListView.setOnItemClickListener(mItemClickListener);
    }
	
    private class MenuItem {

    	boolean mIsGroupStart;
    	boolean mIsGroupEnd;
    	int mIconResId;
        String mTitle;

        public MenuItem(String title, boolean isGroupStart, boolean isGroupEnd, int iconResId) 
        {
            mTitle = title;
            mIsGroupStart = isGroupStart;
            mIsGroupEnd = isGroupEnd;
        	mIconResId = iconResId;
        }
    }
    
	private OnTwoStateSwitchListener mOnStatePauseSwitchListener = new OnTwoStateSwitchListener()
	{

		@Override
		public void onSwitch(boolean isState1) {
			// TODO Auto-generated method stub
			Log.i(LOG_TAG, "is start: " + isState1);
		}
		
	};
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i(LOG_TAG, "onClick");
			
			switch (v.getId())
			{
			case R.id.title_btn_left:
				Log.i(LOG_TAG, "click title_btn_left");
				mMenuDrawer.openMenu();
				break;
			case R.id.layout_warning_ok:
				Log.i(LOG_TAG, "click layout_warning_ok");
				mTextOk.setText("ºÏ≤‚÷–...");
				mCheckFileHandler.sendEmptyMessageDelayed(0, 2000);
				break;
			}
		}
	};
	
	private Handler mCheckFileHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg)
		{
			mLayoutWraning.setVisibility(View.GONE);
		}
	};

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	
//        	Log.i(LOG_TAG, "menu position: " + position);
            mMenuDrawer.closeMenu();
            
            switch(position){
	            case MENU_INDEX_SETTING:{
	            	Log.i(LOG_TAG, "MENU_INDEX_SETTING");
	            	break;
	            }
	            case MENU_INDEX_EXPORT_CALL_RECORD:
	            {
	            	Log.i(LOG_TAG, "MENU_INDEX_EXPORT_CALL_RECORD");
	            	break;
	            }
	            case MENU_INDEX_CHECK_SIM_CARD_INFO:
	            {
	            	Log.i(LOG_TAG, "MENU_INDEX_CHECK_SIM_CARD_INFO");
	            	break;
	            }
	            case MENU_INDEX_ABOUT_US:{
	            	Log.i(LOG_TAG, "MENU_INDEX_ABOUT_US");
	            	break;
	            }
            }
        }
    };
	
    private class MenuAdapter extends BaseAdapter {

        private List<MenuItem> mItems;

        MenuAdapter(List<MenuItem> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof MenuItem ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItem(position) instanceof MenuItem;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
        	if (position<0 || position>=getCount())
        	{
        		return null;
        	}
        	
        	LinearLayout itemView = null;
            MenuItem item = (MenuItem)getItem(position);

            if (convertView == null)
            {
            	itemView = (LinearLayout)getLayoutInflater().inflate(R.layout.left_menu_item_title, parent, false);
            }
            else
            {
            	itemView = (LinearLayout)convertView;
            }
            
            LinearLayout groupSeperator = (LinearLayout)itemView.findViewById(R.id.layout_group_seperator);
            if (item.mIsGroupStart)
            {
            	groupSeperator.setVisibility(View.VISIBLE);
            }
            else
            {
            	groupSeperator.setVisibility(View.GONE);
            }
            LinearLayout endSeperator = (LinearLayout)itemView.findViewById(R.id.layout_end_seperator);
            if (item.mIsGroupEnd)
            {
            	Log.i(LOG_TAG, "hide end pos: " + position);
            	endSeperator.setVisibility(View.GONE);
            }
            else
            {
            	endSeperator.setVisibility(View.VISIBLE);
            }
            LinearLayout lastSeperator = (LinearLayout)itemView.findViewById(R.id.layout_last_seperator);
            if (position == getCount()-1)
            {
            	Log.i(LOG_TAG, "show last pos: " + position);
            	lastSeperator.setVisibility(View.VISIBLE);
            }
            else
            {
            	lastSeperator.setVisibility(View.GONE);
            }
            ImageView imageIcon = (ImageView)itemView.findViewById(R.id.image_icon);
            TextView textTitle = (TextView)itemView.findViewById(R.id.text_title);
            
            imageIcon.setImageResource(item.mIconResId);
            textTitle.setText((item.mTitle));

//            itemView.setTag(R.id.mdActiveViewPosition, position);

            return itemView;
        }
    }
    
	@Override
	protected boolean ifFinishAppByBackKeyPress(){
		return true;
	}
	
}
