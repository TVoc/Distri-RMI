package sessions;

import naming.CompanyNotFoundException;
import naming.ICompanyIterator;
import naming.INamingService;
import rental.CarType;
import rental.ICarRentalCompany;
import utilities.MapHighestFinder;
import utilities.MapMerger;

import java.rmi.RemoteException;
import java.util.*;

import agency.IRentalAgency;

public class ManagerSession implements IManagerSession {
    public ManagerSession(SessionManager sessionManager, IRentalAgency rentalAgency) {
        this.sessionManager = sessionManager;
        this.rentalAgency = rentalAgency;
    }

    @Override
    public void register(ICarRentalCompany newComp) throws RemoteException {
        this.getRentalAgency().register(newComp);
    }

    @Override
    public void unregister(String name) throws RemoteException {
        this.getRentalAgency().unregister(name);
    }

    @Override
    public List<String> getRegisteredCompanies() throws RemoteException {
        ICompanyIterator iter = this.getSessionManager().getIterator();

        List<String> res = new ArrayList<String>();
        ICarRentalCompany temp = null;
        while (iter.hasNext()) {
            temp = iter.next();
            res.add(temp.getName());
        }
        return res;
    }

    @Override
    public List<CarType> getAvailableCarTypesIn(String companyName) throws RemoteException, CompanyNotFoundException {
        return new ArrayList<CarType>(this.getSessionManager().lookupCompany(companyName).getAllCarTypes());
    }

    @Override
    public int getNumberOfReservationsBy(String clientName) throws RemoteException {
        ICompanyIterator iter = this.getSessionManager().getIterator();
        long timestamp = System.currentTimeMillis();

        int res = 0;
        ICarRentalCompany temp = null;
        while (iter.hasNext()) {
            temp = iter.next();
            res += temp.getNumReservationsPerCustomer(timestamp).get(clientName);
        }
        return res;
    }

    /*
    ──────────────────────────────────
    ───────────────▀█▄█▀──────────────
    ────────▄█████▄─███───────────────
    ──────▄████████████▄██████▄───────
    ──────██████████████████████──────
    ──────██████████████████████──────
    ──────██████████████████████──────
    ──────█▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█──────
    ──────██████████████████████──────
    ────▄█▀▀──────────────────▀▀█▄────
    ──▄█▀────────────────────────▀█▄──
    ─█▀────▄▀▀▀▀▄────────▄▀▀▀▀▄────▀█─
    ─█────█──▄▄──█─▄▀▀▄─█──▄▄──█────█─
    ─█────█─████─██░░░░██─████─█────█─
    ██──▄▄███████▀░░░░░░▀███████▄▄──██
    █──▀█░░░░░░░░░░▄▄▄▄░░░░░░░░░░█▀──█
    █───▀█▀▄▄▄▄▄▄▀▀░░░░▀▀▄▄▄▄▄▄▀█▀───█
    █────▀▄░░░░░░░░▄▄▄▄░░░░░░░░▄▀────█
    ██─────▀▀▀▀▀▀▀▀────▀▀▀▀▀▀▀▀─────██
    ─█──────────────────────────────█─
    ─█▄────────────────────────────▄█─
    ──█▄──────────────────────────▄█──
    ───█▄────────────────────────▄█───
    ──────────────────────────────────
     */
    @Override
    public Set<String> getBestClient() throws RemoteException {
        ICompanyIterator iter = this.getSessionManager().getIterator();
        long timestamp = System.currentTimeMillis();

        Map<String, Integer> res_raw = new HashMap<String, Integer>();
        MapMerger<String> leMapMerger = new MapMerger<String>();

        ICarRentalCompany temp = null;


        while (iter.hasNext()) {
            temp = iter.next();
            leMapMerger.merge(res_raw, temp.getNumReservationsPerCustomer(timestamp));
        }

        Set<String> res = new HashSet<String>();

        MapHighestFinder<String> highestFinder = new MapHighestFinder<String>();

        String key = highestFinder.findHighestIn(res_raw);
        Integer max = res_raw.get(key);

        res.add(key);
        res_raw.remove(key);

        while(res_raw.values().contains(max)) {
            key = highestFinder.findHighestIn(res_raw);
            res.add(key);
            res_raw.remove(key);
        }

        return res;
    }

    @Override
    public int getNumberOfReservationsForCarType(String carRentalCompanyName, String carType) throws RemoteException, CompanyNotFoundException {
        return this.getSessionManager().lookupCompany(carRentalCompanyName).getNumberOfReservationsFor(carType, System.currentTimeMillis());
    }

    @Override
    public CarType getMostPopularCarTypeIn(String carRentalCompanyName) throws RemoteException, CompanyNotFoundException {
        return this.getSessionManager().lookupCompany(carRentalCompanyName).getMostPopularCarType(System.currentTimeMillis());
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    private final SessionManager sessionManager;

    private IRentalAgency getRentalAgency() {
        return this.rentalAgency;
    }

    private final IRentalAgency rentalAgency;

    //--------------------------------------------------------------------
    // UNSUPPORTED STUFF
    /*
            ─────────▄██████▀▀▀▀▀▀▄
            ─────▄█████████▄───────▀▀▄▄
            ──▄█████████████───────────▀▀▄
            ▄██████████████─▄▀───▀▄─▀▄▄▄──▀▄
            ███████████████──▄▀─▀▄▄▄▄▄▄────█
            █████████████████▀█──▄█▄▄▄──────█
            ███████████──█▀█──▀▄─█─█─█───────█
            ████████████████───▀█─▀██▄▄──────█
            █████████████████──▄─▀█▄─────▄───█
            █████████████████▀███▀▀─▀▄────█──█
            ████████████████──────────█──▄▀──█
            ████████████████▄▀▀▀▀▀▀▄──█──────█
            ████████████████▀▀▀▀▀▀▀▄──█──────█
            ▀████████████████▀▀▀▀▀▀──────────█
            ──███████████████▀▀─────█──────▄▀
            ──▀█████████████────────█────▄▀
            ────▀████████████▄───▄▄█▀─▄█▀
            ──────▀████████████▀▀▀──▄███
            ──────████████████████████─█
            ─────████████████████████──█
            ────████████████████████───█
            ────██████████████████─────█
            ────██████████████████─────█
            ────██████████████████─────█
            ────██████████████████─────█
            ────██████████████████▄▄▄▄▄█
    */
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public long getTimeStamp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

}