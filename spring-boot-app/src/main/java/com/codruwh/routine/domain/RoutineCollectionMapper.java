package com.codruwh.routine.domain;

import com.codruwh.routine.domain.idClass.RoutineCollectionMapperId;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routine_collections_mapper")
@IdClass(RoutineCollectionMapperId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineCollectionMapper {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", referencedColumnName = "collection_id")
    private RoutineCollection routineCollection; // FK to routine_collections [cite: 27]

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rid", referencedColumnName = "rid")
    private Routine routine; // FK to routines [cite: 27]
}
