package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.TarifarioDTO;
import com.rutaflex.trackflexv3.entity.Tarifario;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {NivelMapper.class, RutaMapper.class})
public interface TarifarioMapper extends BaseMapper<Tarifario, TarifarioDTO> {

    @Override
    TarifarioDTO toDTO(Tarifario entity);

    @Override
    @Mapping(target = "nivel", ignore = true)
    @Mapping(target = "ruta", ignore = true)
    Tarifario toEntity(TarifarioDTO dto);
}