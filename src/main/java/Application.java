import com.chrisboich.data_analysis.model.controller.DatabasePrompter;
import com.chrisboich.data_analysis.model.dao.CountryDao;
import com.chrisboich.data_analysis.model.dao.CountryDaoImplementation;

import java.io.IOException;
import java.util.Scanner;

public class Application {

    //dao object with hibernate configuaration file
    private static CountryDao countryDao = new CountryDaoImplementation("hibernate.cfg.xml");

                                //TODO: REMOVE SHOW QUERY IN CONFIG FILE
    /*Main class Application will be used to implement sessionfactory and utilize methods
    to execute CRUD on the worldbank database
     */
    public static void main(String[] args){

            //Display Greeting
            System.out.println("============ Welcome To Data Analysis ============");
            //new scanner
            Scanner scanner = new Scanner(System.in);
            //database prompter menu with dao object
            DatabasePrompter prompter = new DatabasePrompter(countryDao);
            //initiate prompter with scanner object
            prompter.promptForChoice(scanner);

    }
}//end main

