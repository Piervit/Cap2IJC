/**
 *
 * Initial author: lusing
 * Modified by : Pierre Vittet (FIME)
 *
 * Project recovered from http://sourceforge.net/p/openjcvm/code/HEAD/tree/Trunk/.
 *
 * The project licence is GPLV2 (see LICENCE).
 * 
*/


package net.sourceforge.javacardtools.Cap2IJC;


public class Options{


    private boolean all_component = false;
    private String out_path = null; //path of the dumped ijc file
    private String cap_path = null; //path of the cap to analyze.

    private static Options options = null; 

    public boolean getAllComponent(){
        return all_component;
    }

    public String getOutPath(){
        return out_path;
    }

    public String getCapPath(){
        return cap_path;
    }

    /*
     * read and set the argument at position[idx] in the argument array.
     * Return the position of the next argument to read.
     *
     */
    private int setArg(String[] args, int idx){
        if(args[idx].equals("-h") || args[idx].equals("--help")){
            printHelp();
            return 0;
        }
        else if (args[idx].equals("-a") || args[idx].equals("-all")){
            all_component = true;
            return idx+1;
        }
        else if (args[idx].equals("-o") || args[idx].equals("--out")){
            out_path = args[idx+1];
            return idx+2;
        }
        else if (args[idx].startsWith("-") || args[idx].startsWith("--")){
            failUnrecognizeArg(args[idx]);
            return idx+1;
        }
        else if(cap_path == null){
            cap_path = args[idx];
            return idx+1;
        }
        else{
            System.out.println("Bad usage");
            printHelp();
        }
        return idx+1;
    }

    public void getArgs(String[] args){
        if(args == null || args.length == 0 ){
            System.out.println("Bad usage");
            printHelp();
        } 
        else{
            int idx = 0;
            while(idx < args.length){
                idx = setArg(args, idx);
            }
        }
    }


    public static void failUnrecognizeArg(String badArg){
        System.out.println("You have given an unrecognize argument ("+badArg+")");
        printHelp();
    }

    /*
     * Print help and exit.
     */
    public static void printHelp(){
			System.out.println("Options are:");
			System.out.println("\t-h | --help : Print this help  ");
			System.out.println("\t-a | --all :  Include the descriptor and debug component (not included by default.)");
			System.out.println("\t-o | --out <path> :  dump ijc to the indicated path.");
            System.exit(0);
    }



    private Options(){
    }

    public static Options getOption(){
        if(options == null){
            options = new Options();
        }
        return options;
    }



}


