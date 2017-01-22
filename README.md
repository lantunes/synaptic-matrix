Synaptic Matrix
===============

This project represents an ongoing exploration to find new neurobiologically inspired learning algorithms and computational techniques that are lighter weight and more computationally efficient. It represents a search for alternatives to the neural net/deep learning movement, which has a dependency on--and achieves practicality only through--state-of-the-art computational power and resources (such as GPUs). 

This work is inspired by, and based on, Dr. Arnold Trehub's work. For more information, see his book, **The Cognitive Brain**, and visit his web page: http://people.umass.edu/trehub/

Particularly, this project is based on his concept of the synaptic matrix. For more on the synaptic matrix, see: http://people.umass.edu/trehub/thecognitivebrain/chapter3.pdf

The synaptic matrix implemented in this project supports supervised learning scenarios. During the training phase, examples consisting of a single row (or column) matrix are given to the synaptic matrix, along with corresponding labels. After training, the synaptic matrix can be used to classify new inputs. The result of evaluating an input is an array of numbers, the largest of which corresponds to the predicted class. These resulting numbers can be interpreted as the relative spiking rates of the classification neurons.

The synaptic matrix thus consists of the following API:
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
The first constructor argument, in this case _784_, represents the length of the input vector. The second constructor argument, the **SynapticConfig**, contains the synaptic matrix's hyperparameters. These are denoted by the variables _b_, _c_, and _k_. These are constants used while updating the weights of synapses. For more information about these constants, see Arnold Trehub's work, indicated in the links above.

This synaptic matrix implementation was evaluated against the [MNIST data set](http://yann.lecun.com/exdb/mnist/). It achieves 93% accuracy. This is far from the state-of-the-art values of >99% accuracy, but it represents (as far as I can tell) a novel neurobiologically inspired approach towards machine learning. Moreover, it takes minutes to train on 60,000 examples, and evaluate 10,000 examples, on a standard laptop, requiring no graphics processors or other special computing resources. State-of-the-art techniques for MNIST typically involve much more in terms of computing time and resources. Finally, there is no pre-processing of the images in the data set, nor is the data set augmented with any distorted versions of the original images. State-of-the-art techniques often involve pre-processing of the images, and the addition of more images to the data set obtained through deformation of the original images.

This project constitutes largely speculative, experimental, and exploratory work. As such, it is not meant to be used as a formal library. Nevertheless, feel free to use the code for your own purposes. If you do, let me know how you've used it and what results you obtained.

NOTE: The MNIST utility classes in this project were lifted from the https://github.com/sina-masoud-ansari/MNIST repo, with the exception of MNISTViewer.