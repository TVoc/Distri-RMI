package rental;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilities.MapHighestFinder;
import utilities.MapMerger;

public class CarRentalCompany implements ICarRentalCompany {

	private static Logger logger = Logger.getLogger(CarRentalCompany.class.getName());
	
	private String name;
	private List<Car> cars;
	private Map<String,CarType> carTypes = new HashMap<String, CarType>();
	
	/**
	 * Whenever reservation constraints must be checked, a lock on this object must be established in order
	 * to achieve consistency control.
	 */
	private Object quoteConstraintLock;

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public CarRentalCompany(String name, List<Car> cars) {
		logger.log(Level.INFO, "<{0}> Car Rental Company {0} starting up...", name);
		setName(name);
		this.cars = cars;
		for(Car car:cars)
			carTypes.put(car.getType().getName(), car.getType());
		this.quoteConstraintLock = new Object();
	}

	/* (non-Javadoc)
	 * @see rental.ICarRentalCompany#getName()
	 */

	@Override
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	/*************
	 * CAR TYPES *
	 *************/

	public Collection<CarType> getAllCarTypes() {
		return carTypes.values();
	}
	
	public CarType getCarType(String carTypeName) {
		if(carTypes.containsKey(carTypeName))
			return carTypes.get(carTypeName);
		throw new IllegalArgumentException("<" + carTypeName + "> No car type of name " + carTypeName);
	}
	
	public boolean isAvailable(String carTypeName, Date start, Date end) {
		logger.log(Level.INFO, "<{0}> Checking availability for car type {1}", new Object[]{name, carTypeName});
		if(carTypes.containsKey(carTypeName))
			return getAvailableCarTypes(start, end).contains(carTypes.get(carTypeName));
		throw new IllegalArgumentException("<" + carTypeName + "> No car type of name " + carTypeName);
	}
	
	public Set<CarType> getAvailableCarTypes(Date start, Date end) throws IllegalArgumentException {
		Set<CarType> availableCarTypes = new HashSet<CarType>();
		for (Car car : cars) {
			if (car.isAvailable(start, end)) {
				availableCarTypes.add(car.getType());
			}
		}
		return availableCarTypes;
	}
	
	public CarType getCheapestCarType(Date start, Date end) throws IllegalArgumentException {
		Set<CarType> availableCarTypes = this.getAvailableCarTypes(start, end);
		
		CarType toReturn = null;
		
		for (CarType type : availableCarTypes) {
			if (toReturn == null || (type.getRentalPricePerDay() < toReturn.getRentalPricePerDay())) {
				toReturn = type;
			}
		}
		
		return toReturn;
	}
	
	/*********
	 * CARS *
	 *********/
	
	private Car getCar(int uid) {
		for (Car car : cars) {
			if (car.getId() == uid)
				return car;
		}
		throw new IllegalArgumentException("<" + name + "> No car with uid " + uid);
	}
	
	private List<Car> getAvailableCars(String carType, Date start, Date end) {
		List<Car> availableCars = new LinkedList<Car>();
		for (Car car : cars) {
			if (car.getType().getName().equals(carType) && car.isAvailable(start, end)) {
				availableCars.add(car);
			}
		}
		return availableCars;
	}

	/* (non-Javadoc)
	 * @see rental.ICarRentalCompany#createQuote(rental.ReservationConstraints, java.lang.String)
	 */

	@Override
	public Quote createQuote(ReservationConstraints constraints, String client)
			throws ReservationException {
		logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}", 
                        new Object[]{name, client, constraints.toString()});
		
		CarType type = getCarType(constraints.getCarType());
		
		synchronized(quoteConstraintLock) { 
			if(!isAvailable(constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate()))
				throw new ReservationException("<" + name
						+ "> No cars available to satisfy the given constraints.");

			
		}
		double price = calculateRentalPrice(type.getRentalPricePerDay(),constraints.getStartDate(), constraints.getEndDate());
		return new Quote(client, constraints.getStartDate(), constraints.getEndDate(), getName(), constraints.getCarType(), price);
	}

	// Implementation can be subject to different pricing strategies
	private double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
		return rentalPricePerDay * Math.ceil((end.getTime() - start.getTime())
						/ (1000 * 60 * 60 * 24D));
	}

	/* (non-Javadoc)
	 * @see rental.ICarRentalCompany#confirmQuote(rental.Quote)
	 */
	@Override
	public Reservation confirmQuote(Quote quote) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[]{name, quote.toString()});
		Reservation res;
		synchronized (quoteConstraintLock) {
			List<Car> availableCars = getAvailableCars(quote.getCarType(),
					quote.getStartDate(), quote.getEndDate());
			if (availableCars.isEmpty())
				throw new ReservationException(
						"Reservation failed, all cars of type "
								+ quote.getCarType() + " are unavailable from "
								+ quote.getStartDate() + " to "
								+ quote.getEndDate());
			Car car = availableCars.get((int) (Math.random() * availableCars
					.size()));
			res = new Reservation(quote, car.getId());
			car.addReservation(res);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see rental.ICarRentalCompany#cancelReservation(rental.Reservation)
	 */
	@Override
	public void cancelReservation(Reservation res) {
		logger.log(Level.INFO, "<{0}> Cancelling reservation {1}", new Object[]{name, res.toString()});
		synchronized (quoteConstraintLock) {
			getCar(res.getCarId()).removeReservation(res);
		}
	}

	@Override
	public List<Reservation> getReservationsBy(String clientName, long timeStamp)
			throws RemoteException {
		List<Reservation> toReturn = new ArrayList<Reservation>();
		for (Car car : cars) {
			toReturn.addAll(car.getReservationsBy(clientName, timeStamp));
		}
		return toReturn;
	}

	@Override
	public int getNumberOfReservationsFor(String carType, long timeStamp)
			throws RemoteException {
		int toReturn = 0;
		for (Car car : cars) {
			if (car.getType().getName().equals(carType)) {
				toReturn += car.getNumberOfReservations(timeStamp);
			}
		}
		return toReturn;
	}

	@Override
	public Map<String, Integer> getNumReservationsPerCustomer(long timeStamp)
			throws RemoteException {
		Map<String, Integer> toReturn = new HashMap<String, Integer>();
		MapMerger<String> merger = new MapMerger<String>();
		
		for (Car car : cars) {
			Map<String, Integer> temp = car.getNumReservationsPerCustomer(timeStamp);
			merger.merge(toReturn, temp);
		}
		
		return toReturn;
	}
	
	@Override
	public String getBestCustomer(long timeStamp) throws RemoteException {
		Map<String, Integer> info = this.getNumReservationsPerCustomer(timeStamp);
		MapHighestFinder<String> finder = new MapHighestFinder<String>();
		return finder.findHighestIn(info);
	}

	@Override
	public Map<CarType, Integer> getNumReservationsPerCarType(long timeStamp)
			throws RemoteException {
		Map<CarType, Integer> toReturn = new HashMap<CarType, Integer>();
		
		for (Car car : cars) {
			int count = car.getNumberOfReservations(timeStamp);
			if (count > 0) {
				if (toReturn.containsKey(car.getType())) {
					toReturn.put(car.getType(), toReturn.get(car.getType()) + count);
				} else {
					toReturn.put(car.getType(), count);
				}
			}
		}
		
		return toReturn;
	}
	
	@Override
	public CarType getMostPopularCarType(long timeStamp) throws RemoteException {
		Map<CarType, Integer> info = this.getNumReservationsPerCarType(timeStamp);
		MapHighestFinder<CarType> finder = new MapHighestFinder<CarType>();
		return finder.findHighestIn(info);
	}
}