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
	private final double k;
	
	private final BitSet bitSet;
	private final int bitsPerElement = 32;
	private final int sizeBitSet;
	
	private int salt = 0;
	
	public BloomFilter(int expectedN, double errorProbabilityP) {		
		this.expectedN = expectedN;
		this.errorProbabilityP = errorProbabilityP;

		this.m = (int) (-(this.expectedN * Math.log(this.errorProbabilityP))/(Math.log(2.0) * Math.log(2.0)));
		//TODO k is to low!! inserted "* 100"
		this.k = (m/(double)expectedN)*Math.log(2.0) * 100;
		//TODO for testing: remove later
		System.out.println("m: " + m + " k: " + k);
		
		this.sizeBitSet = m * bitsPerElement;
		this.bitSet = new BitSet(sizeBitSet);
	}
	
	public void add(String s) {
		for (int i = 0; i < k; i++) {
			int hashCode = getHashCode(s);
			bitSet.set(Math.abs(hashCode % sizeBitSet));
		}
	}
	
	public boolean contains(String s) {
	    for (int i = 0; i < k; i++) {
	    	int hashCode = getHashCode(s);
		    if (!bitSet.get(Math.abs(hashCode % sizeBitSet))) {
		    	return false;
		    }
		}
		return true;
	}
	
	private HashFunction getHashWithSalt() {
		return Hashing.murmur3_32(salt++);
	}
	
	private int getHashCode(String s) {
		HashFunction hashFunction = getHashWithSalt();
		return hashFunction.newHasher()
							.putString(s, Charsets.UTF_8)
							.hash()
							.asInt();
	}
	
}
