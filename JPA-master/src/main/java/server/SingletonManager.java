package server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SingletonManager {

	static EntityManager manager;
	
	
	public SingletonManager()
	{
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("dev");
		manager = factory.createEntityManager();

	}
	
	public static  EntityManager getManager()
	{
		if(manager == null)
		{
			SingletonManager t = new SingletonManager();
			return t.getManager();
		}
		else return manager; 
	}
	
}
