package rental;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class CarTest {
	
	Car car;
	CarType type;
	
	@Before
	public void setUp() throws Exception {
		type = new CarType("Test Type", 5, 1337, 12.5, false);
		car = new Car(0, type);
		
		Reservation res = new Reservation(new Quote("John Doe", new Date(0), new Date(1000), "Renty", "Test Type", 12.5), 0);
		Whitebox.setInternalState(res, "timeStamp", 0);
		car.addReservation(res);
		
		res = new Reservation(new Quote("Jane Doe", new Date(1500), new Date(2500), "Renty", "Test Type", 12.5), 0);
		Whitebox.setInternalState(res, "timeStamp", 0);
		car.addReservation(res);
		
		res = new Reservation(new Quote("John Jr. Doe", new Date(3000), new Date(4000), "Renty", "Test Type", 12.5), 0);
		Whitebox.setInternalState(res, "timeStamp", 0);
		car.addReservation(res);

		res = new Reservation(new Quote("John Doe", new Date(4500), new Date(5500), "Renty", "Test Type", 12.5), 0);
		Whitebox.setInternalState(res, "timeStamp", 1);
		car.addReservation(res);
		
		res = new Reservation(new Quote("John Doe", new Date(6000), new Date(6500), "Renty", "Test Type", 12.5), 0);
		Whitebox.setInternalState(res, "timeStamp", 5);
		car.addReservation(res);
	}

	@Test
	public void getReservationsByTest() {
		Collection<? extends Reservation> reservations = car.getReservationsBy("John Doe", 3);
		assertEquals(2, reservations.size());
	}
	
	@Test
	public void getNumberOfReservationsTest() {
		int num = car.getNumberOfReservations(3);
		assertEquals(4, num);
	}
	
	@Test
	public void getNumReservationsPerCustomerTest() {
		Map<String, Integer> info = car.getNumReservationsPerCustomer(3);
		assertEquals(3, info.size());
		assertEquals(2, info.get("John Doe").intValue());
		assertEquals(1, info.get("Jane Doe").intValue());
		assertEquals(1, info.get("John Jr. Doe").intValue());
	}

}
