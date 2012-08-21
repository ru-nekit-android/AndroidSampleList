package ru.nekit.androidsamplelist.activityExtra;

import ru.nekit.androidsamplelist.facade.SampleListFacade;

import com.actionbarsherlock.app.SherlockActivity;

public class FacadeActivity extends SherlockActivity {

	public SampleListFacade facade;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		
		facade = SampleListFacade.getInstance();
		super.onCreate(savedInstanceState);
	};
}
