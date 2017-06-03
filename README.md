Synaptic Matrix
===============

This project is a Java implementation of a synaptic matrix. To learn more about the synaptic matrix, see the project page for the [Python implementation](https://github.com/lantunes/synapy).

The synaptic matrix implemented in this project consists of the following API:
```java
/**
 * The inputExample must be an array containing only 0s and 1s.
 */
void train(int[] inputExample, int classCell);

/**
 * The input must be an array containing only 0s and 1s.
 */
int[] evaluate(int[] input);
```
The _int classCell_ in the _train()_ method above represents the label corresponding to the example given by _int[] inputExample_.

The synaptic matrix is implemented in the **SupervisedSynapticMatrix** class. The following example demonstrates how it is created:
```java
SupervisedSynapticMatrix matrix = new SupervisedSynapticMatrix(784, new SynapticConfig(1, 15, 4200));
```
The first constructor argument, in this case _784_, represents the length of the input vector. The second constructor argument, the **SynapticConfig**, contains the synaptic matrix's hyperparameters. These are denoted by the variables _b_, _c_, and _k_. These are constants used while updating the weights of synapses. For more information about these constants, see [Chapter 3 of Arnold Trehub's book *The Cognitive Brain*](http://people.umass.edu/trehub/thecognitivebrain/chapter3.pdf).

This project constitutes largely speculative, experimental, and exploratory work. As such, it is not meant to be used as a formal library. Nevertheless, feel free to use the code for your own purposes. If you do, let me know how you've used it and what results you obtained.

NOTE: The MNIST utility classes in this project were lifted from the https://github.com/sina-masoud-ansari/MNIST repo, with the exception of MNISTViewer.
