package ru.nekit.androidsamplelist.listAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.model.vo.QuakeVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuakeListAdapter extends ArrayAdapter<QuakeVO> {

	Activity context;
	List<QuakeVO> dataSource;
	LayoutInflater inflater;

	static class ViewHolder {

		public TextView magnitude;
		public TextView date;
		public TextView description;
		public TextView location;
		//public TextView link;
	}

	public QuakeListAdapter(Activity context, List<QuakeVO> objects) {
		super(context, R.layout.quake_list_item, objects);
		this.dataSource = objects;
		this.context 	= context;
		this.inflater 	= context.getLayoutInflater();
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.quake_list_item, null, true);
			holder = new ViewHolder();
			holder.magnitude = (TextView) rowView.findViewWithTag("magnitude");
			holder.date = (TextView) rowView.findViewWithTag("date");
			holder.location = (TextView) rowView.findViewWithTag("location");
			holder.description = (TextView) rowView.findViewWithTag("description");
			//holder.link = (TextView) rowView.findViewWithTag("link");
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		QuakeVO item = dataSource.get(index);
		holder.magnitude.setText("M"+item.magnitude);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd hh:mm:ss");
		holder.date.setText("Date: " +sdf.format(item.date));
		holder.location.setText("Position: [ lat: " + item.location.getLatitude() + " | lon:" + item.location.getLongitude() +" ]" );
		holder.description.setText(item.description);
		return rowView;
	}
}
