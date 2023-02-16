#!/bin/bash
###########################################
# Starts the players for penalties.
# Example: bash start_penalty.sh localhost
###########################################

if [ $# -ne 1 ]; then
	echo "Usage: $0 <Server IP>"
	exit 1
fi

java -cp "lib/*" magma.robots.RoboCupClient --playerid=1 --server=$1 --factory=Penalty &
java -cp "lib/*" magma.robots.RoboCupClient --server=$1 --factory=Penalty &
