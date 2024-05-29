package vortex.annotate.manager;

import java.net.URLClassLoader;

public class PackageLoader {
    
    private static PackageLoader packageLoader;
    private URLClassLoader loader;
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
    public URLClassLoader getLoader() {
	return loader;
    }
    public void setLoader(URLClassLoader loader) {
	packageLoader.loader = loader;
    }

}
