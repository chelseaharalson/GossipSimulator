package project2

import akka.actor._
import scala.math._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Master(numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var startTime: Long = 0
  var finishedCount: Int = 0

  def receive = {
    case msg: String => {
      if (msg.equals("CreateActors")) {
        //println("It works!!!")
        var numNodes: Int = numOfNodes
        if (top == 1 || top == 3) {
          var numNodes = pow(numOfNodes,3).toInt
        }
        for (i <- 0 to numNodes) {
          // Create the actors
          context.actorOf(Props(new Worker(i, numNodes, top, alg)), i.toString)
        }
        // Starting time for the algorithm..
        startTime = System.currentTimeMillis()
        alg match {
          // Gossip
          case 0 => {
            var randomNode = Random.nextInt(numNodes).toString()
            var message = "The TA is annoying"
            context.actorSelection(randomNode) ! Rumor(message)
          }
          // PushSum
          case 1 => {

          }
        }
      }
    }
    case Finish() => {
      finishedCount = finishedCount + 1
      //println("Finished Count: " + finishedCount + "  Num of Nodes: " + numOfNodes)
      if (numOfNodes-1 == finishedCount) {
        System.exit(0)
      }
    }
  }
}