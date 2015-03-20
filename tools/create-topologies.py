#!/usr/bin/python

from topogen import *
import pickle

targetDir = sys.argv[1]

# Paths
pathLengths = range(5,40)
for length in pathLengths:
	pathGraph = createPathGraph(length)
	if pathGraph != None:
		network, json = graphToJSON(pathGraph)

		prefix = targetDir + "/" + "path_%d" % length
		print prefix
		fout = open(prefix + ".json", "w")
		fout.write(json)
		fout.close()

		pickle.dump(pathGraph, open(prefix + ".graph.pickle", "wb"))
		pickle.dump(network, open(prefix + ".network.pickle", "wb"))

# Trees
treeFanouts = range(2,5)
treeHeights = range(3,13)
for fanout in treeFanouts:
	for height in treeHeights:
		treeGraph = createTreeGraph(fanout, 2, height)
		if treeGraph != None:
			network, json = graphToJSON(treeGraph)

			prefix = targetDir + "/" + "tree_%d_%d"% (fanout, height)
			print prefix			

			fout = open(prefix + ".json", "w")
			fout.write(json)
			fout.close()

			pickle.dump(treeGraph, open(prefix + ".graph.pickle", "wb"))
			pickle.dump(network, open(prefix + ".network.pickle", "wb"))

# # Meshes
# numberOfProducers = range(1, 2)
# numberOfConsumers = range(2, 50)
# numberOfRouters = range(2, 100)
# for producerCount in numberOfProducers:
# 	for consumerCount in numberOfConsumers:
# 		for routerCount in numberOfRouters:
# 			meshGraph = createRandomGraph(consumerCount, producerCount, routerCount)
# 			if meshGraph != None:
# 				network, json = graphToJSON(meshGraph)

# 				prefix = targetDir + "/" + "mesh_%d_%d_%d" % (consumerCount, producerCount, routerCount) 
# 				print prefix
# 				fout = open(prefix + ".json", "w")
# 				fout.write(json)
# 				fout.close()

# 				pickle.dump(meshGraph, open(prefix + ".graph.pickle", "wb"))
# 				pickle.dump(network, open(prefix + ".network.pickle", "wb"))
