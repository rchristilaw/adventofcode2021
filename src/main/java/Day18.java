import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day18 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day18.class.getName());
    private final static String inputFile = "day18.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day18 = new Day18();
//        day18.runPart1();
        day18.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());

        final var rows = input.stream()
            .map(this::buildTreeFromString)
            .collect(Collectors.toList());

        var sumTree = rows.get(0);

        for (var addRow : rows.subList(1, rows.size())) {
            var newTree = new Node();
            newTree.left = sumTree;
            newTree.left.parent = newTree;
            newTree.right = addRow;
            newTree.right.parent = newTree;

            evaluateTree(newTree);
            sumTree = newTree;
        }

        var str = new StringBuilder();
        printTree(sumTree, str);
        log.info("TOTAL: " + str);

        var total = calcMagnitude(sumTree);
        log.info("TOTAL: " + total);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().collect(Collectors.toList());


        final var rows = input.stream()
            .collect(Collectors.toList());

        int maxMag = 0;

        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.size(); j++) {
                if (i==j) {
                    continue;
                }

                var newTree = new Node();
                newTree.left = buildTreeFromString(rows.get(i));
                newTree.left.parent = newTree;
                newTree.right = buildTreeFromString(rows.get(j));
                newTree.right.parent = newTree;

                evaluateTree(newTree);
                log.info("TOTAL: " + calcMagnitude(newTree));
                maxMag = Math.max(maxMag, calcMagnitude(newTree));
            }
        }
        var total = 0;
        log.info("TOTAL: " + maxMag);
    }

    private Node buildTreeFromString(String line) {
        Queue<String> charsQueue = new LinkedList<String>();
        for (var c : line.split("")) {
            charsQueue.offer(c);
        }

        return buildTree(charsQueue, null);
    }

    private Node buildTree(Queue<String> line, Node parent) {
        var node = new Node();
        node.parent = parent;

        if (line.peek().equals("[")) {
            line.remove();
            node.left = buildTree(line, node);
        }

        if (line.peek().equals(",")) {
            line.remove();
            node.right = buildTree(line, node);
        }

        Integer value = getValue(line.peek());
        if (value != null) {
            line.remove();
            node.value = value;
        }

        if (line.size() == 0) {
            return node;
        }

        if (line.peek().equals("]")) {
            line.remove();
        }

        return node;
    }

    private Integer getValue(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void evaluateTree(Node node) {
        boolean hasExplosion = true;
        boolean hasSplits = true;
        while (hasExplosion || hasSplits) {
            hasExplosion = checkForExplosions(node);
            if (hasExplosion) {
                continue;
            }
            hasSplits = checkForSplits(node);
        }
    }

    private boolean checkForExplosions(Node node) {
        if (shouldExplode(node)) {
            return true;
        }

        if (node.value != null) {
            return false;
        }

        var explosion = checkForExplosions(node.left);

        if (!explosion) {
            explosion = checkForExplosions(node.right);
        }

        return explosion;
    }

    private boolean checkForSplits(Node node) {

        if (shouldSplit(node)) {
            return true;
        }

        if (node.value != null) {
            return false;
        }

        var explosion = checkForSplits(node.left);

        if (!explosion) {
            explosion = checkForSplits(node.right);
        }

        return explosion;
    }

    private boolean shouldExplode(Node node) {
        if (node.getDepth() >= 4 && node.value == null) {
            // traverse up and add left
            int left = node.left.value;

            int right = node.right.value;

            node.value = 0;

            var prev = findPreviousNode(node.parent, node);
            if (prev != null) {
                prev.value += left;
            }

            var next  = findNextNode(node.parent, node);
            if (next != null) {
                next.value += right;
            }
//
//            if (next != null) {
//                shouldSplit(next);
//            }
//
//            if (prev != null) {
//                shouldSplit(prev);
//            }

            node.left = null;
            node.right = null;
            return true;
        }
        return false;
    }

    private boolean shouldSplit(Node node) {
        if (node.value != null && node.value > 9) {
            double div = (double)node.value / 2;
            int n1 = (int) Math.floor(div);
            int n2 = (int) Math.ceil(div);

            node.left = new Node();
            node.left.parent = node;
            node.left.value = n1;

            node.right = new Node();
            node.right.parent = node;
            node.right.value = n2;

            node.value = null;

//            shouldExplode(node);
            return true;
        }
        return false;
    }

    private static class Node {
        Integer value = null;
        Node parent;
        Node left;
        Node right;
        int magnitude;

        int getDepth() {
            int depth = 0;
            var current = this;
            while (current.parent != null) {
                current = current.parent;
                depth++;
            }
            return depth;
        }
    }

    private void printTree(Node node, StringBuilder val) {
        if (node.value != null) {
            val.append(node.value);
            val.append(", ");
            return;
        }

        printTree(node.left, val);
        printTree(node.right, val);
    }

    private int calcMagnitude(Node node) {
        if (node.value != null) {
            return node.value;
        }

        if (node.left.value != null && node.right.value != null) {
            return (node.left.value*3) + (node.right.value*2);
        }

        int leftMag = calcMagnitude(node.left);
        int rightMag = calcMagnitude(node.right);
        return (leftMag*3) + (rightMag*2);
    }

    private Node findNextNode(Node node, Node previousNode) {
        if (node.parent == null && previousNode == node.right) {
            return null;
        } else if (node.parent == null && previousNode == node.left){
            var currentNode = node.right;
            while (currentNode.left != null){
                currentNode = currentNode.left;
            }
            return currentNode;
        }

        if(node.right != null && node.right != previousNode){ //go right if not been there yet
            var currentNode = node.right;
            while (currentNode.left != null){
                currentNode = currentNode.left;
            }
            return currentNode;
        }

        return findNextNode(node.parent, node);
    }

    private Node findPreviousNode(Node node, Node previousNode) {
        if (node.parent == null && previousNode == node.left) {
            return null;
        } else if (node.parent == null && previousNode == node.right){
            var currentNode = node.left;
            while (currentNode.right != null){
                currentNode = currentNode.right;
            }
            return currentNode;
        }

        if(node.left != null && node.left != previousNode){ //go right if not been there yet
            var currentNode = node.left;
            while (currentNode.right != null){
                currentNode = currentNode.right;
            }
            return currentNode;
        }

        return findPreviousNode(node.parent, node);
    }
}