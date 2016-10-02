package com.chrisboich.data_analysis.model.model;

import javax.persistence.*;

/*POJO that will describe the attributes and methods necessary for
the database and the country object*/
@Entity
@Table(name = "COUNTRY")
public class Country {

    //annotating code as the primary key for the database
    @Id
    @Column(name = "CODE", columnDefinition = "VARCHAR(3)")
    private String code;

    //Annotating name, internetUsers and adultLiteracyRate as columns within database
    @Column (name = "NAME", columnDefinition = "VARCHAR(32")
    private String name;

    @Column (name = "INTERNETUSERS", columnDefinition = "DECIMAL(11,8)")
    private Double internetUsers;

    @Column (name = "ADULTLITERACYRATE", columnDefinition = "DECIMAL(11,8)")
    private Double adultLiteracyRate;

    //empty constructor for Country object
    public Country(){}

    //constructor for builder methods and object
    public Country(CountryBuilder countryBuilder){
        this.code = code;
        this.name = countryBuilder.name;
        this.internetUsers = countryBuilder.internetUsers;
        this.adultLiteracyRate = countryBuilder.adultLiteracyRate;

    }


    //getters and setters for each attribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    //generated toString method for verification and testing
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Country{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", internetUsers=").append(internetUsers);
        sb.append(", adultLiteracyRate=").append(adultLiteracyRate);
        sb.append('}');
        return sb.toString();
    }

    //Country Builder for easier calls and uses with queries, updates, deletes and creates
    public static class CountryBuilder{
        private String code;
        private String name;
        private Double internetUsers;
        private Double adultLiteracyRate;

        //public CountryBuilder(String code){
        //    this.code = code;
        //}
        public CountryBuilder(){

        }

        public CountryBuilder withName(String name){
            this.name = name;
            return this;
        }

        public CountryBuilder withInternetUsers(Double internetUsers){
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(Double adultLiteracyRate){
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }
        public CountryBuilder withCode(String code){
            this.code = code;
            return this;
        }


        public Country build(){
            return new Country(this);
        }



    }//end of CountryBuilder
}//end of Country Class
