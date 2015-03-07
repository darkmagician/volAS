package com.vol.rest.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.type.ClassKey;

@Provider
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json",MediaType.TEXT_PLAIN})
public final class ServerJacksonJsonProvider extends JacksonJsonProvider {


	/* (non-Javadoc)
	 * @see org.codehaus.jackson.jaxrs.JacksonJsonProvider#isWriteable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
	 */
	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
        if (_untouchables.contains(new ClassKey(type))) {
            return false;
        }
        // but some are interface/abstract classes, so
        for (Class<?> cls : _unwritableClasses) {
            if (cls.isAssignableFrom(type)) {
                return false;
            }
        }
        // and finally, may have additional custom types to exclude
        if (_containedIn(type, _cfgCustomUntouchables)) {
            return false;
        }

        // Also: if we really want to verify that we can deserialize, we'll check:
        if (_cfgCheckCanSerialize) {
            if (!locateMapper(type, mediaType).canSerialize(type)) {
                return false;
            }
        }
        return true;
	}

}
