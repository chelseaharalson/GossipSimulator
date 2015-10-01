package project2

import akka.actor._
import scala.math._
import scala.util.Random
import scala.concurrent.duration._
import scala.collection.mutable.ArrayBuffer

/**
 * Created by chelsea on 9/15/15.
 */
class Master(numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var startTime: Long = 0
  var finishedCount: Int = 0
  var numNodes: Int = 0
  var nodeList = new ArrayBuffer[Int]()

  def receive = {
    case msg: String => {
      if (msg.equals("CreateActors")) {
        //println("It works!!!")
        numNodes = numOfNodes
        if (top == 1 || top == 3) {
          //println("num nodes: " + numNodes)
          numNodes = getCubeSize(numOfNodes)
        }
        for (i <- 0 until numNodes) {
          // Create the actors
          //println("Actors: " + i)
          context.actorOf(Props(new Worker(i, numNodes, top, alg)), i.toString)
        }


      if (top == 0 || top == 2) {
        for (i <- 0 until numNodes) {
          nodeList.+=(i)
        }
        //println("NODE LIST: " + nodeList)
      }

        /*println("0: " + getRandomLine(0,nodeList))
        println("7: " + getRandomLine(7,nodeList))
        nodeList.remove(3)
        println("2: " + getRandomLine(2,nodeList))
        println("4: " + getRandomLine(2,nodeList))
        println("6: " + getRandomLine(2,nodeList))
        println("NODE LIST: " + nodeList)*/








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
      println("Finished Count: " + finishedCount + "  Num Finished: " + idx + "   Num of Nodes: " + numNodes)
      nodeList.remove(idx)
      println("NODELIST: " + nodeList)
      if (numNodes == finishedCount) {
        val b = System.currentTimeMillis - startTime
        println("Convergence Time: " + b.millis)
        System.exit(0)
      }
      /*else {
        if (top == 2) {
          val randomNode = getRandomLine(idx, nodeList)
          println("Index: " + idx + " Random: " + randomNode)
          //println("NODELIST: " + nodeList)
          context.actorSelection(idx.toString()) ! SendToWorker(nodeList(randomNode))
        }
      }*/
    }
    case FinishPushSum(idx,s,w) => {
      finishedCount = finishedCount + 1
      //println("Finished Count: " + finishedCount + "  Num of Nodes: " + numNodes)
      //println("Index: " + idx + "   s: " + s + "   w: " + w)
      if (numNodes == finishedCount) {
        val b = System.currentTimeMillis - startTime
        println("Convergence Time: " + b.millis)
        System.exit(0)
      }
    }
    case GetRandomNumber(idx) => {
      if (top == 2) {
        val randomNode = getRandomLine(idx, nodeList)
        //println("Index: " + idx + "  Random: " + nodeList(randomNode))
        //println("NODELIST: " + nodeList)
        context.actorSelection(idx.toString()) ! SendToWorker(nodeList(randomNode))
      }
    }
  }

  def getRandomLine(iName: Int, aList: ArrayBuffer[Int]): Int = {
    var r = 0
    val lr = Random.nextInt(2)
    val idx = aList.indexOf(iName)
    println("iName: " + iName + "   idx: " + idx)
    if (idx == 0) {
      r = 1
      println("First" + r + " Index: " + idx)
    }
    else if (idx == (aList.size-1)) {
      r = idx - 1
      println("Last: " + r + " Index: " + idx)
    }
    else if (lr == 0) {
      r = idx - 1
      println("Left: " + r + " Index: " + idx)
    }
    else if (lr == 1) {
      r = idx + 1
      println("Right: " + r + " Index: " + idx)
    }
    println("ALIST:           " + aList(r))
    aList(r)
  }

  // Gets smallest cube size
  def getCubeSize(numOfNodes: Int): Int = {
    var factor: Int = 3
    while (numOfNodes > pow(factor,3)) {
      factor = factor + 1
    }
    val p = pow(factor,3)
    p.toInt
  }
}