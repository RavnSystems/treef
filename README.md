Treef
=====

[Gini feature importance](http://www.stat.berkeley.edu/~breiman/RandomForests/cc_home.htm#giniimp "definition")
for [RankLib](http://sourceforge.net/p/lemur/wiki/RankLib/ "RankLib website") forests :

- Read a random forest generated by RankLib and run a training set against it.
- Calculate the Gini importance of the different features according to that pass.
- Write a file containing the result which can be plot using a Python script (matplotlib + pandas required).