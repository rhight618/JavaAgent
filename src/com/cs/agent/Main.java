package com.cs.agent;
import java.util.LinkedList;
import java.util.List;

import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Mnemonic;

public class Main {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		
//		Class<?> intercepted = new ByteBuddy()
//				.subclass(Object.class)
//				.method(ElementMatchers.named("getFinalScore"))
//				.intercept(MethodDelegation.to(Augment.class))
//				.make()
//				.load(Main.class.getClassLoader(),ClassLoadingStrategy.Default.WRAPPER)
//				.getLoaded();
//		
//		System.out.println(intercepted.newInstance());
		
		
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
		  		    int index = ci.next();
		  		    int op = ci.byteAt(index);
		  		    System.out.println("Opcode: " + op + " Op: " + Mnemonic.OPCODE[op]);
		  		    operations.add(Mnemonic.OPCODE[op]);
		  		}
		  	    
		  	} catch (NotFoundException | BadBytecode e) {
		  		// TODO Auto-generated catch block
		  		e.printStackTrace();
		  	}

		
		String longName = getLongName("Ryan", "Hightower");
		System.out.println(longName);

	}
	
	public static String getLongName(String first, String last){
		return first + " " + last;
	}

}
