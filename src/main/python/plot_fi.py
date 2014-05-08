#!/usr/bin/python
__author__ = 'rmelisson'

import pandas as pd
import matplotlib.pyplot as plt
import sys

def main():
    if len(sys.argv) != 2:
        print "error : path to the result set file is expected."
        sys.exit(1)

    file = sys.argv[1]
    print "plotting result set in " + file
    df = pd.DataFrame.from_csv(file)
    # print df
    df.plot(kind='bar')
    plt.xticks(rotation=0)
    plt.show()
    sys.exit(0)

if __name__ == "__main__":
    main()