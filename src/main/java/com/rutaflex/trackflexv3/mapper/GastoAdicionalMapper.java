package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.GastoAdicionalDTO;
import com.rutaflex.trackflexv3.entity.GastoAdicional;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GastoAdicionalMapper extends BaseMapper<GastoAdicional, GastoAdicionalDTO> {

    @Override
    @Mapping(target = "servicioId", source = "servicio.id")
    @Mapping(target = "tipoGastoId", source = "tipoGasto.id")
    GastoAdicionalDTO toDTO(GastoAdicional entity);

    @Override
    @Mapping(target = "servicio", ignore = true)
    @Mapping(target = "tipoGasto", ignore = true)
    GastoAdicional toEntity(GastoAdicionalDTO dto);
}