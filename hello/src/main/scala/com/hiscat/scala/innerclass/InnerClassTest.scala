package com.hiscat.scala.innerclass

object InnerClassTest {
  def main(args: Array[String]): Unit = {
    val graph = new Graph
    val node1 = graph.newNode
    val node2 = graph.newNode
    val node3 = graph.newNode
    node1.connectTo(node2)
    node1.connectTo(node3)

    val graph2 = new Graph
    val node4 = graph2.newNode
//    node1.connectTo(node4)
  }

  class Graph {

    class Node {
      var connectedNodes: List[Node] = Nil
//      var connectedNodes: List[Graph#Node] = Nil

//      def connectTo(node: Graph#Node): Unit = {
      def connectTo(node: Node): Unit = {
        if (!connectedNodes.exists(node.equals)) {
          connectedNodes = node :: connectedNodes
        }
      }

    }

    var nodes: List[Node] = Nil

    def newNode: Node = {
      val res = new Node
      nodes = res :: nodes
      res
    }

  }

}
