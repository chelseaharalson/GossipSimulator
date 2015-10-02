Chelsea Metcalf

PROJECT 2

Team Members: No partner.

------------------ USAGE & SOURCE CODE ------------------
I compiled the program into a jar using the assembly plugin.
java -jar project2.jar <numOfNodes> <topology> <algorithm>
Run Example: java -jar project2.jar 27 line gossip
Bonus: java -jar project2-bonus.jar 27 full push-sum

Likewise, the project could also be run as:
sbt "project project2" "run 27 3D gossip"
sbt "project project2-bonus" "run 125 imp3D push-sum"

What is working: Gossip and Push Sum for failure and no failure in all topologies.

Code Structure:
Main sends the message to CreateActors in the master. The master creates the amount of actors that there are nodes, which is passed in from Main. The master asks for the progress of each worker. The building of the topology takes place in the worker, but I put the code to generate the cube in a new class called Topology. The worker gets the next random node and sends its progress to the master. If the value is a 1, then it means it has finished. If the value is a 0, then the message is still sending. The master receives SendProgress and once the finished count is equivalent to the number of nodes, the system terminates.

----------------------------------------------------------------------------------------


------------------ LARGEST NETWORK ------------------
Gossip:
---------
Line: 400000
3D: 3375
imp3D: 3375
Full: 10000

Push Sum:
---------
Line: 400000
3D: 3375
imp3D: 3375
Full: 100000

The reason why the cube network is smaller is because of the inefficiency of my code. Currently, a new cube is generated each time, so it takes a long time. However, in my old version of the code (found in the directory project2oldVersion), I had it more efficient where the cube was not generated each round.

----------------------------------------------------------------------------------------