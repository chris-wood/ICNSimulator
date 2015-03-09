#!/usr/bin/python

from topogen import *
import pickle

# TODO: read in parameters from a configuration file

# Paths
pathLengths = range(5,20)
for length in pathLengths:
	pathGraph = createPathGraph(length)
	if pathGraph != None:
		network, json = graphToJSON(pathGraph)

		prefix = "path_%d" % length
		fout = open(prefix + ".json", "w")
		fout.write(json)
		fout.close()

		fout = open(prefix + ".pickle", "w")
		fout.write(pickle.dumps(network))
		fout.close()

# Trees
treeFanouts = range(2,8)
treeHeights = range(3, 20)
for fanout in treeFanouts:
	for height in treeHeights:
		treeGraph = createTreeGraph(fanout, 2, height)
		if treeGraph != None:
			network, json = graphToJSON(treeGraph)

			prefix = "tree_%d_%d"% (fanout, height)
			fout = open(prefix + ".json", "w")
			fout.write(json)
			fout.close()

			fout = open(prefix + ".pickle", "w")
			fout.write(pickle.dumps(network))
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
				prefix = "mesh_%d_%d_%d" % (consumerCount, producerCount, routerCount) 
				fout = open(prefix + ".json", "w")
				network, json = graphToJSON(meshGraph)
				fout.write(json)
				fout.close()

				fout = open(prefix + ".pickle", "w")
				fout.write(pickle.dumps(network))
				fout.close()
