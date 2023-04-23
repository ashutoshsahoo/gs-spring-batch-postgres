package com.ashu.practice.mapper;

import com.ashu.practice.domain.City;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityRowMapper implements RowMapper<City> {

    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String COUNTRYCODE_COLUMN = "countrycode";
    public static final String DISTRICT_COLUMN = "district";
    public static final String POPULATION_COLUMN = "population";


    @Override
    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getLong(ID_COLUMN));
        city.setName(rs.getString(NAME_COLUMN));
        city.setCountrycode(rs.getString(COUNTRYCODE_COLUMN));
        city.setDistrict(rs.getString(DISTRICT_COLUMN));
        city.setPopulation(rs.getLong(POPULATION_COLUMN));

        return city;
    }
}
