package se.alcardian.jalc.core.sorter;

import java.util.ArrayList;

import se.alcardian.jalc.core.Topic;

public class TopicCategory {
	public static final int TOPIC_ID = 1;
	public static final int TITLE_WORD = 2;
	
	public int setting;
	public String key = "";
	
	public TopicCategory(int setting, String key){
		this.setting = setting;
		this.key = key;
	}
	
	public static boolean isCategory(Topic t, TopicCategory tc){
		if(tc.setting == TOPIC_ID){
			return t.id.equals(tc.key);
		}if(tc.setting == TITLE_WORD){
			if(t.title.equals("")){
				return t.url.substring(t.url.lastIndexOf("/")+1).toLowerCase().contains(tc.key.toLowerCase());
			}else{
				return t.title.toLowerCase().contains(tc.key.toLowerCase());
			}
		}else{
			return false;
		}
	}
	
	/**
	 * Categorizes the topics into the following categories; blacklist, whitelist and other.
	 * It prioritizes a topic into the category blacklist followed by whitelist and leaves the remaining to the category other.
	 * If a topic is put under a category it will not check the lower priority categories if it fits there as well.
	 * @param topics
	 * @param blacklist
	 * @param whitelist
	 * @return
	 */
	public static ArrayList<Topic[]> blackNwhiteLists(Topic[] topics, TopicCategory[] blacklist, TopicCategory[] whitelist){
		ArrayList<Topic> t1 = new ArrayList<Topic>();	//blacklist
		ArrayList<Topic> t2 = new ArrayList<Topic>();	//whitelist
		ArrayList<Topic> t3 = new ArrayList<Topic>();	//others
		
		for(Topic t:topics){
			boolean tagged = false;
			for(TopicCategory tc:blacklist){
				if(TopicCategory.isCategory(t, tc) && !tagged){
					tagged = true;
					t1.add(t);
				}
			}
			for(TopicCategory tc:whitelist){
				if(TopicCategory.isCategory(t, tc) && !tagged){
					tagged = true;
					t2.add(t);
				}
			}
			if(!tagged){
				t3.add(t);
			}
		}
		ArrayList<Topic[]> temp = new ArrayList<Topic[]>();
		temp.add(t1.toArray(new Topic[t1.size()]));
		temp.add(t2.toArray(new Topic[t2.size()]));
		temp.add(t3.toArray(new Topic[t3.size()]));
		return temp;
	}
}
