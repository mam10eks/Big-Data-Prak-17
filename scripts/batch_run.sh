#!/bin/bash

set -e

mvn clean install

JAR_FILE="target/er-bloom-eval-0.0.1-SNAPSHOT-jar-with-dependencies.jar"

java -cp ${JAR_FILE} de.uni_leipzig.er_bloom_eval.batch.run.RunMain
