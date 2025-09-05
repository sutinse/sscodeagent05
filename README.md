# Weather Application

Quarkus-sovellus joka palauttaa säätietoja suomalaisille kaupungeille OpenMeteo API:n kautta.

## Ominaisuudet

- **REST API** säätietojen hakemiseen kaupunkikoodilla
- **Ennalta määritellyt kaupungit** koordinaatteineen (Helsinki, Espoo, Vantaa, Turku, Tampere, Jyväskylä, Kuopio, Oulu)
- **24 tunnin sääennuste** tunnittaisilla tiedoilla
- **JSON-muotoinen vastaus** lämpötila- ja sääkooditiedoilla
- **Konfiguroitava palvelu** mock- tai oikeiden API-kutsujen välillä

## Teknologiat

- **Quarkus 3.26.2** - Java-framework
- **JDK 21** - Java-kehitysympäristö
- **Maven** - riippuvuuksien hallinta
- **Jackson** - JSON-käsittely
- **REST Client** - HTTP-kutsut OpenMeteo API:hin

## API Endpointit

### GET /weather/cities
Palauttaa listan kaikista saatavilla olevista kaupungeista koordinaatteineen.

### GET /weather/{cityCode}
Palauttaa säätiedot määritetylle kaupungille.

**Parametrit:**
- `cityCode` - Kaupungin koodi (esim. "helsinki", "turku", "oulu")

**Vastaus:**
```json
{
  "cityName": "Helsinki",
  "latitude": 60.1699,
  "longitude": 24.9384,
  "hourlyWeather": [
    {
      "time": "2025-09-05T12:00",
      "temperature": 18.5,
      "weatherCode": 0
    }
    // ... 24 tuntia
  ]
}
```

## Käytettävissä olevat kaupungit

- helsinki
- espoo  
- vantaa
- turku
- tampere
- jyväskylä
- kuopio
- oulu

## Sääkoodit (WMO Weather interpretation codes)

- 0-3: Selkeä - osittain pilvistä
- 45-48: Sumu
- 51-67: Sadekuuro/sade
- 71-77: Lumisade
- 80-99: Ukkonen

## Sovelluksen käynnistäminen

### Kehitysmoodi
```bash
mvn quarkus:dev
```

### Tuotantoversio
```bash
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

### Testien ajaminen
```bash
mvn test
```

## Konfiguraatio

Sovellusta voidaan konfiguroida `application.properties` tiedostossa:

```properties
# Käytä mock-palvelua (oletus: true)
weather.service.use-mock=true

# OpenMeteo API URL (käytetään kun mock=false)
quarkus.rest-client.openmeteo-api.url=https://api.open-meteo.com/v1/forecast
```

## Esimerkkikutsut

```bash
# Hae kaupunkilista
curl http://localhost:8080/weather/cities

# Hae Helsingin säätiedot
curl http://localhost:8080/weather/helsinki

# Hae Oulun säätiedot
curl http://localhost:8080/weather/oulu
```

## Kehitys

Sovellus sisältää sekä mock-palvelun että oikean OpenMeteo API -integraation. Mock-palvelu generoi realistisia säätietoja ja mahdollistaa kehityksen ilman internet-yhteyttä.

### Rakenne
- `model/` - Tietomallit (City, WeatherData, WeatherResponse)
- `service/` - Liiketoimintalogiikka (CityService, WeatherService, MockWeatherService)
- `resource/` - REST-endpointit (WeatherResource)

### Testit
Sovellus sisältää kattavat testit JUnit 5:llä ja REST Assuredilla.