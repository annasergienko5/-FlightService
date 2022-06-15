# FlightService

1. get метод "/ticket_no/:ticket_no" - принимает число (номер билета), возвращает JSON с имем пассажира, тарифом,
   статусом, городом вылета:
   {"passenger_name":"String",
   "fare_conditions":"String",
   "status":"String",
   "city":"String"}

2. get метод "/airport_code/:airport_code" - принимает строку (код аэропорта), возвращает JSON с количеством вылетов
   из этого аэропорта, средней стоимостью перелета из этого аэропорта, количеством пассажиров, вылетевших из аэропорта:
   {"departure_count":"String",
   "avg_amount":"String",
   "passenger_count":"String"}

3. post метод "/airports_info" - принимает JSON с кодами аэропортов: {"airport_code": ["String",..]}; отправляет запросы
   по каждому коду отдельно, возвращает JSON с информацией по всем аэропортам:
   {"airportInfoArray": [
   {"airport_code": "String",
   "departure_count": "String",
   "avg_amount": "String",
   "passenger_count": "String"},
   ...]}

4. post метод "/many_airports_info" - принимает JSON с кодами аэропортов: {"airport_code": ["String",..]}; отправляет
   один запрос со всеми кодами, возвращает JSON с информацией по всем аэропортам:
   {"airportInfoArray": [
   {"airport_code": "String",
   "departure_count": "String",
   "avg_amount": "String",
   "passenger_count": "String"},
   ...]}