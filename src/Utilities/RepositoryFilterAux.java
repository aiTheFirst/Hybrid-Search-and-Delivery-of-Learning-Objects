/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

/**
 *
 * @author SWE
 */
public class RepositoryFilterAux extends Thread{
    public String repoInputDir;
    public String repoOutputDir;
    
    public void run(){
        try{
        RepositoryFilter.main( new String[]{repoInputDir,repoOutputDir} );

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
