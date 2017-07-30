package hu.diskay.audiocontrol;

import hu.diskay.audiocontrol.config.ApplicationConfig;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ControlApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder()
            .bannerMode(Mode.OFF)
            .sources(ApplicationConfig.class)
            .run()
            .start();
    }
}
