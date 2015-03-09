#!/usr/bin/python

import os
import sys
import csv
from topogen import *

def computeFileNames(prefix):
	return {"csv": prefix + ".csv", "network": prefix + ".network.pickle", "graph": prefix + ".graph.pickle"}

def processStatesFromFile(fileName):
	fileNames = computeFileNames(fileName)
	csv = open(fileNames["csv"], "rb")
	graph = pickle.load(open(fileNames["graph"], "rb"))

	# # TODO: mangle the CSV here
	# print graph.distanceBetweenNodes(int("0"), int("4"))


def showUsage():
	print "Usage: python process-stats.py <file-name-prefix>"

def main(args):
	if (len(args) != 2):
		showUsage()
	else:
		processStatesFromFile(args[1])

if __name__ == "__main__":
	main(sys.argv)

