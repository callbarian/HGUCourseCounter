package edu.handong.analysis;

import java.util.Comparator;

public class MyComparator implements Comparator<String>{
	public int compare(String o1,String o2) {
		return Integer.parseInt(o1)-Integer.parseInt(o2);
	}
	
}