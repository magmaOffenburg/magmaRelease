#!/bin/bash
RB_FILE=/usr/local/share/rcssserver3d/naosoccersim.rb
[ -f $RB_FILE ] || RB_FILE=/usr/share/rcssserver3d/naosoccersim.rb
sudo sed -i "s/\(addSoccerVar(.MinRobotTypesCount.\).*)/\1, 1)/" $RB_FILE
sudo sed -i "s/\(addSoccerVar(.PenaltyShootout.\).*)/\1, true)/" $RB_FILE
sudo sed -i "s/\(addSoccerVar(.SingleHalfTime.\).*)/\1, true)/" $RB_FILE
sudo sed -i "s/\(addSoccerVar(.RuleHalfTime.\).*)/\1, 40)/" $RB_FILE
killall -9 rcssserver3d
