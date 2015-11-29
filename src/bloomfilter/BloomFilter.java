package bloomfilter;

import java.util.BitSet;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class BloomFilter {
	private HashFunction hashFunction;
	
	private final int expectedN;
	private final double errorProbabilityP;
	
	private final int m;
	//number of hash functions
	private final int k;
	
	private final BitSet data;
	
	public BloomFilter(int expectedN, double errorProbabilityP) {
		this.hashFunction = Hashing.murmur3_32();
		
		this.expectedN = expectedN;
		this.errorProbabilityP = errorProbabilityP;

		this.m = (int) (-(expectedN * Math.log(errorProbabilityP))/(Math.log(2) * Math.log(2)));
		this.k = (int)Math.log(m/(double)expectedN);
		
		this.data = new BitSet(m);
	}
	
	public void add(String s) {
		for (int i = 0; i < k; i++) {
			int hashCode = getHashCode(s, i);
			data.set(hashCode);
		}
	}
	
	public boolean contains(String s) {
	    for (int i = 0; i < k; i++) {
	    	int hashCode = getHashCode(s, i);
		    if (!data.get(hashCode)) {
		    	return false;
		    }
		}
		return true;
	}
	
	private int getHashCode(String s, int pos) {
		return this.hashFunction.newHasher().putString(s, Charsets.UTF_8).putInt(pos).hashCode();
	}
	
}
