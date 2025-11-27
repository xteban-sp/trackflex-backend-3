package com.rutaflex.trackflexv3.mapper;

import com.rutaflex.trackflexv3.dto.ServicioDTO;
import com.rutaflex.trackflexv3.entity.Servicio;
import com.rutaflex.trackflexv3.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicioMapper extends BaseMapper<Servicio, ServicioDTO> {

    @Override
    @Mapping(target = "vehiculoId", source = "vehiculo.id")
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "estadoId", source = "estado.id")
    @Mapping(target = "rutaId", source = "ruta.id")
    ServicioDTO toDTO(Servicio entity);

    @Override
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "ruta", ignore = true)
    Servicio toEntity(ServicioDTO dto);
}