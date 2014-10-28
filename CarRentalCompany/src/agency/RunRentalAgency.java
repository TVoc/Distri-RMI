package agency;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RunRentalAgency {

	public static void main(String[] args) {
		IRentalAgency agency = new RentalAgency();
		try {
			Registry registry = LocateRegistry.getRegistry();
			IRentalAgency stub = (IRentalAgency) UnicastRemoteObject.exportObject(agency, 0);
			registry.rebind("agency", stub);
			System.out.println("Agency ready.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
