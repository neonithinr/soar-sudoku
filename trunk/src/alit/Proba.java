package alit;

import alit.exceptions.SudokuException;
import alit.sudoku.Point;
import alit.sudoku.Sudoku;
import sml.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 * User: Alexander Litvinenko
 * Date: 6/8/11
 * Time: 12:35 PM
 */
public class Proba {

    private static final String PATH = "sudoku.soar";
//    private static final String PATH = "helloworld.soar";


    public Proba() throws SudokuException {

    }

    public int makeChoice(int[] constraints) throws SudokuException {
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

    public static void main(String[] args) {
        try {
            Proba p = new Proba();
            System.out.println(p.makeChoice(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}));
        } catch (SudokuException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            Sudoku s = new Sudoku(new BufferedReader(new FileReader("proba.txt")));
//            Point p = new Point(3, 5);
//            for (int i : s.findConstraints(p))
//                System.out.print(i + " ");
//            System.out.println();
            System.out.println(s);
            s.solve();
            System.out.println(s);
        } catch (SudokuException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
