package de.uni_leipzig.er_bloom_eval.util.io;

import java.io.Serializable;
import java.util.Set;

public interface InternalDataStructureFactory<V extends HasAssociatedPerson> extends Serializable
{
	public V apply(long id, Set<String> addressGramm, Set<Integer> birtdayGramm, Set<String> nameGramm);
}