package HTMLtags;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class MazeSolver {
    static char[][] maze = new char[20][21];
    static int[] final_location = new int[] {maze.length - 1, maze[0].length - 1};
    static int[] previous_location = new int[] {-1, -1};
    static int[] current_location = new int[] {0, 0};
    public static void main(String[] args) throws FileNotFoundException {
        Scanner file = new Scanner(new FileReader("/home/mimo/Java projects/HTMLtags/maze.txt"));
        Stack<int[]> spots = new Stack<>();
        for (int i = 0; file.hasNextLine(); i++) {
            String line = file.nextLine();
            maze[i] = line.toCharArray();
        }

        spots.push(previous_location);
        while (!Arrays.equals(current_location, final_location)) {
            spots.push(current_location);
            int[][] paths = checkPaths(current_location[0], current_location[1]);
            int path_idx = shortestPath(paths);
            if (path_idx == -1) {
                maze[current_location[0]][current_location[1]] = '-';
                spots.pop();
                current_location = spots.pop();
                previous_location = spots.peek();
            }
            else if (path_idx != -1) {
                previous_location = current_location;
                current_location = paths[path_idx];
                maze[previous_location[0]][previous_location[1]] = '*';
            }
        }
        spots.push(current_location);
        getMaze(spots);
    }

    public static boolean checkPath(int x, int y) {
        if (maze[x][y] == '1') return true;
        else return false;
    }

    public static int[][] checkPaths(int x, int y) {
        int[][] paths = new int[4][2];
        if (y != 0) paths[0] = checkPath(x, y - 1)? new int[] {x, y - 1}: null;
        else paths[0] = null;
        if (y != maze[0].length - 1) paths[1] = checkPath(x, y + 1)? new int[] {x, y + 1}: null;
        else paths[1] = null;
        if (x != 0) paths[2] = checkPath(x - 1, y)? new int[] {x - 1, y}: null;
        else paths[2] = null;
        if (x != maze.length - 1) paths[3] = checkPath(x + 1, y)? new int[] {x + 1, y}: null;
        else paths[3] = null;

        return paths;
    }

    public static int shortestPath(int[][] paths) {
        double shortest_path = getDistance(new int[] {0, 0});
        int idx = -1;
        for (int i = 0; i < 4; i++) {
            if (paths[i] == null) continue;
            double path_length = getDistance(paths[i]);
            if (path_length <= shortest_path && !Arrays.equals(paths[i], previous_location)) {
                shortest_path = path_length;
                idx = i;
            }
        }
        return idx;
    }

    public static double getDistance(int[] x) {
        double distance = Math.sqrt(Math.pow((x[0] - final_location[0]), 2) + Math.pow((x[1] - final_location[1]), 2));
        return distance;
    }

    public static void getMaze(Stack<int[]> s) {
        char[][] printed_maze = maze.clone();
        Stack<int[]> stack = (Stack<int[]>) s.clone();
        while (stack.size() != 1) {
            int [] p = stack.pop();
            printed_maze[p[0]][p[1]] = ' ';
        }
        for(char[] i: printed_maze) {
            for (char j : i) {
                if (j != '-') System.out.print(j);
                else System.out.print('1');
            }
            System.out.println();
        }
    }
}
