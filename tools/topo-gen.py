#!/usr/bin/python

import sys
import getopt
import json
import random
import math
import matplotlib.pyplot as plt
import networkx as nx
import argparse
from matplotlib.backends.backend_pdf import PdfPages

def createChannelName(source, dest):
	return "channel:" + str(source) + "-" + str(dest)

def karyPath(k, num, length=8):
	path = []
	while len(path) < length:
		if (num == 0):
			path.insert(0, 0)
		else:
			pathChoice = num % k
			num = num / k
			path.insert(0, pathChoice)
	return path

class Graph(object):
	def __init__(self):
		self.graph = nx.Graph()
		self.consumerNodes = []
		self.routerNodes = []
		self.producerNodes = []

	def getGraph(self):
		return self.graph

	def numberOfNodes(self):
		return len(self.graph.nodes())

	def numberOfEdges(self):
		return len(self.graph.edges())

	def add_node(self, i):
		self.graph.add_node(i)

	def add_consumer(self, i):
		self.graph.add_node(i)
		self.consumerNodes.append(i)

	def add_producer(self, i):
		self.graph.add_node(i)
		self.producerNodes.append(i)

	def add_router(self, i):
		self.graph.add_node(i)
		self.routerNodes.append(i)

	def add_edge(self, u, v):
		self.graph.add_edge(u, v)

	def nodes(self):
		return self.graph.nodes()

	def edges(self):
		return self.graph.edges()

	def neighbors(self, i):
		return self.graph.neighbors(i)

	def restrictedNeighbors(self, i, removeList):
		if None in removeList:
			return self.neighbors(i)
		else:
			return filter(lambda x: x not in removeList, self.graph.neighbors(i))

	def getCenterNodes(self):
		return nx.center(self.graph)

	def partitionNodesFromCenter(self, center):
		queue = []
		visited = {}

		height = 0
		queue.append((center, height))

		while (len(queue) > 0):
			(node, currentLevel) = queue.pop(0)
			if (not (node in visited)):
				visited[node] = currentLevel
				for neighbor in self.graph.neighbors(node):
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

	def partition(self):
		centerNodes = nx.center(self.graph)
		for centerIndex in range(len(centerNodes)):
			centerNode = centerNodes[centerIndex]
			leaves, nonleaves, center = self.partitionNodesFromCenter(centerNode)
			self.consumerNodes.extend(leaves)
			self.routerNodes.extend(nonleaves)
			self.producerNodes.extend([centerNode])

		if not self.isValidPartition():
			raise Exception("Invalid graph partition")

	def isValidPartition(self):
		if (len(intersect(self.producerNodes, self.consumerNodes)) > 0):
			print >> sys.stderr, "Error: producer and consumer nodes not disjoint"
			return False
		if (len(intersect(self.producerNodes, self.routerNodes)) > 0):
			print >> sys.stderr, "Error: producer and router nodes not disjoint"
			return False
		if (len(intersect(self.consumerNodes, self.routerNodes)) > 0):
			print >> sys.stderr, "Error: consumer and router nodes not disjoint"
			return False
		return True

	def getConsumers(self):
		return self.consumerNodes

	def getProducers(self):
		return self.producerNodes

	def getRouters(self):
		return self.routerNodes

class Tree(Graph):
	def __init__(self):
		super(Tree, self).__init__()

	def addPathFromRoot(self, root, path):
		currentNode = root
		previousNode = None
		for i in range(len(path)):
			neighbors = self.restrictedNeighbors(currentNode, [previousNode])
			pathChoice = path[i]
			while len(neighbors) <= pathChoice:
				newNode = self.numberOfNodes()
				self.add_node(newNode)
				self.add_edge(currentNode, newNode)
				neighbors = self.restrictedNeighbors(currentNode, [previousNode])
			previousNode = currentNode
			currentNode = neighbors[pathChoice]

	def partition(self):
		leaves, nonleaves, center = self.partitionNodesFromCenter(0) # 0 is the center, by convention
		self.consumerNodes.extend(leaves)
		self.routerNodes.extend(nonleaves)

