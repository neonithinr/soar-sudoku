package alit.sudoku;

import alit.exceptions.SudokuException;
import sml.Agent;
import sml.Identifier;
import sml.Kernel;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ALitvinenko
 * Date: 6/9/11
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sudoku {

    private static final String PATH = "sudoku.soar";
    private static final int INSERTION = 0;

    private int n = 9;
    private int[][] sudoku = new int[n][n];
    private Stack<Point> places = null;

    public Sudoku(BufferedReader reader) throws SudokuException {
        try {
            for (int i = 0; i < n; i++) {
                String[] nums = reader.readLine().split(" ");
                for (int j = 0; j < n; j++) {
                    sudoku[i][j] = Integer.parseInt(nums[j]);
                }
            }
        } catch (IOException e) {
            throw new SudokuException("Cannot read!", e);
        } catch (NumberFormatException nfe) {
            throw new SudokuException("Cannot convert!", nfe);
        }
        places = getInsertionPlaces();
    }

    public void solve() throws SudokuException {
        Stack<Point> steps = new Stack<Point>();
        Stack<Point> insertionPlaces = getInsertionPlaces();
        //TODO: Add history

        while (!insertionPlaces.isEmpty()) {
            Point place = insertionPlaces.pop();
            steps.push(place);
            Iterable<Integer> constraints = findConstraints(place);
            int insertion = makeChoice(constraints);
            if (insertion == INSERTION) {
                Point back = steps.pop();
                sudoku[back.x][back.y] = INSERTION;
                insertionPlaces.push(back);
            } else {
                sudoku[place.x][place.y] = insertion;
            }
        }

    }

    private Stack<Point> getInsertionPlaces() {
        Stack<Point> stack = new Stack<Point>();
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (sudoku[i][j] == INSERTION) {
                    stack.add(new Point(i, j));
                }
            }
        }
        return stack;
    }

    private int makeChoice(Iterable<Integer> constraints) throws SudokuException {
        int choice = -2;

        Kernel kernel = Kernel.CreateKernelInNewThread();
        if (kernel.HadError())
            throw new SudokuException(kernel.GetLastErrorDescription());

        Agent agent = kernel.CreateAgent("soar");
        if (agent.HadError())
            throw new SudokuException(agent.GetLastErrorDescription());

        boolean status = agent.LoadProductions(PATH);
        if (!status)
            throw new SudokuException(agent.GetLastErrorDescription());

        for (int constraint : constraints) {
            agent.GetInputLink().CreateIntWME("constraint", constraint);
        }

        agent.Commit();
        agent.RunSelfTilOutput();

        Identifier ol = agent.GetOutputLink();
        if (ol == null)
            throw new SudokuException("There is no output-link!");

        try {
            choice = Integer.parseInt(ol.GetChild(0).GetValueAsString());
            agent.ClearOutputLinkChanges();
        } catch (NumberFormatException nfe) {
            throw new SudokuException(nfe);
        } catch (NullPointerException npe) {
            throw new SudokuException(npe);
        } finally {
            kernel.DestroyAgent(agent);
            kernel.Shutdown();
        }

        return choice;
    }

    public Iterable<Integer> findConstraints(Point p) {
        Set<Integer> constraints = new HashSet<Integer>();

        // Horizontal constraints
        for (int j = 0; j < getSize(); j++) {
            if (sudoku[p.x][j] != INSERTION) {
                constraints.add(sudoku[p.x][j]);
            }
        }

        // Vertical constraints
        for (int i = 1; i < getSize(); i++) {
            if (sudoku[i][p.y] != INSERTION) {
                constraints.add(sudoku[i][p.y]);
            }
        }

        // Constraints in square
        int i = getNumberLteAndDividedOnThree(p.x);
        int bJ = getNumberLteAndDividedOnThree(p.y);
        do {
            int j = bJ;
            do {
                if (sudoku[i][j] != INSERTION) {
                    constraints.add(sudoku[i][j]);
                }
                j++;
            } while (j % 3 != 0);
            i++;
        } while (i % 3 != 0);
        return constraints;
    }

    private int getNumberLteAndDividedOnThree(int n) {
        int num = n;
        while (num % 3 != 0)
            num--;
        return num;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("+-------------+-------------+-------------+\n");
        for (int i = 0; i < getSize(); i++) {
            builder.append("| ");
            for (int j = 0; j < getSize(); j++) {
                if (sudoku[i][j] != INSERTION)
                    if(places.contains(new Point(i, j)))
                        builder.append("[").append(sudoku[i][j]).append("] ");
                    else
                        builder.append(" ").append(sudoku[i][j]).append("  ");
                else
                    builder.append("[_]").append(" ");
                if ((j + 1) % 3 == 0)
                    builder.append("| ");
            }
            builder.deleteCharAt(builder.length() - 1).append("\n");
            if ((i + 1) % 3 == 0)
                builder.append("+-------------+-------------+-------------+\n");
        }
        return builder.toString();
    }

    public int getSize() {
        return n;
    }
}

