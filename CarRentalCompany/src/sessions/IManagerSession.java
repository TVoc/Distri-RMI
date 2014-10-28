package sessions;

import rental.CarType;
import rental.ICarRentalCompany;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

/**
 * Created by Month on 28/10/2014.
 */
public interface IManagerSession extends Remote, Session {
    //------------------------------------------------------------------------
    // Register
    //------------------------------------------------------------------------

    /**
     * Register the specified ICarRentalCompany with this CarRentalAgency
     * through this ManagerSession.
     *
     * @param newComp
     *      The new ICarRentalCompany to be registered.
     *
     * @postcondition
     *      | ICarRentalCompany.getName() in (new this).getRegisteredCompanies()
     */
    public void register(ICarRentalCompany newComp)  throws RemoteException;

    /**
     * Unregister the specified car rental company with the specified name
     * from this CarRentalAgency through this ManagerSession.
     *
     * @param name
     *      The name of the ICarRentalCompany to be unregistered.
     *
     * @postcondition
     *      | !(name in (new this).getRegisteredCompanies()
     */
    public void unregister(String name) throws RemoteException;

    //------------------------------------------------------------------------
    // Companies
    //------------------------------------------------------------------------

    /**
     * Get a List containing all the names of the registered companies in this
     * RentalAgency
     *
     * @return a List containing all the names of the registered companies.
     */
    public List<String> getRegisteredCompanies() throws RemoteException;

    /**
     * Get A list containing all the CarTypes that are available at the given
     * (registered) company.
     *
     * @param companyName
     *      The name of the ICarRentalCompany.
     *
     * @return a List containing all the CarTypes in the specified ICarRentalCompnany
     */
    public List<CarType> getAvailableCarTypesIn(String companyName) throws RemoteException;

    //------------------------------------------------------------------------
    // Statistics
    //------------------------------------------------------------------------

    /**
     * Get the number of reservations of clientName in this CarRentalAgency.
     *
     * @param clientName
     *      The name of the client of which the number of reservations is
     *      retrieved.
     *
     * @return The number of reservations by clientName.
     */
    public int getNumberOfReservationsBy(String clientName) throws RemoteException;

    /**
     * Get a List containing the names of the best (highest reservations)
     * clients in this RentalAgency
     *
     * @return the best client in this RentalAgency.
     */
    public Set<String> getBestClient() throws RemoteException;

    /**
     * Get the number of reservation in the specified carRentalCompany for
     * the specified carType.
     *
     * @param carRentalCompanyName
     *      The ICarRentalCompany of which the number of reservations are
     *      retrieved.
     * @param carType
     *      The CarType of which the number of reservations are retrieved.
     *
     * @return the number of reservations for carType in carRentalCompany.
     */
    public int getNumberOfReservationsForCarType(String carRentalCompanyName, String carType) throws RemoteException;

    /**
     * Get the most popular CarType in the specified carRentalCompany.
     *
     * @param carRentalCompanyName
     *      The car rental company.
     *
     * @return the most popular CarType.
     */
    public CarType getMostPopularCarTypeIn(String carRentalCompanyName) throws RemoteException;
}
