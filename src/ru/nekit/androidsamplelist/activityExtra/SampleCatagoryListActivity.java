package ru.nekit.androidsamplelist.activityExtra;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.listAdapter.MenuListAdapter;
import ru.nekit.androidsamplelist.model.vo.MenuItemVO;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public abstract class SampleCatagoryListActivity extends GoUpActivity implements OnItemClickListener {

	ListView menuView;
	MenuListAdapter adapter;
	ArrayList<MenuItemVO> dataList;
	Map<MenuItemVO, Intent> intentMap = new HashMap<MenuItemVO, Intent>();

	abstract protected String getCategory();

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_list_category);
		menuView = (ListView)findViewById(R.id.menuView);
		dataList = new ArrayList<MenuItemVO>();
		getData();
		adapter = new MenuListAdapter(this, dataList);
		menuView.setDivider(null);
		menuView.setDividerHeight(0);
		menuView.setOnItemClickListener(this);
		menuView.setAdapter(adapter);
	}

	private void getData()
	{

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(getCategory());

		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

		final int length = list.size();

		for (int i = 0; i < length; i++) 
		{
			ResolveInfo info = list.get(i);
			CharSequence labelSeq = info.loadLabel(pm);

			String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
			String description = null;
			int descriptionRes = info.activityInfo.descriptionRes;
			if( descriptionRes != 0 )
			{
				description = getString(descriptionRes);
			}
			addItem(dataList, new MenuItemVO(label, description), activityIntent(
					info.activityInfo.applicationInfo.packageName,
					info.activityInfo.name));
		}
		Collections.sort(dataList, displayNameComparator);
	}

	private final static Comparator<MenuItemVO> displayNameComparator =
			new Comparator<MenuItemVO>() {
		private final Collator   collator = Collator.getInstance();

		public int compare(MenuItemVO item1, MenuItemVO item2) {
			return collator.compare(item1.title, item2.title);
		}
	};

	protected void addItem(List<MenuItemVO> list, MenuItemVO item, Intent intent) 
	{
		intentMap.put(item, intent);
		list.add(item);
	}

	protected Intent activityIntent(String pkg, String componentName) 
	{
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long i) 
	{
		MenuItemVO item = this.adapter.getItem(position);
		startActivity(intentMap.get(item));
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}

}