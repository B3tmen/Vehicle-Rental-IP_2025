package org.unibl.etf.carrentalbackend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        setupBooleanToByteTypeConverter(modelMapper);

        return modelMapper;
    }

    private void setupBooleanToByteTypeConverter(ModelMapper modelMapper) {
        // Add custom converter for Boolean <-> Byte
        modelMapper.createTypeMap(Boolean.class, Byte.class)
                .setConverter(ctx -> ctx.getSource() ? (byte) 1 : (byte) 0);

        modelMapper.createTypeMap(Byte.class, Boolean.class)
                .setConverter(ctx -> ctx.getSource() == 1);
    }
}
