## Problem

<img
	src="Bilder/Problem.png"
	alt="Problem"
	style="width: 400px;"/>

- 2 Mengen von Personen
- repräsentiert durch Id, Name, Adresse und Geburtsdatum
- finden gleicher Personen in beiden Datensätzen
- Standardansatz: Entity Resolution mit Jaccard-Koeffizient
- Optimierung mittels Bitarrays (Bloomfilter)

## Entity Resolution mit Jaccard-Koeffizient

![Jaccard-Koeffizient](Bilder/Jaccard-Koeffizient_mit_Formel.png)

## Bloomfilter

![Example insert](Bilder/bloomfilter_example_insert.png)

## Anwendung Bloomfilter 1

![Bloomfilter-Ansatz](Bilder/Jaccard-Koeffizient_Bloomfilter_1.png)

## Anwendung Bloomfilter

![Bloomfilter-Ansatz](Bilder/Jaccard-Koeffizient_Bloomfilter_2.png)
