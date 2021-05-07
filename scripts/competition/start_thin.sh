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
# Starts a magmaOffenburg RoboCup thin client
# example: bash start.sh localhost
###########################################

if [ $# -ne 1 ]; then
	echo "Usage: $0 <Server IP>"
	exit 1
fi

mkdir -p log
today=`date +%Y-%m-%d-%H-%M-%S`
for ((i=1; i<=11; i++)); do
	java -cp "lib/*" magma.robots.RoboCupClient --teamname=releaseTeam --playerid=$i --server=$1 --port=3110 --thinClient 1>log/outAndError$today.log 2>&1 &
	sleep 1
done
