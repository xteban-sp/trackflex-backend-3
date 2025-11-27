package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.TiendaDTO;
import com.rutaflex.trackflexv3.entity.Tienda;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TiendaMapper extends BaseMapper<Tienda, TiendaDTO> {

    @Override
    @Mapping(target = "distritoId", source = "distrito.id")
    TiendaDTO toDTO(Tienda entity);

    @Override
    @Mapping(target = "distrito", ignore = true)
    Tienda toEntity(TiendaDTO dto);
}