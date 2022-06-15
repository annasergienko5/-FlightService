package team.devim.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import team.devim.POJO.AirportInfo;
import team.devim.POJO.TicketInfo;

public class RedisFlightData implements FlightDataDAO{

    private static final Logger log = Logger.getLogger(RedisFlightData.class);

    public Jedis jedis;
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();

    private String result;

    public RedisFlightData(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public TicketInfo getTicketInfo(String ticket_no) {
        TicketInfo ticketInfo;
        if (jedis.exists(ticket_no)) {
            log.info("Используется Redis по номеру билета " + ticket_no);
            result = jedis.get(ticket_no);
            ticketInfo = gson.fromJson(result, TicketInfo.class);
        } else {
            ticketInfo = null;
        }
        return ticketInfo;
    }

    @Override
    public AirportInfo getAirportInfo(String airport_code) {
        AirportInfo airportInfo;
        if (jedis.exists(airport_code)) {
            log.info("Используется Redis по коду аэропорта " + airport_code);
            result = jedis.get(airport_code);
            airportInfo = gson.fromJson(result, AirportInfo.class);
        } else {
            airportInfo = null;
        }
        return airportInfo;
    }
}