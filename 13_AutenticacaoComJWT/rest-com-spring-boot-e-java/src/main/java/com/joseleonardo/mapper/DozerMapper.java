package com.joseleonardo.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerMapper {

	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
	
	public static <O, D> D parseObject(O origem, Class<D> destino) {
		return mapper.map(origem, destino);
	}
	
	public static <O, D> List<D> parseListObjects(List<O> listaOrigem, Class<D> destino) {
		List<D> destinoObjects = new ArrayList<>();
		
		for (O o : listaOrigem) {
			destinoObjects.add(mapper.map(o, destino));
		}
		
		return destinoObjects;
	}
	
}
