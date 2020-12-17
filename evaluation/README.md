# This file contains the commands to reproduce the evaluation

## MESA Evaluation 
```
java -jar ConstrainedKaos -gcStart 0.4 -gcEnd 0.6 -input const.fasts -output results.fasta -hp 4 -length 10
```
## Comparison with Fountain codes 
```
java -jar ConstrainedKaos -gcStart 0.4 -gcEnd 0.6 -input const2.fasts -output results1.fasta -hp 4 -length 10
java -jar ConstrainedKaos -gc 0.5 -input const2.fasts -output results2.fasta -hp 4 -length 10
java -jar ConstrainedKaos -input const2.fasts -output results3.fasta -hp 4 -length 10
```
