#!/bin/bash
#
# Sample start script for 2013 3D Simulation Drop-In Player Challenge
#


AGENT_BINARY=""
BINARY_DIR="."


usage()
{
	(echo "Usage: $0 [options]"
	 echo "Available options:"
	 echo "  --help                       prints this"
	 echo "  HOST                         specifies server host"
	 echo "  -t, --team TEAMNAME          specifies team name"
	 echo "  -u, --unum UNUM              specifies uniform number"
	 echo "  -k, --key KEY                64-bit int key for say message encryption") 1>&2
}


fParsedHost=false

while [ $# -gt 0 ]
do
	case $1 in

		--help)
			usage
			exit 0
			;;

		-t|--team)
			if [ $# -lt 2 ]; then
				usage
				exit 1
			fi
			team="${2}"
			shift 1
			;;
		
		-u|--unum)
			if [ $# -lt 2 ]; then
				usage
				exit 1
			fi
			i="${2}"
			shift 1
			;;
		 
		-k|--key)
			if [ $# -lt 2 ]; then
				usage
				exit 1
			fi
			key="${2}"
			shift 1
			;;
		*)
			if $fParsedHost;
			then
				echo 1>&2
				echo "invalid option \"${1}\"." 1>&2
				echo 1>&2
				usage
				exit 1
			else
				host="${1}"
	fParsedHost=true
			fi
			;;
	esac

	shift 1
done



echo "Running agent No. $i"
java -cp "lib/*" magma.robots.RoboCupClient --teamname=$team --playerid=$i --server=$host &> /dev/null &

sleep 1
