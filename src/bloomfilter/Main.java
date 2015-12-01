package bloomfilter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Main {
	private static final File fileWords = new File("resources/words.txt");
	private static final File fileWrongWords = new File("resources/wordsFalseCases.txt");
	
	private static final double errorProbabilityP = 0.1;
	
	public static void main(String[] args) throws IOException {
		System.out.println("ErrorProbability p: " + errorProbabilityP);
		
		List<String> words = readFile(fileWords);
		System.out.println("Insert line count: " + words.size());
		
		BloomFilter bloomFilter = new BloomFilter(words.size(), errorProbabilityP);
		bloomFilter.addAll(words);
		
        wordsInFilter(readFile(fileWords), bloomFilter);
        wordsInFilter(readFile(fileWrongWords), bloomFilter);
	}
	
	public static List<String> readFile(File file) throws IOException {
        System.out.println("#" + file.getName());
		return Files.readLines(file, Charsets.UTF_8);
	}
	
	/**
	 * Check for every word from List if bloomFilter contains element 
	 * and count positive hits. 
	 * @param words List to be checked.
	 * @param bloomFilter initialized bloomFilter.
	 */
	public static void wordsInFilter(List<String> words, BloomFilter bloomFilter) {
		int correctWordsFound = 0;
		
		for(String s : words) {
			if(bloomFilter.contains(s)) {
				correctWordsFound++;
			}
		}
		double percent =  100/(double)words.size() * correctWordsFound;
		System.out.println("- found: " + correctWordsFound + " from: " + words.size());
		System.out.println(String.format("- percent: %.2f%%", percent));
	}
	
}
