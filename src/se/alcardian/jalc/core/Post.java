package se.alcardian.jalc.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class Post {
	public String date;
	public String postID;
	public String imageName;
	public String imageURL;
	public String smallImageURL;
	public String postText;
	
	//TODO IMPLEMENT & REPLACE THE ITEMS UNDER ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//public String[] responses;
	//public String imageDisplayName;
	//public String imageOriginalName;
	//public String imageGeneratedName;
	public boolean visible;
	
	public Post(String date, String postID, String imageName, String imageURL, String smallImageURL, String postText){
		this.date = date;
		this.postID = postID;
		this.imageName = imageName;
		this.imageURL = imageURL;
		this.smallImageURL = smallImageURL;
		this.postText = postText;
	}
	
	public void print(){
		System.out.println("Date: "+date);
		System.out.println("PostID: "+postID);
		System.out.println("Image name: "+imageName);
		System.out.println("Normal Image: "+imageURL);
		System.out.println("Small Image: "+smallImageURL);
		System.out.println("Post Text: "+postText);
	}
	
	public Element export(String imageName){
		String temp = "<div class=\"postContainer replyContainer\"><div class=\"sub\"><div><span class=\"dateTime\">%s</span>";
		temp+="<span class=\"postNum desktop\"><a title=\"Reply to this post\">%s</a>";
		temp+="</span></div><div class=\"file\" style=\"overflow: hidden; display: inline-block;\">";
		temp+="<div class=\"fileText\">File: image.url</div>";
		temp+="<a class=\"fileThumb\" href=\"http:%s\" style=\"float: left;margin-right: 5px;\">";
		temp+="<img src=\"http:%s\"></a><div></div>";
		temp+="<blockquote style=\"display: inline;margin: 0;\">%s</blockquote>";
		temp+="</div></div></div>";

		//String temp = "<div class=\"postContainer replyContainer\">";
		//temp+="<div class=\"sub\">";
		//temp+="<div>";
		//temp+="<span class=\"dateTime\">%s</span>";
		
		// date, postID, normalImage, smallImage, comment
		temp= String.format(temp, date, postID, imageURL, smallImageURL, postText);
		
		Element e = Jsoup.parse(temp).select("div[class=\"postContainer replyContainer\"]").get(0);
		
		
		
		return e;
	}
	public Element export(){
		String temp = "<div class=\"postContainer replyContainer\"><div class=\"sub\"><div><span class=\"dateTime\">%s</span>";
		temp+="<span class=\"postNum desktop\"><a title=\"Reply to this post\">%s</a>";
		temp+="</span></div><div class=\"file\" style=\"overflow: hidden; display: inline-block;\">";
		temp+="<div class=\"fileText\">File: image.url</div>";
		temp+="<a class=\"fileThumb\" href=\"http:%s\" style=\"float: left;margin-right: 5px;\">";
		temp+="<img src=\"http:%s\"></a><div></div>";
		temp+="<blockquote style=\"display: inline;margin: 0;\">%s</blockquote>";
		temp+="</div></div></div>";

		//String temp = "<div class=\"postContainer replyContainer\">";
		//temp+="<div class=\"sub\">";
		//temp+="<div>";
		//temp+="<span class=\"dateTime\">%s</span>";
		
		// date, postID, normalImage, smallImage, comment
		temp= String.format(temp, date, postID, imageURL, smallImageURL, postText);
		
		Element e = Jsoup.parse(temp).select("div[class=\"postContainer replyContainer\"]").get(0);
		
		
		
		return e;
	}
}
