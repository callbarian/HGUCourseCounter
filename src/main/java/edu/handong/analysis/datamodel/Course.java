package edu.handong.analysis.datamodel;

public class Course{
	private String studentId;
	private String yearMonthGraduated;
	private String firstMajor;
	private String secondMajor;
	private String courseCode;
	private String courseName;
	private String courseCredit;
	private int yearTaken;
	private int semesterCourseTaken;
	
	public Course(String line){
		String[] arrayString = line.split(",");
		
		studentId = arrayString[0].trim();
		yearMonthGraduated = arrayString[1].trim();
		firstMajor = arrayString[2].trim();
		secondMajor = arrayString[3].trim();
		courseCode = arrayString[4].trim();
		courseName = arrayString[5].trim();
		courseCredit = arrayString[6].trim();
		yearTaken = Integer.parseInt(arrayString[7].trim());
		semesterCourseTaken = Integer.parseInt(arrayString[8].trim());
		
		
	}

	public int getYearTaken() {
		return yearTaken;
	}

	public int getSemesterCourseTaken() {
		return semesterCourseTaken;
	}
	
	public String getStudentId() {
		return studentId;
	}


}