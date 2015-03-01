#!/usr/bin/python

import sys
import getopt
import json
import random
import math
import matplotlib.pyplot as plt
import networkx as nx
from matplotlib.backends.backend_pdf import PdfPages

def createChannelName(source, dest):
	return "channel:" + str(source) + "-" + str(dest)

class Network:
	def __init__(self):
		self.connections = []
		self.channels = []
		self.nodes = {}

	def addNode(self, node):
		self.nodes.append(node)

	def addConnection(self, conn):
		self.connections.append(conn)

	def addChannel(self, channel):
		self.channels.append(channel)

	def createChannels(self, graph, dataRateDistribution = [100]):
		for (u, v) in graph.edges():
			identifier = createChannelName(u, v)
			dataRate = random.choice(dataRateDistribution)
			channel = Channel(identifier, dataRate)
			self.channels.append(channel)

	def createInterfaceMapForNode(self, node, neighbors):
		interfaceMap = {}
		for neighbor in neighbors:
			identifier = "interface:" + str(node) + "#" + str(neighbor)
			interfaceMap[neighbor] = Interface(identifier)
		return interfaceMap

	# TODO: refactor this code, pull out common node creation logic
	def createNodes(self, graph, consumerIndices, producerIndices, routerIndices):
		for index in consumerIndices:
			identity = "consumer:" + str(index)
			point = Point(0,0)
			interfaces = self.createInterfaceMapForNode(index, graph.neighbors(index))
			consumer = Consumer(identity, index, point, interfaces)
			self.nodes[index] = consumer

		for index in producerIndices:
			identity = "producer:" + str(index)
			point = Point(0,0)
			interfaces = self.createInterfaceMapForNode(index, graph.neighbors(index))
			producer = Producer(identity, index, point, interfaces, ["lci:/test"])
			self.nodes[index] = producer

		for index in routerIndices:
			identity = "router:" + str(index)
			point = Point(0,0)
			interfaces = self.createInterfaceMapForNode(index, graph.neighbors(index))
			router = Router(identity, index, point, interfaces)
			self.nodes[index] = router

	def createConnections(self, graph):
		linkNumber = 0
		for (u, v) in graph.edges():
			source = self.nodes[u]
			sourceInterface = source.interfaceMap[v]
			destination = self.nodes[v]
			destinationInterface = destination.interfaceMap[u]

			identity = createChannelName(u, v)
			connection = Connection(identity, source.id, sourceInterface, destination.id, destinationInterface)
			self.connections.append(connection)

			linkNumber = linkNumber + 1

	def toJSON(self):
		nodes = "\"nodes\" : [ %s ]" % ",".join(map(lambda node : node.toJSON(), self.nodes.values()))
		channels = "\"channels\" : [ %s ]" % ",".join(map(lambda channel : channel.toJSON(), self.channels))
		connections = "\"connections\" : [ %s ]" % ",".join(map(lambda connection : connection.toJSON(), self.connections))
		return "{ %s }" % ",".join([nodes, channels, connections])

class Connection:
	def __init__(self, identifier, sourceNode, sourceInterface, destNode, destInterface):
		self.identifier = identifier
		self.sourceNode = sourceNode
		self.sourceInterface = sourceInterface
		self.destNode = destNode
		self.destInterface = destInterface

	def toJSON(self):
		sourceNode = "\"source_id\" : \"%s\"" % self.sourceNode
		sourceInterface = "\"source_interface\" : \"%s\"" % self.sourceInterface
		destNode = "\"destination_id\" : \"%s\"" % self.destNode
		destInterface = "\"destination_interface\" : \"%s\"" % self.destInterface
		channel = "\"channel_id\" : \"%s\"" % self.identifier
		return "{ %s, %s, %s, %s, %s }" % (sourceNode, sourceInterface, destNode, destInterface, channel)

	def __str__(self):
		return str(self.identifier)

