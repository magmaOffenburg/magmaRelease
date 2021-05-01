# magmaOffenburg RoboCup 3D simulation code

This software is released under the GPL. See file [gpl.txt](gpl.txt)
 for more information.
If you use this software please acknowledge it appropriately.

A tutorial is available on YouTube describing the basics of working with the magmaOffenburg source code:
https://www.youtube.com/watch?v=_FNaMjJlNfI

## Setup Instructions

Clone the repository into a workspace folder of your choice.

```bash
git clone <address from github>
```


### Eclipse
#### Import Project
1. Open Eclipse using the workspace folder (not the RoboCup3D folder but the one above).
1. In Project Explorer View right click -> Import... -> Choose Maven -> Existing Maven Projects... Next
1. Select RoboCup3D as Root Directory
1. Check that all projects are selected -> Finish.
1. (optional) In Project Explorer View (not the package explorer!) click on View Menu (triangle) -> Choose Projects Presentation -> hierarchical

#### Start Single Player
First make sure that an instance of rcssserver3d is running on your local machine.
There is already an existing launch target:

1. Chose Menu->Run::RunConfigurations...
1. Select Java Application->RoboCup Client (8)
1. Press Run Button

To change the player or robot type, adjust the command line parameters:
  --playerid=8 --factory=NaoToe
  For a list of all command line parameters see class `RoboCupClient`.

#### Deploy Team
Make a deployment:

1. Chose Menu->Run::RunConfigurations...
1. Select Maven Build->Package magmaagent
1. Press Run Button

This will create a folder target/magmaagent. Copy the folder with subfolders to the server you want to run your team.

### IntelliJ
#### Import Project
1. Start IntelliJ to get to the welcome screen
1. (optional) If a project is already open: Chose "File" -> "Close Project"
1. In the welcome screen select "Open" in the "Project" category
1. Select the RoboCup3D folder, press "OK" and then wait a few seconds for IntelliJ to initialize the project

#### Start Single Player
First make sure that an instance of rcssserver3d is running on your local machine.
1. Chose "Run" -> "Edit Configurations..."
1. Press the plus button ("Add New Configuration") -> select "Application"
1. Chose a fitting name for the launch configuration, for example "RoboCupClient"
1. (optional) For newer IntelliJ versions: Press "Modify options" -> unselect in the category "Java" -> "Use classpath of module"
1. Select your JDK in the module dropdown
1. Chose "magma.robots.RoboCupClient" as the main class
1. Use the following as "program arguments" -> --playerid=8 --factory=NaoToe
1. Press "OK" and then press the green run button

For a list of all command line parameters ("program arguments") see class `RoboCupClient`.

#### Deploy Team
1. Press "File" -> "Settings..." -> "Build, Execution, Deployment" -> "Build Tools" -> "Maven" -> "Runner" -> Make sure "Delegate IDE build/run actions to Maven" is selected
1. Press "OK" and then press the green build button

This will create the target folder for magmaagent (full path RoboCup3D/magmaagent/target/magmaagent). Copy the folder with subfolders to the server you want to run your team.

### Command Line

Building the entire project:

```bash
./mvnw package
```

Building only the agent module along with its dependencies:

```bash
./mvnw package -pl magmaagent -am
```

## Start team

```bash
cd magmaagent/target/magmaagent
bash start.sh <serverIP>
```

### Main class

```java
// magmaagent module
magma.robots.RoboCupClient
```

## Further Information

### Architecture

First level architecture:

 ![](base/uml/images/Architecture_Firstlevel.jpg)

Second level architecture:

 ![](base/uml/images/Architecture_Secondlevel.jpg)


### Magma Team URL

[http://robocup.hs-offenburg.de](http://robocup.hs-offenburg.de)

### Authors
Ester Amelia, Maximilian Baritz, Martin Baur, Nico Bohlinger, Hannes Braun, Kim Christmann, Alexander Derr, 
Klaus Dorer, Mathias Ehret, Sebastian Eppinger, Jens Fischer, 
Camilo Gelvez, Stefan Glaser, Stefan Grossmann, Marcel Gruessinger, Julian Hohenoecker, Danny Huber, Thomas Huber, 
Stephan Kammerer, Fabian Korak, Maximilian Kroeg, Pascal Liegibel, Duy Nguyen, 
Simon Raffeiner, Srinivasa Ragavan, Thomas Rinklin, Bjoern Ritter, 
Mahdi Sadeghi, Joachim Schilling, Rico Schillings, Ingo Schindler, Carmen Schmider, Frederik Sdun, Jannik Seiler, Rajit Shahi, 
Bjoern Weiler, David Weiler, David Zimmermann, Denis Zimmermann

### Contact

klaus dot dorer at hs-offenburg dot de
