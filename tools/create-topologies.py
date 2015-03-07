#!/usr/bin/python

from topogen import *

# Paths
pathLengths = range(5,20)
for length in pathLengths:
	pathGraph = createPathGraph(length)
	if pathGraph != None:
		print >> sys.stdout, graphToJSON(pathGraph)

# Trees
treeFanouts = range(2,8)
treeHeights = range(3, 20)
for fanout in treeFanouts:
	for height in treeHeights:
		treeGraph = createTreeGraph(fanout, 2, height)
		if treeGraph != None:
			print >> sys.stdout, graphToJSON(treeGraph)

# Meshes
numberOfProducers = range(2, 10)
numberOfConsumers = range(2, 50)
numberOfRouters = range(2, 100)
for producerCount in numberOfProducers:
	for consumerCount in numberOfConsumers:
		for routerCount in numberOfRouters:
			meshGraph = createRandomGraph(consumerCount, producerCount, routerCount)
			if meshGraph != None:
				print >> sys.stdout, graphToJSON(meshGraph)


