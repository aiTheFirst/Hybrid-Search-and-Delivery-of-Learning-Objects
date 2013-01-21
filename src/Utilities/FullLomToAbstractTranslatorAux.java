/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

/**
 *
 * @author Jarrett G. Steele
 * Auxiliary class so serve as thread.
 */
public class FullLomToAbstractTranslatorAux extends Thread {

    public static FullLomToAbstractTranslator t = null;
    public String repoDir = null;
    public String outputDir = null;


    public void run(){
        try{
            FullLomToAbstractTranslator.main(new String[] {repoDir, outputDir });
        }
        catch(Exception e){
            //oops. give up.
            System.out.println(e.getMessage());
            return;
        }
    }

}
