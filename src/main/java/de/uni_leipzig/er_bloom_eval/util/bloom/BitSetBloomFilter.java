package de.uni_leipzig.er_bloom_eval.util.bloom;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.BitSet;

/**
 * Implementation of a bloomfilter with BitSet
 */
public class BitSetBloomFilter<V> implements BloomFilter<V> {
    private BitSet bitSet;
    private HashFunction hashFunction;
    private int length;

    /**
     * Constructor
     * @param length length (bits) of the bloomfilter
     */
    public BitSetBloomFilter(int length) {
        this.bitSet = new BitSet(length);
        this.length = length;
        hashFunction = Hashing.murmur3_128(0);
    }

    /**
     * Add an item to the bloomfilter
     * @param item item
     */
    @Override
    public void add(V item) {
        bitSet.set(hash(item.toString().getBytes()));
    }

    /**
     * Run hash function for an item
     * @param bytes
     * @return
     */
    private int hash(byte[] bytes) {
        return (int) (Math.abs(hashFunction.hashBytes(bytes).asLong()) % length);
    }

    /**
     * Returns the BitSet
     * @return BitSet
     */
    @Override
    public BitSet getBitSet() {
        return this.bitSet;
    }
}
