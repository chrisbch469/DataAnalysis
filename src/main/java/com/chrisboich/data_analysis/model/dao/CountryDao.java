package com.chrisboich.data_analysis.model.dao;


import com.chrisboich.data_analysis.model.model.Country;

import java.util.List;

public interface CountryDao {

    Country findCountryById(String code);
    List<Country> findCountryByName(String name);
    List<Country> findAllCountries();
    void updateCountry(Country country);
    String saveCountry(Country country);
    void deleteCountry(Country country);
    Double getMinimumLiteracyRate();
    Double getMaximumLiteracyRate();
    Double getMinimumInternetUsers();
    Double getMaximumInternetUsers();
    Double getCorrelationCoefficient();






}
