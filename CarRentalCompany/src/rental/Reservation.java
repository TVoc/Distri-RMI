package rental;

import java.io.Serializable;

public class Reservation extends Quote implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4010288946971833774L;
	
	private int carId;
    
    /***************
	 * CONSTRUCTOR *
	 ***************/

    Reservation(Quote quote, int carId) {
    	super(quote.getCarRenter(), quote.getStartDate(), quote.getEndDate(), 
    			quote.getRentalCompany(), quote.getCarType(), quote.getRentalPrice());
        this.carId = carId;
        this.timeStamp = System.currentTimeMillis();
    }
    
    /******
     * ID *
     ******/
    
    public int getCarId() {
    	return carId;
    }
    
    /**
     * A time stamp set during Reservation creation, for synchronisation purposes.
     */
    private long timeStamp;
    
    /**
     * @return This Reservation's time stamp.
     */
    public long getTimeStamp() {
    	return this.timeStamp;
    }
    
    /*************
     * TO STRING *
     *************/
    
    @Override
    public String toString() {
        return String.format("Reservation for %s from %s to %s at %s\nCar type: %s\tCar: %s\nTotal price: %.2f", 
                getCarRenter(), getStartDate(), getEndDate(), getRentalCompany(), getCarType(), getCarId(), getRentalPrice());
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + carId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		Reservation other = (Reservation) obj;
		if (carId != other.carId)
			return false;
		return true;
	}
}