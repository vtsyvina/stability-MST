# stability-MST
Algorithm to calculate radius of stability for MST problem. See:

N. Chakravarti, A. Wagelmans, Calculation of stability radius forcombinatorial optimization problems, Operations Research Letters.1998. V. 23. P. 1–7.

## Input
Input file in the first line contains the size of the graph followed by weight matrix(simetrical).
To mark non-existing edge put symbol 'X'.
Example:
```
4
0 2 7 1
2 0 3 6
7 3 0 8
1 6 8 0
```

```
4
0 2 7 X
2 0 3 6
7 3 0 8
X 6 8 0
```

## How to run
After git clone
```
mvn clean install
```

It will create stability.jar file. Then

```
java -jar stability.jar <input_file>
```

```
java -jar stability.jar data/graph.in
```

Example output:
```
Radius = 2.0

Process finished with exit code 0
```