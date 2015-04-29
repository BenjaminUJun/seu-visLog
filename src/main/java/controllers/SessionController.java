package controllers;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import services.OverviewService;

import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.StringReader;
import java.net.UnknownHostException;

@Path("/sessions")
public class SessionController {

    final String mongoURI = "mongodb://223.3.80.243:27017";
    final MongoClient mongoClient;
    final DB siteDatabase;
    final OverviewService overviewService;

    public SessionController() throws UnknownHostException {
        mongoClient = new MongoClient(new MongoClientURI(mongoURI));
        siteDatabase = mongoClient.getDB("jiaodian");
        overviewService = new OverviewService(siteDatabase);
    }

    @Path("/distribution/{date}") //2014-10-22
    @GET
    @Produces("application/json")
    public JsonObject getDistribution(@PathParam("date") String date) {
        String jsonString = overviewService.getSessionDistributionByDate(date).toString();
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject json = reader.readObject();
        reader.close();
        return json;
    }

    @Path("/overview/status")
    @GET
    @Produces("application/json")
    public JsonObject getSessionStatus() {
        return overviewService.getSessionCountsAndBounceRate();
    }

    @Path("/overview/sources/se")
    @GET
    @Produces("application/json")
    public JsonArray getSearchEngineContribution() { return overviewService.getTopSearchEngines(10); }

    @Path("/overview/sources/countries")
    @GET
    @Produces("application/json")
    public JsonArray getTopCountriesFlowContribution() {
        return overviewService.getTopCountriesFlow(10);
    }

    @Path("/overview/frequent/pages")
    @GET
    @Produces("application/json")
    public JsonArray getFrequentVisitedPages() {
        return overviewService.getFrequentVisitedPages(20);
    }

    @Path("/overview/frequent/categories")
    @GET
    @Produces("application/json")
    public JsonArray getFrequentVisitedCategory() {
        return overviewService.getTopCategories(7);
    }

    @Path("/overview/landings/categories")
    @GET
    @Produces("application/json")
    public JsonArray getMainLandingCategories() {
        return overviewService.getMainLandingCategories(10);
    }

    @Path("/overview/dropoff/categories")
    @GET
    @Produces("application/json")
    public JsonArray getMainDropOffCategories() {
        return overviewService.getMainDropOffCategories(10);
    }
}
