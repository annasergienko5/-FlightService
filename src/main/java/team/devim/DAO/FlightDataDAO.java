package team.devim.DAO;

import team.devim.POJO.AirportInfo;
import team.devim.POJO.TicketInfo;

public interface FlightDataDAO {
    TicketInfo getTicketInfo(String ticket_no);

    AirportInfo getAirportInfo(String airport_code);
}