#!/bin/bash
###########################################
# Kills all magmaOffenburg RoboCup clients
# example: bash kill.sh
###########################################

kill `ps -ef | grep 'magma.robots.RoboCupClient' | grep -v grep | awk '{print $2}'`
