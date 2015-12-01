package bloomfilter;

import java.util.BitSet;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class BloomFilter {
	private final int expectedN;
	private final double errorProbabilityP;
	
	/** arraySize */
	private final int m;
	/**number of hash functions */
	private final int k;
	/** generadted hashfuntions for k */
	private final HashFunction[] hashFunctions;
	
	private final BitSet bitSet;
	
	/**
	 * Create BloomFilter.
	 * @param expectedN Elements to insert into filter.
	 * @param errorProbabilityP Error probability.
	 */
	public BloomFilter(int expectedN, double errorProbabilityP) {		
		this.expectedN = expectedN;
		this.errorProbabilityP = errorProbabilityP;

		this.m = (int) (-(this.expectedN * Math.log(this.errorProbabilityP))/(Math.log(2.0) * Math.log(2.0)));
		this.k = (int)Math.ceil((m/(double)expectedN)*Math.log(2.0));
		hashFunctions = new HashFunction[k];
		for(int i = 0; i < k; i++) {
			hashFunctions[i] = Hashing.murmur3_32((int)(Math.random() * 10000));
		}
		this.bitSet = new BitSet(m);
		System.out.println("m: " + m + " k: " + k);
	}
	
	/**
	 * Add a string to the bloomFilter.
	 * @param string String to add.
	 */
	public void add(String string) {
		for (int i = 0; i < hashFunctions.length; i++) {
			int hashCode = getHashCode(string, hashFunctions[i]);
			bitSet.set(Math.abs(hashCode % m));
		}
	}
	
	/**
	 * Add a list of words to the filter.
	 * @param words elements witch sould be added.O
	 */
	public void addAll(List<String> words) {
		for(String word: words){
			this.add(word);
		}
	}
	
	/**
	 * Check if string is in BloomFilter.
	 * @param string String to check.
	 * @return true if value is in filter
	 */
	public boolean contains(String string) {
	    for (int i = 0; i < hashFunctions.length; i++) {
	    	int hashCode = getHashCode(string, hashFunctions[i]);
		    if (!bitSet.get(Math.abs(hashCode % m))) {
		    	return false;
		    }
		}
		return true;
	}
	
	/**
	 * Return hashCode of string with the given hashFunction.
	 * @param string String to be used as input.
	 * @param hashFunction hashfunction to generate hash.
	 * @return hash
	 */
	private int getHashCode(String string, HashFunction hashFunction) {
		return hashFunction.newHasher()
							.putString(string, Charsets.UTF_8)
							.hash()
							.asInt();
	}
	
}
