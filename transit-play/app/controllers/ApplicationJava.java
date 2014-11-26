package controllers;

import com.weizilla.transit.bus.BusController;
import com.weizilla.transit.data.Direction;
import com.weizilla.transit.data.Prediction;
import com.weizilla.transit.data.Route;
import com.weizilla.transit.data.Stop;
import com.weizilla.transit.source.stream.StreamingBusDataSource;
import com.weizilla.transit.source.stream.http.HttpInputStreamProvider;
import com.weizilla.transit.source.stream.http.HttpReader;
import com.weizilla.transit.favorites.sqlite.JdbcSqliteFavoritesStore;
import com.weizilla.transit.groups.sqlite.JdbcSqliteGroupsStore;
import play.mvc.Result;
import views.html.message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import static play.mvc.Results.ok;

public class ApplicationJava
{
    private static final Path favDbPath = createPath();
    private static final Path groupDbPath = createPath();

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

        try
        {
            JdbcSqliteFavoritesStore favStore = JdbcSqliteFavoritesStore.createStore(favDbPath);
            JdbcSqliteGroupsStore groupsStore = JdbcSqliteGroupsStore.createStore(groupDbPath);

            return new BusController(source, favStore, groupsStore);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to create bus controller", e);
        }
    }

    public static Result index()
    {
        BusController controller = createController();
        Collection<Route> routes = controller.getRoutes();
        Collection<String> favRoutes = controller.getFavoriteRouteIds();
//        return ok(index.render("Bus - routes",  routes, favRoutes));
        return null;
    }

    public static Result directions(String routeId)
    {
        Collection<Direction> dirs = createController().getDirections(routeId);
//        return ok(directions.render("Bus - directions", routeId, dirs));
        return ok();
    }

    public static Result stops(String routeId, String direction)
    {
        BusController controller = createController();
        Direction dir = Direction.valueOf(direction);
        Collection<Stop> allStops = controller.getStops(routeId, dir);

        Collection<Integer> favStopIds = controller.getFavoriteStopIds(routeId, dir);
        Collection<Stop> favStops = new ArrayList<>(favStopIds.size());
        for (Stop stop : allStops)
        {
            if (favStopIds.contains(stop.getId()))
            {
                favStops.add(stop);
            }
        }
//        return ok(stops.render("Bus - stops", routeId, allStops, favStops));
        return ok();
    }

    public static Result predictions(String routeId, int stopId)
    {
        //TODO generate route here using just id?
        Route route = new Route(routeId);
        Stop stop = new Stop(stopId);
        Collection<Prediction> prds = createController().getPredictions(routeId, stopId);
        if (prds == null)
        {
            return ok(message.render("Bus - predictions", "No predictions"));
        }
        else {
//            return ok(predictions.render("Bus - predictions", prds));
            return ok();
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
