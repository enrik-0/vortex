package vortex.annotate.manager;

import java.net.URLClassLoader;

public class PackageLoader {
    
    private static PackageLoader packageLoader;
    private ClassLoader loader;
    public static PackageLoader getInstance() {
	synchronized (PackageLoader.class) {
	    if (packageLoader == null) {
		packageLoader = new PackageLoader();
	    }

	}


	return packageLoader;
    }
    private PackageLoader() {
    }
    public ClassLoader getLoader() {
	return loader;
    }
    public void setLoader(ClassLoader loader) {
	packageLoader.loader = loader;
    }

}
