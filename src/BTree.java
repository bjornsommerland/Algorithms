import com.sun.source.tree.ParenthesizedTree;

import javax.swing.text.AsyncBoxView;
import java.util.ArrayList;

public class BTree {

    class Node {
        Node ParentNode;
        ArrayList<Integer> elements;
        ArrayList<Node> children;

        public Node() {
            elements = new ArrayList<>(degree);
            children = new ArrayList<>(degree + 1);
        }

        public int getIndexRelativeToParent() {
            int nodeIndex = 0;
            for (int i = 0; i < ParentNode.children.size(); i++) {
                if (ParentNode.children.get(i) == this) {
                    nodeIndex = i;
                    break;
                }
            }
            return nodeIndex;
        }
        // Zwykłe usuwanie, nic więcej nie robi
        public void removeIfLeaf(Integer value) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i) == value) {
                    elements.remove(i);
                    break;
                }
            }
            // Sprawdzamy czy trzeba naprawić strukture drzewa.

            if (!elements.isEmpty()) {
                return;
            }
            int nodeIndex = getIndexRelativeToParent();

            // sprawdzamy czy będzie trzeba swampować.

            if (ParentNode.children.size() > 1) {


                System.out.println(nodeIndex);
                // Szukamy "dawcę" int'a
                Node LNode = null;
                Node RNode = null;


                if (nodeIndex != 0) {
                    LNode = ParentNode.children.get(nodeIndex - 1);
                }
                if (LNode != null && LNode.elements.size() > 1) {
                    elements.add(ParentNode.elements.removeFirst());
                    ParentNode.add(LNode.elements.removeLast());
                    return;
                }
                if (nodeIndex != (ParentNode.children.size() - 1)) {
                    RNode = ParentNode.children.get(nodeIndex + 1);
                }
                if (RNode != null && RNode.elements.size() > 1) {
                    elements.add(ParentNode.elements.removeFirst());
                    ParentNode.add(RNode.elements.removeFirst());
                    return;
                }

                // Jeżeli nie znaleźliśmy żadnego zastępczego node'a, to wtedy
                // mergujemy
                System.out.println(ParentNode.elements);
                Node setNode = null;
                if (LNode != null) {
                    LNode.add(ParentNode.elements.removeFirst());
                    setNode = LNode;
                } else if (RNode != null) {
                    RNode.add(ParentNode.elements.removeLast());
                    setNode = RNode;
                }
                if (setNode == null) {
                    return;
                }
                if (ParentNode.elements.isEmpty()) {
                    setNode.setParentNode(ParentNode.ParentNode);
                    ParentNode = setNode;
                }
            }
        }



        // funckja mergowania
        public Node merge(Node node1,Node node2) {
            Node hNode;
            Node lNode;

            if (node1.elements.getLast() < node2.elements.getFirst()) {
                // Node2 jest większy
                lNode = node1;
                hNode = node2;
            } else {
                // Node1 jest większy
                lNode = node2;
                hNode = node1;
            }
            for (int i=lNode.elements.size()-1;i>=0;i--) {
                hNode.elements.addFirst(lNode.elements.get(i));
            }
            for (int i=lNode.children.size()-1;i>=0;i--) {
                hNode.children.addFirst(lNode.children.get(i));
            }

            lNode.setParentNode(null);
            for (int i = 0; i <hNode.ParentNode.children.size();i++) {
                if (hNode.ParentNode.children.get(i) == lNode) {
                    hNode.ParentNode.children.remove(i);
                    break;
                }
            }
            if (hNode.children.size() > degree + 1) {
                merge(hNode.children.getLast(),hNode.children.get(hNode.children.size()-2));
            }
            return hNode;
        }
        public void removeIfNotLeaf(Integer value) {
            int nodeIndex = -1;
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i) == value) {
                    nodeIndex = i;
                    elements.remove(i);
                    break;
                }
            }

            if (nodeIndex == -1) {return;}

            Node LNode = children.get(nodeIndex);
            if (LNode.elements.size() > 1) {
                elements.add(LNode.elements.getLast());
                if (LNode.isLeaf()) {
                    LNode.elements.removeLast();
                    return;
                } else {
                    LNode.removeIfNotLeaf(LNode.elements.getLast());
                }
            } else {
                if (children.size() > (nodeIndex + 1)) {
                    Node RNode = children.get(nodeIndex + 1);
                    if (RNode.elements.size() > 1) {
                        elements.add(RNode.elements.getFirst());
                        if (RNode.isLeaf()) {
                            RNode.elements.removeFirst();
                            return;
                        } else {
                            RNode.removeIfNotLeaf(RNode.elements.getFirst());
                        }
                    } else {
                        // Lewe i prawe dziecię ma 1 element.
                        // LNode --> RNode
                        Node x = merge(LNode,RNode);
                        System.out.println(x.elements);
                        if (x.ParentNode.elements.isEmpty())  {
                            if (x.ParentNode.ParentNode.elements.size() == 1) {
                                x.ParentNode.elements.add(x.ParentNode.ParentNode.elements.removeFirst());
                                int index = x.ParentNode.getIndexRelativeToParent();
                                Node b;
                                if (x.ParentNode.ParentNode.children.size() - 1 < index + 1) {
                                    b = merge(x.ParentNode,x.ParentNode.ParentNode.children.get(index-1));
                                } else {
                                    b = merge(x.ParentNode,x.ParentNode.ParentNode.children.get(index+1));
                                }
                                System.out.println(b.elements);
                                System.out.println(root.elements);
                                if (b.ParentNode == root) {
                                    root = b;
                                } else {
                                    b.setParentNode((b.ParentNode.ParentNode));
                                }
                            }
                        }
                    }
                }
            }
        }

        public void setParentNode(Node parent) {
            this.ParentNode = parent;
        }

        public void splitElements() {
            boolean hasParentNode;

            int medianindex = degree / 2;

            if (ParentNode == null) {
                hasParentNode = false;
                ParentNode = new Node();
            } else {
                hasParentNode = true;
            }

            int index = ParentNode.add(elements.get(medianindex));
            Node lNode = new Node();
            Node rNode = new Node();


            //
            for (int i = medianindex + 1; i < elements.size(); i++) {
                rNode.elements.add(elements.get(i));
            }
            for (int i = 0; i < medianindex; i++) {
                lNode.elements.add(elements.get(i));
            }
            /*for (int i = 0; i <medianindex;i++) {
                lNode.children.add(children.get(i));
            }*/

            lNode.setParentNode(ParentNode);
            rNode.setParentNode(ParentNode);

            for (int i = 0; i < children.size(); i++) {
                ArrayList<Integer> childElements = children.get(i).elements;

                Integer highestElement = childElements.get(childElements.size() - 1);
                Node childNode = children.get(i);
                if (highestElement < elements.get(medianindex)) {
                    lNode.children.add(childNode);
                    childNode.setParentNode(lNode);

                } else {
                    rNode.children.add(childNode);
                    childNode.setParentNode(rNode);
                }
            }


            if (ParentNode.children.size() > index) {
                ParentNode.children.set(index, lNode);
                ParentNode.children.add(index + 1, rNode);
            } else {
                ParentNode.children.add(lNode);
                ParentNode.children.add(rNode);
            }

            if (hasParentNode) {
                ParentNode.children.remove(this);
            }

            if (this == root) {
                root = ParentNode;
            }
        }

        public int getSize() {
            return elements.size();
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public int add(Integer value) {
            int index = 0;
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i) < value) {
                    index = i + 1;
                }
            }


            elements.add(index, value);
            if (elements.size() == degree) {
                splitElements();
            }
            return index;
        }


    }

    private Node root;
    private int height = 0;
    private int degree;


    private void printNode(Node node) {
        for (int i = 0; i < node.elements.size(); i++) {
            System.out.print(node.elements.get(i) + " ");
        }
        System.out.println(" ");
    }

    public void printLeaves(Node node) {
        printNode(node);
        for (int i = 0; i < node.children.size(); i++) {
            Node x = node.children.get(i);
            printLeaves(x);
        }
    }

    public Node find(int n) {
        if (root == null) {
            return null;
        }
        Node scannedNode = root;

        /*while (true) {
            int firstHighIndex = 0;

            for (int i = 0;i<scannedNode.elements.size();i++) {
                if (scannedNode.elements.get(i) == n) {
                    return scannedNode;
                }
                if (i + 1 > scannedNode.elements.size()-1) {
                    firstHighIndex = scannedNode.elements.size();
                    break;
                };
                if (scannedNode.elements.get(i) < n && scannedNode.elements.get(i + 1) > n) {
                    firstHighIndex = i + 1;
                    break;
                }
            }
            if (scannedNode.children.isEmpty()) {
                return null;
            }
            scannedNode = scannedNode.children.get(firstHighIndex);
            if (scannedNode == null) {
                return null;
            }
        }*/

        while (true) {
            int childNodeIndex = -1;
            // Czy posiada w sobie numer
            for (int i = 0; i < scannedNode.elements.size(); i++) {
                int num = scannedNode.elements.get(i);
                // Jeżeli obecny node posiada to dziecię
                if (num == n) {
                    return scannedNode;
                } else if (num > n) {
                    // 3 > 2 TAK!
                    if (i > 0) {
                        childNodeIndex = i - 1;
                    } else {
                        childNodeIndex = 0;
                    }
                } else if (i == scannedNode.elements.size() - 1) {
                    childNodeIndex = i + 1;
                    if (scannedNode.children.size() < childNodeIndex + 1) {
                        return null;
                    }
                }
            }
            // Wyznaczamy następnego childnodeIndex
            if (childNodeIndex != -1) {
                if (scannedNode.children.isEmpty()) {
                    return null;
                }
                scannedNode = scannedNode.children.get(childNodeIndex);

                if (scannedNode == null) {
                    return null;
                } else {
                    continue;
                }
            }
        }
    }

    // O(n log n)
    public void remove(int n) {
        Node node = this.find(n);
        if (node == null) {
            return;
        }

        int nodeIndexRelativeToParent = 0;

        // Jeżeli parentnode istnieje to szukamy indeksu, który jest relatywny do childrena.
        if (node.ParentNode != null) {
            for (int i = 0; i < node.ParentNode.children.size(); i++) {
                if (node.ParentNode.children.get(i) == node) {
                    nodeIndexRelativeToParent = i;
                    break;
                }
            }
        }

        if (node.isLeaf()) {
            // (1) Jeżeli element jest liściem to go odrazu usuwamy.
            // To działą!!!
            node.removeIfLeaf(n);
            return;
        } else {
            // zrobić
            node.removeIfNotLeaf(n);
        }


    }

    public void put(int n) {
        // Jeżeli root nie istnieje
        if (root == null) {
            root = new Node();
            root.add(n);
            return;
        }
        if (this.find(n) != null) {
            return;
        }

        // Jeżeli root istnieje, inicjujemy binary search
        Node isPut = null;
        Node currentScanned = root;
        while (isPut == null) {
            if (currentScanned.isLeaf()) {
                // Dowolne wkładanie dzieci
                isPut = currentScanned;
            } else {
                // Rodzielanie dzieci

                Integer pickedi = null;

                for (int i = 0; i < currentScanned.elements.size(); i++) {
                    if (currentScanned.elements.get(i) > n) {
                        pickedi = i;
                        break;
                    }
                }

                if (pickedi == null) {
                    pickedi = currentScanned.elements.size();
                }

                if (currentScanned.children.get(pickedi) != null) {
                    currentScanned = currentScanned.children.get(pickedi);
                } else {
                    currentScanned.add(n);
                }
            }
        }
        isPut.add(n);
    }

    public BTree(int degree) {
        if (degree <= 1) {
            this.degree = 2;
        }
        this.degree = degree;
    }

    public static void main(String[] x) {
        BTree btre = new BTree(3);

        btre.put(20);
        btre.put(10);
        btre.put(35);
        btre.put(5);
        btre.put(15);
        btre.put(30);
        btre.put(70);

        System.out.println();
        btre.printLeaves(btre.root);
        btre.remove(35);
        System.out.println();
        btre.printLeaves(btre.root);
    }
}
