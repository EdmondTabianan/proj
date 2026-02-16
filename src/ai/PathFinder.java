package ai;

import java.util.ArrayList;

import entity.Entity;
import main.GamePanel;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                node[col][row] = new Node(col, row);
            }
        }
    }
    
    public void resetNode() {
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                node[col][row].open = false;
                node[col][row].checked = false;
                node[col][row].solid = false;
                node[col][row].parent = null;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {
        resetNode();

        // Validate coordinates
        if (startCol < 0 || startCol >= gp.maxWorldCol || 
            startRow < 0 || startRow >= gp.maxWorldRow ||
            goalCol < 0 || goalCol >= gp.maxWorldCol || 
            goalRow < 0 || goalRow >= gp.maxWorldRow) {
            return;
        }

        // set start and goal node
        startNode = node[startCol][startRow];   
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        // FIRST: Set all solid nodes based on tiles
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                // Check tile collision
                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
                if (gp.tileM.tile[tileNum].collision) {
                    node[col][row].solid = true;
                }
            }
        }

        // SECOND: Check interactive tiles
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                for(int i = 0; i < gp.iTile[1].length; i++) {
                    if(gp.iTile[gp.currentMap][i] != null) {
                        int itCol = gp.iTile[gp.currentMap][i].worldX / gp.TileSize;
                        int itRow = gp.iTile[gp.currentMap][i].worldY / gp.TileSize;
                        if(itCol == col && itRow == row) {
                            // Only set solid if it's not destructible OR if it's still alive
                            if(!gp.iTile[gp.currentMap][i].destructible || 
                               gp.iTile[gp.currentMap][i].life > 0) {
                                node[col][row].solid = true;
                            }
                            break;
                        }
                    }
                }
            }
        }

        // THIRD: Check objects
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                for(int i = 0; i < gp.obj[1].length; i++) {
                    if(gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].collision) {
                        int objCol = gp.obj[gp.currentMap][i].worldX / gp.TileSize;
                        int objRow = gp.obj[gp.currentMap][i].worldY / gp.TileSize;
                        if(objCol == col && objRow == row) {
                            node[col][row].solid = true;
                            break;
                        }
                    }
                }
            }
        }

        // FOURTH: Check other monsters (optional - to avoid crowding)
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                for(int i = 0; i < gp.monster[1].length; i++) {
                    if(gp.monster[gp.currentMap][i] != null && 
                       gp.monster[gp.currentMap][i] != entity) { // Don't avoid itself
                        int monsterCol = gp.monster[gp.currentMap][i].worldX / gp.TileSize;
                        int monsterRow = gp.monster[gp.currentMap][i].worldY / gp.TileSize;
                        if(monsterCol == col && monsterRow == row) {
                            node[col][row].solid = true;
                            break;
                        }
                    }
                }
            }
        }

        // Calculate costs for all nodes
        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                getCost(node[col][row]);
            }
        }
    }
    
    public void getCost(Node node) {
        // gCost (distance from start)
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // hCost (distance to goal)
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // fCost (total)
        node.fCost = node.gCost + node.hCost;
    }
    
    public boolean search() {
        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            // Open adjacent nodes
            if (row - 1 >= 0) openNode(node[col][row - 1]); // Up
            if (row + 1 < gp.maxWorldRow) openNode(node[col][row + 1]); // Down
            if (col - 1 >= 0) openNode(node[col - 1][row]); // Left
            if (col + 1 < gp.maxWorldCol) openNode(node[col + 1][row]); // Right

            // Find the best node in openList
            if (openList.isEmpty()) {
                break;
            }

            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                Node testNode = openList.get(i);
                
                if (testNode.fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = testNode.fCost;
                } 
                else if (testNode.fCost == bestNodeFCost) {
                    // If fCost is equal, choose the one with lower hCost
                    if (testNode.hCost < openList.get(bestNodeIndex).hCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }
    
    public void openNode(Node node) {
        if (!node.solid && !node.checked && !node.open) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    
    public void trackThePath() {
        Node current = goalNode;
        
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
            
            // Safety check to prevent infinite loop
            if (current == null) {
                pathList.clear();
                break;
            }
        }
    }
}