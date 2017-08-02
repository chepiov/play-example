package controllers.json;

import com.google.inject.AbstractModule;

/**
 * Object mapper module integration.
 */
public class ObjectMapperModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectMapperEx.class).asEagerSingleton();
    }
}
