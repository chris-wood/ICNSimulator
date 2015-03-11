#! /usr/bin/env python

import os
import sys
import argparse
import subprocess
from subprocess import call
from subprocess import check_output

class Simulator(object):
	def __init__(self, simulatorPath):
		self.simulatorPath = simulatorPath
		self.tasks = []

	def addTask(self, task):
		self.tasks.append(task)

	def run(self):
		for task in self.tasks:
			task.run(self.simulatorPath)

class SimulationTask(object):
	def __init__(self, targetPath):
		self.targetPath = targetPath

	def run(self, simulatorPath):
		executionCommand = 'java ' + simulatorPath + ' ' + self.targetPath
		print >> sys.stderr, "Executing: " + executionCommand
		status = call(executionCommand)
		if status != 0:
			raise Exception("Error: target " + self.targetPath + " failed with status code: " + str(status))

def main(args):
	simulator = Simulator(args.simulator)

	path = args.path
	for root, dirs, files in os.walk(path):
		for name in files:
			if name.endswith(".json"):
				task = SimulationTask(name)
				simulator.addTask(task)

	simulator.run()

if __name__ == "__main__":
	desc = "Run all ICN simulator experiments prescribed by the configuration files contained in the target directory."
	parser = argparse.ArgumentParser(prog='run-simulations', formatter_class=argparse.RawDescriptionHelpFormatter, description=desc)
	parser.add_argument('-p', '--path', default='.', action="store", help="Path to the directory containing the configuration files")
	parser.add_argument('-s', '--simulator', default='Simulator', action="store", help="Path to the main simulator file")

	args = parser.parse_args()
	main(args)
