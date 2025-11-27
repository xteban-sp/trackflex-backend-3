package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.DocumentoEntregaDTO;
import com.rutaflex.trackflexv3.entity.DocumentoEntrega;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentoEntregaMapper extends BaseMapper<DocumentoEntrega, DocumentoEntregaDTO> {

    @Override
    @Mapping(target = "entregaId", source = "entrega.id")
    DocumentoEntregaDTO toDTO(DocumentoEntrega entity);

    @Override
    @Mapping(target = "entrega", ignore = true)
    DocumentoEntrega toEntity(DocumentoEntregaDTO dto);
}