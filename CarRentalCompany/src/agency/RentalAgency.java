package agency;

import naming.INamingService;
import rental.ICarRentalCompany;
import sessions.SessionManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Mediator Server that serves as access point for both the INamingService of
 * the system, as well as the SessionManager.
 */
public class RentalAgency implements Remote {
    //------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------
    public RentalAgency() {

    }

    //------------------------------------------------------------------------
    // Naming service
    //------------------------------------------------------------------------
    /**
     * Register a new ICarRentalCompany in the INamingService of this
     * RentalAgency
     *
     * @param newCompany
     *      The new IRentalCompany to be registered within this RentalAgency.
     *
     * @throws RemoteException
     *     | ICarRentalCompany == null
     *     | ICarRentalCompany.getName() == null
     */
    public void register(ICarRentalCompany newCompany) throws RemoteException {
        this.getNamingService().register(newCompany);
    }

    /**
     * Unregister the ICarRentalCompany with the specified name from
     * this RentalAgency INamingService.
     *
     * @param name
     *      The name of the ICarRentalCompany to be removed.
     *
     * @throws RemoteException
     *      | name == null
     */
    public void unregister(String name) throws RemoteException {
        this.getNamingService().unregister(name);
    }

    /**
     * Get the INamingService of this RentalAgency.
     *
     * @return The INamingService of this RentalAgency.
     */
    private INamingService getNamingService() {
        return this.namingService;
    }

    /** The INamingService of this RentalAgency. */
    private INamingService namingService;

    //------------------------------------------------------------------------
    // SessionManager
    //---------------------------------------------    ---------------------------
    /** The SessionManager of this RentalAgency */
    private final SessionManager sessionManager;
}