class Channel:
	def __init__(self, identifier, dataRate):
		self.id = identifier
		self.dataRate = dataRate

	def toJSON(self):
		channelId = "\"channel_id\" : \"%s\"" % self.id
		dataRate = "\"data_rate\" : \"%s\"" % self.dataRate
		return "{ %s, %s }" % (channelId, dataRate)	

	def __str__(self):
		return str(self.id)

class Interface:
	def __init__(self, identifier):
		self.id = identifier

	def toJSON(self):
		return "{ \"interface_id\" : \"%s\" }" % self.id

	def __str__(self):
		return str(self.id)

class Point:
	def __init__(self, xCoord, yCoord):
		self.xCoord = xCoord
		self.yCoord = yCoord

	def toJSON(self):
		xCoord = "\"x-coord\" : \"%s\"" % self.xCoord	
		yCoord = "\"y-coord\" : \"%s\"" % self.yCoord
		return "{ %s, %s }" % (xCoord, yCoord)	

class Node(object):
	def __init__(self, identifier, index, point, interfaceMap):
		self.id = identifier
		self.index = index
		self.point = point
		self.interfaceMap = interfaceMap

	def getCommonJSON(self):
		nodeId = "\"node_id\" : \"%s\"" % self.id
		nodePoint = "\"point\" : %s " % self.point.toJSON()
		nodeInterfaces = "\"interfaces\" : [ %s ]" % ",".join(map(lambda interface : interface.toJSON(), self.interfaceMap.values()))
		return (nodeId, nodePoint, nodeInterfaces)

	def toJSON(self):
		return ""

	def __str__(self):
		return str(self.id)

class Consumer(Node):
	def __init__(self, identifier, index, point, interfaceMap):
		super(Consumer, self).__init__(identifier, index, point, interfaceMap)

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		return "{ %s }" % ",".join([parentJSON, nodeType])

class Router(Node):
	def __init__(self, identifier, index, point, interfaceMap):
		super(Router, self).__init__(identifier, index, point, interfaceMap)

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		return "{ %s }" % ",".join([parentJSON, nodeType])

class Producer(Node):
	def __init__(self, identifier, index, point, interfaceMap, prefixes):
		super(Producer, self).__init__(identifier, index, point, interfaceMap)
		self.prefixes = prefixes

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		prefixes = "\"prefixes\" : [ %s ]" % ",".join(map(lambda prefix : "\"" + prefix + "\"", self.prefixes))
		return "{ %s }" % ",".join([parentJSON, nodeType, prefixes])

def buildNetwork(graph, consumerNodes, producerNodes, routerNodes):
	network = Network()
	network.createChannels(graph)
	network.createNodes(graph, consumerNodes, producerNodes, routerNodes)
	network.createConnections(graph)
	return network

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
	# G1 = nx.balanced_tree(2, 2)
	# G2 = nx.balanced_tree(2, 2)



	# point = Point(0, 0)
	# interface = Interface("interface1")
	# c1 = Consumer("consumer1", 0, point, [interface])
	# print c1.toJSON()

	# http://networkx.lanl.gov/reference/algorithms.operators.html
	# https://networkx.github.io/documentation/latest/reference/generators.html

	# G = nx.cartesian_product(G1, G2)
	# G = nx.union(G1, G2)
	# G = nx.strong_product(G1, G2)
	# G = G1

	G = nx.Graph()
	G.add_node(0)
	G.add_node(1)
	G.add_node(2)
	G.add_node(3)
	G.add_node(4)

	G.add_edge(0,1)
	G.add_edge(1,2)
	G.add_edge(2,3)
	G.add_edge(3,4)

	print nx.center(G)

	print G.edges()

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

	# intersection -- TODO: fix if needed, i.e., if they're not all empty.
	# print intersect(producerNodes, consumerNodes)
	# print intersect(producerNodes, routerNodes)
	# print intersect(consumerNodes, routerNodes)

	network = buildNetwork(G, consumerNodes, producerNodes, routerNodes)
	print network.toJSON()

	# nx.draw(G)
	# plt.show(G)

if __name__ == "__main__":
	main(sys.argv)