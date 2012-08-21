package ru.nekit.androidsamplelist.activityExtra;

import ru.nekit.androidsamplelist.facade.SampleListFacade;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FacadeFragment extends SherlockFragmentActivity {

	public SampleListFacade facade;

	protected void onCreate(android.os.Bundle savedInstanceState) {

		facade = SampleListFacade.getInstance();
		super.onCreate(savedInstanceState);
	};
}
