package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.IncidenciaDTO;
import com.rutaflex.trackflexv3.entity.Incidencia;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidenciaMapper extends BaseMapper<Incidencia, IncidenciaDTO> {

    @Override
    @Mapping(target = "entregaId", source = "entrega.id")
    @Mapping(target = "tipoIncidenciaId", source = "tipoIncidencia.id")
    @Mapping(target = "estadoId", source = "estado.id")
    IncidenciaDTO toDTO(Incidencia entity);

    @Override
    @Mapping(target = "entrega", ignore = true)
    @Mapping(target = "tipoIncidencia", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Incidencia toEntity(IncidenciaDTO dto);
}