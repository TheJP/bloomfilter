package bloomfilter;

import java.util.BitSet;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class BloomFilter {
	private final int expectedN;
	private final double errorProbabilityP;
	
	private final int m;
	//number of hash functions
	private final int k;
	private final HashFunction[] hashFunctions;
	
	private final BitSet bitSet;
	private final int bitsPerElement = 1;
	private final int sizeBitSet;
	
	public BloomFilter(int expectedN, double errorProbabilityP) {		
		this.expectedN = expectedN;
		this.errorProbabilityP = errorProbabilityP;

		this.m = (int) (-(this.expectedN * Math.log(this.errorProbabilityP))/(Math.log(2.0) * Math.log(2.0)));
		this.k = (int)Math.ceil((m/(double)expectedN)*Math.log(2.0));
		hashFunctions = new HashFunction[k];
		for(int i = 0; i < k; i++) {
			hashFunctions[i] = Hashing.murmur3_32(i);
		}
		this.sizeBitSet = m * bitsPerElement;
		this.bitSet = new BitSet(sizeBitSet);
		System.out.println("m: " + m + " k: " + k);
	}
	
	public void add(String s) {
		for (int i = 0; i < hashFunctions.length; i++) {
			int hashCode = getHashCode(s, hashFunctions[i]);
			bitSet.set(Math.abs(hashCode % sizeBitSet));
		}
	}
	
	public boolean contains(String s) {
	    for (int i = 0; i < hashFunctions.length; i++) {
	    	int hashCode = getHashCode(s, hashFunctions[i]);
		    if (!bitSet.get(Math.abs(hashCode % sizeBitSet))) {
		    	return false;
		    }
		}
		return true;
	}
	
	private int getHashCode(String s, HashFunction hashFunction) {
		return hashFunction.newHasher()
							.putString(s, Charsets.UTF_8)
							.hash()
							.asInt();
	}
	
}
