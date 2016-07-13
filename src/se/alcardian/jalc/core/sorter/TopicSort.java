package se.alcardian.jalc.core.sorter;

import java.util.Comparator;
import se.alcardian.jalc.core.Topic;

public class TopicSort implements Comparator<Topic>{
	public static final int SORT_AGE = 1;
	public static final int SORT_NAME = 2;
	
	private int sort = 0;
	
	public TopicSort(int sorting){
		this.sort = sorting;
	}
	
	@Override
	public int compare(Topic arg0, Topic arg1) {
		if(sort == SORT_AGE){
			return arg0.post[arg0.post.length-1].date.compareTo(arg1.post[arg1.post.length-1].date);
		}else if(sort == SORT_NAME){
			return arg0.title.compareTo(arg1.title);
		}else{	//failsafe....
			return arg0.title.compareTo(arg1.title);
		}
	}
}
