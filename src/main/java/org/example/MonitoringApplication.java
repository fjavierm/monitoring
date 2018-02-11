package org.example;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;
import org.example.config.MonitoringConfiguration;
import org.example.healthcheck.RunningHealthCheck;
import org.example.resource.GreetingResource;
import uk.gov.re.gds.metrics.AuthenticationFilter;
import uk.gov.re.gds.metrics.Configuration;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class MonitoringApplication extends Application<MonitoringConfiguration> {

	public static void main(String[] args) throws Exception {
		new MonitoringApplication().run(args);
	}

	@Override
	public void run(final MonitoringConfiguration monitoringConfiguration, final Environment environment) throws Exception {
		final Configuration configuration = Configuration.getInstance();

		environment.jersey().register(new GreetingResource());

		environment.healthChecks().register("running", new RunningHealthCheck());

		environment.servlets().addServlet("metrics", new MetricsServlet())
				.addMapping(configuration.getPrometheusMetricsPath());

		environment.servlets()
				.addFilter("LoggerFilter", new AuthenticationFilter())
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, configuration.getPrometheusMetricsPath());
	}

	@Override
	public void initialize(Bootstrap<MonitoringConfiguration> bootstrap) {
		final MetricRegistry metrics = bootstrap.getMetricRegistry();

		CollectorRegistry.defaultRegistry.register(new DropwizardExports(metrics));
	}
}
