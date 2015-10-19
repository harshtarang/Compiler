# Compiler

This application is a compiler for a small language. The program to be compiled and run should have no method.All the code to be processed should be written within the class consruct itself. Following is the example of a program which compiles and runs successfully using this compiler.

class B {

def k: int;
k = 6;
while (k>0) 
{ print k; 
k=k-1;
}; 
print "done";

def l1 : @[boolean]; 
    l1 = @[true, false]; 
		l1[2]=true; 
		print l1[0];
		print l1[1];
		print l1[2];
		
		};

}


Output:

prints 6,5,4,3,2,1,done, true, false, true
