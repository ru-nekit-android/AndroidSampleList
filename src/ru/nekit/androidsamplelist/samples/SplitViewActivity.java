package ru.nekit.androidsamplelist.samples;

import java.util.ArrayList;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpFragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class SplitViewActivity extends GoUpFragmentActivity {

	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options;
	private static ArrayList<ImageData> data;

	public class ImageData
	{
		public String name;
		public String[] images;

		public ImageData(String name, String[] images)
		{
			this.name = name;
			this.images = images;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		data = new ArrayList<ImageData>();

		data.add(new ImageData("One",getResources().getStringArray(R.array.one_images)));
		data.add(new ImageData("Two",getResources().getStringArray(R.array.two_images)));

		options = new DisplayImageOptions.Builder()
		.showStubImage(android.R.drawable.ic_input_add)
		.cacheInMemory()
		.build();
		setContentView(R.layout.fragment_split_view);
	}

	public static class MyListFragment extends SherlockListFragment
	{

		boolean isSplitView;
		public int currentPosition = 0;

		public void onActivityCreated(Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);
			ArrayList<String> titles = new ArrayList<String>(); 
			for( ImageData image : data )
			{
				titles.add(image.name);
			}
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.simple_list_item_checkable,
					R.id.textView, titles));

			View detailsFrame = getActivity().findViewById(R.id.details);
			isSplitView =  detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
			if (savedInstanceState != null) 
			{
				currentPosition = savedInstanceState.getInt("currentPosition", 0);
			}
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			if( isSplitView )
			{
				showDetails(currentPosition);
			}

		}

		@Override
		public void onListItemClick(ListView list, View view, int position, long id)
		{
			showDetails(position);
		}

		private void showDetails(int index) 
		{
			currentPosition = index;
			if( isSplitView )
			{
				getListView().setItemChecked(currentPosition, true);
				DetailsFragment details = (DetailsFragment)getFragmentManager().findFragmentById(R.id.details);
				if( details == null || currentPosition != details.getCurrentPosition() )
				{
					details = DetailsFragment.getInstance(currentPosition);
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.replace(R.id.details, details);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE | FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					ft.commit();
				}
			}else
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), DetailsActivity.class);
				intent.putExtra("currentPosition", currentPosition);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		}

		@Override
		public void onSaveInstanceState(Bundle outState)
		{
			super.onSaveInstanceState(outState);
			outState.putInt("currentPosition", currentPosition);
		}
	}

	public static class DetailsActivity extends GoUpFragmentActivity
	{
		@Override
		protected void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			{
				finish();
				return;
			}
			if (savedInstanceState == null) 
			{
				DetailsFragment details = new DetailsFragment();
				details.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
			}
		}
	}

	private static class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ImageData imageData;

		public ImageAdapter(Context context, ImageData imageData)
		{
			this.imageData = imageData;
			inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() 
		{
			return imageData.images.length;
		}

		@Override
		public Object getItem(int position) 
		{
			return null;
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView)inflater.inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView)convertView;
			}
			imageLoader.displayImage(imageData.images[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingFailed(FailReason failReason) {
					super.onLoadingFailed(failReason);
					if( failReason == FailReason.OUT_OF_MEMORY )
					{
						System.gc();
					}
				}
			});
			return imageView;
		}
	}

	public static class DetailsFragment extends SherlockFragment 
	{

		public int getCurrentPosition()
		{
			return getArguments().getInt("currentPosition", 0);
		}

		public static DetailsFragment getInstance(int currentPosition) 
		{
			DetailsFragment fragment = new DetailsFragment();
			Bundle args = new Bundle();
			args.putInt("currentPosition", currentPosition);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onDestroy() 
		{
			imageLoader.stop();
			imageLoader.clearMemoryCache();
			super.onDestroy();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		{
			if (container == null) 
			{
				return null;
			}
			GridView grid = new GridView(getActivity());
			int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getActivity().getResources().getDisplayMetrics());
			if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
			{
				grid.setNumColumns(GridView.AUTO_FIT);
			}
			else
			{
				grid.setNumColumns(1);
			}
			grid.setSmoothScrollbarEnabled(true);
			grid.setBackgroundColor(0x33333333);
			grid.setGravity(Gravity.CENTER);
			imageLoader.stop();
			imageLoader.clearMemoryCache();
			grid.setAdapter(new SplitViewActivity.ImageAdapter(getActivity(), data.get(getCurrentPosition())));

			grid.setHorizontalSpacing(padding);
			grid.setVerticalSpacing(padding);
			return grid;
		}
	}
}