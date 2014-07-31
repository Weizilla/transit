package controllers;

import com.weizilla.transit.bus.BusController;
import com.weizilla.transit.bus.data.Direction;
import com.weizilla.transit.bus.data.Prediction;
import com.weizilla.transit.bus.data.Route;
import com.weizilla.transit.bus.data.Stop;
import com.weizilla.transit.bus.source.stream.StreamingBusDataSource;
import com.weizilla.transit.bus.source.stream.http.HttpInputStreamProvider;
import com.weizilla.transit.bus.source.stream.http.HttpReader;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.Collection;

public class Application extends Controller
{
    private static BusController createController() {
        String apiKey = "vqfwTE6FQG778wgBkcy5vfFgh";
        HttpReader reader = new HttpReader(new HttpReader.HttpURLConnectionFactory());
        HttpInputStreamProvider provider = new HttpInputStreamProvider(reader, apiKey);
        StreamingBusDataSource source = new StreamingBusDataSource(provider);

        return new BusController(source, null);
    }

    public static Result index()
    {
        Collection<Route> routes = createController().getRoutes();
        return ok(index.render("Bus - transit", routes));
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
        //TODO generate route here using just id?
        Route route = new Route(routeId);
        Collection<Stop> stps = createController().getStops(route, Direction.valueOf(direction));
        return ok(stops.render("Bus - stops", route, stps));
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
}
