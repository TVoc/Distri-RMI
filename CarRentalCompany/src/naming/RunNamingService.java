package naming;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import agency.IRentalAgency;
import agency.RentalAgency;

public class RunNamingService {

	public static void main(String[] args) {
		INamingService service = new NamingService();
		try {
			Registry registry = LocateRegistry.getRegistry();
			IRentalAgency agency = (IRentalAgency) registry.lookup("agency");
			INamingService stub = (INamingService) UnicastRemoteObject.exportObject(service, 0);
			agency.registerNamingService(stub);
			System.out.println("Naming service ready.");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

}
