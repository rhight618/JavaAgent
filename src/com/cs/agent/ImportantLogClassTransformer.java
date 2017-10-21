package com.cs.agent;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class ImportantLogClassTransformer implements ClassFileTransformer {
	private ClassPool pool;

	public ImportantLogClassTransformer() {
		pool = ClassPool.getDefault();
	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		try {
			
			if (!className.contains("com/cs/assignment/main")){
				return null;
			}
			
			
			pool.insertClassPath(new ByteArrayClassPath(className, classfileBuffer));
			CtClass cclass = pool.get(className.replaceAll("/", "."));
			if (!cclass.isFrozen()) {
				for (CtMethod currentMethod : cclass.getDeclaredMethods()) {
				
					currentMethod.instrument(
						    new ExprEditor() {
						        public void edit(MethodCall m)
						                      throws CannotCompileException
						        {
						        	m.replace("{ System.out.println(\"Class: " + className + "-> Method Name: " + m.getMethodName() + " at Line Number: " + m.getLineNumber() + "\"); $_ = $proceed($$); }");

						        }
						        
						        public void edit(FieldAccess f)
					                      throws CannotCompileException
						        {
						        	
						        	f.replace("{ System.out.println(\"Class: " + className + "-> Field Access Name: " + f.getFieldName() + " at Line Number: " + f.getLineNumber() + "\"); $_ = $proceed($$); }");

						        }
						        
						        
						    });
				}
				return cclass.toBytecode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}