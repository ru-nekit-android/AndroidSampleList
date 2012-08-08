package ru.nekit.androidsamplelist.tools;

import ru.nekit.androidsamplelist.listAdapter.ActionListAdapter;
import ru.nekit.androidsamplelist.model.vo.ActionItemVO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;

public class WidgetTools {

	public static void showSelectAccountTypeDialog(Activity context, String title, 
			ActionItemVO[] data, OnClickListener dialogListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setAdapter(new ActionListAdapter(context, data), dialogListener);
		builder.create().show();
	}

}
