package controllers;

import com.weizilla.transit.bus.BusController;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.bus.source.stream.StreamingBusDataSource;
import com.weizilla.transit.bus.source.stream.http.HttpInputStreamProvider;
import com.weizilla.transit.bus.source.stream.http.HttpReader;
import com.weizilla.transit.favorites.sqlite.SqliteFavoritesStore;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class Application extends Controller
{
    private static final Path favDbPath = createPath();

    private static Path createPath()
    {
        try
        {
            Path favDbPath = Files.createTempFile("transit-play-", ".db");
            favDbPath.toFile().deleteOnExit();
            return favDbPath;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static BusController createController()
    {
        String apiKey = "vqfwTE6FQG778wgBkcy5vfFgh";
        HttpReader reader = new HttpReader(new HttpReader.HttpURLConnectionFactory());
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, apiKey);
        StreamingBusDataSource source = new StreamingBusDataSource(provider);

        SqliteFavoritesStore store = null;
        try
        {
            store = SqliteFavoritesStore.createStore(favDbPath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new BusController(source, store);
    }

    public static Result index()
    {
        BusController controller = createController();
        Collection<Route> routes = controller.getRoutes();
        Collection<String> favRoutes = controller.getFavoriteRoutes();
        return ok(index.render("Bus - routes",  routes, favRoutes));
    }

    public static Result directions(String routeId)
    {
        //TODO generate route here using just id?
        Route route = new Route(routeId);
        Collection<Direction> dirs = createController().getDirections(route);
        return ok(directions.render("Bus - directions", route, dirs));
    }

    public static Result stops(String routeId, String direction)
    {
        BusController controller = createController();
        //TODO generate route here using just id?
        Route route = new Route(routeId);
        Direction dir = Direction.valueOf(direction);
        Collection<Stop> allStops = controller.getStops(route, dir);

        Collection<Integer> favStopIds = controller.getFavoriteStops(routeId, dir);
        Collection<Stop> favStops = new ArrayList<>(favStopIds.size());
        for (Stop stop : allStops)
        {
            if (favStopIds.contains(stop.getId()))
            {
                favStops.add(stop);
            }
        }
        return ok(stops.render("Bus - stops", route, allStops, favStops));
    }

    public static Result predictions(String routeId, int stopId)
    {
        //TODO generate route here using just id?
        Route route = new Route(routeId);
        Stop stop = new Stop(stopId);
        Collection<Prediction> prds = createController().getPredictions(route, stop);
        if (prds == null)
        {
            return ok(message.render("Bus - predictions", "No predictions"));
        }
        else {
            return ok(predictions.render("Bus - predictions", prds));
        }
    }

    public static Result saveFavoriteRoute(String routeId)
    {
        BusController controller = createController();
        controller.saveFavorite(new Route(routeId));
        return ok(message.render("Bus - Favorite", "Route " + routeId + " saved as favorite"));
    }

    public static Result saveFavoriteStop(String routeId, String direction, int stopId)
    {
        BusController controller = createController();
        //TODO create stop here?
        Stop stop = new Stop(stopId);
        stop.setDirection(Direction.valueOf(direction));
        stop.setRouteId(routeId);
        controller.saveFavorite(stop);
        return ok(message.render("Bus - Favorite", "Stop " + stopId + " saved as favorite"));
    }
}