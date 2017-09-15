package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import java.io.Serializable;
import java.util.Set;

public interface NGrammTransformer extends Serializable
{
	public Set<String> apply(String input);
}