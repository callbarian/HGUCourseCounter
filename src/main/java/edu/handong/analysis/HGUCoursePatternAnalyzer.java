package edu.handong.analysis;

import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {
	
	private HashMap<String,Student> students;
	private String input;
	private String output;
	private String analysis;
	private String coursecode;
	private String startyear;
	private String endyear;
	private boolean help;
	
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		
		
		Options options = createOptions();
		
		if(parseOptions(options, args)){
			
			
			if (help){
				printHelp(options);
				return;
			}
			
			try {
				// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
				if(args.length<5)
					throw new NotEnoughArgumentException();
			} catch (NotEnoughArgumentException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
			
			try {
				File inputFile = new File(input);
				File outputFile = new File(output);
				
				if(!inputFile.exists()) {
					throw new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
				}
				
				//File parentFile = new File(outputFile.getParentFile().getAbsolutePath());
				//parentFile.mkdirs();
				
			
				//if(!pathFile.exists()) {
				//	throw new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
				//}
				
			}catch(NotEnoughArgumentException e){
				System.out.println(e.getMessage());
				System.exit(0);
			}
			
			
			CSVParser csvParser = Utils.getLines(input, true);
			
			students = loadStudentCourseRecords(csvParser);
			
			
			
			
			
			Map<String,Student> sortedStudents = new TreeMap<String,Student>(new MyComparator());
			sortedStudents.putAll(students);
	
			if(analysis.contains("2")&& coursecode==null) {
				System.out.println("analysis is 2 but coursecode is not given. please check CLI argument");
				System.exit(0);
			}
			
			if(analysis.contains("1")) {
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				Utils.writeAFile(linesToBeSaved, output);
				//System.out.println("analysis is " + analysis);
			}
			
			if(analysis.contains("2")) {
				ArrayList<String> rateToBeSaved = rateOfStudentsTakingThisCourse(sortedStudents);
				Utils.writeAFile(rateToBeSaved, output);
				//System.out.println("analysis is " + analysis);
			}
			
			
			
			
		}
		
				
		/*
		//String dataPath = args[0]; // csv file to be analyzed
		//String resultPath = args[1]; // the file path where the results are saved.
		CSVParser csvParser = Utils.getLines(input, true);
		
		students = loadStudentCourseRecords(csvParser);
		
		//for(String keyString : students.keySet()) {
		//	System.out.println(students.get(keyString).getStudentId());
		//}
		
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String,Student> sortedStudents = new TreeMap<String,Student>(new MyComparator());
		sortedStudents.putAll(students);
		//for(String keyString : sortedStudents.keySet()) {
		///	System.out.println(sortedStudents.get(keyString).getStudentId());
		//}
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, output);
		*/
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	
	private HashMap<String,Student> loadStudentCourseRecords(CSVParser csvParser) {
		students = new HashMap<String,Student>();
		
		
		String previousId = "0001";
		Student eachStudent = new Student("0001");
		boolean flag = false;
		for(CSVRecord csvRecord : csvParser) {
			if(flag) {
				Course eachCourse = new Course(csvRecord);
				if(Integer.parseInt(eachCourse.getStudentId())!=Integer.parseInt(previousId)) {
					students.put(previousId,eachStudent);
					eachStudent = null;
					eachStudent = new Student(eachCourse.getStudentId());
					previousId = eachCourse.getStudentId();
				
				}
				else 
					eachStudent.addCourse(eachCourse);
			}
			else
				flag = true;
		}
		// TODO: Implement this method
		
		return students; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> rateOfStudentsTakingThisCourse(Map<String,Student> sortedStudents){
		
		Reader reader = null;
		try {
			reader = Files.newBufferedReader(Paths.get(input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CSVParser csvParser = null;
		try {
			csvParser = new CSVParser(reader,CSVFormat.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String,Integer> studentsTaken = new HashMap<String,Integer>();
		HashMap<String,Integer> totalStudents = new HashMap<String,Integer>();
	
		
		
		String courseName = null;
		
		boolean courseNameAdded = false;
		boolean flag = false;
		ArrayList<String> lineToReturn = new ArrayList<String>();
		lineToReturn.add("Year,Semester,CouseCode, CourseName,TotalStudents,StudentsTaken,Rate");
		for(CSVRecord csvRecord : csvParser) {
			if(flag) {
				
				if(!courseNameAdded) {
				
					if(csvRecord.get(4).trim().contains(coursecode)) {
						courseName = csvRecord.get(5).trim();
						courseNameAdded = true;
					}
				}	
				
					
				String courseCode = csvRecord.get(4).trim();
				String year = csvRecord.get(7).trim();
				String semester = csvRecord.get(8).trim();
				String yearAndSemester = year+"-"+semester;
				
				if(Integer.parseInt(year)<Integer.parseInt(startyear) || Integer.parseInt(year)>Integer.parseInt(endyear))
					continue;
				if(totalStudents.containsKey(yearAndSemester))
					totalStudents.put(yearAndSemester,totalStudents.get(yearAndSemester)+1);
				else
					totalStudents.put(yearAndSemester,1);
				//adding number of students in the year.
				
					if(courseCode.contains(coursecode)) {
						if(studentsTaken.containsKey(yearAndSemester))
							studentsTaken.put(yearAndSemester,studentsTaken.get(yearAndSemester)+1);
						else
							studentsTaken.put(yearAndSemester,1);
						
					}
				
			}
			else
				flag=true;
			
		}
		Map<String,Integer> sortedStudentsTaken = new TreeMap<String,Integer>(studentsTaken);
		//sortedStudentsTaken.putAll(studentsTaken);
		Map<String,Integer> sortedTotalStudents = new TreeMap<String,Integer>(totalStudents);
		//sortedTotalStudents.putAll(totalStudents);
		
		for(String key : sortedStudentsTaken.keySet()) {
			String[] mapYearAndSemester = key.split("-");
			lineToReturn.add(mapYearAndSemester[0].trim()+","+mapYearAndSemester[1]
					+","+coursecode
					+","+courseName
					+","+sortedTotalStudents.get(key)
					+","+sortedStudentsTaken.get(key)
					+","+String.format("%.1f",100*(((float)sortedStudentsTaken.get(key))/sortedTotalStudents.get(key)))
					+"%"); 
			
		}
		
		/*
		for(String parseString : lineToReturn) {
			System.out.println(parseString);
		}
		*/
		
		return lineToReturn;
	}
	
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		int totalStudents = 0;
		ArrayList<String> outputString = new ArrayList<String>();
		outputString.add(totalStudents++,"StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
		
		for(String key : sortedStudents.keySet()) {
			//System.out.println(key);
			Student eachStudent = sortedStudents.get(key);
			eachStudent.getSemestersByYearAndSemester();
			int size = eachStudent.getTheSemestersByYearAndSemester().size();
			String sizeString = String.valueOf(size);
			
			//System.out.println(sizeString);
			for(int i=1; i<=size;i++) {
			
				
				String stringToAdd = eachStudent.getStudentId()+","
					    +sizeString+","
						+i+","
					    +eachStudent.getNumCourseInNthSementer(i);
						
				
				//System.out.println(stringToAdd);
				outputString.add(totalStudents+i-1,stringToAdd);
				//System.out.println(outputString.get(i-1));
			}
			totalStudents+=(size);
			
			
			
		}
		// TODO: Implement this method
		
		return outputString; // do not forget to return a proper variable.
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);
			
			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			analysis = cmd.getOptionValue("a");
			coursecode = cmd.getOptionValue("c");
			startyear = cmd.getOptionValue("s");
			endyear = cmd.getOptionValue("e");
			help = cmd.hasOption("h");

			
		} catch (Exception e) {
			printHelp(options);
			return(false);
			
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
	
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());
		
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg()
				.argName("Analysis option")
				.required()
				.build());
		
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg()
				.argName("course code")
				.build());
		
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg()
				.argName("Start year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("Set the end year for analysis e.g., -e 2005")
				.hasArg()
				.argName("End year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Help")
		        .build());
		
		return options;
	}
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer = "";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}

}
