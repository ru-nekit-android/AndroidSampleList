package ru.nekit.androidsamplelist.listAdapter;

import java.util.List;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.model.vo.TwitterItemVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedorvlasov.lazylist.ImageLoader;

public class JsonTwitterListAdapter extends ArrayAdapter<TwitterItemVO> {

	private List<TwitterItemVO> dataSource;
	private LayoutInflater inflater;
	private ImageLoader imageLoader; 

	static class ViewHolder {
		public TextView username;
		public TextView message;
		public ImageView avatar;
	}

	public JsonTwitterListAdapter(Activity context, List<TwitterItemVO> objects) {
		super(context, R.layout.json_twitter_list_item, objects);
		this.dataSource = objects;
		this.inflater 	= context.getLayoutInflater();
		imageLoader     = new ImageLoader(context);
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.json_twitter_list_item, null, true);
			holder = new ViewHolder();
			holder.username = (TextView) rowView.findViewWithTag("username");
			holder.message = (TextView) rowView.findViewWithTag("message");
			holder.avatar = (ImageView) rowView.findViewWithTag("avatar");
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		TwitterItemVO item = dataSource.get(index);
		holder.username.setText(item.username);
		holder.message.setText(item.message);
		imageLoader.DisplayImage(item.image_url, holder.avatar);
		return rowView;
	}
}