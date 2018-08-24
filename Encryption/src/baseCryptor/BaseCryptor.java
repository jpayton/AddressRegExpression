package baseCryptor;
//https://docs.oracle.com/javase/6/docs/api/javax/crypto/Cipher.html
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Object;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.lang.IllegalStateException;
import java.security.InvalidAlgorithmParameterException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.security.Security;

public class BaseCryptor {

	static final String ERROR_HERE = "ERROR OCCURED HERE: ";
	static final String ERROR_CAUSEDBY = "\nCAUSED BY:\t ";
	static final int ENCHEADER = 23;
	static final int ENCLIST = 24;
	static final String DEFAULT_ENC_ALGORITHMS [ ] = {"DES/CBC", "DES","AES", "AES-GCM", "CFB", "OFB"};
	static final int HDR_DATALEN = 1;
	static final int HDR_ALGORITHMCHOICE = 0;	
	static final int HDR_MARKER = 2;
	static final int MARKER_ENCLIST = 1;
	static final int MARKER_KEY = 0;
	private String encryptedData = null;
	private String outputData = "";
	
	private SecretKeySpec key = null; 
	private IvParameterSpec ivSpec = null; 
	private String BaseListOfEncryptions [] = null;
	
	private String listOfEncryptions [ ] = null;
	int numOfEncryptionTypes= 0;
	
	private Cipher cipher = null;
	
	public BaseCryptor() {}
	
	public BaseCryptor(int mode, String theData, String randomKey ) {
	
		init();
		listOfEncryptions = BaseListOfEncryptions;
		outputData = "";
		
		//load the various encryption types
		
	}

	private void init() {
	
		this.BaseListOfEncryptions = DEFAULT_ENC_ALGORITHMS;
		numOfEncryptionTypes = this.BaseListOfEncryptions.length;
	}
	/**
	 * Define the encryptionlist of the encryption types that can be used 
	 * to encrypt the data. 
	 * 
	 * @param encList
	 */
	private void setEncryptorList(String encList []) {
		
		this.BaseListOfEncryptions = encList;
		this.numOfEncryptionTypes = BaseListOfEncryptions.length;
	}
	
	private String [ ] getEncryptorList() {
		return BaseListOfEncryptions;
	}
	/**
	 * Return the seconds and mode it with the number of encryptoins types
	 * use this number to make a selection of encryptions to use.
	 * @return
	 */
	private int selectEncyption() {
	
		LocalDateTime timePoint = LocalDateTime.now(); 
		
		return randomSelection(numOfEncryptionTypes);
	}
	private int selectEncyptionMode() {
		
		LocalDateTime timePoint = LocalDateTime.now(); 
		//TODO:  NumofEncryptionMode
		return randomSelection(numOfEncryptionTypes);
	}
	
	private int randomSelection(int modulusValue) {
		
		LocalDateTime timePoint = LocalDateTime.now(); 
		
		return (timePoint.getSecond() % modulusValue);
	}

	/**
	 * Generate a timestamp to use as a random-key to encrypt the data. 
	 * 
	 * @return
	 */
	private String getTimeStamp() {
		
		LocalDateTime timePoint = LocalDateTime.now(); 
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss");
		return timePoint.format(formatter);
	}
	
	public String process(int mode, String theData, String theCommand) throws Exception {
		
		String outputData = "";
		
		String randomKey = getTimeStamp();
		listOfEncryptions = BaseListOfEncryptions;
		
		switch(mode) {
			case 0:
				outputData = encrypt(theData, randomKey, theCommand);
				break;
				
			case 1:
				outputData = decrypt(theData, randomKey, theCommand);
				break;
				
			default:
				throw new Exception ("Unknow mode type" + mode);  
		}
		
		return outputData;
	}
	
	private void buildKey(String randomKey, String encodeOptions) {
		
		byte[] keyBytes;
		byte[] encodeOptionsBytes;
		
		keyBytes = randomKey.getBytes();
		encodeOptionsBytes = encodeOptions.getBytes();
		// wrap key data in Key/IV specs to pass to cipher
		key = new  SecretKeySpec(keyBytes, "AES");
		//ivSpec = new IvParameterSpec(ivBytes);
		ivSpec = new IvParameterSpec(encodeOptionsBytes);

	}

