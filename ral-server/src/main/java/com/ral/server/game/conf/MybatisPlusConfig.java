package com.ral.server.game.conf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.ral.server.game.db.*.dao"})
public class MybatisPlusConfig {
}
