package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student{
	
	private String studentId;
	private ArrayList<Course> coursesTaken; 
	private HashMap<String,Integer> semestersByYearAndSemester; 
	
	public Student(String studentId){
		this.studentId = studentId;
		coursesTaken = new ArrayList<Course>();
	}
	
	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
	}
	
	public HashMap<String,Integer> getSemestersByYearAndSemester(){
		semestersByYearAndSemester = new HashMap<String,Integer>();
		int semester = 0;
		
		for(Course singleCourse : coursesTaken) {
			String key = String.valueOf(singleCourse.getYearTaken())+"-"+String.valueOf(singleCourse.getSemesterCourseTaken());
			if(!semestersByYearAndSemester.containsKey(key)) {
				semester = semester+1;
				semestersByYearAndSemester.put(key,semester);
				//System.out.println(semester);
				
			}
			
			//System.out.println(key);
			//System.out.println(semestersByYearAndSemester.containsKey(key));
		}
		
		return semestersByYearAndSemester;
		
	}
	
	public int getNumCourseInNthSementer(int semester) {
		int count = 0;
		for(Course singleCourse : coursesTaken) {
			String key = String.valueOf(singleCourse.getYearTaken())+"-"+String.valueOf(singleCourse.getSemesterCourseTaken());
			if(semestersByYearAndSemester.get(key).intValue()==semester) {
				count++;
			}
			
		}
		return count;
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public HashMap<String,Integer> getTheSemestersByYearAndSemester(){
		return semestersByYearAndSemester;
	}

}