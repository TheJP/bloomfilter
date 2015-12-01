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
		List<String> words = Files.readLines(fileWords, Charsets.UTF_8);
		List<String> wrongWords = Files.readLines(fileWrongWords, Charsets.UTF_8);

		System.out.println("InsertLines: " + words.size());
		System.out.println("TestLines: " + wrongWords.size());
		System.out.println("ErrorProbability p: " + errorProbabilityP);
		BloomFilter bloomFilter = new BloomFilter(words.size(), errorProbabilityP);
		for(String word : words) {
			bloomFilter.add(word);
		}
        System.out.println("#" + fileWords.getName());
        wordsInFilter(words, bloomFilter);
        System.out.println("#" + fileWrongWords.getName());
        wordsInFilter(wrongWords, bloomFilter);
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
		System.out.println("- found: " + correctWordsFound);
		System.out.println(String.format("- percent: %.2f%%", percent));
	}
	
}
