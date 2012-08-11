package ru.nekit.androidsamplelist.samples;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import ru.nekit.androidsamplelist.listAdapter.QuakeListAdapter;
import ru.nekit.androidsamplelist.model.vo.QuakeVO;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class XMLQuakeActivity extends GoUpActivity {

	private static final int REFRESH = 1;
	private List<QuakeVO> dataSource;
	private QuakeListAdapter adapter;
	private ListView list;
	private static LoadXML task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xml_quake);
		list = (ListView)findViewById(android.R.id.list);
		dataSource = new ArrayList<QuakeVO>();
		adapter = new QuakeListAdapter(this, dataSource);
		list.setAdapter(adapter);
		refresh();
	}

	private void refresh()
	{
		task = new LoadXML();
		task.execute();
	}

	@Override
	protected void onStop() 
	{
		task.cancel(true);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(0, REFRESH, 0, "Refresh")
		.setIcon( R.drawable.ic_refresh_inverse )
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if( item.getItemId() == REFRESH )
		{
			task.cancel(true);
			refresh();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private class LoadXML extends AsyncTask<Void, QuakeVO, Integer>{

		static final String QUAKE_FEED = "http://earthquake.usgs.gov/eqcenter/catalogs/1day-M2.5.xml";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
			adapter.clear();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			URL url;
			Integer result = -1;
			try {
				url = new URL(QUAKE_FEED);
				URLConnection connection;
				connection = url.openConnection();
				HttpURLConnection httpConnection = (HttpURLConnection)connection; 
				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) 
				{ 
					InputStream in = httpConnection.getInputStream(); 
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document dom = db.parse(in);      
					Element docEle = dom.getDocumentElement();
					NodeList nl = docEle.getElementsByTagName("entry");
					result =  nl.getLength();
					if (nl != null && nl.getLength() > 0) {
						for (int i = 0 ; i < result; i++) {
							if( isCancelled() )
							{
								return -1;
							}
							Element entry = (Element)nl.item(i);
							Element title = (Element)entry.getElementsByTagName("title").item(0);
							Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
							Element when = (Element)entry.getElementsByTagName("updated").item(0);
							Element link = (Element)entry.getElementsByTagName("link").item(0);
							String description = title.getFirstChild().getNodeValue();
							String linkString = link.getAttribute("href");
							String point = g.getFirstChild().getNodeValue();
							String dt = when.getFirstChild().getNodeValue();  
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
							Date qdate = new GregorianCalendar(0,0,0).getTime();
							try {
								qdate = sdf.parse(dt);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							String[] locationString = point.split(" ");
							Location location = new Location("GPS");
							location.setLatitude(Double.parseDouble(locationString[0]));
							location.setLongitude(Double.parseDouble(locationString[1]));
							String magnitudeString = description.split(" ")[1];
							int end =  magnitudeString.length()-1;
							double magnitude = Double.parseDouble(magnitudeString.substring(0, end));
							description = description.split(",")[1].trim();
							QuakeVO quake = new QuakeVO(qdate, description, location, magnitude, linkString);
							publishProgress(quake);
						}
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
			finally {
			}
			sort(dataSource);
			return result;
		}

		@Override
		protected void onProgressUpdate(QuakeVO... values) {
			dataSource.add(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Integer result) {
			adapter.notifyDataSetChanged();
			setSupportProgressBarIndeterminateVisibility(false);
			Toast.makeText( XMLQuakeActivity.this, (result != -1 ?"Count: "+ result : "Canceled"), Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}

		private void sort(List<QuakeVO> list)
		{
			Comparator<? super QuakeVO> comparator = new Comparator<QuakeVO>(){
				public int compare(QuakeVO item1, QuakeVO item2) {
					if( item1.magnitude > item2.magnitude )
						return -1;
					if( item1.magnitude < item2.magnitude )
						return 1;
					return 0;
				};
			};
			Collections.sort(list, comparator);
		}
	}
}