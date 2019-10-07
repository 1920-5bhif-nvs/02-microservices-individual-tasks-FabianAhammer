package at.htl.planner;


import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("planner")
public class Planner {
    @Inject
    @RestClient
    PlanWrapperResource planWrapperResource;

    @GET
    @Path("/plans")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "performedChecks", description = "Amount of Checks performed.")
    @Timed(name = "checksTimer", description = "Hearthbleed of Request", unit = MetricUnits.MILLISECONDS)
    @Retry(maxRetries = 3)
    @Fallback(fallbackMethod = "fallback")
    public Response loans() {
        return Response.ok().entity(planWrapperResource.getLoans()).build();
    }

    public Response fallback(){
        return Response.ok().entity(Json.createArrayBuilder().build()).build();
    }
}
