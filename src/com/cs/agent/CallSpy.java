package com.cs.agent;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Mnemonic;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

public class CallSpy implements ClassFileTransformer {
  @Override
  public byte[] transform(//region other parameters
                          ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          //endregion
                          byte[] classfileBuffer) throws IllegalClassFormatException {

	  
	ClassPool cp = ClassPool.getDefault();
    cp.importPackage("com.cs.assignment2");

    //region filter agent classes
    // we do not want to profile ourselves
    if (className.startsWith("com/zeroturnaround/callspy")) {
      return null;
    }
    //endregion

    //region filter out non-application classes
    // Application filter. Can be externalized into a property file.
    // For instance, profilers use blacklist/whitelist to configure this kind of filters
    if (!className.startsWith("com/cs")) {
      return classfileBuffer;
    }
    //endregion
    
    System.out.println("Classname: " + className);
    
    try {
      	
      CtClass ct = cp.makeClass(new ByteArrayInputStream(classfileBuffer));

      CtMethod[] declaredMethods = ct.getDeclaredMethods();
      CtField[] declaredFields = ct.getDeclaredFields();
      
      for (CtMethod method : declaredMethods) { 	  
    	  String methodString = "Executing method: " + method.getName();
    	  method.insertBefore("System.out.println(\"" + methodString + "\");");
      }
      
      for (CtField field : declaredFields) {

      }
      
      ClassPool classPool = ClassPool.getDefault();
      ClassFile classfile;
	  	try {
	  		classfile = classPool.get("Main")
	  		  .getClassFile();
	  	    MethodInfo minfo = classfile.getMethod("getLongName");
	  	    CodeAttribute ca = minfo.getCodeAttribute();
	  	    CodeIterator ci = ca.iterator();
	  	    
	  		List<String> operations = new LinkedList<>();
	  		while (ci.hasNext()) {
	  			System.out.println(ci.get().toString());
	  		    int index = ci.next();
	  		    int op = ci.byteAt(index);
	  		    operations.add(Mnemonic.OPCODE[op]);
	  		}
	  	    
	  	} catch (NotFoundException | BadBytecode e) {
	  		// TODO Auto-generated catch block
	  		e.printStackTrace();
	  	}


      return ct.toBytecode();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    
    
    

    return classfileBuffer;
  }
}
