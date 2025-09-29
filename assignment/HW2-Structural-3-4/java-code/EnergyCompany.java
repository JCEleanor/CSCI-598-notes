import java.util.*;

//1.Proxy
interface IDataBaseService {

    String getWeatherData(String byLocation, String byDate);
}

class WeatherData implements IDataBaseService {
    @Override
    public String getWeatherData(String byLocation, String byDate) {
        return "From DB: " + byLocation + ":" + byDate +
                " daytime, average temperature 75°F, 32% Humidity";
    }
}

class CacheWeatherData implements IDataBaseService {
    private WeatherData data;
    private HashMap<String, String> cache = new HashMap<>();

    @Override
    public String getWeatherData(String byLocation, String byDate) {
        String key = byLocation + ":" + byDate;
        if (cache.containsKey(key)) {
            return "From cache data: " + byLocation + ":" + byDate +
                    " daytime, average temperature 75°F, 32% Humidity";
        } else {
            if (data == null) {
                data = new WeatherData();
            }
            var fromDb = data.getWeatherData(byLocation, byDate);
            cache.put(key, fromDb);
            return fromDb;
        }
    }
}

// 2.Adapter
class RemoteWeatherAPI {
    public String fetchByLocationandDate(String locationString, String dateString) {

        System.out.println("Calling the Remote API");
        return "From Remote API: Weather Data for" + locationString +
                "on " + dateString + " daytime, average temperature 54°F, 57% Humidity";
    }
}

class RemoteAdapter implements IDataBaseService {
    private RemoteWeatherAPI remoteAPI;

    public RemoteAdapter(RemoteWeatherAPI remoteAPI) {
        this.remoteAPI = remoteAPI;
    }

    @Override
    public String getWeatherData(String byLocation, String byDate) {
        return remoteAPI.fetchByLocationandDate(byLocation, byDate);
    }
}

// 3.Facade
class Authorization {
    private final Set<String> authorizedUsers = new HashSet<>(
            Arrays.asList("Adam", "Nicole", "Austin", "JP", "Richad"));

    public boolean isAuthorized(String user) {
        return authorizedUsers.contains(user);
    }
}

class Log {
    public void log(String message) {
        System.out.println("Logged" + message);
    }
}

class Parser {
    public String parseInfo(String info) {
        return "The Weather Data: " + info;
    }
}

class SendReport {
    public void Report(String info) {
        System.out.println("Info has been sent to your email as well!");
    }
}

class WeatherFacade {
    private IDataBaseService weatherDataSource;
    private Authorization eligibility = new Authorization();
    private Log logger = new Log();
    private Parser parse = new Parser();
    private SendReport send = new SendReport();

    public WeatherFacade(IDataBaseService dataSource) {
        this.weatherDataSource = dataSource;
    }

    public String getWeatherInformation(String user, String location, String date) {
        if (!eligibility.isAuthorized(user)) {
            logger.log("Unauthorized access attempt by user: " + user);
            return "Access Denied";
        }
        logger.log("User " + user + " requested weather for location: " + location);
        
        String info = parse.parseInfo(weatherDataSource.getWeatherData(location, date));
        
        send.Report(info);
        return info;
    }
}

class EnergyCompany {
    public static void main(String[] args) {
        // Fetching info from Database
        IDataBaseService dataBase = new CacheWeatherData();
        WeatherFacade weather = new WeatherFacade(dataBase);
        System.out.println(weather.getWeatherInformation("Austin", "Golden,CO", "September 25th,2025"));
        System.out.println(weather.getWeatherInformation("JP", "Golden,CO", "September 29th,2025"));

        System.out.println(weather.getWeatherInformation("Richad", "Golden,CO", "September 25th,2025"));
        // Fetching info from Remote Database
        IDataBaseService remoteAPI = new RemoteAdapter(new RemoteWeatherAPI());
        WeatherFacade remoteWeather = new WeatherFacade(remoteAPI);
        System.out.println(remoteWeather.getWeatherInformation("Nicole", "Denver,CO", "September 25th,2025"));
    }
}