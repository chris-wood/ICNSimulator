#!/usr/bin/python

import sys
import getopt
import json
import random


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

    maxXCoord= 0
    maxYCoord = 0

    minRoutersLinks = 0;
    maxRoutersLinks = 0;
    minConsumersLinks = 0;
    maxConsumersLinks = 0;
    minProducersLinks = 0;
    maxProducersLinks = 0;

    minChannelRate = 0;
    maxChannelRate = 0;

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

    if "dimensions" in config:
        maxXCoord = config["dimensions"].split("x")[0]
        maxYCoord = config["dimensions"].split("x")[1]
    else:
        print "Missing dimensions in config"
        sys.exit(1)

    if "routers-links" in config:
        minRoutersLinks = config["routers-links"].split("-")[0]
        maxRoutersLinks = config["routers-links"].split("-")[1]
    else:
        print "Missing number of routers links in config"
        sys.exit(1)

    if "consumers-links" in config:
        minConsumersLinks = config["consumers-links"].split("-")[0]
        maxConsumersLinks = config["consumers-links"].split("-")[1]
    else:
        print "Missing number of consumers links in config"
        sys.exit(1)

    if "producers-links" in config:
        minProducersLinks = config["producers-links"].split("-")[0]
        maxProducersLinks = config["producers-links"].split("-")[1]
    else:
        print "Missing number of producers links in config"
        sys.exit(1)

    if "channel-rate" in config:
        minChannelRate = config["channel-rate"].split("-")[0]
        maxChannelRate = config["channel-rate"].split("-")[1]
    else:
        print "Missing channel rates in config"
        sys.exit(1)

    r_startindex = 1
    r_endindex = routers
    c_startindex = routers + 1
    c_endindex = routers + consumers
    p_startindex = routers + consumers + 1
    p_endindex = routers + consumers + producers

    topology = {"nodes": [], "channels": [], "connections": []}

    routersDict = {}
    for r in range(r_startindex, r_endindex + 1):
        xCoord = round(random.uniform(0, maxXCoord), 2)
        yCoord = round(random.uniform(0, maxYCoord), 2)
        topology["nodes"].append({
            "node_id": "node" + str(r),
            "node_type": "router",
            "x-coord": str(xCoord),
            "y-coord": str(yCoord),
            "interfaces": [{"interface_id": "interface1"}]})
        routersDict[r] = [xCoord, yCoord]

    for r in range(r_startindex, r_endindex + 1):
        nearest = nearestN(routersDict, r, 
                           random.randint(minRoutersLinks,
                                          maxRoutersLinks))
        for i in range(0, len(nearest)):
            topology["connections"].append({
                "source_id": "node" + str(r),
                "source_interface": "interface1",
                "destination_id": "node" + str(nearest[i]),
                "destination_interface": "interface1"})

    # HERE

    for c in range(c_startindex, c_endindex + 1):
        topology["nodes"].append({
            "node_id": "node" + str(c),
            "node_type": "consumer",
            "x-coord": "0",
            "y-coord": "0",
            "interfaces": [{"interface_id": "interface1"}]})
        topology["connections"].append({
            "source_id": "node" + str(c),
            "source_interface": "interface1",
            "destination_id": "node" + str(
                random.randint(r_startindex, r_endindex)),
            "destination_interface": "interface1"})


    for p in range(p_startindex, p_endindex + 1):
        topology["nodes"].append({
            "node_id": "node" + str(p),
            "node_type": "producer",
            "x-coord": "0",
            "y-coord": "0",
            "interfaces": [{"interface_id": "interface1"}]})
        topology["connections"].append({
            "source_id": "node" + str(p),
            "source_interface": "interface1",
            "destination_id": "node" + str(
                random.randint(r_startindex, r_endindex)),
            "destination_interface": "interface1"})

    l = 0
    while l < num_of_links:
        src = random.randint(r_startindex, r_endindex)
        dst = random.randint(r_startindex, r_endindex)
        if src != dst:
            topology["connections"].append({
                "source_id": "node" + str(src),
                "source_interface": "interface1",
                "destination_id": "node" + str(dst),
                "destination_interface": "interface1"})
            l = l + 1

    with open(output_file, "w") as ofile:
        json.dump(topology, ofile, sort_keys=True,
                  indent=4, separators=(',', ': '))


def nearestN(routersDict, index, N):
    distances = []
    for r in routersDict:
        if r != index:
            distances.append(
                math.hypot(
                    routersDict[index][0] - routersDict[r][0],
                    routersDict[index][1] - routersDict[r][1]))

    distances.sort()
    return distances[:N]

if __name__ == "__main__":
    main(sys.argv)
