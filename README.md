# Fractal Construction of Constrained Code Words for DNA Storage Systems

This repository is the official implementation of Fractal Construction of Constrained Code Words for
DNA Storage Systems.

With the increasing speed of digitization, the amount of digital data produced is growing exponentially. To store this increasingly large amount of data, DNA is an alternative data storage medium. This comes with certain constraints, such as run-length limitation or guanine-cytosine (GC) content. Here we present a new approach, derived from chaos game representation for DNA, transformed into a matrix representation, to generate those DNA code words with certain constraints, namely GC content, homopolymers, and undesired motifs, which can then be used to build DNA storage systems.

An implementation of the equations can be found in the R-Script. The optimized algorithms adhere to DNA storage constraints can be found in the Java implementation, within a compiled version: ConstrainedKaos.jar .

## Requirements

### R Script

R version 3.5.3 or higher
R-packages: kaos, viridis, ggplot2, reshape2, RcolorBrewer


### Java Sourcecode

Java 12.0.1
dependencies: Apache Commons Math 3.6.1, JFreeChart 1.0.19, Common 1.0.23

### JAR (ConstrainedKaos.jar)

Java 12.0.1 or higher

## Usage

### R Script

The R Script contains functions for the implementation of the equations adhere to the homopolymer/motif constraint, the GC content/Hamming distance calculation, and the Hamming distance calculation for one codeword against the others. For every equation at least one example is provided. E.g. for homopolymer >=2:


<img width="250" alt="Image HP" src="https://github.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/blob/master/documentation/images/hp2.png?raw=true">

<!---
![Image HP](https://raw.githubusercontent.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/master/documentation/images/hp2.png?token=AO45UWLDZUONZLKMEYS4ONS7LIGSY)
--->
The calculation of the GC Content of a wordlength of 4:

<img width="250" alt="Image GC" src="https://github.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/blob/master/documentation/images/GC4.png?raw=true">

<!---
![Image GC](https://raw.githubusercontent.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/master/documentation/images/GC4.png?token=AO45UWOERVE3EORFZBHVMR27LIG3I)
--->
The codewords with an exact 50 % GC content:

<img width="250" alt="Image GC50" src="https://github.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/blob/master/documentation/images/GC4BW.png?raw=true">

<!---
![Image GC50](https://raw.githubusercontent.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/master/documentation/images/GC4BW.png?token=AO45UWI7SNR6I7KHP4ZJVIS7LIG7S)
--->

For combination of several motifs with same length, the following sourcecode can be applied:
```
color.plot2<-function(data,col){
  ggplot(melt((data)), aes(x = Var1, y = Var2)) +
    geom_raster(aes(fill = as.factor(value))) +
    theme_bw() +
    theme(panel.grid.major = element_blank(),
          panel.grid.minor = element_blank(),
          axis.line=element_blank(),
          axis.text.x=element_blank(),
          axis.text.y=element_blank(),
          axis.ticks=element_blank(),
          axis.title.x=element_blank(),
          axis.title.y=element_blank(),
          legend.position="none")+
    scale_fill_brewer(palette=col)
}

hp.com=function(strings,n){
  m=matrix(0,nrow=2^n,ncol =2^n)
  hp=matrix(0,nrow=2^n,ncol =2^n)
  for (s in strings) {
    m=m+hp(s,n)
  }
  m
}

color.plot2(hp.com2(c("ATG","AGT","CGT","CTG","TCA","TAC","GAC","GCA"),8),"Spectral")
```
<img width="250" alt="Image GC50" src="https://github.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/blob/master/documentation/images/motifs.png?raw=true">

<!---
![Image GC](https://raw.githubusercontent.com/HFLoechel/Fractal-Construction-of-Constrained-DNA-Codewords/master/documentation/images/motifs.png?token=AO45UWONALRSRW4UL364PXS7LII7Y)
--->

### JAR

The JAR can be executed with:

java -jar ConstrainedKaos.jar -length 6 -output path/codewords.fasta -hp 2

The codeword length **-length** and the output **-output** path to store the codewords, are always required to run the application.
At least one of the following options is required as well:

**-hp**: length of the homopolymer, which should be constrained

**input**: path to fasta file with constrained sequences

**-gc**: GC content as float


or for an interval both of the following are required

**-gcStart**: GC content start as float

**-gcEnd**: GC content end as float

Also, there is the option to plot the code words:

**-plot**: size as an integer of the dots in the CGR plot, if **-plot** is not used, no plot will be created


For a codeword length greater 12 the application will throw a warning, that the heap size may be exceeded. So for longer codewords, the heap size of the JVM has to be adapted. 
The longest words we tested for were 16, which also let to a significant increase in the runtime.
