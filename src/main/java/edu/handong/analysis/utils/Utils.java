package edu.handong.analysis.utils;

/*
import org.apache.commons.csv.*;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
*/

import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import edu.handong.analysis.utils.NotEnoughArgumentException;


public class Utils{
	public static ArrayList<String> getLines(String file,boolean removeHeader) {
		
		
		Scanner inputStream = null;
		ArrayList<String> inputArray = new ArrayList<String>();
		File readFile = new File(file);
		try {
			if(!readFile.exists()) {
				throw new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
			}
		}catch(NotEnoughArgumentException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		try {
			inputStream = new Scanner(new File(file));
			
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
	
		}
		
		if(removeHeader) {
			inputStream.nextLine();
		}
		while(inputStream.hasNextLine()) {
			inputArray.add(inputStream.nextLine());
			
		}
		
		inputStream.close();
		
		return inputArray;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		PrintWriter outputStream = null;
		File outputFile = new File(targetFileName);
		File parentFile = new File(outputFile.getParentFile().getAbsolutePath());
		parentFile.mkdirs();
		
		
		try {
			outputStream = new PrintWriter(targetFileName);
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		for(String outputLine : lines) {
			outputStream.println(outputLine);
		}
		
		outputStream.close();
		
	
	}
	
}

