package team.devim;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        get("/ticket_no/:ticket_no", (request, response) -> {
            Service service = new Service();
            String ticketNumber = request.params(":ticket_no");
            return service.processTicket(ticketNumber);
        });

        get("/airport_code/:airport_code", (request, response) -> {
            Service service = new Service();
            String airportCode = request.params(":airport_code");
            return service.processAirport(airportCode);
        });

        post("/airports_info", (request, response) -> {
            String body = request.body();
            Service service = new Service();
            return service.processAirports(body);
        });

        post("/many_airports_info", (request, response) -> {
            String body = request.body();
            Service service = new Service();
            return service.processManyAirports(body);
        });
    }
}