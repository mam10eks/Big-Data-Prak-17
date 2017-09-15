package de.uni_leipzig.er_bloom_eval.util.bloom;

import java.util.BitSet;

public interface BloomFilter<V>
{
	public BitSet getBitSet();
	
	public void add(V item);
}