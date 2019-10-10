package com.altran.carrinhodecompras.mongodb.events;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import com.altran.carrinhodecompras.mongodb.annotations.Sequence;
import com.altran.carrinhodecompras.mongodb.services.SequenceGeneratorService;

public abstract class AbstractSequenceListener<T> extends AbstractMongoEventListener<T> {

	@Autowired
	private SequenceGeneratorService sequenceGenerator;

	public void onBeforeConvert(BeforeConvertEvent<T> event) {

		try {
			
			Field id = event.getSource().getClass().getDeclaredField("id");
			id.setAccessible(true);
			Long idValue = (Long) id.get(event.getSource());
			
			if (  idValue == null || idValue < 1L) {

				Sequence sequence = event.getSource().getClass().getAnnotation(Sequence.class);
				
				id.set(event.getSource(), sequenceGenerator.generateSequence(sequence.sequence()));

			}

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}