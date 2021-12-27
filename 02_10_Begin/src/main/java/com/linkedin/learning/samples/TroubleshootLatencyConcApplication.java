package com.linkedin.learning.samples;

import com.linkedin.learning.samples.util.Scrambler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;


public class TroubleshootLatencyConcApplication {

	final static String sourceText = "MyNameIsWhat";
	final static List<String> listOfStrings = Arrays.asList("MyNameIsWhat","MyNameIsWho","MyNameIs","MeShady","Tango","Alpha","Yankee","Oscar");
	final static Logger LOGGER = LoggerFactory.getLogger(TroubleshootLatencyConcApplication.class);

	public static void main(String[] args) {
		try {
			LOGGER.info("Generating anagrams");
			for(int i=1;i<=5;i++) {
				LOGGER.info("Anagrams by method #"+i+": \n" + Scrambler.scramble(sourceText, null, i));
			}
			for(int i=6;i<=9;i++) {
				LOGGER.info("Anagrams by method #"+i+": \n" + Scrambler.scramble(null, listOfStrings, i));
			}
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
