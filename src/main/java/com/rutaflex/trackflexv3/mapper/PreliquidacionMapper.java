package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.PreliquidacionDTO;
import com.rutaflex.trackflexv3.entity.Preliquidacion;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreliquidacionMapper extends BaseMapper<Preliquidacion, PreliquidacionDTO> {

    @Override
    @Mapping(target = "servicioId", source = "servicio.id")
    PreliquidacionDTO toDTO(Preliquidacion entity);

    @Override
    @Mapping(target = "servicio", ignore = true)
    Preliquidacion toEntity(PreliquidacionDTO dto);
}