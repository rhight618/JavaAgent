package com.cs.agent;

import java.lang.instrument.Instrumentation;

public class Agent {

	public static void premain(String args, Instrumentation inst)
			throws InstantiationException, IllegalAccessException {
		
		ImportantLogClassTransformer transformer = new ImportantLogClassTransformer();
		inst.addTransformer(transformer);
	}

	public static void agentmain(String args, Instrumentation inst)
			throws InstantiationException, IllegalAccessException {

		premain(args, inst);
	}
	
	public static void main (String[] args){
		
		int lineNumber =  1;
		String className = "stuff";
		String methodName = "things";
		
		String line = "{ System.out.println(\"Class: " + className + "-> Method Name: " + methodName + " at Line Number: " + lineNumber + "\"); $_ = $proceed($$); }";
		
		System.out.println(line);
		
		
		
		
		
	}
	
	
}
