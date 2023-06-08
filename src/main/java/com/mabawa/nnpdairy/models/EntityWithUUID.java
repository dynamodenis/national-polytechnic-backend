package com.mabawa.nnpdairy.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public class EntityWithUUID {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    protected UUID id = UUID.randomUUID();
}
