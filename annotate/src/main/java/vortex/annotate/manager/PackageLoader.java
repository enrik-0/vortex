package vortex.annotate.manager;


/**
 * modifies the class loader when needed
 */
public class PackageLoader {
    
    private static PackageLoader packageLoader;
    private ClassLoader loader;
    /**
     * instanciate the package loader
     * @return {@link PackageLoader}
     */
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
    /**
     * 
     * @return {@link ClassLoader}
     */
    public ClassLoader getLoader() {
	return loader;
    }
    /**
     * set the current {@link ClassLoader}
     * @param loader {@link ClassLoader}
     */
    public void setLoader(ClassLoader loader) {
	packageLoader.loader = loader;
    }

}
