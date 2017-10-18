package com.cs.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Agent {

	public static void premain(String args, Instrumentation inst)
			throws InstantiationException, IllegalAccessException {
		
		// CallSpy transformer = new CallSpy();
		
		// ImportantLogClassTransformer transformer = new ImportantLogClassTransformer();
		// inst.addTransformer(transformer);

		inst.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classFileBuffer) {
				
				if(!className.contains("com/cs")){
					return null;
				}
				
				System.out.println("Intercepted Class: " + className);
				ClassReader cr = new ClassReader(classFileBuffer);
				ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
				cr.accept(cw, 0);
				return cw.toByteArray();
			}
		});

	}

	public static void agentmain(String args, Instrumentation inst)
			throws InstantiationException, IllegalAccessException {

		premain(args, inst);
	}

}
