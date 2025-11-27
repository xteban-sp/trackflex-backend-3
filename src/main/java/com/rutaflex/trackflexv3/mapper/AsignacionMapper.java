package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.AsignacionDTO;
import com.rutaflex.trackflexv3.entity.Asignacion;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsignacionMapper extends BaseMapper<Asignacion, AsignacionDTO> {

    @Override
    @Mapping(target = "servicioId", source = "servicio.id")
    @Mapping(target = "conductorId", source = "conductor.id")
    @Mapping(target = "personal1Id", source = "personal1.id")
    @Mapping(target = "personal2Id", source = "personal2.id")
    AsignacionDTO toDTO(Asignacion entity);

    @Override
    @Mapping(target = "servicio", ignore = true)
    @Mapping(target = "conductor", ignore = true)
    @Mapping(target = "personal1", ignore = true)
    @Mapping(target = "personal2", ignore = true)
    Asignacion toEntity(AsignacionDTO dto);
}