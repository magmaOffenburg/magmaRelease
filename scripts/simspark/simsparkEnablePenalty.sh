#!/bin/bash
RB_FILE=/usr/local/share/rcssserver3d/naosoccersim.rb
[ -f $RB_FILE ] || RB_FILE=/usr/share/rcssserver3d/naosoccersim.rb
sudo sed -i "s/\(addSoccerVar(.PenaltyShootout'\).*)/\1, true)/" $RB_FILE
killall -9 rcssserver3d
