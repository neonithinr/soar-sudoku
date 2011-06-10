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

    public static void main(String[] args) {

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
