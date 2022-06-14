package pachetul_1;
//clasa abstracta pentru argumente variabile
public abstract class Abs {
	double startTime = System.nanoTime(); //start timer
	
	//varargs
	private static boolean varArgs(String ... arguments){
        long start = System.currentTimeMillis(); //start timer
        if(arguments.length == 3) {
            String parameter1 = arguments[0].toLowerCase();
            if(!parameter1.equals("and")&&!parameter1.equals("or")&&!parameter1.equals("xor"))
                return false;
        }
        return false;
    }
	double stopTime = System.nanoTime(); //end timer
}
