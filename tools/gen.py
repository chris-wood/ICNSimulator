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
	def __init__(self, id, index, point, interfaces):
		super(Producer, self).__init__(identifier, index, point, interfaces)

	def toJSON(self):
		parentJSON = ",".join(self.getCommonJSON())
		nodeType = "\"node_type\" : \"%s\"" % type(self).__name__.lower()
		return "{ %s }" % ",".join([parentJSON, nodeType])

def createChannels():
	return None

def createNodes(graph, consumerIndices, producerIndices, routerIndices):
	return None

def createConnections(graph):
	return None

def main(argv):
	G1 = nx.balanced_tree(2, 2)
	G2 = nx.balanced_tree(2, 2)

	# point = Point(0, 0)
	# interface = Interface("interface1")
	# c1 = Consumer("consumer1", 0, point, [interface])
	# print c1.toJSON()

	# G = nx.cartesian_product(G1, G2)
	# G = nx.union(G1, G2)
	G = nx.strong_product(G1, G2)
	print G2.nodes()
	print G2.edges()
	print nx.center(G2)


	# TODO: BFS from each vertex in the center to the edges, those become the consumers
	# everythinng in between is a router
	# create them as such...

	# nx.draw(G)
	# plt.show(G)

if __name__ == "__main__":
	main(sys.argv)