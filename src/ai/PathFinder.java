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

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            
            node[col][row] = new Node(col, row);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    public void reseNode() {
            
            int col = 0;
            int row = 0;
    
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                
                node[col][row].open = false;
                node[col][row].checked = false;
                node[col][row].solid = false;
                node[col][row].parent = null;
    
                col++;
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {

        reseNode();

        // set start and goal node
        startNode = node[startCol][startRow];   
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            
            // check tile collision
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if (gp.tileM.tile[tileNum].collision == true) {
                node[col][row].solid = true;
            }
            else {
                node[col][row].solid = false;
            }
            // check interactive tile collision
            for(int i = 0; i < gp.iTile[1].length; i++) {
                if(gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible == true) {
                    int itCol = gp.iTile[gp.currentMap][i].worldX / gp.TileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY / gp.TileSize;
                    node[itCol][itRow].solid = true;
                }
            }

            // set cost
            getCost(node[col][row]);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
            // check if any entity is colliding
            for(int i = 0; i < gp.obj[1].length; i++) {
                if(gp.obj[gp.currentMap][i] != null) {
                    if(gp.obj[gp.currentMap][i].collision == true) {
                        if(gp.obj[gp.currentMap][i].worldX / gp.TileSize == col &&
                            gp.obj[gp.currentMap][i].worldY / gp.TileSize == row) {
                                node[col][row].solid = true;
                            }
                    }
                }
            }
        }
    }
    public void getCost(Node node) {

        // gCost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // hCost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // fCost
        node.fCost = node.gCost + node.hCost;
    }
    public boolean search() {

        while (goalReached == false && step < 500) {
                
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            // open the up node
            if(row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            // open the down node
            if(row + 1 < gp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            // open the left node
            if(col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            // open the right node
            if(col + 1 < gp.maxWorldCol) {
                openNode(node[col + 1][row]);
            }

            // find the best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // check if the node in openList has lower fCost than currentNode
                if(openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } 
                // if fCost is equal, check hCost
                else if (openList.get(i).fCost == bestNodeFCost) {
                    if(openList.get(i).hCost < openList.get(bestNodeIndex).hCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // if there is no node in openList, end the search
            if (openList.size() == 0) {
                break;
            }

            // after the loop, check if the currentNode is the goalNode
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }
    public void openNode(Node node) {

        if(node.solid == false && node.checked == false && node.open == false) {
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
        }
    }
}
