package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.SoatDTO;
import com.rutaflex.trackflexv3.entity.Soat;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SoatMapper extends BaseMapper<Soat, SoatDTO> {

    @Override
    @Mapping(target = "empresaSoatId", source = "empresaSoat.id")
    @Mapping(target = "vehiculoId", source = "vehiculo.id")
    @Mapping(target = "tipoSoatId", source = "tipoSoat.id")
    SoatDTO toDTO(Soat entity);

    @Override
    @Mapping(target = "empresaSoat", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "tipoSoat", ignore = true)
    Soat toEntity(SoatDTO dto);
}