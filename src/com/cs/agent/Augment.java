package com.cs.agent;
import java.lang.reflect.Method;

import net.bytebuddy.implementation.bind.annotation.Origin;

public class Augment {
	
	public static String intercept (@Origin Method m) {
	
		return "Intercepted: Augment Class " + m.getName() ;
	}

}
