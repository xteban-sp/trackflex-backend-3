package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.PsExDTO;
import com.rutaflex.trackflexv3.entity.PsEx;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PsExMapper extends BaseMapper<PsEx, PsExDTO> {

    @Override
    @Mapping(target = "almacenId", source = "almacen.id")
    PsExDTO toDTO(PsEx entity);

    @Override
    @Mapping(target = "almacen", ignore = true)
    PsEx toEntity(PsExDTO dto);
}