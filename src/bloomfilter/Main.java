package bloomfilter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Main {
	private static final File file = new File("resources/words.txt");
	private static final File fileWrong = new File("resources/wordsFalsCases.txt");
	
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
		
		if(lines != null) {
			BloomFilter bloomFilter = new BloomFilter(lines.size(), 0.1);
			for(String word : lines) {
				bloomFilter.add(word);
			}
			System.out.println("expected: true, result: " + bloomFilter.contains("abraded"));
			System.out.println("expected: false, result: " + bloomFilter.contains("abradedd"));
		
			int wrongMarkedCorrect = 0;
			
			for(String s : wrongLines) {
				if(bloomFilter.contains(s)) {
					wrongMarkedCorrect++;
//					System.out.println(s);
				}
			}
			double percent =  100/(double)wrongLines.size() * wrongMarkedCorrect;
			System.out.println("as correct marked wrongs: " + wrongMarkedCorrect);
			System.out.println("tested: " + wrongLines.size());
			System.out.println("in percent: " + percent);
		}
		
	}
	
}
