package ru.nekit.androidsamplelist.listAdapter;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.model.vo.ActionItemVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public final class ActionListAdapter extends ArrayAdapter<ActionItemVO> {

	private ActionItemVO[] dataSource;
	static private LayoutInflater inflater;

	public ActionListAdapter(Activity context, ActionItemVO[] dataSource) {
		super(context, R.layout.action_list_item, dataSource);
		this.dataSource = dataSource;
		inflater = context.getLayoutInflater();
	}

	static class ViewHolder 
	{
		public TextView title;
		public ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.action_list_item, null, true);
			holder = new ViewHolder();
			holder.title = (TextView) rowView.findViewWithTag("title");
			holder.icon = (ImageView) rowView.findViewWithTag("icon");
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		ActionItemVO item = dataSource[position];
		holder.title.setText(item.title);
		holder.icon.setImageResource(item.iconID);
		return rowView;
	}
}