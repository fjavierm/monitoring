package org.example.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/greeting")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

	@GET
	@Path("/en")
	public Response english() {
		return Response.ok("Hi").build();
	}

	@GET
	@Path("/es")
	public Response spanish() {
		return Response.ok("Hola").build();
	}

	@GET
	@Path("/fr")
	public Response french() {
		return Response.ok("Salut").build();
	}
}
