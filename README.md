# Data-Stream-Mining-With-DGIM-Algorithm

Reference Paper : Mayur Datar, Aristides Gionis, Piotr Indyk, And Rajeev Motwani Maintaining Stream Statistics over Sliding Window paper

Mini project to implement DGIM Algorithm for estimating number of ones in continuous bit stream.

Objective of this project is to estimate number of ones in past K data with a tolerance of not more than 50%. The major challenge with continuous stream of bit is that storing the stream in main memory is not possible because of the continuous flow of bit stream which will start accumulating and eventually exceeds the size of memory. Therefore, using DGIM Algorithm, n number of bits can be stored in log n memory space.

Often, it is much more efficient to get an approximate answer to our problem than an exact solution.

Theory behind DGIM
To begin, each bit of the stream has a timestamp, the position in which it arrives. The first bit has timestamp 1, the second has timestamp 2, and so on.

Since we only need to distinguish positions within the window of length N , we shall represent timestamps modulo N, so they can be represented by log N bits. If we also store the total number of bits ever seen in the stream (i.e., the most recent timestamp) modulo N , then we can determine from a timestamp modulo N where in the current window the bit with that timestamp is.

We divide the window into buckets, 5 consisting of:

The timestamp of its right (most recent) end.
The number of 1’s in the bucket. This number must be a power of 2, and we refer to the number of 1’s as the size of the bucket.
There are six rules that must be followed when representing a stream by buckets. • The right end of a bucket is always a position with a 1. • Every position with a 1 is in some bucket. • No position is in more than one bucket. • There are one or two buckets of any given size, up to some maximum size ( r ). • All sizes must be a power of 2. • Buckets cannot decrease in size as we move to the left (back in time).

Implementation
Implementation of DGIM algorithm involves creating bucket of new bits and maintaining the bucket table. Second part of this algorithm is to estimating the count of ones.

In this project, Multithreading program has been implemented. One thread handles the continuous bit of stream from server and eventually responsible for maintainance of bucket table. Whereas other thread handles the query and estimate the count of ones.

Usage:
        
    To run the server

    $ ./server.py <#int> min max

where,

     #int : Number of bits min : minimum delay between bits max : maximum delay between bits

To run the program

$ ./Main.java
Input Format

First line is size of window (any positive number)
Second line is host:port pair (for example, localhost:50359), where port number would be from the output of server.py 
Third line would be query in following format "What is the sum for last XXXXXX values?"
