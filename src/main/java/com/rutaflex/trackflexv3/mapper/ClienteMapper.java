package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.ClienteDTO;
import com.rutaflex.trackflexv3.entity.Cliente;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper extends BaseMapper<Cliente, ClienteDTO> {

    @Override
    @Mapping(target = "almacenesIds", ignore = true) // Resolver en servicio si se necesita
    ClienteDTO toDTO(Cliente entity);

    @Override
    Cliente toEntity(ClienteDTO dto);
}