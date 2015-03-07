#!/usr/bin/python

from topogen import *

# Paths
pathLengths = range(5,20)
for length in pathLengths:
	pathGraph = createPathGraph(length)
	if pathGraph != None:
		fout = open("path_%d.json" % length, "w")
		fout.write(graphToJSON(pathGraph))
		fout.close()

# Trees
treeFanouts = range(2,8)
treeHeights = range(3, 20)
for fanout in treeFanouts:
	for height in treeHeights:
		treeGraph = createTreeGraph(fanout, 2, height)
		if treeGraph != None:
			fout = open("tree_%d_%d.json" % (fanout, height), "w")
			fout.write(graphToJSON(treeGraph))
			fout.close()

# Meshes
numberOfProducers = range(2, 10)
numberOfConsumers = range(2, 50)
numberOfRouters = range(2, 100)
for producerCount in numberOfProducers:
	for consumerCount in numberOfConsumers:
		for routerCount in numberOfRouters:
			meshGraph = createRandomGraph(consumerCount, producerCount, routerCount)
			if meshGraph != None:
				fout = open("mesh_%d_%d_%d.json" % (consumerCount, producerCount, routerCount), "w")
				fout.write(graphToJSON(meshGraph))
				fout.close()


