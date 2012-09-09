package yourcv.xing;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static final EntityManagerFactory emfinstance = Persistence.createEntityManagerFactory("transactions-optional");
	
	private EMF () {}
	
	public static EntityManagerFactory get() {
		return emfinstance;
	}

}
