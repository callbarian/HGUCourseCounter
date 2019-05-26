package edu.handong.analysis;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
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

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {
	
	private HashMap<String,Student> students;
	private String path;
	private String savepath;
	private boolean help;
	
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		/*
		Options options = createOptions();
		
		if(parseOptions(options, args)){
			
			if (help){
				printHelp(options);
				return;
			}
			
			try {
				File pathFile = new File(path);
				File saveFile = new File(savepath);
				
				if(saveFile.getAbsolutePath()==null) {
					throw new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
				}
				
				File parentFile = new File(saveFile.getParentFile().getAbsolutePath());
				parentFile.mkdirs();
				
				
				
				
				
				
				if(!pathFile.exists()) {
					throw new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
				}
				
			}catch(NotEnoughArgumentException e){
				System.out.println(e.getMessage());
				System.exit(0);
			}
		}
		*/
				
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		
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
		Utils.writeAFile(linesToBeSaved, resultPath);
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		students = new HashMap<String,Student>();
		
		
		String previousId = "0001";
		Student eachStudent = new Student("0001");
		for(String eachLine : lines) {
			Course eachCourse = new Course(eachLine);
			if(Integer.parseInt(eachCourse.getStudentId())!=Integer.parseInt(previousId)) {
				students.put(previousId,eachStudent);
				eachStudent = null;
				eachStudent = new Student(eachCourse.getStudentId());
				previousId = eachCourse.getStudentId();
				
			}
			else 
				eachStudent.addCourse(eachCourse);
			
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
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		int totalStudents = 0;
		ArrayList<String> outputString = new ArrayList<String>();
		for(String key : sortedStudents.keySet()) {
			System.out.println(key);
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

			path = cmd.getOptionValue("p");
			savepath = cmd.getOptionValue("s");
			help = cmd.hasOption("h");

		} catch (Exception e) {
			System.out.println("No CLI argument Exception! Please put a file path.");
			//System.out.println("p, path for the file path of the csv file");
			//System.out.println("s, savepath for the file to save");
			return false;
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("p").longOpt("path")
				.desc("Set a path of a directory or a file to display")
				.hasArg()
				.argName("Path name to display")
				.required()
				.build());
		
	
		options.addOption(Option.builder("s").longOpt("savepath")
				.desc("save path to store results of execution")
				.hasArg()
				.argName("Path name to save")
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
		String header = "HGUCourseCounter";
		String footer ="\nPlease report issues at https://github.com/callbarian/HGUCourseCounter/issues";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}

}
