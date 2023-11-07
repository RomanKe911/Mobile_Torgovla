package kg.roman.Mobile_Torgovla.Spravochnik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;

import java.util.List;
import java.util.Vector;

import adapters.MyFragmentPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import fragments.Tab1Fragment_Month;
import fragments.Tab2Fragment_Month;
import kg.roman.Mobile_Torgovla.R;

import static android.R.id.tabhost;


public class SPR_Srtih_Activity extends AppCompatActivity implements
        OnTabChangeListener, ViewPager.OnPageChangeListener {

	private TabHost tabHost;
	private ViewPager viewPager;
	private MyFragmentPagerAdapter myViewPagerAdapter;
	int i = 0;
	public Intent intent;
	public String mounth_this;
	public RadioGroup radioGroup;
	public Context context_Activity;
	public ListView listView;

	// fake content for tabhost
	class FakeContent implements TabContentFactory {
		private final Context mContext;

		public FakeContent(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumHeight(0);
			v.setMinimumWidth(0);
			return v;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_viewpager_layout);

		/*getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.logo_bella);*/
		intent = getIntent();
		mounth_this = intent.getStringExtra("Month_this");

		i++;

		// init tabhost
		this.initializeTabHost(savedInstanceState);

		// init ViewPager
		this.initializeViewPager();

	}

	private void initializeViewPager() {
		List<Fragment> fragments = new Vector<Fragment>();

		fragments.add(new Tab1Fragment_Month());
		fragments.add(new Tab2Fragment_Month());





		this.myViewPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		this.viewPager = (ViewPager) super.findViewById(R.id.viewPager);
		this.viewPager.setAdapter(this.myViewPagerAdapter);
		this.viewPager.setOnPageChangeListener(this);
		onRestart();

	}

	private void initializeTabHost(Bundle args) {

		/*tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		for (int i = 1; i <= 6; i++) {

			TabHost.TabSpec tabSpec;
			tabSpec = tabHost.newTabSpec("Tab " + i);
			tabSpec.setIndicator("Tab " + i);
			tabSpec.setContent(new FakeContent(this));
			tabHost.addTab(tabSpec);
		}
		tabHost.setOnTabChangedListener(this);*/

		tabHost = (TabHost) findViewById(tabhost);
		// инициализация
		tabHost.setup();

		TabHost.TabSpec tabSpec;

		// создаем вкладку и указываем тег
		tabSpec = tabHost.newTabSpec("tag1");
		//tabSpec.setIndicator(getString(R.string.tab_host_bonus_1));
		View v1 = getLayoutInflater().inflate(R.layout.tab_header_1, null);
		tabSpec.setIndicator(v1);
		//tabSpec.setContent(R.id.i_layout_1);
		tabSpec.setContent(new FakeContent(this));
		tabHost.addTab(tabSpec);
		//tabHost.setCurrentTab(0);


		tabSpec = tabHost.newTabSpec("tag2");
		//tabSpec.setIndicator(getString(R.string.tab_host_bonus_2));
		View v2 = getLayoutInflater().inflate(R.layout.tab_header_2, null);
		tabSpec.setIndicator(v2);
		tabSpec.setContent(new FakeContent(this));
		//tabSpec.setContent(R.id.i_layout_2);
		tabHost.addTab(tabSpec);


		tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.tab_color_primary));
		tabHost.setOnTabChangedListener(this);
	}

	@Override
	public void onTabChanged(String tabId) {
		int pos = this.tabHost.getCurrentTab();
		this.viewPager.setCurrentItem(pos);

		switch (tabId) {
			case "tag1": {
				tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.tab_color_primary));
				tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(getResources().getColor(R.color.tab_color_primary_back));


			}
			break;
			case "tag2": {
				tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.tab_color_primary_back));
				tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(getResources().getColor(R.color.tab_color_primary));

			}
			break;

			default: {
				tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(getResources().getColor(R.color.title_colorAccent));
				tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(getResources().getColor(R.color.title_colorAccent));

			}
			break;
		}

		HorizontalScrollView hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollView);
		View tabView = tabHost.getCurrentTabView();
		int scrollPos = tabView.getLeft()
				- (hScrollView.getWidth() - tabView.getWidth()) / 2;
		hScrollView.smoothScrollTo(scrollPos, 0);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		this.tabHost.setCurrentTab(position);
	}
}
