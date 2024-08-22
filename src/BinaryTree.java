import javax.management.InstanceNotFoundException;
import java.util.EventListener;

public class BinaryTree {



    private class Node {
        int value;
        Node LC;
        Node RC;


        public Node(int node_value) {
            this.value = node_value;
        }
    }

    Node treeRoot;

    public void addUp(int value) {
        if (treeRoot == null) {
            treeRoot = new Node(value);
        } else {
            Node root = treeRoot;
            boolean permittedToBreak = false;

            while (!permittedToBreak) {
                if (root.value < value) {
                    if (root.RC == null) {
                        root.RC = new Node(value);
                        permittedToBreak = true;
                    } else {
                        root = root.RC;
                    }
                } else {
                    if (root.LC == null) {
                        root.LC = new Node(value);
                        permittedToBreak = true;
                    } else {
                        root = root.LC;
                    }
                }
            }
        }
    }


    private void preOrderWalkASegment(Node node) {
        if (node.LC != null) {
            System.out.print(node.LC.value + " ");
            preOrderWalkASegment(node.LC);
        }


        if (node.RC != null) {
            System.out.print(node.RC.value + " ");
            preOrderWalkASegment(node.RC);
        }
        return;
    }

    public void preOrderWalk() {
        System.out.print(treeRoot.value + " ");
        preOrderWalkASegment(treeRoot);
        System.out.println("");
    }




    private Node searchNode(int value) {
        Node current = treeRoot;
        Node numbernode = null;

        while (numbernode == null && current != null) {
            if (current.value == value) {
                numbernode = current;
            } else if (current.value > value) {
                current = current.LC;
            } else {
                current = current.RC;
            }
        }

        return numbernode;
    }
    public int search(int value)  {
        Node current = treeRoot;
        Node numbernode = null;

        while (numbernode == null && current != null) {
            if (current.value == value) {
               numbernode = current;
            } else if (current.value > value) {
                current = current.LC;
            } else {
                current = current.RC;
            }
        }

        if (numbernode == null) {
            throw new RuntimeException();
        }

        return numbernode.value;
    }


    private Node findSuccessor(Node node){
        Node current = node;
        current = node.RC;
        while (current.LC != null) {
            current = current.LC;
        }
        return current;
    }

    private Node getAncestor(Node node) {
        Node current = treeRoot;
        Node ancestor = null;

        while (ancestor == null) {
            if (current.LC == node) {
                ancestor = current;
                continue;
            }
            if (current.RC == node) {
                ancestor = current;
                continue;
            }
            if (current.value > node.value) {
                current = current.LC;
                continue;
            }
            current = current.RC;
        }

        return ancestor;
    }
    public Integer delete(int value) {
        Node node = searchNode(value);
        if (node == null) {return null;}
        int nodeValue = node.value;
        Node parentNode = getAncestor(node);
        // 1. is leaf - has no descendants
        if (node.RC == null && node.LC == null) {

            if (node.value > parentNode.value) {
                parentNode.RC = null;
            } else {
                parentNode.LC = null;
            }
            return nodeValue;
        }
        // 2. has one child
        Node leftChild = node.LC;
        Node rightChild = node.RC;

        if (leftChild == null) {
            if (node.value > parentNode.value) {
                parentNode.RC = rightChild;
            } else {
                parentNode.LC = rightChild;
            }
            return nodeValue;
        }

        if (rightChild == null) {
            if (node.value > parentNode.value) {
                parentNode.RC = leftChild;
            } else {
                parentNode.LC = leftChild;
            }
            return nodeValue;
        }
        // 3. has two children
        Node successor = findSuccessor(node);
        Node successorParent = getAncestor(successor);
        if (successorParent.value > successor.value) {
            successorParent.LC = null;
        } else {
            successorParent.RC = null;
        }
        node.value = successor.value;


        return nodeValue;
    }

    public BinaryTree() {}







}
