package ru.nekit.androidsamplelist.samples;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableLinerLayout extends RelativeLayout implements Checkable {

	private boolean checked;

	public CheckableLinerLayout(Context context) {
		super(context);
	}

	public CheckableLinerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		setBackgroundDrawable(checked ? new ColorDrawable(0x33333333) : null);
	}

	public boolean isChecked() {
		return checked;
	}

	public void toggle() {
		setChecked(!checked);
	}

}
