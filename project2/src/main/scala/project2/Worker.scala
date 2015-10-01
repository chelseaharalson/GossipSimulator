package project2

import akka.actor._
import scala.util.Random
import scala.collection.mutable.ArrayBuffer

/**
 * Created by chelsea on 9/15/15.
 */
class Worker(nodeName: Int, numOfNodes: Int, top: Int, alg: Int) extends Actor {

  var numOfMessages: Int = 0
  var gossipTermination: Int = 10
  var numOfTimesSent: Int = 1
  var messageCounter: Int = 0
  var cycleCounter: Int = 0
  var s: Double = nodeName
  var w: Double = 1
  var pushSumTermination: Int = 3
  var pushSumFinished: Boolean = false
  var failureTest: Boolean = false
  var failureIndex: Int = 5
  var nodeList = new ArrayBuffer[Int]()

  val t = new Topology(numOfNodes)

  if (top == 0 || top == 2) {
    for (i <- 0 until numOfNodes) {
      nodeList.+=(i)
    }
  }
  

  def receive = {
    // Gossip Rumor
    case Rumor(message) => {
      numOfMessages = numOfMessages + 1
      if (numOfMessages <= gossipTermination) {
        t.topType = top

        val pos = nodeList.indexOf(nodeName)
        if (failureTest == false) {
          for (i <- 0 until numOfTimesSent) {
            val nextNode = getRandomLine()
            if (nextNode == -1) {
              context.parent ! FinishGossip(nodeName)
            }
            else {
              println("Node Name: " + pos + "   Next Node: " + nextNode)
              context.actorSelection("../" + nextNode.toString()) ! Rumor(message)
            }
          }
        }
        /*else {
          // Simulate failure
          val r = Random.nextInt(failureIndex)
          // Generate numbers 0 to 4
          // If r = 0, then FAILED
          // Else go on like normal..
          //println("r: " + r)
          if (r == 0) {
            println("FAILED CONNECTION!")
          }
          if (r > 0) {
            for (i <- 0 until numOfTimesSent) {
              val nextNode = t.findNode()
              //println("Failure at: " + t.idx)
              //println("Index: " + t.idx + "   Next Node: " + nextNode)
              context.actorSelection("../" + nextNode.toString()) ! Rumor(message)
            }
          }
        }*/
      }
      //println("Num Of Messages: " + numOfMessages + "   Index: " + idx)
      if (numOfMessages == gossipTermination) {
        //println("Index Finished: " + idx)
        context.parent ! FinishGossip(nodeName)
      }
      if (numOfMessages > gossipTermination) {
        context.sender() ! WorkerStopped(nodeName)
      }
    }
    case PushSum(ps,pw) => {
      if (pushSumFinished == false) {
        if (((((s + ps) / 2) / ((w + pw) / 2)) - (s / w)) < (10E-10)) {
          cycleCounter = cycleCounter + 1
        }
        else if (cycleCounter < 3) {
          cycleCounter = 0
        }
        if (cycleCounter < 3) {

          /*if (idx == 3) {
          println("Index: " + idx + "   s: " + s + "   ps: " + ps)
        }*/

          //println("Index: " + idx + "   s: " + s + "   ps: " + ps)

          s = (s + ps) / 2
          w = (w + pw) / 2

          t.topType = top
          for (i <- 0 to numOfTimesSent) {
            val nextNode = t.findNode()
            context.actorSelection("../" + nextNode.toString()) ! PushSum(s, w)
          }
        }
        else {
          pushSumFinished = true
        }
        if (cycleCounter == pushSumTermination) {
          context.parent ! FinishPushSum(nodeName, s, w)
        }
      }
    }
    case msg: String => {
      println(msg)
    }
    case WorkerStopped(nodeName) => {
      val pos = nodeList.indexOf(nodeName)
      nodeList.remove(pos)
      val nextNode = getRandomLine()
      if (nextNode == -1) {
        context.parent ! FinishGossip(nodeName)
      }
      else {
        context.actorSelection("../" + nextNode.toString()) ! Rumor("hi")
      }
    }
  }

  def getRandomLine(): Int = {
    val pos = nodeList.indexOf(nodeName)
    var result = 0
    println("POS: " + pos + "  Node Name: " + nodeName + " Node List Size: " + nodeList.size)
    val lr = Random.nextInt(2)
    var nextNode = 0
    if (nodeList.size == 1) {
      nextNode = -1
    }
    else if (pos == 0) {
      nextNode = pos + 1
    }
    else if (pos == nodeList.size-1) {
      nextNode = pos - 1
    }
    else if (lr == 0) {
      nextNode = pos - 1
    }
    else if (lr == 1) {
      nextNode = pos + 1
    }

    if (nextNode == -1) {
      result = -1
    }
    else {
      result = nodeList(nextNode)
    }
    //println("@@@@@@@@ " + nodeList(nextNode) + "    Next Node: " + nextNode + " Node List Size: " + nodeList.size)
    println("RESULT: " + result)
    result
  }

}