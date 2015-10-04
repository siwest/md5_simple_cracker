import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SimpleCracker {

	public SimpleCracker() {
		// TODO Auto-generated constructor stub
	}
	
	/* Reads any text file. 
	 * Each line is saved as a string in an array.
	 * 
	 */
	static String[] readFile(String file) throws IOException {
		
	        FileReader fileReader = new FileReader(file);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        List<String> lines = new ArrayList<String>();
	        String line = null;
	        while ((line = bufferedReader.readLine()) != null) {
	            lines.add(line);
	        }
	        bufferedReader.close();
	        return lines.toArray(new String[lines.size()]);
	    }
	
	
	/* 
	 * Given by Prof Reza
	 * Converts a a string of characters, which may contain non-printable characters, 
	 * to the hexadecimal representation of the characters.
	 * 
	 */
	public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
	
	


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/* Read shadow file, save the Usernames, salt, and hashes into memory
		 * 
		 */
		byte[] h = null;
		try {
			String[] shadowPassword = readFile("shadow-simple");
			String[] users = new String[9];
			String[] salts = new String[9];
			String[] hashes = new String[9];
			
			for (int i=0; i< shadowPassword.length -1; i++) {
				users[i] = shadowPassword[i].substring(0, 5);
				salts[i] = shadowPassword[i].substring(6, 14);
				hashes[i] = shadowPassword[i].substring(15, shadowPassword[i].length());
		//		System.out.println("user hash " +hashes[i]);

			}
			
			
			/* Read the dictionary*/
			String[] dictionary = readFile("common-passwords.txt");
			
			
			/* Create hash array with a hash for every dictionary item concatenated with every salt*/
			byte[][] dictionaryHashes = new byte [(dictionary.length-1)*(salts.length-1)][32];
			String[] hexDictionaryHashes = new String [(dictionary.length-1)*(salts.length-1)];
			
			/* Create a every possible dictionary hash, given each salt from shadowPasswords*/
			for (int i=0; i< dictionary.length -1; i++) 
				for ( int j=0; j< salts.length -1; j++) {
					
					String dictionaryItem = dictionary[i];
					String userSalt = salts[j];
					String dict_salt = dictionaryItem + userSalt;
					
					
					/* MD5 message-digest algorithm takes as input a message of arbitrary length 
					 * and produces as output a 128-bit "fingerprint" or "message digest" of the input
					*/
					MessageDigest hashGenerator = null;
			        try
			        {
			            //initialize the MD5 hash generator
			            hashGenerator = MessageDigest.getInstance("MD5");
			        }
			        catch (NoSuchAlgorithmException ex)
			        {
			            ex.printStackTrace();
			        }
		
					
					// here's the digest: 32 characters
					dictionaryHashes[i * (salts.length -1) + j] = hashGenerator.digest(dict_salt.getBytes());
					byte[] aHash = dictionaryHashes[i * (salts.length -1) + j];

					/* Convert each dictionaryHash to Hex */
					String hexHashString = toHex(aHash);
					System.out.println(hexHashString);		
					hexDictionaryHashes[i * (salts.length -1) + j] = hexHashString;
				}
			}
			
			// Here's the test.. is there a match?
			int null_count = 0;
			for (int m=0; m< hexDictionaryHashes.length -1; m++) {
			//	System.out.println("dict harsh " + hexDictionaryHashes[m]);
				for (int n=0; n< hashes.length -1; n++) {
				//	System.out.println("user hash " + hashes[n]);
					if (hexDictionaryHashes[m].trim().compareTo(hashes[n].trim()) == 0) {
						System.out.print(users[n] + " is a match");
					}
				}
			}
			
			System.out.println("Here already");
		//	
		//	String hexPassword = toHex(shadowPassword);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		}
}
