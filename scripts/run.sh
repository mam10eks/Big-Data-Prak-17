#!/bin/bash

mvn clean install

JAR_FILE="target/er-bloom-eval-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
MODES=("SET" "SORTED_SET" "BIT_ARRAY_TWO_STAGE")

for MODE in "${MODES[@]}"
do
	echo -e "$(date)"
	START=$(date +%s.%N)

	java -jar "${JAR_FILE}" \
		--similarityThreshold 0.7 \
		--bitsInBloomFilter 64 \
		-i dummy_input_data/25000/dup_E_25000.csv dummy_input_data/25000/org_E_25000.csv \
		-o "${MODE}.csv" \
		--internalBloomFilter BITSET_BLOOM_FILTER \
		-er ${MODE}

	END=$(date +%s.%N)
	DIFF=$(echo "$END - $START" | bc)

	echo -e "\n$(date)\nEntity-resolution took ${DIFF} seconds"
done
