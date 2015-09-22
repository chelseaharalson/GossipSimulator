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
  var numNodes: Int = 0

  def receive = {
    case msg: String => {
      if (msg.equals("CreateActors")) {
        //println("It works!!!")
        numNodes = numOfNodes
        if (top == 1 || top == 3) {
          numNodes = pow(numOfNodes,3).toInt
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
            val randomNode = Random.nextInt(numNodes).toString()
            val message = "The TA is annoying"
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
    case FinishGossip() => {
      finishedCount = finishedCount + 1
      println("Finished Count: " + finishedCount + "  Num of Nodes: " + numNodes)
      //val n = numNodes-1
      if (numNodes == finishedCount) {
        val b = System.currentTimeMillis - startTime
        println("Time: " + b + " ms")
        System.exit(0)
      }
    }
    case FinishPushSum(s,w) => {
      finishedCount = finishedCount + 1
      println("Finished Count: " + finishedCount + "  Num of Nodes: " + numNodes)
      //val n = numNodes-1
      if (numNodes == finishedCount) {
        val b = System.currentTimeMillis - startTime
        println("Time: " + b + " ms")
        System.exit(0)
      }
    }
  }
}