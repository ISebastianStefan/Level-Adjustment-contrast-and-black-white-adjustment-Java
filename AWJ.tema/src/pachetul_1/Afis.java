package pachetul_1;

//clasa ce implementeaza interfata Interf
public class Afis implements Interf {
	double startTime = System.nanoTime(); // start timer
	//suprascrierea facuta peste comportamentul interfetei
	@Override
	public void intAf() // executia programului a avut loc cu succes
	{
		System.out.println("Succes!");
	}
	double stopTime = System.nanoTime(); //end timer
}
