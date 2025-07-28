package com.codruwh.routine.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "titles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // int 타입의 PK이므로 auto_increment 설정
    @Column(name = "title_id", nullable = false)
    private Integer titleId; // PK: int 타입

    @Column(name = "value", length = 255, nullable = false) // 칭호 이름, varchar
    private String value;
}
