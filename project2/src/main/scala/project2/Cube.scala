package project2

import scala.math._
import scala.util.Random
import java.util.Scanner

/**
 * Created by chelsea on 9/17/15.
 */

class Cube {
  var NODES: Int = 0
  var EDGES: Int = 0

  var neighborList = Array.ofDim[Int](6)
  var imperfectList = Array.ofDim[Int](7)

  var node1: Int = 0
  var node2: Int = 0

  var imperfect: Boolean = false

  def getRandomCubeNeighbor(input: String, idx: Int): Int = {
    //println(input)
    val scan: Scanner = new Scanner(input)
    NODES = scan.nextInt()
    EDGES = scan.nextInt()
    val cubeList = Array.ofDim[Int](EDGES,2)
    //println("Nodes: " + NODES + "    Edges: " + EDGES)

    for (i <- 0 to EDGES-1) {
      node1 = scan.nextInt()
      node2 = scan.nextInt()

      cubeList(i)(0) = node1
      cubeList(i)(1) = node2
    }

    var counter = 0
    for (i <- 0 to EDGES-1) {
      //println(cubeList(i)(0) + ", " + cubeList(i)(1))
      if (cubeList(i)(0) == idx) {
        if (checkDuplicates(cubeList(i)(1)) == false) {
          neighborList(counter) = cubeList(i)(1)
          counter = counter + 1
        }
      }
      if (cubeList(i)(1) == idx) {
        if (checkDuplicates(cubeList(i)(0)) == false) {
          neighborList(counter) = cubeList(i)(0)
          counter = counter + 1
        }
      }
    }

    // If imperfect, then add into neighborList a random node that is NOT in the neighborList
    if (imperfect == true) {
      for (i <- 0 to neighborList.size-1) {
        imperfectList(i) = neighborList(i)
      }
      var randNode = Random.nextInt(NODES)
      // Go through neighborList and assign random number that is not found in there
      while (neighborList.indexOf(randNode) > 0) {
        randNode = Random.nextInt(NODES)
      }
      imperfectList(counter) = randNode
    }

    // Prints out neighbor list - used for debugging
    /*println("Neighbor List (3D): ")
    println("Counter: " + counter)
    for (i <- 0 to neighborList.size-1) {
      println(neighborList(i))
    }*/

    // Prints out neighbor list for imperfect 3D - used for debugging
    /*println("Neighbor List (3D Imperfect): ")
    for (i <- 0 to imperfectList.size-1) {
      println(imperfectList(i))
    }*/

    val r = Random.nextInt(counter)
    //println("RANDOM: " + neighborList(r))
    neighborList(r)
  }

  def checkDuplicates(num: Int): Boolean = {
    var duplicate = false
    for (i <- 0 to neighborList.size-1) {
      if (num == neighborList(i)) {
        duplicate = true
      }
    }
    duplicate
  }


  // Generate the cube
  def generateCube(n: Int): String = {
    val SIDE = n    // Number of nodes in one side of the cube
    var links = ""  // Holds the final output
    var link = 0    // Counts the number of links
    val nodes = pow(SIDE,3)

    for (row <- 0 to SIDE) {
      for (col <- 0 to SIDE) {
        for (depth <- 0 to SIDE) {
          val current = depth + (col * SIDE) + (row * SIDE * SIDE)

          // If not last depth
          if(depth != SIDE-1) {
            if ((current < nodes) && (current+1 < nodes)) {
              links += "%d %d\n".format(current, current+1)
              link = link + 1
            }
          }

          // If not last col
          if(col != SIDE-1) {
            if ((current < nodes) && (current + SIDE < nodes)) {
              links += "%d %d\n".format(current, current + SIDE)
              link = link + 1
            }
          }

          // If not last row
          if(row != SIDE-1) {
            if ((current < nodes) && (current + (SIDE * SIDE) < nodes)) {
              links += "%d %d\n".format(current, current + (SIDE * SIDE))
              link = link + 1
            }
          }
        }
      }
    }
    // return #Nodes, #Edges, links ...
    "%d %d\n%s".format(SIDE*SIDE*SIDE, link, links)
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

  // Gets smallest cube size
  def getCubeFactor(numOfNodes: Int): Int = {
    var factor: Int = 3
    while (numOfNodes > pow(factor,3)) {
      factor = factor + 1
    }
    factor
  }
}