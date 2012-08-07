package ru.nekit.androidsamplelist.model.vo;

public class TwitterItemVO {
	public String username;
	public String message;
	public String image_url;

	public TwitterItemVO(String username, String message, String url) {
		this.username = username;
		this.message = message;
		this.image_url = url;
	}
}