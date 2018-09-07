import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RSA {

	public static void main(String[] args) {
        // Ask user for ASCII String input
        // Convert to integers
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter the message you would like to encode, using any ASCII characters: ");
		String input = keyboard.nextLine();
		int[] ASCIIvalues = new int[input.length()];
		for (int i = 0; i < input.length(); i++) {
			ASCIIvalues[i] = input.charAt(i);
		}
		String ASCIInumbers = "";
		for (int j = 0; j < ASCIIvalues.length; j++) {
			ASCIInumbers += ASCIIvalues[j] + " ";
		}
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println("The ASCII coded sequence is:");
		System.out.println();
		System.out.println(ASCIInumbers);
		System.out.println();
		System.out.println("-----------------------------------------");

		long P = bigPrime();
		long Q = P;
		while (Q == P) {
			Q = bigPrime();
		}


		System.out.println();
		System.out.println("The two primes are P = " + P + " and Q = " + Q);
		System.out.println("-----------------------------------------");


		// Calculate phi value
		long phi = (P-1)*(Q-1);

		// Find relative prime E

		long E = -1;
		
		// Use loop starting from 1/3*phi to phi-2 to find some arbitrary large value
		for(long i = phi/3; i<phi-2;i++){
			if(gcd(i,phi) == 1){ // Check to make sure that gcd of e and phi is 1
				E = i;
				break; // Stop once arbitrary value is found
			}
		}

		System.out.println();
		System.out.println("The totient is phi = " + phi);
		System.out.println("The public key is E = " + E);
		System.out.println();
		System.out.println("-----------------------------------------");

        // Find the multiplicative inverse of E mod (P - 1)(Q - 1)
        // Bezout coefficients of E and phi
        // Use Extended Euclidean Algorithm

		// Creating placeholder variables
		long hold = E;
		long value = phi;
		long value2 = hold;

		// Instantiating ArrayLists to hold quotients, remainders
		// One ArrayList used as placeholder

		ArrayList<Long> l1 = new ArrayList<Long>();
		l1.add(value); // value represents phi
		l1.add((long) 1);	// Values used in part 2 of algorithm
		l1.add((long) 0);

		ArrayList<Long> l2 = new ArrayList<Long>();
		l2.add((long) value2); // value2 represents E
		l2.add((long) 0);	// Values used in part 2 of algorithm
		l2.add((long) 1);

		ArrayList<Long> l3 = new ArrayList<Long>();
		l3.add((long) 0); // Just to fill up the array
		l3.add((long) 0);
		l3.add((long) 0); 

		// While totient is still greater than 0...
		while (l1.get(0)-l2.get(0)*(l1.get(0)/l2.get(0)) > 0) {
			for (int j = 0; j < 3; j++){ 
				l3.set(j, l2.get(j)); // Let 3rd ArrayList hold values for ArrayList 2
			}
			long q = l1.get(0)/l2.get(0);	// Quotient

			// Use substitution in order to find multiplicative inverse of E

			for (int i = 0; i < 3; i++) {
				l2.set(i, l1.get(i)-l2.get(i)*q); // Fill ArrayList 2 with remainders
			}

			for (int k = 0; k < 3; k++){
				l1.set(k, l3.get(k));	// Fill ArrayList 1 with what ArrayList 2 originally had
			}
		}
		

		// Set variables for Bezout Coefficients
		long x = l2.get(1);
		long y = l2.get(2);

		//Display results
		System.out.println("The Bezout Equation for E and phi is given by:");
		System.out.println(y + "*" + E + "+" + x + "*" + phi + " = " + (y * E + x * phi));

		// Check to make sure y is negative
		if(y < 0){
			y = y + phi; // Make y positive
		}

		long D = y;

		System.out.println("Private Key: " + D);
		System.out.println("D*E mod phi = " + (D * E) % phi);
		System.out.println(D + " * " + E + " mod " + phi + " = " + (D * E) % phi);
		System.out.println();
		System.out.println("-----------------------------------------");

        // Encrypt ASCII-coded message using the public key

		// Empty string to be filled with encrypted numbers
		String stringEncrypted = "";
		
		// Let use be P*Q to make calculations easier
		long use = P*Q;
		
		// Array to be used to hold ASCIIvalues
		long[] val = new long[ASCIIvalues.length];

		for(int i = 0; i<ASCIIvalues.length;i++){
			// "a" will always be the value we are trying to encrypt
			long a = ASCIIvalues[i];
			
			// Just instantiating c (value will be changed through loop)
			long c = 0;

			for(int z = 0; z<E-1; z++){
				if(z == 0){ // If at initial loop then we can let c = (a*a)%use then use that value of c in the next part of the loop
					c = (a * a)%use;
				}
				else{
					c = (c * a)%(use); // Updating c until the end of the loop
				}
			}
			val[i] = c;
		}

		// Use loop to setup encrypted String
		for(int k = 0;k<val.length;k++){
			stringEncrypted = stringEncrypted + val[k] + " ";
		}

		System.out.println();
		System.out.println("After encrypting with public key:");
		System.out.println();
		System.out.println(stringEncrypted);
		System.out.println();
		System.out.println("-----------------------------------------");

        // Decrypt the ASCII encoded text so that we get original plaintext
       
		// Same scenario as encryption expect we know have D - 1 instead of E - 1.
		String stringDecrypted = "";

		long[] val2 = new long[val.length];

		for(int i = 0; i<val.length;i++){
			long first = val[i];

			for(int k = 0; k<D-1;k++){
				val2[i] = (first * val[i])%(use); // Value will always be updated after each iteration
				first = val2[i];
			}
		}

		// Fill string with decrypted version, should be original ASCII values.
		for(int i = 0;i<val2.length;i++){
			stringDecrypted = stringDecrypted + val2[i] + " ";
		}

		/////////////////////////////////////////////////////
		System.out.println();
		System.out.println("The ASCII sequence after decrypting with the private key is:");
		System.out.println();
		System.out.println(stringDecrypted);
		System.out.println();
		System.out.println("-----------------------------------------");


        // Print ASCII characters based on array of integers
		String message = "";
		for (int o = 0; o < val2.length; o++) {
			message = message + (char) val2[o];
		}

		System.out.println();
		System.out.println("The decrypted message is:");
		System.out.println();
		System.out.println(message);
		System.out.println();
		System.out.println("-----------------------------------------");

		// Digitally sign your message
        
        //To digitally sign your message, input a 4 digit PIN...

		System.out.println("Enter a 4 digit integer 'PIN' : ");
		int pin = keyboard.nextInt();

		// Let variable be equal to -1 just to make debugging easier (value should be changed anyways in theory)
		long PINencrypted = -1;

		// Just to make things clearer when doing calculations
		long first = pin;

		// Same scenario when we were encrypting and decrypting expect with different variables
		for(int z = 0; z<D-1; z++){
			PINencrypted = (first * pin)%(use);
			first = PINencrypted;
		}

		/////////////////////////////////////////////////////////

		System.out.println();
		System.out.println("The encrypted PIN is: " + PINencrypted);
		System.out.println();

		// Then you can tell the receiver, "my PIN is 1234, do not accept any message 
		// from someone whose PIN does not decrypt to 1234." Then, you simply attach 
		// your encrypted PIN at the end of the message you send. To decrypt, the 
		// receiver uses the public key, which everyone knows. If the decrypted PIN 
		// matches 1234, then the receiver knows the mssage came from you.

		// Same situation as encrypting the original pin, but with different variables
		long PINdecrypted = -1;

		first = PINencrypted;

		for(int i = 0; i<E-1;i++){
			PINdecrypted = (first * PINencrypted)%(use);
			first = PINdecrypted;
		}

		/////////////////////////////////////////////////////

		System.out.println();
		System.out.println("The decrypted PIN is: " + PINdecrypted);
		System.out.println();


		keyboard.close();
	}

    // Helper Functions / Subroutines

    // Defines two large random primes
	public static int bigPrime() {
		boolean prime = false;
		int n = 0;
		while (!prime) {
			Random rand = new Random();
			n = rand.nextInt(500);
			n = 2 * (n + 500) + 1;
			int sqrtn = (int) Math.pow(n, 0.5) + 1;
			for (int i = 3; i < sqrtn; i += 2) {
				if (n % i == 0) {
					prime = false;
					break;
				} else {
					prime = true;
				}
			}
		}
		return n;
	}

	// GCD of two long integers
	public static long gcd(long a, long b) {
		if (b == 0) {
			return a;
		}
		return gcd(b, a % b);
	}

}
