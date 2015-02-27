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

	def toJSON():
		channelId = "\"channel_id\" \"%s\"" % (self.id)	
		dataRate = "\"data_rate\" \"%s\"" % (self.dataRate)	
		return "{ %s, %s }" % (channelId, dataRate)	

class Interface:
	def __init__(self, identifier):
		self.id = identifier

	def toJSON():
		return "{ \"interface_id\" \"%s\" }" % (self.id)

class Location:
	def __init__(self, xCoord, yCoord):
		self.xCoord = xCoord
		self.yCoord = yCoord

	def toJSON():
		xCoord = "\"x-coord\" \"%s\"" % (self.xCoord)	
		yCoord = "\"y-coord\" \"%s\"" % (self.yCoord)	
		return "{ %s, %s }" % (xCoord, yCoord)	

class Node:
	def __init__(self, identifier, index, point, interfaces):
		self.id = identifier
		self.index = index
		self.point = point
		self.interfaces = interfaces

	def getCommonJSON(self):
		nodeId = "\"node_id\" : \"%s\"" % self.id
		nodePoint = self.point.toJSON()
		interfaces =

	def toJSON(self):
		return ""

class Consumer(Node):
	def __init__(self, identifier, index, point, interfaces):
		super(identifier, index, point, interfaces)

	def toJSON(self):
		nodeId = "\"node_id\" : \"%s\"" % self.id
		nodeIndex = 
		return "{  }"

class Router(Node):
	def __init__(self, identifier, index, point, interfaces):
		super(identifier, index, point, interfaces)

	def toJSON(self):
		return ""

class Producer(Node):
	def __init__(self, id, index, point, interfaces):
		super(identifier, index, point, interfaces)

	def toJSON(self):
		return "{ }"

def createChannels():
	return None

def createNodes(graph, consumerIndices, producerIndices, routerIndices):
	return None

def createConnections(graph):
	return None

def main(argv):
	G1 = nx.balanced_tree(2, 2)
	G2 = nx.balanced_tree(2, 2)

	# G = nx.cartesian_product(G1, G2)
	# G = nx.union(G1, G2)
	G = nx.strong_product(G1, G2)

	nx.draw(G)
	plt.show(G)

if __name__ == "__main__":
	main(sys.argv)