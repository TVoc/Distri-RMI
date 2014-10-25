package rental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Car {

    private int id;
    private CarType type;
    private List<Reservation> reservations;

    /***************
     * CONSTRUCTOR *
     ***************/
    
    public Car(int uid, CarType type) {
    	this.id = uid;
        this.type = type;
        this.reservations = new ArrayList<Reservation>();
    }

    /******
     * ID *
     ******/
    
    public int getId() {
    	return id;
    }
    
    /************
     * CAR TYPE *
     ************/
    
    public CarType getType() {
        return type;
    }

    /****************
     * RESERVATIONS *
     ****************/

    public boolean isAvailable(Date start, Date end) {
        if(!start.before(end))
            throw new IllegalArgumentException("Illegal given period");

        for(Reservation reservation : reservations) {
            if(reservation.getEndDate().before(start) || reservation.getStartDate().after(end))
                continue;
            return false;
        }
        return true;
    }
    
    public void addReservation(Reservation res) {
        reservations.add(res);
    }
    
    public void removeReservation(Reservation reservation) {
        // equals-method for Reservation is required!
        reservations.remove(reservation);
    }

	public Collection<? extends Reservation> getReservationsBy(String clientName, long timeStamp) {
		List<Reservation> toReturn = new ArrayList<Reservation>();
		for (Reservation reservation : reservations) {
			if (reservation.getTimeStamp() <= timeStamp && reservation.getCarRenter().equals(clientName)) {
				toReturn.add(reservation);
			}
		}
		return toReturn;
	}
	
	public int getNumberOfReservations(long timeStamp) {
		int count = 0;
		
		for (Reservation reservation : reservations) {
			if (reservation.getTimeStamp() <= timeStamp) {
				count++;
			}
		}
		return count;
	}
	
	public Map<String, Integer> getNumReservationsPerCustomer(long timeStamp) {
		Map<String, Integer> toReturn = new HashMap<String, Integer>();
		
		for (Reservation reservation : reservations) {
			if (reservation.getTimeStamp() <= timeStamp) {
				if (toReturn.containsKey(reservation.getCarRenter())) {
					toReturn.put(reservation.getCarRenter(), toReturn.get(reservation.getCarRenter()) + 1);
				} else {
					toReturn.put(reservation.getCarRenter(), 1);
				}
			}
		}
		return toReturn;
	}
}