package de.uni_leipzig.er_bloom_eval.util.bloom;

public enum PredefinedBloomFilter
{
	BITSET_BLOOM_FILTER(new BloomFilterFactory()
	{
		@Override
		public <V> BloomFilter<V> newBloomFilter(Integer size)
		{
			return new BitSetBloomFilter<>(size);
		}
	});
	
	private static interface BloomFilterFactory
	{
		public <V> BloomFilter<V> newBloomFilter(Integer size);
	}
	
	private final BloomFilterFactory _factory;
	
	private PredefinedBloomFilter(BloomFilterFactory factory)
	{
		_factory = factory;
	}
	
	public <V> BloomFilter<V> createNewBloomFilter(Integer size)
	{
		return _factory.newBloomFilter(size);
	}
}