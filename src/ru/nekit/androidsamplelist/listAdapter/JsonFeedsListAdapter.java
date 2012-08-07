package ru.nekit.androidsamplelist.listAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.model.vo.FeedSimpleItemVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JsonFeedsListAdapter extends ArrayAdapter<FeedSimpleItemVO> {

	Activity context;
	List<FeedSimpleItemVO> dataSource;
	LayoutInflater inflater;

	static class ViewHolder {

		public TextView name;
		public TextView date;
		public ImageView image;
	}

	public JsonFeedsListAdapter(Activity context, List<FeedSimpleItemVO> objects) {
		super(context, R.layout.json_feeds_list_item, objects);
		this.dataSource = objects;
		this.context 	= context;
		this.inflater 	= context.getLayoutInflater();
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.json_feeds_list_item, null, true);
			holder = new ViewHolder();
			holder.name = (TextView) rowView.findViewWithTag("name");
			holder.date = (TextView) rowView.findViewWithTag("date");
			holder.image = (ImageView) rowView.findViewWithTag("image");
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		FeedSimpleItemVO item = dataSource.get(index);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd hh:mm:ss");
		holder.date.setText("Date: " +sdf.format(item.date));
		holder.name.setText(item.name);
		return rowView;
	}
}
