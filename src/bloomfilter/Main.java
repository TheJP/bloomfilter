package bloomfilter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Main {
	private static final File file = new File("resources/words.txt");
	
	public static void main(String[] args) {
		List<String> lines = null;
		try {
			lines = Files.readLines(file, Charsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Could not read File " + e.getMessage());
			e.printStackTrace();
		}
		
		if(lines != null) {
			BloomFilter bloomFilter = new BloomFilter(lines.size(), 0.9);
			for(String word : lines) {
				bloomFilter.add(word);
			}
			System.out.println("expected: true, result: " + bloomFilter.contains("abraded"));
			System.out.println("expected: false, result: " + bloomFilter.contains("abradedd"));
		}
		
	}
	
}
