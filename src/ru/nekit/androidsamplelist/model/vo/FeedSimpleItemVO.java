package ru.nekit.androidsamplelist.model.vo;

import java.net.URL;
import java.util.Date;

public class FeedSimpleItemVO 
{

	public String name; 
	public Date date;
	public URL imageUrl;
	public URL url;

	public FeedSimpleItemVO(String name, Date date, URL imageUrl, URL url)
	{
		this.name = name;
		this.date = date;
		this.imageUrl = imageUrl;
		this.url = url;
	}
}