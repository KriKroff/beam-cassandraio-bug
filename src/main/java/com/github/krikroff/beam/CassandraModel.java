package com.github.krikroff.beam;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

import static com.github.krikroff.beam.CassandraModel.TABLE_NAME;

@Data
@Table(name = TABLE_NAME)
public class CassandraModel implements Serializable {

    public static final String TABLE_NAME = "demo";
    
    @PartitionKey
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

}
