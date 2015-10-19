import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/* Created by Sarah West
 * for NJIT CS645-851
 * Project 1 - Part 1
 * Simple Cracker for MD5 dictionary attack
 * 
 */



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

		String[] shadowPassword;
		String[] users = new String[9];
		String[] salts = new String[9];
		String[] hashes = new String[9];
		
		MessageDigest hashGenerator;

		try {
			hashGenerator = MessageDigest.getInstance("MD5");
			
			shadowPassword = readFile("shadow-simple");
			
			for (int i=0; i< shadowPassword.length -1; i++) {
				users[i] = shadowPassword[i].substring(0, 5);
				salts[i] = shadowPassword[i].substring(6, 14);
				hashes[i] = shadowPassword[i].substring(15, shadowPassword[i].length());
			}
		
			
			/*
			 * Read dictionary file into memory
			 */
			String[] dictionary = readFile("common-passwords.txt");
			for (int j = 0; j < hashes.length-1; j++) {
				for (int i=0; i < dictionary.length; i++) {
					/*
					 * Concatenate each dictionary item with each salt
					 */
					byte[] saltyPasswordBytes = ((String)(salts[j] +  dictionary[i])).getBytes();
					
					/*
					 * Hash it, and convert it to hex
					 */
					String saltedHashForPassword = toHex(hashGenerator.digest(saltyPasswordBytes));
					
					
					/*
					 * Print if we have a match
					 */
					if (saltedHashForPassword.compareTo(hashes[j]) == 0) {
						System.out.println(users[j] + ":" + dictionary[i]);
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		System.exit(0);
		}
}

