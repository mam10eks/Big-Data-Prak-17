# [Big Data Prak SS 17](https://dbs.uni-leipzig.de/study/ss_2017/bigdprak): Speed up Entity Resolution with Bit Arrays

This repository contains the work that was done during the big data internship during the
summer semester 2017 to the subject "Speed up Entity Resolution with Bit Arrays".

If you want fast introduction, you could use:
* some scripts [(located in /scripts)](scripts)
  * If you want to execute a single entity resolution for two input data sets of size 5000 and 20000,
    you should execute the [./scripts/run.sh script](scripts/run.sh).
	This script finishes within 10 minutes on my laptop,
	but you could already compare the performance of the trivial, the sorted set and the two stage filter approach.
	All three will yield to the same result, but since the last one uses bit arrays to filter the dataset, it will be much faster.
  * If you want a more detailed evaluation of more input data sets and other approaches with different configurations,
    you could continue with the [./scripts/batch_run.sh script](scripts/batch_run.sh) which could be used to run
	a huge amount of different entity resolution approaches in a single batch run.
	This script will generate detailed outputs into an json file which you could use to evaluate the different
	approaches in its different configurations against each other and draw nice graphs.

Since this is an internship, there are additionally:
* an [final presentation with our summary of the internship](documentation/final_presentation/output/vortrag.pdf)
* an [concept which was generated at the start of the internship](documentation/concept/output/document.pdf)
