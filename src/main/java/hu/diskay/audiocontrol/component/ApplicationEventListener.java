package hu.diskay.audiocontrol.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationEventListener.class);

    @Autowired
    private ApplicationContext context;

    @EventListener
    public void handleContextRefresh(ContextStartedEvent contextStartedEvent) {
        // TODO: for data invalidation
        LOG.info("TODO: add and check data timestamps");
    }
}
