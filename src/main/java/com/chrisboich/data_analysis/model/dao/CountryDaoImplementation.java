package com.chrisboich.data_analysis.model.dao;

import com.chrisboich.data_analysis.model.model.Country;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import java.util.List;
import java.util.OptionalDouble;

/**
 * Created by chris on 9/29/2016.
 */
public class CountryDaoImplementation implements CountryDao {

    //session factory
    private final SessionFactory mSessionFactory;
    //database configuration file
    private final String mConfigurationFile;

    //build session factory and service registry
    private SessionFactory buildSessionFactory(){
        //create a standardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    //dao constructor
    public CountryDaoImplementation(String configurationFile){
        mConfigurationFile = configurationFile;
        mSessionFactory = buildSessionFactory();
    }

    @Override
    public Country findCountryById(String code) {
        //open a session
        Session session = mSessionFactory.openSession();

        //Retrieve object or null based on code
        Country country = session.get(Country.class, code);

        //close session
        session.close();

        //return country
        return country;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Country> findCountryByName(String name) {

        //open a session
        Session session = mSessionFactory.openSession();

        //retrieve based on country name since it is simpler for user to remember
        List<Country> countries = session.createCriteria(Country.class)
                .add(Restrictions.like("name", name)).list();

        //close session
        session.close();

        //return country
        return countries;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Country> findAllCountries() {

        //open session
        Session session = mSessionFactory.openSession();

        //Create criteria object
        Criteria criteria = session.createCriteria(Country.class);

        //list of countries from database using criteria
        List<Country> countries = criteria.list();

        //close session
        session.close();

        //return countries list
        return countries;
    }

    @Override
    public void updateCountry(Country country) {
        //open a session
        Session session = mSessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //update persisted object
        session.update(country);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();
    }

    @Override
    public String saveCountry(Country country) {
        //open a session
        Session session = mSessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //use session to save contact
        String code  = (String)session.save(country);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();

        //return code
        return code;
    }

    @Override
    public void deleteCountry(Country country) {
        //open a session
        Session session = mSessionFactory.openSession();

        //begin transaction
        session.beginTransaction();

        //delete statement
        session.delete(country);

        //commit transaction
        session.getTransaction().commit();

        //close session
        session.close();
    }

    //method to use a stream to find minimum literacy rate from database
    @Override
    public Double getMinimumLiteracyRate() {
        OptionalDouble minimumLiteracyRate = findAllCountries()
                .stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .mapToDouble(Country::getAdultLiteracyRate)
                .min();
        if(minimumLiteracyRate.isPresent()){
            return minimumLiteracyRate.getAsDouble();
        }
        else{
            return null;
        }
    }

    //method to use a stream to find maximum literacy rate from database
    @Override
    public Double getMaximumLiteracyRate() {
        OptionalDouble maximumLiteracyRate = findAllCountries()
                .stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .mapToDouble(Country::getAdultLiteracyRate)
                .max();
        if(maximumLiteracyRate.isPresent()){
            return maximumLiteracyRate.getAsDouble();
        }
        else{
            return null;
        }
    }

    //method to use a stream to find minimum internet users from database
    @Override
    public Double getMinimumInternetUsers() {
        OptionalDouble minimumInternetUsers = findAllCountries()
                .stream()
                .filter(country -> country.getInternetUsers() != null)
                .mapToDouble(Country::getInternetUsers)
                .min();
        if(minimumInternetUsers.isPresent()){
            return minimumInternetUsers.getAsDouble();
        }
        else{
            return null;
        }
    }

    //method to use a stream to find maximum internet users from database
    @Override
    public Double getMaximumInternetUsers() {
        OptionalDouble maximumInternetUsers = findAllCountries()
                .stream()
                .filter(country -> country.getInternetUsers() != null)
                .mapToDouble(Country::getInternetUsers)
                .max();
        if(maximumInternetUsers.isPresent()){
            return maximumInternetUsers.getAsDouble();
        }
        else{
            return null;
        }
    }

    /*method to calculate correlation between literacy rate and internet users
    by creating arrays using streams to filter out specific data from database.
    The Pearsons Correlation will be used from the Apache Commons Math repository.*/
    @Override
    public Double getCorrelationCoefficient() {
        double[] internetUsers = findAllCountries()
                .stream()
                .filter(country -> country.getInternetUsers() != null)
                .filter(country -> country.getAdultLiteracyRate() != null)
                .mapToDouble(Country::getInternetUsers)
                .toArray();
        double[] adultLiteracyRate = findAllCountries()
                .stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .filter(country -> country.getInternetUsers() != null)
                .mapToDouble(Country::getAdultLiteracyRate)
                .toArray();
        // actual correlation calculation
        Double correlation;
        try {
            correlation = new PearsonsCorrelation()
                    .correlation(internetUsers, adultLiteracyRate);
        } catch (MathIllegalArgumentException iae) {
            System.out.println("Arrays are too small for the correlation to be calculated");

            return null;
        }

        return correlation;
    }
}
