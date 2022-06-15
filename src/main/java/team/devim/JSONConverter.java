package team.devim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.devim.POJO.AirportCodes;
import team.devim.POJO.AirportCodesInfo;
import team.devim.POJO.AirportInfo;
import team.devim.POJO.TicketInfo;

public class JSONConverter {
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();

    public String ticketInfoToJSON(TicketInfo ticketInfo) {
        String json = gson.toJson(ticketInfo);
        return json;
    }

    public String airportInfoToJSON(AirportInfo airportInfo) {
        String json = gson.toJson(airportInfo);
        return json;
    }

    public AirportInfo airportInfoFromJSON(String json) {
        AirportInfo airportInfo = gson.fromJson(json, AirportInfo.class);
        return airportInfo;
    }

    public AirportCodes airportCodeFromJSON(String json) {
        AirportCodes airportCodes = gson.fromJson(json, AirportCodes.class);
        return airportCodes;
    }

    public String airportCodesInfoToJSON(AirportCodesInfo airportCodesInfo) {
        String json = gson.toJson(airportCodesInfo);
        return json;
    }
}