	private String decrypt(String theData, String randDomKey, String command) throws Exception {
		
		String outData = null;
		String hdr = null;
		String marker = null;
		int dataLen = 0;
		int algorithmChoice = 0;
		int markerLen = 0;
		byte encryptedDataBytes [] = null;
		byte outDataBytes [] = null;
		int p1 = 0;
		int p2 = 0;
		String randomKey = null;
		String encryptionAlgorithm = null;
		
		hdr = theData.substring(0, ENCHEADER);
		dataLen = this.decodeHeader(hdr,HDR_DATALEN);
		algorithmChoice = this.decodeHeader(hdr,HDR_ALGORITHMCHOICE);
		markerLen = this.decodeHeader(hdr,HDR_MARKER);
		p1 = Math.round(dataLen/2);
		p2 = dataLen - p1;
		
		marker = theData.substring(ENCHEADER + Math.round(dataLen/2),markerLen); 
		//randomKey = decodeMarker(marker,MARKER_KEY);		
		this.setEncryptorList(decodeMarker(marker,MARKER_ENCLIST)); 
		
		encryptedData = theData.substring(ENCHEADER, p1) 
		 	   + theData.substring(ENCHEADER + p1 + markerLen, p2);
		encryptedDataBytes = encryptedData.getBytes();

		if (!validateEncryptionType(listOfEncryptions[algorithmChoice])) {
			throw new Exception("Unknown encryption: " + listOfEncryptions[algorithmChoice]);
		}
		//TODO: you may need to store the full encryption mode 
		encryptionAlgorithm = listOfEncryptions[algorithmChoice] + "/PKCS5Padding";
		buildKey(randomKey,encryptionAlgorithm);
		
		try {
			cipher = Cipher.getInstance(encryptionAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
			//cipher.doFinal(encryptedDataBytes, 0, encryptedDataBytes.length, outDataBytes,0);
			outDataBytes = cipher.doFinal(encryptedDataBytes); 
		} 
		catch (InvalidAlgorithmParameterException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (IllegalArgumentException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (InvalidKeyException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (IllegalBlockSizeException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		//catch (ShortBufferException sb) {System.out.println("Error: " + sb);}
		catch (IllegalStateException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (NullPointerException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (NoSuchAlgorithmException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (BadPaddingException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}		
		catch (Exception ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}

		outData = outDataBytes.toString();
		return outData;
	}
	
	private boolean validateEncryptionType(String enc) {
		
		switch (enc) {
		case "DES":
		case "AES":
		case "CFB":
		case "CBC":
		case "OFB":
		case "CFB8":
		case "OFB8":
			return true; 
		default:
			return false;
		}
	}
	
	private String encrypt(String theData, String randomKey, String command) throws Exception {

		String hdr = "";
		String marker = "";
		int algorithmChoice = 0;
		String encryptionType = "";
		byte inDataBytes []= null;
		byte outDataBytes []= null;
		String commandItem [ ] = null;

		//String xx [ ] = {"DES/CBC", "CFB", "OFB", "CFB8", "OFB8"};
		
		//this.setEncryptorList(xx);
	
		commandItem = command.split("/");
		if (commandItem[0] != null)
			this.setEncryptorList(commandItem[0].split("|"));

		algorithmChoice = this.selectEncyption();

		if (!validateEncryptionType(listOfEncryptions[algorithmChoice])) {
			throw new Exception("Unknown encryption: " + listOfEncryptions[algorithmChoice]);
		}
		encryptionType = listOfEncryptions[algorithmChoice];
		
		//encryptionType+= "/PKCS5Padding";
		if (randomKey.trim() == null) { 
			randomKey = getTimeStamp();
		}
		buildKey(randomKey, encryptionType);
		inDataBytes = theData.getBytes();
		
		try {
			cipher = Cipher.getInstance(encryptionType);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			//cipher.doFinal(inDataBytes, 0, inDataBytes.length,outDataBytes,0);
			outDataBytes = cipher.doFinal(inDataBytes); 
			marker = buildMarker(getEncryptorList(), randomKey);
			encryptedData = outDataBytes.toString();
			hdr = buildHeader(encryptedData.length(),algorithmChoice,marker.length());
		} 
		catch (InvalidAlgorithmParameterException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (IllegalArgumentException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (InvalidKeyException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (IllegalBlockSizeException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		//catch (ShortBufferException sb) {System.out.println("Error: " + sb);}
		catch (IllegalStateException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (NullPointerException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (NoSuchAlgorithmException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		catch (BadPaddingException ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}		
		catch (Exception ex) {
			System.out.print(ERROR_HERE + ex + ERROR_CAUSEDBY);
			ex.printStackTrace();}
		
		int p1 = Math.round(encryptedData.length()/2);
		int p2 = encryptedData.length() - p1; 
		return hdr + encryptedData.substring(0,p1)
				+ marker + encryptedData.substring(p1, p2);
	}
	
	private String buildHeader(int dataLen, int choice, int markerLen) {
		
		return String.format("}%4d:%8d:%4d{",choice, dataLen,markerLen); 
	}
	
	private String buildMarker(String enc[], String theKey) {
		
		String marker = null; 
		for (int x = 0; x < enc.length; x++)
			if (x != 0 )
				marker += "^" + enc[x];
			else
				marker = enc[x];
		
		return "}" + theKey + "|" + marker + "{";
	}

	private String [ ] decodeMarker(String marker, int markerPlace) {
		
		String items [] = marker.substring(1, marker.length() -2).split("|");
		if (markerPlace > items.length || markerPlace < 0)
			return null;
		
		switch(markerPlace) {
		case MARKER_ENCLIST:
			return items[MARKER_ENCLIST].split("^");
		case MARKER_KEY:
			return items[MARKER_KEY].split("");
		default:
				return null;
		}
	}
	
	private int decodeHeader(String header, int hdrPlace) {
		
		int ans = 0;
		String items [] = header.substring(1, header.length() -2).split(":");
		if (hdrPlace > items.length || hdrPlace < 0)
			ans = -1;
		else {
			switch(hdrPlace) {
			case HDR_MARKER:
				ans = Integer.parseInt(items[hdrPlace]);
				break;
			case HDR_ALGORITHMCHOICE:
				ans = Integer.parseInt(items[hdrPlace]);
				break;
			case HDR_DATALEN:
				ans = Integer.parseInt(items[hdrPlace]);
				break;
			}
		}
		return ans; 
	}
	
	private void SecurityListings() { 
		
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider: " + provider.getName());
            for (Provider.Service service : provider.getServices()) {
                System.out.println("  Algorithm: " + service.getAlgorithm());
            }
        }

    }
	
	public static void main(String [] Var) {

		
		BaseCryptor bc = new BaseCryptor();
		
		bc.SecurityListings();
		String theCommand = "AES/ECB/PKCS%Padding";
		String theData = "The quick brown fox jumped over the lazy dog. "
				+  "Please don't let the cat know about this.";
		String buzz [] = {"AES", "DES"};

		try {
			bc.setEncryptorList(buzz);
			bc.process(0, theData, theCommand) ; 
		} catch (Exception ex) {System.out.println("Error: " + ex);}
	}

}
