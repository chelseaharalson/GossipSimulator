package project2

import akka.actor._
import scala.util.Random

/**
 * Created by chelsea on 9/15/15.
 */
class Master(numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var startTime: Long = 0
  var finishedCount: Int = 0
  var numNodes: Int = 0

  def receive = {
    case msg: String => {
      if (msg.equals("CreateActors")) {
        //println("It works!!!")
        numNodes = numOfNodes
        if (top == 1 || top == 3) {
          val c = new Cube()
          numNodes = c.getCubeSize(numOfNodes)
          //println("num nodes: " + numNodes)
        }
        for (i <- 0 until numNodes) {
          // Create the actors
          //println("Actors: " + i)
          context.actorOf(Props(new Worker(i, numNodes, top, alg)), i.toString)
        }
        // Starting time for the algorithm..
        startTime = System.currentTimeMillis()
        alg match {
          // Gossip
          case 0 => {
            val randomNode = Random.nextInt(numNodes).toString()
            //println("NEXT RANDOM NODE: " + randomNode)
            val message = "hi"
            context.actorSelection(randomNode) ! Rumor(message)
          }
          // PushSum
          case 1 => {
            val randomNode = Random.nextInt(numNodes).toString()
            context.actorSelection(randomNode) ! PushSum(randomNode.toDouble,0)
          }
        }
      }
    }
    case FinishGossip(idx) => {
      //println("FINISHED INDEX: " + i)
      finishedCount = finishedCount + 1
      //println("Finished Count: " + finishedCount + "  Index: " + idx + "   Num of Nodes: " + numNodes)
      if (numNodes == finishedCount) {
        val b = System.currentTimeMillis - startTime
        println("Convergence Time: " + b + " ms")
        System.exit(0)
      }
    }
    case FinishPushSum(idx,s,w) => {
      finishedCount = finishedCount + 1
      //println("Finished Count: " + finishedCount + "  Num of Nodes: " + numNodes)
      //println("Index: " + idx + "   s: " + s + "   w: " + w)
      if (numNodes == finishedCount) {
        val b = System.currentTimeMillis - startTime
        println("Convergence Time: " + b + " ms")
        System.exit(0)
      }
    }
  }
}