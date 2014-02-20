package SQLQT_Utilities;

import java.io.File;

import javax.swing.filechooser.FileFilter;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class QueryFileFilter extends FileFilter 
{
    public boolean accept(File f) 
    {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".sql");
    }
    
    public String getDescription() 
    {
        return "SQL files (*.sql)";
    }
}
