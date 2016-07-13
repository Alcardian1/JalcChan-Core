package se.alcardian.jalc.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import se.alcardian.io.Alc_IO;

public class Topic {
	/**
	 * The number of pages with topic's a board has
	 */
	public static final int MaxPage=10;
	public static final String pathSmall = "FileThumb\\";
	public static final String pathFull = "FileFull\\";
	
	/**
	 * URL of the topic
	 */
	public String url;
	/**
	 * Title of the topic
	 */
	public String title;
	/**
	 * ID of the topic
	 */
	public String id;
	/**
	 * The posts within a topic
	 */
	public Post post[];
	//TODO IMPLEMENT ARCHIVED & UPDATEARCHIVE
	//public boolean archived;
	//public boolean updateArchive;
	
	/**
	 * Create a topic using the parameters
	 * @param url
	 * @param title
	 * @param id
	 * @param post
	 */
	public Topic(String url, String title, String id, Post post[]){
		this.url = url;
		this.title = title;
		this.id = id;
		this.post = post;
	}
	
	/**
	 * Retrieves all topics from a board.
	 * @param url URL of the board the topic's should be retrieved from
	 * @param maxPage 
	 * @return An Array of Topic's
	 */
	public static Topic[] parseFromBoard(String url, int maxPage){
		ArrayList<Topic> topicList = new ArrayList<Topic>();
		Topic t;
		
		try {
			Document doc = Jsoup.connect(url).userAgent("Mozilla").execute().parse();
			
			//System.out.println(doc.select("body").select("div[class=thread]"));
			for(int i=1; i<maxPage+1; i++){
				String pageURL;
				if(i==1){
					pageURL = url;
				}else{
					pageURL = url+i;
				}
				
				doc = Jsoup.connect(pageURL).method(Method.GET).userAgent("Mozilla").execute().parse();
				
				
				for(Element x:doc.select("body").select("div[class=thread]")){
					t = parseFromTopic(x, url);
					boolean duplicate = false;
					//t.print();
					//System.out.println();
					
					for(int j=0; j<topicList.size(); j++){
						if(topicList.get(j).id.equals(t.id)){
							System.out.println("ERROR! Topic with this ID already indexed!");
							System.out.println("    Titles: " + topicList.get(j).title + " & " + t.title);
							System.out.println("    ID: " + t.id + "\n");
							duplicate = true;
						}
					}
					if(topicList.size() == 0 || duplicate == false){
						topicList.add(t);
					}
					//moved to the if statement to prevent duplicate topics
					//topicList.add(t);
				}
				//System.out.println("-----------------------"+i+"-----------------------");
			}
			//System.out.println("topics: "+topicList.size());
			Topic topics[] = new Topic[topicList.size()];
			for(int n=0; n<topicList.size(); n++){
				topics[n] = topicList.get(n);
			}
			return topics;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Topic parseFromTopic(String url){
		
		try {
			Document doc = Jsoup.connect(url).method(Method.GET).userAgent("Mozilla").execute().parse();
			Element x = doc.select("body").select("div[class=thread]").get(0);
			return parseFromTopic(x, url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Topic parseFromTopic(Element x, String url){
		String id = x.attr("id");
		String title = x.select("span[class=subject]").get(1).text();
		String urlExtension = x.select("div[class=post op]").get(0).select("span[class=\"postNum desktop\"]")
				.select("a[class=replylink]").attr("href");
		
		//Posts
		Post p[] = new Post[x.select("div[class=\"postContainer replyContainer\"]").size()+1];
		
		String imageName;
		if(x.select("div[class=\"post op\"]").select("div[class=fileText]").select("a").hasAttr("title")){	//use title if it exists
			imageName = x.select("div[class=\"post op\"]").select("div[class=fileText]").select("a").attr("title");
		}else if(x.select("div[class=\"post op\"]").select("div[class=fileText]").select("a").hasAttr("href")){	//else use href if exists
			if(x.select("div[class=\"post op\"]").select("div[class=fileText]").select("a").text().contains("(")){
				String temp = x.select("div[class=\"post op\"]").select("div[class=fileText]").select("a").attr("href");
				imageName = temp.substring(temp.lastIndexOf('/')+1);
			}else{
				imageName = x.select("div[class=\"post op\"]").select("div[class=fileText]").select("a").text();
			}
			
		}else{
			imageName = "";
		}
		
		//p[0] = OP
		p[0] = new Post(x.select("div[class=post op]").get(0).select("span[class=dateTime]").text()
				, x.select("div[class=post op]").get(0).select("span[class=\"postNum desktop\"]").select("a[title=\"Reply to this post\"]").text()
				, imageName
				, x.select("div[class=post op]").get(0).select("a[class=fileThumb]").attr("href")
				, x.select("div[class=post op]").get(0).select("a[class=fileThumb]").select("img").attr("src")
				, x.select("div[class=post op]").get(0).select("blockquote[class=postMessage]").text());
		//System.out.println("OP");
		//p[0].print();
		//System.out.println("--\n\n");
		int i= 1;
		for(Element y: x.select("div[class=\"postContainer replyContainer\"]")){
			//System.out.println(y.select("a[class=fileThumb]").select("img").attr("src"));	//small image
			//System.out.println(y.select("a[class=fileThumb]").attr("href"));	//full size image
			//System.out.println(y.select("blockquote[class=postMessage]"));	//comment
			//System.out.println(y.select("span[class=dateTime]").text()); //Date + time
			//System.out.println(y.select("span[class=\"postNum desktop\"]").select("a[title=\"Reply to this post\"]").text()); //post id
			//System.out.println("\n\n");
			
			//System.out.println(y.select("div[class=fileText]").select("a"));
			//System.out.println(y.select("div[class=fileText]").select("a").hasAttr("title"));
			//System.out.println(y.select("div[class=fileText]").select("a").hasAttr("href"));
			//System.out.println("------");
			
			if(y.select("div[class=fileText]").select("a").hasAttr("title")){	//use title if it exists
				imageName = y.select("div[class=fileText]").select("a").attr("title");
			}else if(y.select("div[class=fileText]").select("a").hasAttr("href")){	//else use href if exists
				if(y.select("div[class=fileText]").select("a").text().contains("(")){
					String temp = y.select("div[class=fileText]").select("a").attr("href");
					imageName = temp.substring(temp.lastIndexOf('/')+1);
				}else{
					imageName = y.select("div[class=fileText]").select("a").text();
				}
			}else{
				imageName = "";
			}
			//System.out.println(imageName);
			
			p[i] = new Post(y.select("span[class=dateTime]").text()
					, y.select("span[class=\"postNum desktop\"]").select("a[title=\"Reply to this post\"]").text()
					, imageName
					, y.select("a[class=fileThumb]").attr("href")
					, y.select("a[class=fileThumb]").select("img").attr("src")
					, y.select("blockquote[class=postMessage]").text());
			//p[i].print();
			//System.out.println("\n\n");
			i++;
		}
		//System.out.println("------------------------------------------------------------------------------------------------");
		//Topic t = new Topic(title, id, p);
		//t.print();
		return new Topic(url+urlExtension, title, id, p);
	}

	/**
	 * Unimplimented!
	 * @return
	 */
	public static Topic parseFromArchieve(){
		//TODO create this method
	return null;
	}
	
	public void print(){
		System.out.println("-Topic information-");
		System.out.println("URL: "+url);
		System.out.println("Title: "+title);
		System.out.println("ID: "+id);
		System.out.println("Number of loaded posts: "+post.length);
		System.out.println("Last update: "+post[post.length-1].date);
	}
	
	public void archive(String savePath){
		int delay = 500;
		
		if(!new File(savePath).isDirectory()){	//Checks if the folder already exists.
			System.out.println("Directory does not exists! "+savePath);
			System.out.println("Creating directory: "+savePath);
			new File(savePath).mkdirs();
			//System.out.println("Writing \""+imageName+"\" to file at: \""+savePath+"\"");
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!new File(savePath+pathFull).isDirectory()){	//Checks if the folder already exists.
			System.out.println("Directory does not exists! "+savePath+pathFull);
			System.out.println("Creating directory: "+savePath+pathFull);
			new File(savePath+pathFull).mkdirs();
			//System.out.println("Writing \""+imageName+"\" to file at: \""+savePath+"\"");
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!new File(savePath+pathSmall).isDirectory()){	//Checks if the folder already exists.
			System.out.println("Directory does not exists! "+savePath+pathSmall);
			System.out.println("Creating directory: "+savePath+pathSmall);
			new File(savePath+pathSmall).mkdirs();
			//System.out.println("Writing \""+imageName+"\" to file at: \""+savePath+"\"");
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		for(Post p:post){
			if(!p.smallImageURL.equals("") && !p.imageURL.equals("")){
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				saveFile(savePath+pathFull, p.imageURL, p.imageURL.substring(p.imageURL.lastIndexOf("/")+1));
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				saveFile(savePath+pathSmall, p.imageURL, p.imageURL.substring(p.smallImageURL.lastIndexOf("/")+1));
			}else{
				System.out.println("no image for post...");
			}
		}
	}
	public static void saveFile(String savePath, String url, String name){
		if(new File(savePath).isFile()){	//Checks if there is a file with the name already.
			System.out.println("The file aldready exists! \""+url+"\" was skipped.");
		}else{
			System.out.println("Saving : "+savePath+name);
			try {
				Alc_IO.download("http:"+url, savePath+name);
			} catch (IOException e1) {
				System.out.println("Failed to write image: "+url);
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @return true if no names are the same, false if there is any conflict
	 */
	public static boolean checkImageNames(Topic t){
		for(int i=0; i<t.post.length; i++){
			for(int j=i+1; j<t.post.length; j++){
				if(t.post[i].imageName.equals(t.post[j].imageName)){
					return false;
				}
			}
		}
		return true;
	}
	
	public Document export(){
		String head = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>4chan Template</title>"+
		"<style>.sub {margin: 0,4em 0em 0em 0em;background-color:#EFE0D6;border-style: solid;"+
		"border-color: #D7BFB7;border-width: 1px;display: inline-block; padding-left: 1.5em}"+
		".bg_a {background-color:#FFFFEE;color: #800000;font-size: 10pt;font-family: arial,helvetica,sans-serif;}"+
		"div.post {width: auto;}"+
		".subject {color: #cc1105;font-family: arial,helvetica,sans-serif;font-size: 10pt;font-weight: bold;float:top;}"+
		"</style></head>";
		String body = "<body class=\"bg_a\">%s</body></html>";
		
		String temp = "";
		
		for(Post p:post){
			temp+=p.export();
		}
		body= String.format(body, temp);
		
		Document doc = Jsoup.parse(head+body);
		
		return doc;
	}
	
	/**
	 * Not implemented yet!
	 * @return
	 */
	public static Topic[] categorize(){
		return null;
	}
}
