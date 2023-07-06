package com.batch.processor;

import com.batch.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonaItemProcessor implements ItemProcessor<Persona, Persona> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonaItemProcessor.class);

    @Override
    public Persona process(Persona item) throws Exception {
        String primerNombre = item.getNombre().toUpperCase();
        String segundoNombre = item.getApellido().toUpperCase();
        String telefono = item.getTelefono();

        Persona persona = new Persona(primerNombre, segundoNombre, telefono);

        LOG.info("Convirtiendo (" + item + ") a (" + persona + ")");
        return persona;
    }
}
