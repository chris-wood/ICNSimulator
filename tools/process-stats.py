#!/usr/bin/python

import os
import sys
import csv
from topogen import *

def computeFileNames(prefix):
	return {"csv": prefix + ".csv", "pickle": prefix + ".pickle"}

def processStatesFromFile(fileName):
	fileNames = computeFileNames(fileName)
	csv = fopen(fileNames["csv"], "rb")
	network = pickle.load(fileNames["pickle"])

	# TODO: mangle the CSV here


def showUsage():
	print "Usage: python process-stats.py <file-name-prefix>"

def main(args):
	if (len(args) != 2):
		showUsage()
	else:
		processStatesFromFile(args[1])

if __name__ == "__main__":
	main(sys.argv)

