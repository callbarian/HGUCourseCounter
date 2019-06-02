package edu.handong.analysis.datamodel;

import org.apache.commons.csv.CSVRecord;

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
	
	public Course(CSVRecord csvRecord){
		//String[] arrayString = line.split(",");
		
		studentId = csvRecord.get(0).trim();
		yearMonthGraduated = csvRecord.get(1).trim();
		firstMajor = csvRecord.get(2).trim();
		secondMajor = csvRecord.get(3).trim();
		courseCode = csvRecord.get(4).trim();
		courseName = csvRecord.get(5).trim();
		courseCredit = csvRecord.get(6).trim();
		yearTaken = Integer.parseInt(csvRecord.get(7).trim());
		semesterCourseTaken = Integer.parseInt(csvRecord.get(8).trim());
		
		
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