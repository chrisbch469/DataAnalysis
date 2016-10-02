package com.chrisboich.data_analysis.model.controller;


import com.chrisboich.data_analysis.model.dao.CountryDao;
import com.chrisboich.data_analysis.model.dao.CountryDaoImplementation;
import com.chrisboich.data_analysis.model.model.Country;
import org.apache.commons.lang.WordUtils;
import org.hibernate.HibernateException;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;



public class DatabasePrompter {

    //dao object
    private CountryDaoImplementation mCountryDaoImplementation;

    //constant for valid code string regex for code
    private final String VALID_CODE = "[a-zA-Z]{3}$";
    //constant for valid name string regex for country name
    private final String VALID_NAME = "^(?=([a-zA-Z]+)).{1,32}$";
    //constant for valid decimal string regex for internetUsage and literacyRate
    private final String VALID_DECIMAL = "^([0-9]{0,3}\\.[0-9]+|[0-9]{1,3}+|[0-9]{1,3}\\.|null)$";


    //constructor with CountryDao
    public DatabasePrompter(CountryDao countryDaoImplementation) {
        mCountryDaoImplementation = (CountryDaoImplementation) countryDaoImplementation;

    }

    //string containing main menu
    private void displayMenu() {
        String menu = ("Select A Choice From The Following Menu: \n" +
                "1) View Data For All Countries \n" +
                "2) Add A New Country \n" +
                "3) View Data For A Specific Country \n" +
                "4) Update The Data For A Country \n" +
                "5) Delete Data For A Country \n" +
                "6) View Statistics \n" +
                "7) Exit Data Analysis Application");

        System.out.println(menu);
    }

    //String containing menu for viewing statistics
    private void displayStatisticsMenu(){
        String statsMenu = ("Select A Choice From The Following Menu: \n" +
                "1) View MAXIMUM For Literacy Rate And Internet Users \n" +
                "2) View MINIMUM For Literacy Rate And Internet Users \n" +
                "3) View Correlation Coefficient For Literacy Rate And Internet Users");

        System.out.println(statsMenu);
    }

    //Do while loop to display switch statement for user's menu choice
    public void promptForChoice(Scanner scan) {
        //int variable to store user input as menu choice
       // int menuChoice = 0;
        int menuChoice;
        int statsMenuChoice;

        do {
            //country object
            Country country;
            //count to be incremented if user fails to choose valid option
            int count = 0;

            //Set scanner object to the user input
            //menuChoice = scan.nextInt();
            menuChoice = validateInput(scan);


            //check to verify that choice is valid
            while (menuChoice < 1 || menuChoice > 7) {
                System.out.println("\nError! The Choice Must Be Between 1 and 6. Please Try Again!");

                //set menu choice to user input
                //menuChoice = scan.nextInt();
                menuChoice = validateInput(scan);

                //increment count
                count = count + 1;

                //if user selects incorrectly 4 times, the system will exit
                if (count == 3) {
                    System.out.println("Sorry, Please Try Again Later!");
                    System.exit(0);
                }
            }

            switch (menuChoice) {
                case 1:
                    //display initial countries list
                    try {
                        mCountryDaoImplementation.findAllCountries().stream().forEach(System.out::println);
                    } catch (HibernateException he) {
                        System.out.println("\nError. Unable To Retrieve Records From Database.");
                    }
                    break;
                case 2:
                    //message variables
                    String internetMessage = "Enter The Number Of Internet Users Per 100 People: ";
                    String literacyMessage = "Enter The Literacy Rate Per 100 People: ";

                    //build new country object to be added
                    country = new Country.CountryBuilder()
                            .withName(promptForValidCountryName())
                            .withAdultLiteracyRate(promptForValidDecimal(literacyMessage))
                            .withInternetUsers(promptForValidDecimal(internetMessage))
                            .build();
                    country.setCode(promptForValidCode());
                    //try and save country
                    try {
                        mCountryDaoImplementation.saveCountry(country);
                    }catch(HibernateException he){
                        System.out.println("\nError. Failed To Save Country Record.");
                    }
                    break;
                case 3:
                    //view data for country by querying database
                    //find country record to update by validating user input for valid country code
                    String code = promptForValidCode();
                    country = mCountryDaoImplementation.findCountryById(code.toUpperCase().trim());
                    //check to see if country was found, if not display error, else display data for country
                    if (country == null) {
                        System.out.println(code + " Is Either Invalid, Or The Country Record Does Not Exist.");
                    } else {
                        //display data for country in formatted way
                        System.out.printf("======== Country Data ========:    %nCode: %s: %nName: %s " +
                                        "%nInternet Users Per 100: %s%nLiteracy Rate Per 100: %s%n",
                                country.getCode(),
                                country.getName(),
                                country.getInternetUsers(),
                                country.getAdultLiteracyRate()
                        );
                    }
                    break;
                case 4:
                    //find country record to update by validating user input for valid country code
                    code = promptForValidCode();
                    //find country and set it to country object
                    country = mCountryDaoImplementation.findCountryById(code.toUpperCase().trim());
                    //check to see if country is found, if so display prompts for editing
                    if (country != null) {
                        /*prompt user for input for editting, every field must be updated, may change to menu for
                        selection of fields to update*/

                        internetMessage = ("Enter The Number Of Internet Users Per 100 People: ");
                        literacyMessage = ("Enter The Literacy Rate Per 100 People: ");

                        //set country attributes to field input
                        country.setName(promptForValidCountryName());
                        country.setAdultLiteracyRate(promptForValidDecimal(literacyMessage));
                        country.setInternetUsers(promptForValidDecimal(internetMessage));

                        //update country record
                        mCountryDaoImplementation.updateCountry(country);

                        //success message
                        System.out.println("The " + country.getName() + " Has Been Updated.");
                    } else {
                        //message if record not found
                        System.out.println("The Code Entered Is Either Invalid, Or The Country Record Does Not Exist.");
                    }
                    break;
                case 5:
                    //delete country record
                    //find country record to update by validating user input for valid country code
                    code = promptForValidCode();
                    //find country and set it to country object
                    country = mCountryDaoImplementation.findCountryById(code.toUpperCase().trim());
                    //if country is found, prompt to confirm delete. If yes, execute delete
                    if (country != null) {
                        //prompt for confirmation of delete
                        System.out.println("Are You Sure You Want To Delete The Record For "
                                + country.getName() + " (Y or N): ");
                        String promptChoice = scan.next();
                        //if yes, execute delete
                        if (promptChoice.equals("Y") || promptChoice.equals("y")) {
                            mCountryDaoImplementation.deleteCountry(country);
                            System.out.println(country.getName() + " Has Been Deleted From The Database.");
                        } else {
                            //display clarification of record not being deleted
                            System.out.println("Record Has Not Been Deleted.");
                        }
                    } else {
                        //display error for record not existing
                        System.out.println("The Code Entered Is Either Invalid, Or The Country Record Does Not Exist.");
                    }
                    break;
                case 6:
                    //display statistics menu
                    statsMenuChoice = validateStatisticMenuInput(scan);

                    switch(statsMenuChoice){
                        case 1:
                            //display maximum for literacy rate and internet users
                            System.out.println("======== Maximum Statistics ========");
                            System.out.println("Maximum Literacy Rate Per 100 People: "
                                    + mCountryDaoImplementation.getMaximumLiteracyRate());
                            System.out.println("Maximum Internet Users Per 100 People: "
                                    + mCountryDaoImplementation.getMaximumInternetUsers() +"\n");
                            break;
                        case 2:
                            //display minimum for literacy rate and internet users
                            System.out.println("======== Minimum Statistics ========");
                            System.out.println("Minimum Literacy Rate Per 100 People: "
                                    + mCountryDaoImplementation.getMinimumLiteracyRate());
                            System.out.println("Minimum Internet Users Per 100 People: "
                                    + mCountryDaoImplementation.getMinimumInternetUsers() +"\n");
                            break;
                        case 3:
                            //display correlation coefficient
                            System.out.println("======== Correlation Coefficient ========");
                            //format for correlation coefficient
                            DecimalFormat df = new DecimalFormat("#.###");
                            System.out.println("Correlation Coefficient: "
                                    + df.format(mCountryDaoImplementation.getCorrelationCoefficient()) +"\n");
                            break;
                        default:
                            System.out.println("\nNo Valid Menu Option Was Selected.");
                            break;

                    }
                    break;
                case 7:
                    //exit program
                    System.exit(0);
                    break;
                default:
                    System.out.println("No Valid Menu Option Was Selected.");
                    break;
            }
        } while (menuChoice != 7);
    }//end promptForChoice


