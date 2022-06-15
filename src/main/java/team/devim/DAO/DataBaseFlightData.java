package team.devim.DAO;

import org.apache.log4j.Logger;
import team.devim.POJO.AirportCodes;
import team.devim.POJO.AirportCodesInfo;
import team.devim.POJO.AirportInfo;
import team.devim.POJO.TicketInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataBaseFlightData implements FlightDataDAO {
    private static Connection connection = null;
    private static final Logger log = Logger.getLogger(DataBaseFlightData.class);

    public DataBaseFlightData() throws SQLException {
        this.connection = DataBaseSource.getConnection();
    }

    @Override
    public TicketInfo getTicketInfo(String ticket_no) {
        log.info("Используется PostgreSQL по номеру билета " + ticket_no);
        TicketInfo ticketInfo = new TicketInfo();
        try {
            PreparedStatement stmt = connection.prepareStatement("select passenger_name, fare_conditions, status, city\n" +
                    "from (select passenger_name, fare_conditions, flight_id\n" +
                    "from bookings.tickets t \n" +
                    "join bookings.ticket_flights tf \n" +
                    "on t.ticket_no = tf.ticket_no\n" +
                    "where t.ticket_no = ?) as one\n" +
                    "join (select status, departure_airport, flight_id, city\n" +
                    "from bookings.flights f\n" +
                    "join bookings.airports a\n" +
                    "on f.departure_airport = a.airport_code)\n" +
                    "as two\n" +
                    "on one.flight_id  = two.flight_id");
            stmt.setString(1, ticket_no);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticketInfo.passenger_name = rs.getString("passenger_name");
                ticketInfo.fare_conditions = rs.getString("fare_conditions");
                ticketInfo.status = rs.getString("status");
                ticketInfo.city = rs.getString("city");
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
        return ticketInfo;
    }

    @Override
    public AirportInfo getAirportInfo(String airport_code) {
        log.info("Используется PostgreSQL по коду аэропорта " + airport_code);
        AirportInfo airportInfo = new AirportInfo();
        airportInfo.airport_code = airport_code;
        try {
            PreparedStatement stmt = connection.prepareStatement("select count(distinct actual_departure) as departure_count,\n" +
                    "avg(amount) as avg_amount, count(ticket_no) as passenger_count\n" +
                    "from (select actual_departure, flight_id\n" +
                    "from bookings.flights f\n" +
                    "where departure_airport = ?) as one\n" +
                    "join (select amount, tf.flight_id, ticket_no\n" +
                    "from bookings.ticket_flights tf) as two\n" +
                    "on one.flight_id = two.flight_id");
            stmt.setString(1, airport_code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("departure_count").equals("0")) {
                    return null;
                } else {
                    airportInfo.departure_count = rs.getString("departure_count");
                    airportInfo.avg_amount = rs.getString("avg_amount");
                    airportInfo.passenger_count = rs.getString("passenger_count");
                }
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
        return airportInfo;
    }

    public AirportCodesInfo getManyAirportsInfo(AirportCodes airportCodes) {
        log.info("Используется PostgreSQL");
        AirportCodesInfo airportCodesInfo = new AirportCodesInfo();
        airportCodesInfo.airportInfoArray = new ArrayList<>();
        String params = Arrays.toString(airportCodes.airport_code);
        try {
            PreparedStatement stmt = connection.prepareStatement("select departure_airport, " +
                    "count(distinct actual_departure) as departure_count, avg(amount) as avg_amount, " +
                    "count(ticket_no) as passenger_count\n" +
                    "from ((select actual_departure, flight_id, departure_airport\n" +
                    "from bookings.flights f\n" +
                    "where (departure_airport = ANY(string_to_array(?, ', ')))) as one\n" +
                    "join (select amount, tf.flight_id, ticket_no\n" +
                    "from bookings.ticket_flights tf) as two\n" +
                    "on one.flight_id = two.flight_id)\n" +
                    "group by one.departure_airport");
            stmt.setString(1, params);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("departure_count").equals("0")) {
                    return null;
                } else {
                    AirportInfo airportInfo = new AirportInfo();
                    airportInfo.airport_code = rs.getString("departure_airport");
                    airportInfo.departure_count = rs.getString("departure_count");
                    airportInfo.avg_amount = rs.getString("avg_amount");
                    airportInfo.passenger_count = rs.getString("passenger_count");
                    airportCodesInfo.airportInfoArray.add(airportInfo);
                }
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getClass().getName() + ": " + e.getMessage());
        }
        return airportCodesInfo;
    }
}