package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.VehiculoDTO;
import com.rutaflex.trackflexv3.entity.Vehiculo;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = NivelMapper.class)
public interface VehiculoMapper extends BaseMapper<Vehiculo, VehiculoDTO> {

    @Override
    @Mapping(target = "nivel", source = "nivel") // Usa NivelMapper
    VehiculoDTO toDTO(Vehiculo entity);

    @Override
    @Mapping(target = "nivel", ignore = true)
    Vehiculo toEntity(VehiculoDTO dto);
}