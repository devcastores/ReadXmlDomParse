package com.grupocastores.mcc.xml;

import java.util.HashMap;  
import java.util.TreeMap;  
import java.util.Iterator;

public class CoincidenciaISMA {
	public static void main(String[] args) {

		String text = "impuestos sobre medio ambiente";
		text = text.toLowerCase(); //all minusculas
		
		boolean is_I = false;
		boolean is_S = false;
		boolean is_M = false;
		boolean is_A = false;
		
		HashMap<Integer, String> hm = new HashMap<Integer, String>();  
		
		for (int i = -1; (i = text.indexOf("i", i + 1)) != -1; i++) {
			hm.put(i, "i");
		}
		for (int i = -1; (i = text.indexOf("s", i + 1)) != -1; i++) {
			hm.put(i, "s");
		}
		for (int i = -1; (i = text.indexOf("m", i + 1)) != -1; i++) {
			hm.put(i, "m");
		}
		for (int i = -1; (i = text.indexOf("a", i + 1)) != -1; i++) {
			hm.put(i, "a");
		}
		  
		TreeMap<Integer,String> tm = new  TreeMap<Integer,String> (hm);  
		Iterator<Integer> itr = tm.keySet().iterator();      
		
		while(itr.hasNext())    
		{    
			int key=(int)itr.next(); //posicion
			String letra = hm.get(key); //letra
			
			if(letra.equalsIgnoreCase("i")) {
				is_I = true;
			}
			
			if(is_I) {
				if(letra.equalsIgnoreCase("s")) {
					is_S = true;
				}
			}
			
			if(is_I) {
				if(is_S) {
					if(letra.equalsIgnoreCase("m")) {
						is_M = true;
					}
				}
			}
			
			if(is_I) {
				if(is_S) {
					if(is_M) {
						if(letra.equalsIgnoreCase("a")) {
							is_A = true;
						}
					}
				}
			}
			
			if(is_A) {
				System.out.println("no:  "+key+"     name:   "+letra+" [ SUCCESS ]"); 
				break;
			}else {
				System.out.println("no:  "+key+"     name:   "+letra); 
			}
			
			
		}    

	}
}
