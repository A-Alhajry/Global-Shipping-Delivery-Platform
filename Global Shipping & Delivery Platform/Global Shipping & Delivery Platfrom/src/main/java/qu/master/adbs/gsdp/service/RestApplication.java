package qu.master.adbs.gsdp.service;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import io.helidon.common.CollectionsHelper;
import io.helidon.config.Config;
import io.helidon.microprofile.config.MpConfig;
import io.helidon.microprofile.server.Server;

@ApplicationScoped
@ApplicationPath("/")
public class RestApplication extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
		return CollectionsHelper.setOf(AccountsService.class, ShipmentsService.class, LocationsService.class, ReportsService.class);
	}
	
	public static void startService(String host, int port) throws Exception{
		try {			
			Weld weld = new Weld();
			WeldContainer weldContainer = weld.initialize();
			
			Server server = Server.builder()
					.addApplication(RestApplication.class)
					.cdiContainer(weldContainer)
					.config(MpConfig.builder().config(Config.create()).build())
					.host(host)
					.port(port)
					.build();
			
			server.start();
			return;
			
		}
		
		catch (Exception e) {
			throw e;
		}
		
		
		
	}
}
