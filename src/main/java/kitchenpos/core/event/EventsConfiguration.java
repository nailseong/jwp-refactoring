package kitchenpos.core.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsConfiguration {

    private final ApplicationContext applicationContext;

    public EventsConfiguration(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public InitializingBean eventsInitializer() {
        return () -> Events.setPublisher(applicationContext);
    }
}
