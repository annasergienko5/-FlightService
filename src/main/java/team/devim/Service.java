package team.devim;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import team.devim.DAO.DataBaseFlightData;
import team.devim.DAO.FlightDataDAO;
import team.devim.DAO.RedisFlightData;
import team.devim.POJO.AirportCodes;
import team.devim.POJO.AirportCodesInfo;
import team.devim.POJO.AirportInfo;
import team.devim.POJO.TicketInfo;

import java.sql.SQLException;
import java.util.ArrayList;

public class Service {

    private static final Logger log = Logger.getLogger(Service.class);
    private Jedis jedis;

    private String result;

    private JSONConverter jsonConverter = new JSONConverter();
    private FlightDataDAO flightDataDAO;
    {
        if (Constants.REDIS_FLAG) {
            jedis = new Jedis(Constants.REDIS_HOST);
            flightDataDAO = new RedisFlightData(jedis);
        } else {
            try {
                flightDataDAO = new DataBaseFlightData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String processTicket(String ticketNumber) {
            TicketInfo ticketInfo = flightDataDAO.getTicketInfo(ticketNumber);
            if (ticketInfo == null) {
                try {
                    flightDataDAO = new DataBaseFlightData();
                    ticketInfo = flightDataDAO.getTicketInfo(ticketNumber);
                    result = jsonConverter.ticketInfoToJSON(ticketInfo);
                    jedis.set((ticketNumber), result);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                result = jsonConverter.ticketInfoToJSON(ticketInfo);
            }
        return result;
    }

    public String processAirport(String airportCode) {
            AirportInfo airportInfo = flightDataDAO.getAirportInfo(airportCode);
            if (airportInfo == null) {
                try {
                    flightDataDAO = new DataBaseFlightData();
                    airportInfo = flightDataDAO.getAirportInfo(airportCode);
                    if (airportInfo == null) {
                        log.info("Неверный код аэропорта: " + airportCode);
                        return "Неверный код аэропорта";
                    }
                    result = jsonConverter.airportInfoToJSON(airportInfo);
                    jedis.set((airportCode), result);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                result = jsonConverter.airportInfoToJSON(airportInfo);
            }
        return result;
    }

    public String processAirports(String body) {
        AirportCodes airportCodes = jsonConverter.airportCodeFromJSON(body);
        AirportCodesInfo airportCodesInfo = new AirportCodesInfo();
        airportCodesInfo.airportInfoArray = new ArrayList<>();
        for (String airportCode : airportCodes.airport_code) {
            Service service = new Service();
            String info = service.processAirport(airportCode);
            AirportInfo airportInfo;
            if (info.equals("Неверный код аэропорта")) {
                airportInfo = null;
            } else {
                airportInfo = jsonConverter.airportInfoFromJSON(info);
            }
            airportCodesInfo.airportInfoArray.add(airportInfo);
        }
        return jsonConverter.airportCodesInfoToJSON(airportCodesInfo);
    }

    public String processManyAirports(String body) throws SQLException {
        AirportCodes airportCodes = jsonConverter.airportCodeFromJSON(body);
        DataBaseFlightData dataBaseFlightData = new DataBaseFlightData();
        AirportCodesInfo airportCodesInfo = dataBaseFlightData.getManyAirportsInfo(airportCodes);
        return jsonConverter.airportCodesInfoToJSON(airportCodesInfo);
    }
}