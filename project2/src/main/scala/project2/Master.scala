package project2

import akka.actor._

/**
 * Created by chelsea on 9/15/15.
 */
class Master(topology: Topology, alg: String, numOfNodes: String) extends Actor {

  var startTime: Long = 0

  def receive = {
    /*case CreateActors => {
      for (i <- 0 to numOfNodes.toInt) {
        // Create the actors
        //context.actorOf(Props(new ))
        startTime = System.currentTimeMillis()
        alg match {
          case Gossip => {
            //context.
          }*/
          case PushSum(s,w) => {

          }
        //}
      //}
    //}
  }

}
