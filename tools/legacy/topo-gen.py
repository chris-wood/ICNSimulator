#!/usr/bin/python

import sys
import getopt
import json
import random
import math
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages
from collections import deque


def usage(tool_name):
    print ""
    print "usage: " + tool_name + " -c <configFile> -o <outputFile>"
    print ""


def main(argv):
    configFile = ''
    outputFile = ''

    routers = 0
    consumers = 0
    producers = 0
    channels = 0

    maxXCoord= 0
    maxYCoord = 0

    minRoutersLinks = 0
    maxRoutersLinks = 0
    minConsumersLinks = 0
    maxConsumersLinks = 0
    minProducersLinks = 0
    maxProducersLinks = 0

    channelRates = []

    topologies = 1
    plotTopology = False

    try:
        opts, args = getopt.getopt(argv[1:],
                                   "hc:o:",
                                   ["configFile=",
                                    "outputFile="])
    except getopt.GetoptError:
        usage(argv[0])
        sys.exit(1)

    for opt, arg in opts:
        if opt in ("-h", "--help"):
            usage(argv[0])
            sys.exit()
        elif opt in ("-c", "--configFile"):
            configFile = arg
        elif opt in ("-o", "--outputFile"):
            outputFile = arg

    if configFile == '':
        print "Must specify config file"
        sys.exit(1)

    if outputFile == '':
        print "Must specify output file"
        sys.exit(1)

    with open(configFile, 'r') as cFile:
        config = json.load(cFile)

    if "routers" in config:
        routers = int(config["routers"])
    else:
        print "Missing number of routers in config"
        sys.exit(1)

    if "consumers" in config:
        consumers = int(config["consumers"])
    else:
        print "Missing number of consumers in config"
        sys.exit(1)

    if "producers" in config:
        producers = int(config["producers"])
    else:
        print "Missing number of producers in config"
        sys.exit(1)

    if "channels" in config:
        channels = int(config["channels"])
    else:
        print "Missing number of channels in config"
        sys.exit(1)

    if "dimensions" in config:
        maxXCoord = int(config["dimensions"].split("x")[0])
        maxYCoord = int(config["dimensions"].split("x")[1])
    else:
        print "Missing dimensions in config"
        sys.exit(1)

    if "routers-links" in config:
        if "-" in config["routers-links"]:
            minRoutersLinks = int(config["routers-links"].split("-")[0])
            maxRoutersLinks = int(config["routers-links"].split("-")[1])
        else:
            minRoutersLinks = int(config["routers-links"])
            maxRoutersLinks = int(config["routers-links"])
    else:
        print "Missing number of routers links in config"
        sys.exit(1)

    if "consumers-links" in config:
        if "-" in config["consumers-links"]:
            minConsumersLinks = int(
                config["consumers-links"].split("-")[0])
            maxConsumersLinks = int(
                config["consumers-links"].split("-")[1])
        else:
            minConsumersLinks = int(config["consumers-links"])
            maxConsumersLinks = int(config["consumers-links"])
    else:
        print "Missing number of consumers links in config"
        sys.exit(1)

    if "producers-links" in config:
        if "-" in config["producers-links"]:
            minProducersLinks = int(
                config["producers-links"].split("-")[0])
            maxProducersLinks = int(
                config["producers-links"].split("-")[1])
        else:
            minProducersLinks = int(config["producers-links"])
            maxProducersLinks = int(config["producers-links"])
    else:
        print "Missing number of producers links in config"
        sys.exit(1)

    if "channel-rates" in config:
        channelRates = config["channel-rates"].split(",")
    else:
        print "Missing channel rates in config"
        sys.exit(1)

    if "topologies" in config:
        topologies = int(config["topologies"])

    if "plot" in config:
        if config["plot"] in ("True", "true"):
            plotTopology = True
        elif config["plot"] in ("False", "false"):
            plotTopology = False
        else:
            print "Invalid plot value (should be True/False)"
            sys.exit(1)

    r_startindex = 1
    r_endindex = routers
    c_startindex = routers + 1
    c_endindex = routers + consumers
    p_startindex = routers + consumers + 1
    p_endindex = routers + consumers + producers

    fn = 1
    while fn < topologies + 1:
        topology = {"nodes": [], "channels": [], "connections": []}

        # Add channels
        for ch in range(1, channels + 1):
            topology["channels"].append({
                "channel_id": "channel" + str(ch),
                "data_rate": str(channelRates[random.randint(
                    0, len(channelRates) - 1)])})

        # Add routers
        nodes = {}
        connections = []
        routersDict = {}
        routersConnections = []
        for r in range(r_startindex, r_endindex + 1):
            xCoord = round(random.uniform(0 + 0.1 * maxXCoord, 
                                          maxXCoord - 0.1 * maxYCoord), 
                           2)
            yCoord = round(random.uniform(0 + 0.1 * maxYCoord, 
                                          maxYCoord - 0.1 * maxYCoord), 
                           2)
            topology["nodes"].append({
                "node_id": "node" + str(r),
                "node_type": "router",
                "x-coord": str(xCoord),
                "y-coord": str(yCoord),
                "interfaces": [{"interface_id": "interface1"}]})
            routersDict[r] = [xCoord, yCoord]
            nodes[r] = [xCoord, yCoord, "router"]

        # Connect each routers to nearest N neighbors
        for r in range(r_startindex, r_endindex + 1):
            nearest = nearestN(routersDict, r, 
                               routersDict[r][0],
                               routersDict[r][1],
                               random.randint(minRoutersLinks,
                                              maxRoutersLinks))
            for i in range(0, len(nearest)):
                tmp = str(r) + "-" + str(nearest[i][0])
                if tmp not in routersConnections:
                    topology["connections"].append({
                        "source_id": "node" + str(r),
                        "source_interface": "interface1",
                        "destination_id": "node" + str(nearest[i][0]),
                        "destination_interface": "interface1",
                        "channel_id": "channel" + str(random.randint(
                            1, channels))})
                    routersConnections.append(tmp)
                    connections.append([r, nearest[i][0]])

        # Calculate the area where routers are located
        routerMinX = routersDict[r_startindex][0]
        routerMaxX = routersDict[r_startindex][0]
        routerMinY = routersDict[r_startindex][1]
        routerMaxY = routersDict[r_startindex][1]
        for r in range(r_startindex + 1, r_endindex + 1):
            if routersDict[r][0] < routerMinX:
                routerMinX = routersDict[r][0]
            if routersDict[r][0] > routerMaxX:
                routerMaxX = routersDict[r][0]
            if routersDict[r][1] < routerMinY:
                routerMinY = routersDict[r][1]
            if routersDict[r][1] > routerMaxY:
                routerMaxY = routersDict[r][1]

        routersAreaX = routerMaxX - routerMinX
        routersAreaY = routerMaxY - routerMinY

        # Add consumers
        for c in range(c_startindex, c_endindex + 1):
            xCoord = round(random.uniform(0,
                                          routerMinX + maxXCoord - 
                                          routerMaxX), 2)
            yCoord = round(random.uniform(0,
                                          routerMinY + maxYCoord - 
                                          routerMaxY), 2)
            if xCoord > routerMinX:
                xCoord = xCoord + routersAreaX
            if yCoord > routerMinY:
                yCoord = yCoord + routersAreaY
            topology["nodes"].append({
                "node_id": "node" + str(c),
                "node_type": "consumer",
                "x-coord": str(xCoord),
                "y-coord": str(yCoord),
                "interfaces": [{"interface_id": "interface1"}]})
            nodes[c] = [xCoord, yCoord, "consumer"]

            # Connect consumer to nearest N routers
            nearest = nearestN(routersDict, -1,
                               xCoord, yCoord,
                               random.randint(minConsumersLinks,
                                              maxConsumersLinks))
            for i in range(0, len(nearest)):
                topology["connections"].append({
                    "source_id": "node" + str(c),
                    "source_interface": "interface1",
                    "destination_id": "node" + str(nearest[i][0]),
                    "destination_interface": "interface1",
                    "channel_id": "channel" + str(random.randint(
                        1, channels))})
                connections.append([nearest[i][0], c])

        # Add producers
        for p in range(p_startindex, p_endindex + 1):
            xCoord = round(random.uniform(0,
                                          routerMinX + maxXCoord - 
                                          routerMaxX), 2)
            yCoord = round(random.uniform(0,
                                          routerMinY + maxYCoord - 
                                          routerMaxY), 2)
            if xCoord > routerMinX:
                xCoord = xCoord + routersAreaX
            if yCoord > routerMinY:
                yCoord = yCoord + routersAreaY
            topology["nodes"].append({
                "node_id": "node" + str(p),
                "node_type": "producer",
                "x-coord": str(xCoord),
                "y-coord": str(yCoord),
                "interfaces": [{"interface_id": "interface1"}]})
            nodes[p] = [xCoord, yCoord, "producer"]

            # Connect producer to nearest N routers
            nearest = nearestN(routersDict, -1,
                               xCoord, yCoord,
                               random.randint(minProducersLinks,
                                              maxProducersLinks))
            for i in range(0, len(nearest)):
                topology["connections"].append({
                    "source_id": "node" + str(p),
                    "source_interface": "interface1",
                    "destination_id": "node" + str(nearest[i][0]),
                    "destination_interface": "interface1",
                    "channel_id": "channel" + str(random.randint(
                        1, channels))})
                connections.append([nearest[i][0], p])

        # Verify that the graph has a single component
        if not isConnected(nodes, connections):
            continue

        # Write out json file
        with open(outputFile + "_" + str(fn) + ".json", "w") as ofile:
            json.dump(topology, ofile, sort_keys=True,
                      indent=4, separators=(',', ': '))

        # Plot to PDF
        if plotTopology:
            fig = plt.figure()
            splt = fig.add_subplot(1, 1, 1)
            # Plotting all connections
            for conn in connections:
                x = [nodes[conn[0]][0],
                     nodes[conn[1]][0]]
                y = [nodes[conn[0]][1],
                     nodes[conn[1]][1]]
                splt.plot(x, y, '-k')

            # Plotting nodes
            for n in nodes:
                faceColor = ""
                if nodes[n][2] == "router":
                     faceColor = "red"
                elif nodes[n][2] == "consumer":
                     faceColor = "blue"
                elif nodes[n][2] == "producer":
                     faceColor = "green"

                splt.plot(nodes[n][0], nodes[n][1], "or", 
                          ms=10, mfc=faceColor, 
                          mec="black", mew=2)

            splt.set_xlim([-.5, maxXCoord + .5])
            splt.set_ylim([-.5, maxYCoord + .5])
            splt.axes.get_xaxis().set_visible(False)
            splt.axes.get_yaxis().set_visible(False)
            splt.grid()
            pdf = PdfPages(outputFile + "_" + str(fn) + ".pdf")
            pdf.savefig(fig)
            pdf.close()

            fn = fn + 1


def nearestN(routersDict, index, xCoord, yCoord, N):
    distances = []
    for r in routersDict:
        if r != index:
            distances.append([r,
                              math.hypot(
                                  routersDict[r][0] - xCoord,
                                  routersDict[r][1] - yCoord)])

    distances.sort(key=lambda x: x[1])
    return distances[:N]


def isConnected(vertices, connections):
    edges = {}
    for conn in connections:

        if conn[0] not in edges:
            edges[conn[0]] = []
        edges[conn[0]].append(conn[1])

        if conn[1] not in edges:
            edges[conn[1]] = []
        edges[conn[1]].append(conn[0])

    colors = {}
    for v in vertices:
        colors[v] = "w"

    colors[1] = "g"
    count = 0
    queue = deque([1])
    while len(queue) is not 0:
        u = queue.popleft()
        for v in edges[u]:
            if colors[v] == "w":
                colors[v] = "g"
                queue.append(v)
        colors[u] = "b"
        count = count + 1

    if count == len(vertices):
        return True
    else:
        return False

if __name__ == "__main__":
    main(sys.argv)
