package be.ac.ulb.infof307.g06.models;

import be.ac.ulb.infof307.g06.utils.ConstantsUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * allows the user to choose a theme
 */
public class ThemeModel {
    ArrayList<String> themes;
    public ThemeModel() {
        themes = new ArrayList<>();
        themes.add(ConstantsUtils.defaultString);
        themes.add("dark");
        themes.add("light");
        themes.add("solarized");
        themes.add("black");
    }

    /**
     * reads the selected theme in the selectedTheme file
     * @return the name of the css theme file, or dark if any error occured
     */
    public String getTheme() {
        try {
            File myObj = new File(this.getClass().getResource("/styling/selectedTheme.txt").getPath());
            Scanner myReader = new Scanner(myObj);
            String data = myReader.nextLine();
            myReader.close();
            if (themes.contains(data)) {
                return data;
            } else {
                return ConstantsUtils.defaultString;
            }
        } catch (FileNotFoundException e) {
            return ConstantsUtils.defaultString;
        } catch (NullPointerException e) {
            return ConstantsUtils.defaultString;
        }
    }

    /**
     * changes the selectedTheme content to a new theme
     * @param theme defined
     */
    public void setTheme(String theme) {
            File resource = new File(this.getClass().getResource("/styling/selectedTheme.txt").getPath());
            try {
            PrintWriter writer = new PrintWriter(resource);
            writer.write(theme);
            writer.close();
        } catch (FileNotFoundException e) { }
    }

    /**
     * @return the available themes
     */
    public ArrayList<String> getThemes() {
        return themes;
    }
}