class Network:
	def __init__(self):
		self.connections = []
		self.channels = []
		self.nodes = {}

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
	def __init__(self, identifier, index, point, interfaceMap, capacity):
		super(Router, self).__init__(identifier, index, point, interfaceMap)
		self.capacity = capacity

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		capacity = "\"cache_capacity\" : \"" + str(capacity) + "\""
		return "{ %s }" % ",".join([parentJSON, nodeType, capacity])

class Producer(Node):
	def __init__(self, identifier, index, point, interfaceMap, prefixes):
		super(Producer, self).__init__(identifier, index, point, interfaceMap)
		self.prefixes = prefixes

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		prefixes = "\"prefixes\" : [ %s ]" % ",".join(map(lambda prefix : "\"" + prefix + "\"", self.prefixes))
		return "{ %s }" % ",".join([parentJSON, nodeType, prefixes])

def buildNetworkFromGraph(graph):
	network = Network()
	network.createChannels(graph)
	network.createNodes(graph, graph.getConsumers(), graph.getProducers(), graph.getRouters())
	network.createConnections(graph)
	return network

def intersect(a, b):
	return list(set(a) & set(b))

def createPathGraph(l):
	G = Graph()
	for i in range(0, l):
		G.add_node(i)
	for i in range(0, l - 1):
		G.add_edge(i, i + 1)

	try:
		G.partition()
	except Exception as e:
		print str(e)
		return None

	return G

def createTreeGraph(k, minLength, maxLength):
	tree = Tree()

	producerNode = 0
	tree.add_producer(producerNode)

	numLeaves = k**maxLength
	formatString = '#0' + str(maxLength) + 'b'
	for leaf in range(numLeaves):
		height = random.randint(minLength, maxLength)
		path = karyPath(k, leaf, maxLength)
		tree.addPathFromRoot(producerNode, path)

	try:
		tree.partition()
	except Exception as e:
		print str(e)
		return None

	return tree

# TODO: need to create routers with a finite cache capacity -> do that here
def createRandomGraph(c, p, r, capacityDistribution=[1000000,1000000000]):
	G = Graph()

	for i in range(c):
		G.add_consumer(i)
	for i in range(p):
		G.add_producer(i)
	for i in range(r):
		G.add_router(i)

	# TODO: make the connections as needed

	return G

def createBasicGraph():
	return createPathGraph(5) # simple path graph of length 5

def createGraph(args):
	if args.random:
		values = args.random
		return createRandomGraph(values[0], values[1], values[2])
	elif args.tree:
		values = args.tree
		return createTreeGraph(values[0], values[1], values[2])
	elif args.path:
		values = args.path
		return createPathGraph(values[0])
	else:	
		return createBasicGraph()

def main(args):
	graph = createGraph(args)
	network = buildNetworkFromGraph(graph)
	print >> sys.stdout, network.toJSON()
	if len(args.draw) > 0:
		nx.draw(graph.getGraph())
		plt.savefig(args.draw)

if __name__ == "__main__":
	parser = argparse.ArgumentParser(prog='gen', formatter_class=argparse.RawDescriptionHelpFormatter, description="Generator for CCN network topologies")
	parser.add_argument('-r', '--random', type=int, metavar=('C', 'P', 'R'), nargs=3, help="Produce a random graph topology with C consumers, P producers, and R routers")
	parser.add_argument('-t', '--tree', type=int, metavar=('k', 'min', 'max'), nargs=3, help="Produce a k-ary tree where each path has height between min and max")
	parser.add_argument('-p', '--path', type=int, metavar=('l'), nargs=1, help="Producer a path graph of the length l")
	parser.add_argument('-d', '--draw', default="", action="store", help="Plot the resulting network to the specified file")
	args = parser.parse_args()
	main(args)