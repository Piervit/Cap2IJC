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

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Cap2IJC {


	/**
	 * 
	 * @param sCapFileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void readCap(Options options)
			throws FileNotFoundException, IOException {
		byte[] baHeader = null;
		byte[] baDirectory = null;
		byte[] baApplet = null;
		byte[] baImport = null;
		byte[] baConstantPool = null;
		byte[] baClass = null;
		byte[] baMethod = null;
		byte[] baStaticField = null;
		byte[] baRefLocation = null;
		byte[] baDescriptor = null;
		byte[] baExport = null;
		byte[] baDebug = null;

        String sCapFileName = options.getCapPath();

		ZipInputStream zin = new ZipInputStream(new FileInputStream(
				sCapFileName));
		ZipEntry zEntry;

		while ((zEntry = zin.getNextEntry()) != null) {
			// System.out.println("Reading --- " + zEntry.getName());

			if (zEntry.isDirectory()) {
				System.out.println("Found " + zEntry.getName() + " directory!");
			} else {
				String[] sDirs;
				String sFileName = zEntry.getName();
				sDirs = sFileName.split("/");
				if (sDirs[sDirs.length - 1].equals("Header.cap")) {
					System.out.println("Processing Header.cap...");
					baHeader = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Directory.cap")) {
					System.out.println("Processing Directory.cap...");
					baDirectory = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Applet.cap")) {
					System.out.println("Processing Applet.cap...");
					baApplet = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Import.cap")) {
					System.out.println("Processing Import.cap...");
					baImport = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("ConstantPool.cap")) {
					System.out.println("Processing ConstantPool.cap...");
					baConstantPool = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Class.cap")) {
					System.out.println("Processing Class.cap...");
					baClass = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Method.cap")) {
					System.out.println("Processing Method.cap...");
					baMethod = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("StaticField.cap")) {
					System.out.println("Processing StaticField.cap...");
					baStaticField = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("RefLocation.cap")) {
					System.out.println("Processing RefLocation.cap...");
					baRefLocation = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Export.cap")) {
					System.out.println("Processing Export.cap...");
					baExport = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Descriptor.cap")) {
					System.out.println("Processing Descriptor.cap...");
					baDescriptor = setComponent(zEntry, zin);
				} else if (sDirs[sDirs.length - 1].equals("Debug.cap")) {
					System.out.println("Processing Debug.cap...");
					baDebug = setComponent(zEntry, zin);
				}
			}
		}
		zin.close();

		String[] sDirsOut;
		sDirsOut = sCapFileName.split("/");
		String sCapFile = sDirsOut[sDirsOut.length - 1];
		String sIJCFileName = null; 
        if(options.getOutPath() != null){
            sIJCFileName = options.getOutPath();
        }
        else{
            sIJCFileName = sCapFile.substring(0, sCapFile.length() - 4)
				+ ".ijc";
        }
		System.out.println(sIJCFileName);
		File fOut = new File(sIJCFileName);

		BufferedOutputStream fos = new BufferedOutputStream(
				new FileOutputStream(fOut));

		if (!options.getAllComponent()) {
			/*
			 * Install order defined in JCVM specification section 6.2
			 * Descriptor component will be not included.
			 */
			outComponent(fos, baHeader);
			outComponent(fos, baDirectory);
			outComponent(fos, baImport);
			outComponent(fos, baApplet);
			outComponent(fos, baClass);
			outComponent(fos, baMethod);
			outComponent(fos, baStaticField);
			outComponent(fos, baExport);
			outComponent(fos, baConstantPool);
			outComponent(fos, baRefLocation);
			// outComponent(fos,baDescriptor);
			// outComponent(fos,baDebug);
		} else {
			/*
			 * Install order defined in JCVM specification section 6.2 All
			 * components are included.
			 */
			outComponent(fos, baHeader);
			outComponent(fos, baDirectory);
			outComponent(fos, baImport);
			outComponent(fos, baApplet);
			outComponent(fos, baClass);
			outComponent(fos, baMethod);
			outComponent(fos, baStaticField);
			outComponent(fos, baExport);
			outComponent(fos, baConstantPool);
			outComponent(fos, baRefLocation);
			outComponent(fos, baDescriptor);
			outComponent(fos, baDebug);
		}
		fos.close();
	}

	public static void outComponent(OutputStream os, byte[] baComp)
			throws IOException {
		if (baComp != null) {
			os.write(baComp);
		}
	}

	public static byte[] setComponent(ZipEntry zEntry, ZipInputStream zin)
			throws IOException {
		int length = (int) zEntry.getSize();
		byte[] baComp = new byte[length];
		int b;
		int iPos = 0;
		while ((b = zin.read()) != -1) {
			baComp[iPos++] = (byte) b;
		}
		return baComp;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		try {
			System.out.println("---Cap2IJC version 1.0---");
            Options options = Options.getOption();
            options.getArgs(args);
            readCap(options);

		} catch (FileNotFoundException fnfe) {
			System.out.println("File " + args[0] + " Not Found");
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("IO Exception!");
			ioe.printStackTrace();
		}
	}
}
