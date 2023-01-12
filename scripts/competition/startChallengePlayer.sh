#*******************************************************************************
# Copyright 2008, 2011 Hochschule Offenburg
# Klaus Dorer, Mathias Ehret, Stefan Glaser, Thomas Huber, Fabian Korak,
# Simon Raffeiner, Srinivasa Ragavan, Thomas Rinklin,
# Joachim Schilling, Ingo Schindler, Rajit Shahi, Bjoern Weiler
#
# This file is part of magmaOffenburg.
#
# magmaOffenburg is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# magmaOffenburg is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with magmaOffenburg. If not, see <http://www.gnu.org/licenses/>.
#*******************************************************************************
#!/bin/bash
###########################################
# Starts a magmaOffenburg RoboCup client for the magma challenge tool
# example: bash startChallengePlayer.sh localhost 3100 0 0 KickChallenge
###########################################

if [ $# -ne 5 ]; then
	echo "Usage: $0 <Server IP> <Server Port> <Start Pos X> <Start Pos Y> <Challenge Name>"
	exit 1
fi

SERVER_IP=$1
SERVER_PORT=$2
PLAYER_TYPE=NaoToe
DECISION_MAKER=$5

start()
{
	java -cp "lib/*" magma.robots.RoboCupClient --playerid=$1 --server=$SERVER_IP --port=$SERVER_PORT \
	--factory=$PLAYER_TYPE --decisionmaker=$DECISION_MAKER 1>outAndError.log 2>&1 &
}

if [ $DECISION_MAKER == "GazeboRunChallenge" ]; then
	PLAYER_TYPE=NaoGazebo
fi

if [ $DECISION_MAKER == "KeepAwayChallenge" ]; then
	start 8
	start 9
	start 10

elif [ $DECISION_MAKER == "PassingChallenge" ]; then
	start 7
	start 8
	start 9
	start 10
	
elif [ $DECISION_MAKER == "GoalieChallenge" ]; then
	DECISION_MAKER=PenaltyGoalie
	start 1
else
	start 8
fi
