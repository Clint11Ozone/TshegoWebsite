/* Mthebule
   Siphiwe Clinton
   4041743
   CSC311 2023 AI Practical */

package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

class State implements Comparable<State> {
    private int[] board; // board array
    private int heuristic; // heuristic value
    private int pos; // position of value "9"

    // constructor
    public State(int[] board, int heuristic, int pos) {
        this.board = board;
        this.heuristic = heuristic;
        this.pos = pos;
    }

    // accessor methods
    public int[] getBoard() {return board;}
    public int getHeuristic() {return heuristic;}
    public int getPos() {return pos;}
    // mutator methods
    public void setBoard(int[] board) {this.board = board;}
    public void setHeuristic(int heuristic) {this.heuristic = heuristic;}
    public void setPos(int pos) {this.pos = pos;}


    // generate children states
    public List<State> generateChildren() {
        List<State> children = new ArrayList<>();
        int row = pos / 3;
        int col = pos % 3;
        int[][] moves = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}}; // possible moves: up, left, down, right

        for (int[] move : moves) {
            int newRow = row + move[0];
            int newCol = col + move[1];

            if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) { // check if move is within board limits
                int newPos = newRow * 3 + newCol;
                int[] newBoard = Arrays.copyOf(board, 9);
                newBoard[pos] = newBoard[newPos];
                newBoard[newPos] = 9;
                State child = new State(newBoard, calculateHeuristic(newBoard), newPos);
                children.add(child);
            }
        }
        return children;
    }

    // calculate heuristic value
    public static int calculateHeuristic(int[] board) {
        int size = (int) Math.sqrt(board.length);
        int magicConstant = ((size * size * size) + size) / 2;
        int distance = 0;
        for (int i = 0; i < size; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < size; j++) {
                rowSum += board[i * size + j];
                colSum += board[i + j * size];
            }
            distance += Math.abs(rowSum - magicConstant) + Math.abs(colSum - magicConstant);
        }

        int diagonalSum1 = 0;
        int diagonalSum2 = 0;
        for (int i = 0; i < size; i++) {
            diagonalSum1 += board[i * size + i];
            diagonalSum2 += board[(size - i - 1) * size + i];
        }
        distance += Math.abs(diagonalSum1 - magicConstant) + Math.abs(diagonalSum2 - magicConstant);

        return distance;
    }

    // print state contents
    public void printState() {
        for (int i = 0; i < 9; i++) {
            System.out.print(board[i] + " ");
            if ((i + 1) % 3 == 0) {
                System.out.println();
            }
        }
        System.out.println("Heuristic value: " + heuristic);
    }

    //function to find the index of #9 in order to make it the magicCell or number
    public static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int[] readInput(String filename) {
        List<Integer> values = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextInt()) {
                values.add(scanner.nextInt());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int[] board = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            board[i] = values.get(i);
        }
        return board;
    }

    //representation of the 3x3 grid in string format
    public String getStateString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append(board[i]).append(" ");
            if ((i + 1) % 3 == 0) {
                sb.append("\n");
            }
        }
        sb.append("Heuristic value: ").append(heuristic).append("\n");
        return sb.toString();
    }

    // comparison method for priority queue sorting
    @Override
    public int compareTo(State other) {
        return Integer.compare(heuristic, other.heuristic);
    }

    public static void main(String[] args) {
        int[] initialBoard = readInput("/Users/clint/Desktop/CSC311/practicals/AI prac/InputA.txt");   // initial state
        System.out.println("initial board: ");
        System.out.println(Arrays.toString(initialBoard));

        State initialState = new State(initialBoard, State.calculateHeuristic(initialBoard), indexOf(initialBoard, 9));
        Set<String> visitedStates = new HashSet<>();
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(State::getHeuristic));
        frontier.add(initialState);
        visitedStates.add(Arrays.toString(initialState.getBoard()));
        State solutionState = null;
        // expand states until goal state is found or frontier is empty
        while (!frontier.isEmpty()) {
            State currentState = frontier.poll();
            System.out.println("Expanded state:");
            currentState.printState();
            if (currentState.getHeuristic() == 0) { // goal state found
                solutionState = currentState;
                break;
            }
            List<State> children = currentState.generateChildren();
            for (State child : children) {
                String childBoardString = Arrays.toString(child.getBoard());
                if (!visitedStates.contains(childBoardString)) {
                    frontier.add(child);
                    visitedStates.add(childBoardString);
                }
            }
        }

        // print solution state to .txt file
        try {
            PrintWriter output = new PrintWriter(new File("OutputA.txt"));
            if (solutionState != null) {
                output.println();
                output.println("Solution state:\n" + solutionState.getStateString());
                while (!frontier.isEmpty()){
                    State thisState = frontier.poll();
                    output.println("expanded state: \n" + thisState.getStateString());
                }
                solutionState.printState();
            } else {
                output.println("No solution found.");
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
