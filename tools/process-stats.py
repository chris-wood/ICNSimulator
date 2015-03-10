#!/usr/bin/python

import os
import sys
import csv
import matplotlib.pyplot as plt
from topogen import *

def computeFileNames(prefix):
	return {"csv": prefix + ".csv", "network": prefix + ".network.pickle", "graph": prefix + ".graph.pickle"}

def processStatsFromFile(fileName, root):
	fileNames = computeFileNames(fileName)
	csv = open(fileNames["csv"], "rb")
	graph = pickle.load(open(fileNames["graph"], "rb"))
	computeStatistics(fileName, csv, graph, root)

def computeStatistics(fileName, csvfile, graph, root):
	nodeCountMap = {}
	nodeSizeMap = {}
	nodeTimeMap = {}
	maxDistance = 0

	msgTypeFiles = {}

	for line in csvfile:
		lineData = line.strip().split(",")
		countTotal, countPercentage = extractCountFromLine(lineData)
		timeTotal, timePercentage = extractTimeFromLine(lineData)
		sizeTotal, sizePercentage = extractSizeFromLine(lineData)
		identity = extractIdentityFromLine(lineData)
		messageType = extractTypeFromLine(lineData)

		if not (messageType in msgTypeFiles):
			msgTypeFiles[messageType] = open(fileName + "." + messageType + ".csv", "w")

		distance = graph.distanceBetweenNodes(identity, root)
		if distance > maxDistance:
			maxDistance = distance

		if (distance in nodeCountMap and messageType in nodeCountMap[distance]):
			nodeCountMap[distance][messageType] = nodeCountMap[distance][messageType] + countTotal
		elif (distance in nodeCountMap and not (messageType in nodeCountMap[distance])):
			nodeCountMap[distance][messageType] = countTotal
		else:
			nodeCountMap[distance] = {}
			nodeCountMap[distance][messageType] = countTotal

		if (distance in nodeSizeMap and messageType in nodeSizeMap[distance]):
			nodeSizeMap[distance] = nodeSizeMap[distance][messageType] + sizeTotal
		elif (distance in nodeSizeMap and not (messageType in nodeSizeMap[distance])):
			nodeSizeMap[distance][messageType] = sizeTotal
		else:
			nodeSizeMap[distance] = {}
			nodeSizeMap[distance][messageType] = sizeTotal

		if (distance in nodeTimeMap and messageType in nodeTimeMap[distance]):
			nodeTimeMap[distance] = nodeTimeMap[distance][messageType] + timeTotal
		elif (distance in nodeTimeMap and not (messageType in nodeTimeMap[distance])):
			nodeTimeMap[distance][messageType] = timeTotal
		else:
			nodeTimeMap[distance] = {}
			nodeTimeMap[distance][messageType] = timeTotal

	for distance in range(maxDistance):
		for msgType in nodeCountMap[distance]:
			msgTypeFiles[msgType].write(str(distance) + "," + str(nodeCountMap[distance][msgType]) + "\n")

	for msgType in msgTypeFiles:
		msgTypeFiles[msgType].close()


def extractIdentityFromLine(csvline):
	return int(csvline[0].split(":")[1])

def extractTypeFromLine(csvline):
	return csvline[1]

def extractCountFromLine(csvline):
	offset = 2
	return extractDataPointWithOffset(csvline, offset)

def extractTimeFromLine(csvline):
	offset = 5
	return extractDataPointWithOffset(csvline, offset)

def extractSizeFromLine(csvline):
	offset = 8
	return extractDataPointWithOffset(csvline, offset)

def extractDataPointWithOffset(csvline, offset):
	total = float(csvline[offset + 1])
	percentage = float(csvline[offset + 2])
	return total, percentage	

def main(args):
	if (len(args) != 3):
		showUsage()
	else:
		processStatsFromFile(args[1], int(args[2]))

def showUsage():
	print "Usage: python process-stats.py <file-name-prefix>"

if __name__ == "__main__":
	main(sys.argv)

