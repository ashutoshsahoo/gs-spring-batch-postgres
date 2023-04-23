package com.ashu.practice.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class City {

    private Long id;
    private String name;
    private String countrycode;
    private String district;
    private Long population;
}
