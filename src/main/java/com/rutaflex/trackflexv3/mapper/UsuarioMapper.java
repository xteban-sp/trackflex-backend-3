package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.UsuarioDTO;
import com.rutaflex.trackflexv3.entity.security.Usuario;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioDTO> {

    @Override
    @Mapping(target = "rol", expression = "java(entity.getRol().getNombre())")
    UsuarioDTO toDTO(Usuario entity);

    @Override
    @Mapping(target = "rol", ignore = true)  // se asigna en el servicio
    Usuario toEntity(UsuarioDTO dto);
}