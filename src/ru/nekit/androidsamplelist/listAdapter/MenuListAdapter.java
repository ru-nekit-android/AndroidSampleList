package ru.nekit.androidsamplelist.listAdapter;

import java.util.List;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.model.vo.MenuItemVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuListAdapter extends ArrayAdapter<MenuItemVO> 
{

	Activity context;
	List<MenuItemVO> dataSource;
	LayoutInflater inflater;

	static class ViewHolder {

		public TextView title;
		public TextView subtitle;

	}

	public MenuListAdapter(Activity context, List<MenuItemVO> objects)
	{
		super(context, R.layout.menu_item, objects);
		this.dataSource = objects;
		this.context 	= context;
		this.inflater 	= context.getLayoutInflater();
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.menu_item, null, true);
			holder = new ViewHolder();
			holder.title = (TextView) rowView.findViewWithTag("title");
			holder.subtitle = (TextView) rowView.findViewWithTag("subtitle");
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		if( dataSource.size() > 1 )
		{
			if( index == 0 )
			{
				rowView.setBackgroundResource(R.drawable.background_view_rounded_top);
			}else if( index == dataSource.size() - 1 )
			{
				rowView.setBackgroundResource(R.drawable.background_view_rounded_bottom);
			}
			else
			{
				rowView.setBackgroundResource(R.drawable.background_view_rounded_middle);
			}
		}else{
			rowView.setBackgroundResource(R.drawable.background_view_rounded_single);
		}
		MenuItemVO item = dataSource.get(index);
		holder.title.setText(item.title);
		holder.subtitle.setText(item.subtitle);
		return rowView;
	}
}