package project2

import scala.math._
import scala.util.Random
import java.util.Scanner
import scala.collection.mutable.ArrayBuffer

/**
 * Created by chelsea on 9/15/15.
 */
class Topology(numOfNodes: Int) {

  var idx: Int = 0
  var topType: Int = 0
  //var numOfNodes: Integer = 0

  var nodes: Int = 0
  var edges: Int = 0

  var neighborList = Array.ofDim[Int](6)
  var imperfectList = Array.ofDim[Int](7)

  // Initialize with negative so 0 doesn't get counted as duplicate
  for (i <- 0 until neighborList.size) {
    neighborList(i) = -1
  }
  for (i <- 0 until imperfectList.size) {
    imperfectList(i) = -1
  }

  var node1: Int = 0
  var node2: Int = 0

  var imperfect: Boolean = false

  val sideNodes = getCubeFactor(numOfNodes)
  var cl = generateCube(sideNodes)

  def findNode() = {
    var nextNode = 0

    topType match {
      // Full
      case 0 => {
        do {
          nextNode = Random.nextInt(numOfNodes)
        } while (nextNode == idx)
      }
      // 3D
      case 1 => {
        nextNode = getRandomCubeNeighbor(cl,idx)
      }
      // Line
      case 2 => {
        val lr = Random.nextInt(2)
        if (idx == 0) {
          nextNode = idx + 1
        }
        else if (idx == numOfNodes-1) {
          nextNode = idx - 1
        }
        else if (lr == 0) {
          //println("0: " + idx)
          nextNode = idx - 1
        }
        else if (lr == 1) {
          //println("1: " + idx)
          nextNode = idx + 1
        }
      }
      // Imperfect 3D
      case 3 => {
        imperfect = true
        nextNode = getRandomCubeNeighbor(cl,idx)
      }
    }
    //println("Next Node: " + nextNode)
    nextNode
  }

  def getRandomCubeNeighbor(cl: Array[Array[Int]], idx: Int): Int = {
    var counter = 0
    for (i <- 0 until edges) {
      if (cl(i)(0) == idx) {
        neighborList(counter) = cl(i)(1)
        counter = counter + 1
      }
      if (cl(i)(1) == idx) {
        neighborList(counter) = cl(i)(0)
        counter = counter + 1
      }
    }

    // If imperfect, then add into neighborList a random node that is NOT in the neighborList
    if (imperfect == true) {
      for (i <- 0 until neighborList.size) {
        imperfectList(i) = neighborList(i)
      }
      var randNode = Random.nextInt(nodes)
      // Go through neighborList and assign random number that is not found in there
      while (neighborList.indexOf(randNode) > 0) {
        randNode = Random.nextInt(nodes)
      }
      imperfectList(counter) = randNode
      counter = counter + 1
    }

    // Prints out neighbor list - used for debugging
    /*println("Neighbor List (3D): ")
    println("Counter: " + counter)
    for (i <- 0 until counter) {
      println(neighborList(i))
    }*/

    // Prints out neighbor list for imperfect 3D - used for debugging
    /*println("Neighbor List (3D Imperfect): ")
    println("Counter: " + counter)
    for (i <- 0 until imperfectList.size) {
      println(imperfectList(i))
    }*/

    val r = Random.nextInt(counter)
    if (imperfect == true) {
      imperfectList(r)
    }
    else {
      neighborList(r)
    }
  }

  def checkDuplicates(num: Int): Boolean = {
    var duplicate = false
    for (i <- 0 until neighborList.size) {
      if (num == neighborList(i)) {
        duplicate = true
      }
    }
    duplicate
  }

  // Generate the cube
  def generateCube(n: Int): Array[Array[Int]] = {
    val cubeFactor = n
    var cubeListString = ""
    var cubeEdges = 0
    val nodesCube = pow(cubeFactor,3)
    var cubeStr = ""

    for (row <- 0 until cubeFactor) {
      for (col <- 0 until cubeFactor) {
        for (depth <- 0 until cubeFactor) {
          val currentNode = depth + (col * cubeFactor) + (row * cubeFactor * cubeFactor)

          // If not last depth
          if(depth != cubeFactor-1) {
            if ((currentNode < nodesCube) && ((currentNode+1) < nodesCube)) {
              cubeListString += "%d %d\n".format(currentNode, currentNode+1)
              cubeEdges = cubeEdges + 1
            }
          }

          // If not last col
          if(col != cubeFactor-1) {
            if ((currentNode < nodesCube) && ((currentNode + cubeFactor) < nodesCube)) {
              cubeListString += "%d %d\n".format(currentNode, currentNode + cubeFactor)
              cubeEdges = cubeEdges + 1
            }
          }

          // If not last row
          if(row != cubeFactor-1) {
            if ((currentNode < nodesCube) && ((currentNode + (cubeFactor * cubeFactor)) < nodesCube)) {
              cubeListString += "%d %d\n".format(currentNode, currentNode + (cubeFactor * cubeFactor))
              cubeEdges = cubeEdges + 1
            }
          }
        }
      }
    }
    // return Nodes, Edges, Cube List
    //System.out.println(cubeListString)
    //"%d %d\n%s".format(cubeFactor*cubeFactor*cubeFactor, cubeEdges, cubeListString)
    cubeStr = "%d %d\n%s".format(cubeFactor*cubeFactor*cubeFactor, cubeEdges, cubeListString)

    //println(cubeStr)

    val scan: Scanner = new Scanner(cubeStr)
    nodes = scan.nextInt()
    edges = scan.nextInt()
    val cubeList = Array.ofDim[Int](edges,2)

    //println("Nodes: " + nodes + "    Edges: " + edges)

    for (i <- 0 until edges) {
      node1 = scan.nextInt()
      node2 = scan.nextInt()

      cubeList(i)(0) = node1
      cubeList(i)(1) = node2
    }
    cubeList
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

  // Gets smallest cube factor
  def getCubeFactor(numOfNodes: Int): Int = {
    var factor: Int = 3
    while (numOfNodes > pow(factor,3)) {
      factor = factor + 1
    }
    factor
  }

}
