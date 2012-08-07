package ru.nekit.androidsamplelist.samples;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends GoUpActivity implements OnClickListener {

	static final String IMAGE_DATA = "imageData";
	static final int CAMERA_PICTURE_REQUEST = 1;
	private Bitmap imageData;
	private Button camera;
	private ImageView image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		camera = (Button)findViewById(R.id.button_camera);
		camera.setOnClickListener(this);
		image = (ImageView) findViewById(R.id.imageView);  
	}

	@Override
	public void onSaveInstanceState(Bundle outstate)
	{
		if( imageData != null )
		{
			outstate.putParcelable(IMAGE_DATA, imageData);
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		if( savedInstanceState != null )
		{
			imageData = (Bitmap)savedInstanceState.getParcelable(IMAGE_DATA);
			if( imageData != null )
			{
				image.setImageBitmap(imageData);
			}
		}
	}

	@Override
	public void onClick(View v) 
	{
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		startActivityForResult(cameraIntent, CAMERA_PICTURE_REQUEST); 
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == CAMERA_PICTURE_REQUEST )
		{
			imageData = (Bitmap) data.getExtras().get("data");  
			image.setImageBitmap(imageData);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}