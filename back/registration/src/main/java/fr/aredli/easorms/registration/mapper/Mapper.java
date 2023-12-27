package fr.aredli.easorms.registration.mapper;

import org.modelmapper.ModelMapper;

public abstract class Mapper {
	public static ModelMapper getMapper() {
		return new ModelMapper();
	}
}