    //keeps prompting for valid code that meets the alphabetical, 3 character requirement
    private String promptForValidCode(){
        String countryCode;
        Scanner scan = new Scanner(System.in);

        do{
            System.out.println("Enter The 3 Letter Country Code: ");
            countryCode = scan.next().toUpperCase().trim();
            if (!countryCode.matches(VALID_CODE)){
                System.out.println("The Country Code Needs To Be 3 Letters, No Numbers.");
            }
            else {
                return countryCode;
            }
        }while(!countryCode.matches(VALID_CODE));

        return countryCode;
    }

    //keeps prompting for valid country name that meets the alphabetical, 32 character requirement
    private String promptForValidCountryName(){
        String countryName;
        Scanner scan = new Scanner(System.in);

        do{
            System.out.println("Enter The Name Of The Country: ");
            countryName = scan.next().trim();


            if (!countryName.matches(VALID_NAME)){
                System.out.println("The Country Name Needs To Be Alphabetical And Less Than 32 Letters, No Numbers.");
            }
            else {
                //capitalize first letter of each word and return country name
                return WordUtils.capitalizeFully(countryName);
            }
        }while(!countryName.matches(VALID_NAME));

        return countryName;
    }

    private Double promptForValidDecimal(String message){
        String value;
        Scanner scan = new Scanner(System.in);

        do{
            System.out.println(message);
            value = scan.nextLine();
            if (!value.matches(VALID_DECIMAL)){
                System.out.println("The Value Must Be A Number Below 100 And Can Be A Decimal Or Null.");
            }
            else {
                return Double.parseDouble(value);
            }
        }while(!value.matches(VALID_DECIMAL));

        return Double.parseDouble(value);
    }

    //scan and validate input to make sure that a number is entered
    private int validateInput(Scanner scan){
        boolean success = false;
        int input = 0;
        while(!success){
            try{
                displayMenu();
                input = scan.nextInt();
                success = true;
            }catch(InputMismatchException ime){
                scan.next();
                System.out.println("\nError. The Input Must Be A Number Between 1 and 6. Not An Alphabetical Letter.");
            }
        }
        return input;
    }

    //validation for statistics menu
    private int validateStatisticMenuInput(Scanner scan){
        boolean success = false;
        int input = 0;
        while(!success){
            try{
                displayStatisticsMenu();
                input = scan.nextInt();
                success = true;
            }catch(InputMismatchException ime){
                scan.next();
                System.out.println("\nError. The Input Must Be A Number Between 1 and 3. Not An Alphabetical Letter.");
            }
        }
        return input;
    }
}
