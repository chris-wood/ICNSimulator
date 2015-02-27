#!/usr/bin/python

import sys
import getopt
import json
import random
import math
import matplotlib.pyplot as plt
import networkx as nx
from matplotlib.backends.backend_pdf import PdfPages

class Channel:
	def __init__(self, identifier, dataRate):
		self.id = identifier
		self.dataRate = dataRate

	def toJSON(self):
		channelId = "\"channel_id\" : \"%s\"" % self.id
		dataRate = "\"data_rate\" : \"%s\"" % self.dataRate
		return "{ %s, %s }" % (channelId, dataRate)	

class Interface:
	def __init__(self, identifier):
		self.id = identifier

	def toJSON(self):
		return "{ \"interface_id\" : \"%s\" }" % self.id

class Point:
	def __init__(self, xCoord, yCoord):
		self.xCoord = xCoord
		self.yCoord = yCoord

	def toJSON(self):
		xCoord = "\"x-coord\" : \"%s\"" % self.xCoord	
		yCoord = "\"y-coord\" : \"%s\"" % self.yCoord
		return "{ %s, %s }" % (xCoord, yCoord)	

class Node(object):
	def __init__(self, identifier, index, point, interfaces):
		self.id = identifier
		self.index = index
		self.point = point
		self.interfaces = interfaces

	def getCommonJSON(self):
		nodeId = "\"node_id\" : \"%s\"" % self.id
		nodePoint = "\"point\" : %s " % self.point.toJSON()
		nodeInterfaces = "\"interfaces\" : [ %s ]" % ",".join(map(lambda interface : interface.toJSON(), self.interfaces))
		return (nodeId, nodePoint, nodeInterfaces)

	def toJSON(self):
		return ""

class Consumer(Node):
	def __init__(self, identifier, index, point, interfaces):
		super(Consumer, self).__init__(identifier, index, point, interfaces)

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		return "{ %s }" % ",".join([parentJSON, nodeType])

class Router(Node):
	def __init__(self, identifier, index, point, interfaces):
		super(Router, self).__init__(identifier, index, point, interfaces)

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		return "{ %s }" % ",".join([parentJSON, nodeType])

class Producer(Node):
	def __init__(self, id, index, point, interfaces, prefixes):
		super(Producer, self).__init__(identifier, index, point, interfaces)
		self.prefixes = prefixes

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		prefixes = "\"prefixes\" : \"%s\"" % ",".join(self.prefixes)
		return "{ %s }" % ",".join([parentJSON, nodeType, prefixes])

def createChannels():
	return None

def createNodes(graph, consumerIndices, producerIndices, routerIndices):
	return None

def createConnections(graph):
	return None

def partitionNodesInGraph(graph, center):
	queue = []
	visited = {}

	height = 0
	queue.append((center, height))

	while (len(queue) > 0):
		(node, currentLevel) = queue.pop(0)
		if (not (node in visited)):
			visited[node] = currentLevel
			for neighbor in graph.neighbors(node):
				queue.append((neighbor, currentLevel + 1))
			if (height == currentLevel):
				height = height + 1

	leaves = []
	nonleaves = []
	for node in visited:
		if (visited[node] == height - 1):
			leaves.append(node)
		elif node != center:
			nonleaves.append(node)

	return leaves, nonleaves, center

def intersect(a, b):
	return list(set(a) & set(b))

def main(argv):
	G1 = nx.balanced_tree(2, 2)
	G2 = nx.balanced_tree(2, 2)

	# point = Point(0, 0)
	# interface = Interface("interface1")
	# c1 = Consumer("consumer1", 0, point, [interface])
	# print c1.toJSON()

	# http://networkx.lanl.gov/reference/algorithms.operators.html
	# https://networkx.github.io/documentation/latest/reference/generators.html

	# G = nx.cartesian_product(G1, G2)
	# G = nx.union(G1, G2)
	G = nx.strong_product(G1, G2)
	# G = G1
	print nx.center(G)

	consumerNodes = []
	routerNodes = []
	producerNodes = []

	centerNodes = nx.center(G)
	for centerIndex in range(len(centerNodes)):
		centerNode = centerNodes[centerIndex]
		leaves, nonleaves, center = partitionNodesInGraph(G, centerNode)
		
		consumerNodes.extend(leaves)
		routerNodes.extend(nonleaves)
		producerNodes.extend([centerNode])

	print "producers = " + str(producerNodes)
	print "consumers = " + str(consumerNodes)
	print "routers = " + str(routerNodes)

	# intersection -- TODO: fix if needed
	print intersect(producerNodes, consumerNodes)
	print intersect(producerNodes, routerNodes)
	print intersect(consumerNodes, routerNodes)

	# nx.draw(G)
	# plt.show(G)

if __name__ == "__main__":
	main(sys.argv)