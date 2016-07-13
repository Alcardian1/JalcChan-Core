package se.alcardian.jalc.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import se.alcardian.io.Alc_IO;
import se.alcardian.jalc.core.sorter.TopicCategory;

/**
 * This class exists only for testing and development of the JalcChan core
 * @author Alcardian
 *
 */
public class JalcMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// args[0] = "https://boards.4chan.org/vg/"
		// args[1] = specific topic in a board like "https://boards.4chan.org/vg/thread/148286490/world-of-warcraft-general-wowg"
		// Note: args[1] needs to be supplied a new URL to a topic every now and then as these topics gets deleted after getting old
		// args[2] = Save location on the computer (e.g. "F:\Downloads\_TEMP")
		// args[3] = computer location of the blacklist (e.g. "F:\Downloads\_TEMP\JalcChan_Blacklist.txt")
		// args[4] = computer location of the whitelist (e.g. "F:\Downloads\_TEMP\JalcChan_Whitelist.txt")
		
		//test1(args[0]);	// Prints out a list of topics from the specified board
		//test2(args[1]);	// Prints a single topics information (not posts) using the topics URL to locate it
		//test3(args[0]);	// Lists all posts in the first topic of the specified board
		//test4(args[0], args[2]);	// Reading the second topic of the specified board and saves it to file at specified location
		test5(args[0], args[3], args[4]);	// Categorizes all topics of a board into; blacklist, whitelist and normal.
	}
	
	/**
	 * Prints out a list of topics from the specified board
	 */
	public static void test1(String url){
		Topic topic1[] = Topic.parseFromBoard(url, Topic.MaxPage);
		//Collections.sort(topic1, new TopicSort());
		//Arrays.sort(topic1, new TopicSort(TopicSort.SORT_NAME));
		
		for(Topic t:topic1){
			t.print();
			System.out.println();
		}
		
		System.out.println("Number of topics: "+topic1.length);
	}
	
	/**
	 * Prints a single topics information (not posts) using the topics URL to locate it
	 * Update The URL to a working one before running this test.
	 */
	public static void test2(String url){
		Topic topic1 = Topic.parseFromTopic(url);
		//Topic topic1 = Topic.parseFromTopic("https://boards.4chan.org/vg/thread/148209618/tesg-the-elder-scrolls-general");
		topic1.print();
	}
	
	/**
	 * Lists all posts in the first topic of the specified board
	 */
	public static void test3(String url){
		Topic topic1[] = Topic.parseFromBoard(url, 1);
		//Topic topic1[] = Topic.parseFromBoard("https://boards.4chan.org/vg/", 1);
		topic1[0].print();
		
		System.out.println("URL: "+topic1[0].url+"\n");
		for(Post p:topic1[0].post){
			p.print();
			System.out.println("\n");
		}
		
		System.out.println("\n\n\n");
		Topic topic2 = Topic.parseFromTopic(topic1[0].url);
		
		for(Post p:topic2.post){
			p.print();
			System.out.println("\n");
		}
	}
	
	/**
	 * Reading the second topic of the specified board and saves it to file at specified location
	 * @param url URL of the board
	 * @param location location on the computer to save to
	 */
	public static void test4(String url, String location){
		//String location="F:\\Downloads\\_TEMP\\";
		
		// Fix, original problem of argument:  "F:\Downloads\_TEMP\"  would turn into  F:\Downloads\_TEMP"
		// Changed argument to:  "F:\Downloads\_TEMP"  which turns into  F:\Downloads\_TEMP   so a \ is added to end
		location += "\\";
		
		//System.out.println(location);
		Topic topic1[] = Topic.parseFromBoard(url, 1);
		//Topic topic1[] = Topic.parseFromBoard("https://boards.4chan.org/vg/", 1);
		topic1[1].print();
		
		System.out.println("URL: "+topic1[0].url+"\n");
		for(Post p:topic1[1].post){
			p.print();
			System.out.println("\n");
		}
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		
		for(Post p:Topic.parseFromTopic(topic1[1].url).post){
			p.print();
			System.out.println("\n");
		}
		
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------");
		
		System.out.println("Any Name Conflicts: "+Topic.checkImageNames(topic1[1]));
		System.out.println("\n"+topic1[1].post[1].export());
		save(location+"post.html", topic1[1].post[1].export().outerHtml());
		save(location+"topic.html", topic1[1].export().outerHtml());
		//Topic.parseFromTopic(topic1[0].url).archive(location);
	}
	
	/**
	 * Categorizes all topics of a board into; blacklist, whitelist and normal.
	 * @param url URL to the board
	 * @param blacklistLocation computer location of the blacklist file
	 * @param whitelistLocation computer location of the whitelist file
	 */
	public static void test5(String url, String blacklistLocation, String whitelistLocation){
		Topic topic1[] = Topic.parseFromBoard(url, Topic.MaxPage);
		//Topic topic1[] = Topic.parseFromBoard("https://boards.4chan.org/vg/", Topic.MaxPage);
		
		TopicCategory blacklist[];
		{
			ArrayList<String> temp = Alc_IO.ReadFile(blacklistLocation);
			//ArrayList<String> temp = Alc_IO.ReadFile("F:\\Downloads\\_TEMP\\JalcChan_Blacklist.txt");
			blacklist = new TopicCategory[temp.size()];
			
			for(int i=0; i<temp.size(); i++){
				blacklist[i] = new TopicCategory(TopicCategory.TITLE_WORD, temp.get(i));
			}
		}
		
		System.out.println("--Blacklisted keys--");
		for(int i=0; i<blacklist.length; i++){
			System.out.println(blacklist[i].key);
		}
		System.out.println("---------------------------------------------------------------\n");
		TopicCategory whitelist[];
		{
			ArrayList<String> temp = Alc_IO.ReadFile(whitelistLocation);
			//ArrayList<String> temp = Alc_IO.ReadFile("F:\\Downloads\\_TEMP\\JalcChan_WhiteList.txt");
			whitelist = new TopicCategory[temp.size()];
			
			for(int i=0; i<temp.size(); i++){
				whitelist[i] = new TopicCategory(TopicCategory.TITLE_WORD, temp.get(i));
			}
		}
		System.out.println("--Whitelisted keys--");
		for(int i=0; i<blacklist.length; i++){
			System.out.println(whitelist[i].key);
		}
		System.out.println("---------------------------------------------------------------\n");
		ArrayList<Topic[]> lists = TopicCategory.blackNwhiteLists(topic1, blacklist, whitelist);
		
		System.out.println("---------------------------------------------------------------");
		System.out.println("Whitelist");
		for(Topic t:lists.get(1)){
			t.print();
			System.out.println("");
		}
		
		System.out.println("---------------------------------------------------------------");
		System.out.println("Normal");
		for(Topic t:lists.get(2)){
			t.print();
			System.out.println("");
		}
		
		System.out.println("---------------------------------------------------------------");
		System.out.println("Blacklist");
		for(Topic t:lists.get(0)){
			t.print();
			System.out.println("");
		}
	}
	
	private static void save(String location, String content){
			try {
				OutputStream out = null;
				out = new FileOutputStream(location);
				out.write(content.getBytes(Charset.forName("UTF-8")));
				if (out != null) {
					out.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
