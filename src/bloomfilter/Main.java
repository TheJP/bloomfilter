package bloomfilter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Main {
	private static final File file = new File("resources/words.txt");
	private static final File fileWrong = new File("resources/wordsFalseCases.txt");
	
	private static final double errorProbabilityP = 0.1;
	
	public static void main(String[] args) {
		List<String> lines = null;
		List<String> wrongLines = null;
		try {
			lines = Files.readLines(file, Charsets.UTF_8);
			wrongLines = Files.readLines(fileWrong, Charsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Could not read File " + e.getMessage());
			e.printStackTrace();
		}
		
		if(lines != null && wrongLines != null) {
			System.out.println("InsertLines: " + lines.size());
			System.out.println("TestLines: " + lines.size());
			System.out.println("ErrorProbability p: " + errorProbabilityP);
			BloomFilter bloomFilter = new BloomFilter(lines.size(), errorProbabilityP);
			for(String word : lines) {
				bloomFilter.add(word);
			}
//			System.out.println("expected: true, result: " + bloomFilter.contains("abraded"));
//			System.out.println("expected: false, result: " + bloomFilter.contains("abradedd"));
            System.out.println("#words.txt");
            wordsInFilter(lines, bloomFilter);
            System.out.println("#wordsFalseCases.txt");
            wordsInFilter(wrongLines, bloomFilter);
		}
	}
	
	public static void wordsInFilter(List<String> words, BloomFilter bloomFilter) {
		int correctWordsFound = 0;
		
		for(String s : words) {
			if(bloomFilter.contains(s)) {
				correctWordsFound++;
			}
		}
		double percent =  100/(double)words.size() * correctWordsFound;
		System.out.println("found: " + correctWordsFound);
		System.out.println(String.format("in percent: %.2f%%", percent));
	}
	
}
