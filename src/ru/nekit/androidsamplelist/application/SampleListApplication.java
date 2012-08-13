package ru.nekit.androidsamplelist.application;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SampleListApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.threadPoolSize(3)
		//.offOutOfMemoryHandling()
		.threadPriority(ImageLoaderConfiguration.Builder.DEFAULT_THREAD_PRIORITY)
		.memoryCacheSize(1024*1024*2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.build();
		ImageLoader.getInstance().init(config);
	}
}