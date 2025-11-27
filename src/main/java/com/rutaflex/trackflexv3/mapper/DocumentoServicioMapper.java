package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.DocumentoServicioDTO;
import com.rutaflex.trackflexv3.entity.DocumentoServicio;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentoServicioMapper extends BaseMapper<DocumentoServicio, DocumentoServicioDTO> {

    @Override
    @Mapping(target = "servicioId", source = "servicio.id")
    DocumentoServicioDTO toDTO(DocumentoServicio entity);

    @Override
    @Mapping(target = "servicio", ignore = true)
    DocumentoServicio toEntity(DocumentoServicioDTO dto);
}