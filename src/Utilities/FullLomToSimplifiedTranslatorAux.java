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
public class FullLomToSimplifiedTranslatorAux extends Thread {

    public static FullLomToSimplifiedTranslator t = null;
    public String repoDir = null;
    public String outputDir = null;

    public static void main(String[] args){
        
    }

    public void run(){
        try{
            FullLomToSimplifiedTranslator.main(new String[] {repoDir,outputDir});
        }
        catch(Exception e){
            //oops. give up.
            return;
        }
    }

}
