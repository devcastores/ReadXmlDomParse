package com.grupocastores.mcc.xml;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;

public class CoincidenciasStringv2 {

	public static void main(String[] args) {

		HashMap<Integer, Character> hm = new HashMap<Integer, Character>();

		String text = "impuestos sobre hospedaje";
		text = text.toLowerCase();

		char array[] = { 'i', 's', 'h' }; //

		boolean is_letra[] = new boolean[array.length];

		for (int x = 0; x < array.length; x++) {
			for (int i = -1; (i = text.indexOf(array[x], i + 1)) != -1; i++) {
				hm.put(i, array[x]);
			}
		}

		TreeMap<Integer, Character> tm = new TreeMap<Integer, Character>(hm);
		Iterator<Integer> itr = tm.keySet().iterator();
		int contadorvanderas = 0;
		while (itr.hasNext()) {
			int key = (int) itr.next();
			Character letra = hm.get(key);

			if (letra.equals(array[contadorvanderas])) {
				contadorvanderas++;
			}

			for (int x = 0; x < is_letra.length; x++) {
				if (contadorvanderas == (x + 1)) {
					is_letra[x] = true;
					break;
				}
			}

			int contador = 0;
			for (int x = 0; x < is_letra.length; x++) {
				if (is_letra[x]) {
					contador++;
				}
			}

			if (contador == is_letra.length) {
				System.out.println("no:  " + key + "     name:   " + letra + " [ SUCCESS ]");
				break;
			} else {
				System.out.println("no:  " + key + "     name:   " + letra);
			}
		}

	}
}
