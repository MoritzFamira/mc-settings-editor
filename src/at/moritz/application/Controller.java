package at.moritz.application;

import at.moritz.GameNotInstalledException;
import at.moritz.datamodel.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    public ArrayList<String> textFileLoaded = new ArrayList<String>();
    public boolean oneEight = true;
    public String selectedProfile;

    public static double mouseSens;
    public static boolean autoJump;
    public static boolean sprint;
    public static boolean crouch;
    public static double distanceScaling;
    public static int biomeBlend;

    public ObservableList<String> items;
    @FXML
    public ListView<String> listView;
    @FXML
    public Button applyBtn;
    @FXML
    public MenuItem applyMenuItem;
    @FXML
    public TextField textFieldDistanceScaling;
    @FXML
    public Slider distanceScalingSlider;
    @FXML
    public Label labelDistanceScaling;
    @FXML
    public Slider mouseSenseSlider;
    @FXML
    public TextField textFieldSense;
    @FXML
    public Button toEightBtn;
    @FXML
    public Button toSixteenBtn;
    @FXML
    public CheckBox toggleAutoJump;
    @FXML
    public CheckBox toggleCrouch;
    @FXML
    public CheckBox toggleSprint;
    @FXML
    public Slider biomeBlendSlider;
    @FXML
    public Label labelBiomeBlend;
    @FXML
    public TextField textFieldBiomeBlend;

    @FXML
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("About this application");
        alert.setContentText("This application is a Minecraft Settings editor, that can load and save \"options.txt\"" +
                " files. \nThis application was made by Moritz Famira 2EHIF.");
        alert.setTitle("About");
        alert.show();
    }
    @FXML
    public void helpSensitivity() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Mouse sensitivity help");
        alert.setContentText("0 = 0%; 0.5 = 100%; 1 = 200%\nThe buttons on the right of the slider let you " +
                "automatically change the sensitivity number, to result in the same effective sensitivity, but in the different versions.");
        alert.setTitle("Mouse Sensitivity");
        alert.show();
    }
    @FXML
    public void helpDistanceScaling() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Entity Distance Scaling help");
        alert.setContentText("0 = 0%; 1 = 100%; 5 = 500%\nThe Entity Distance scale is multiplied by the render distance" +
                " to determine the maximum distance at which entities will be rendered.");
        alert.setTitle("Entity Distance Scaling");
        alert.show();
    }
    @FXML
    public void sensSliderMoved() {
        mouseSenseSlider.setMin(0d);
        mouseSenseSlider.setMax(1.0d);
        //System.out.println(mouseSenseSlider.getValue());
        mouseSens = mouseSenseSlider.getValue();
        textFieldSense.setText(mouseSens+"");
    }
    @FXML
    public void toSixteen() {
        mouseSens = mouseSens * 0.75;
        mouseSenseSlider.setValue(mouseSens);
        textFieldSense.setText(mouseSens+"");
        toSixteenBtn.setDisable(true);
        toEightBtn.setDisable(false);
    }
    @FXML
    public void toEight() {
        mouseSens = mouseSens / 0.75;
        if (mouseSens > 1) mouseSens = 1d;
        mouseSenseSlider.setValue(mouseSens);
        textFieldSense.setText(mouseSens+"");
        toSixteenBtn.setDisable(false);
        toEightBtn.setDisable(true);
    }
    @FXML
    public void sensInputDetected() {
            try {
                mouseSens = Double.parseDouble(textFieldSense.getText());
                if(mouseSens>1.0) {
                    mouseSens = 1d;
                } else if(mouseSens<0d) {
                    mouseSens = 0d;
                }
                mouseSenseSlider.setValue(mouseSens);
            } catch (Exception e) {
                mouseSens = 0.0;
                mouseSenseSlider.setValue(mouseSens);
            }
    }
    @FXML
    public void distanceScalingSliderMoved() {
        distanceScalingSlider.setMin(0.5);
        distanceScalingSlider.setMax(5.0);
        //System.out.println(distanceScalingSlider.getValue());
        distanceScaling = distanceScalingSlider.getValue();
        textFieldDistanceScaling.setText(distanceScaling+"");
    }
    @FXML
    public void biomeBlendSliderMoved() {
        //System.out.println("slider moved");
        biomeBlendSlider.setMin(0);
        biomeBlendSlider.setMax(7);
        biomeBlend = (int) Math.round(biomeBlendSlider.getValue());
        textFieldBiomeBlend.setText(biomeBlend+"");
        biomeBlendSlider.setValue(biomeBlend);
    }
    @FXML
    public void load() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+"/AppData/Roaming/.minecraft"));
        File selectedFile = fileChooser.showOpenDialog(MinecraftSettingsEditor.getPrimaryStage());
        try {
            //System.out.println(selectedFile.getAbsolutePath());
            //Path p = Path.of(selectedFile.getPath());
            Path p =selectedFile.toPath();
            BufferedReader in = Files.newBufferedReader(p);

            String line = in.readLine();
            String[] read;
            Setting currentSetting = null;
            while (line != null) {
                textFileLoaded.add(line);
                //System.out.println(line);
                if(!line.contains(":")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Text File not Valid");
                    alert.setContentText("Please choose the options.txt file from the directory Minecraft is installed in.");
                    alert.setHeaderText("No settings found");
                    alert.show();
                    return;
                }
                read = line.split(":");
                boolean hasValue = false;
                try {
                    String doesThisExist = read[1];
                    hasValue = true;
                } catch (Exception e) {
                    hasValue = false;
                }
                //System.out.println(read[1].contains("."));
                if(hasValue) {
                    boolean decimal = true;
                    try {
                        Double.parseDouble(read[1]);
                    } catch (Exception e) {
                        decimal = false;
                    }
                    if(decimal&&read[1].contains(".")) {
                        currentSetting = new DoubleSetting(read[0], Double.parseDouble(read[1]));
                    }else if (decimal&&!read[1].contains(".")) {
                        currentSetting = new IntSetting(read[0], Integer.parseInt(read[1]));
                    }
                    else if (read[1].equals("true")||read[1].equals("false") && !read[1].contains("[")) {
                        currentSetting = new BoolSetting(read[0], Boolean.parseBoolean(read[1]));
                        //System.out.println("should be key " + read[0] + " is key " + currentSetting.key);
                    }
                    else {
                        boolean numeric = true;
                        try {
                            Integer.parseInt(read[1]);
                        } catch (Exception e) {
                            numeric = false;
                        }
                        if(numeric) {
                            currentSetting = new IntSetting(read[0], Integer.parseInt(read[1]));
                        }
                    }
                }
                try {
                    switch (currentSetting.getKey()) {
                        case "autoJump":
                            BoolSetting autoJumpSetting = (BoolSetting) currentSetting;
                            if(autoJumpSetting.getValue()) {
                                //System.out.println("AutoJump is true\n");
                                autoJump = true;
                            } else if(!autoJumpSetting.getValue()) {
                                //System.out.println("AutoJump is false\n");
                                autoJump=false;
                            }
                            toggleAutoJump.setDisable(false);
                            toggleAutoJump.setSelected(autoJump);
                            toggleAutoJump.setText("");
                            oneEight = false;
                            break;
                        case "toggleCrouch":
                            BoolSetting toggleCrouchSetting = (BoolSetting) currentSetting;
                            if(toggleCrouchSetting.getValue()) {
                                crouch = true;
                            } else if(!toggleCrouchSetting.getValue()) {
                                crouch=false;
                            }
                            toggleCrouch.setDisable(false);
                            toggleCrouch.setSelected(crouch);
                            toggleCrouch.setText("");
                            oneEight = false;
                            break;
                        case "toggleSprint":
                            BoolSetting toggleSprintSetting = (BoolSetting) currentSetting;
                            if(toggleSprintSetting.getValue()) {
                                sprint = true;
                            } else if(!toggleSprintSetting.getValue()) {
                                sprint=false;
                            }
                            toggleSprint.setDisable(false);
                            toggleSprint.setSelected(sprint);
                            toggleSprint.setText("");
                            oneEight = false;
                            break;
                        case "biomeBlendRadius":
                            IntSetting biomeBlendSetting = (IntSetting) currentSetting;
                            //System.out.println("Biome blend: "+biomeBlendSetting.getValue());
                            labelBiomeBlend.setText("");
                            biomeBlend = biomeBlendSetting.getValue();
                            textFieldBiomeBlend.setText(biomeBlend+"");
                            textFieldBiomeBlend.setDisable(false);
                            biomeBlendSlider.setValue(biomeBlend);
                            biomeBlendSlider.setDisable(false);
                            oneEight = false;
                            break;
                        case "entityDistanceScaling":
                            DoubleSetting distanceScalingSetting = (DoubleSetting) currentSetting;
                            labelDistanceScaling.setText("");
                            distanceScaling = distanceScalingSetting.getValue();
                            textFieldDistanceScaling.setText(distanceScaling+"");
                            textFieldDistanceScaling.setDisable(false);
                            distanceScalingSlider.setValue(distanceScaling);
                            distanceScalingSlider.setDisable(false);
                            oneEight = false;
                            break;
                        case "mouseSensitivity":
                            //System.out.println(currentSetting);
                            DoubleSetting mouseSensSetting = (DoubleSetting) currentSetting;
                            mouseSens = mouseSensSetting.getValue();
                            textFieldSense.setText(mouseSens + "");
                            mouseSenseSlider.setValue(mouseSens);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                line = in.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(oneEight);
        if(oneEight) {
            toggleAutoJump.setDisable(true);
            toggleAutoJump.setText("Not Available; 1.8 options file");
            toggleCrouch.setDisable(true);
            toggleCrouch.setText("Not Available; 1.8 options file");
            toggleSprint.setDisable(true);
            toggleSprint.setText("Not Available; 1.8 options file");
            textFieldBiomeBlend.setDisable(true);
            labelBiomeBlend.setText("Not Available; 1.8 options file");
            biomeBlendSlider.setDisable(true);
            textFieldDistanceScaling.setDisable(true);
            distanceScalingSlider.setDisable(true);
            labelDistanceScaling.setText("Not Available; 1.8 options file");
        }
        oneEight=true;
        //System.out.println(textFileLoaded);

    }
    @FXML
    public void save() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showSaveDialog(MinecraftSettingsEditor.getPrimaryStage());
        String name = selectedFile.getPath();

        addProfile(name);

        try (PrintWriter out = new PrintWriter(new FileWriter(name, true))) {
                String[] read = new String[2];
                autoJump = toggleAutoJump.isSelected();
                crouch = toggleCrouch.isSelected();
                sprint = toggleSprint.isSelected();
                
            for (String line: textFileLoaded) {
                    read = line.split(":");
                    try {
                        switch (read[0]) {
                            case "autoJump":
                                line = "autoJump:"+autoJump;
                                break;
                            case "toggleCrouch":
                                line = "toggleCrouch:"+crouch;
                                break;
                            case "toggleSprint":
                                line = "toggleSprint:"+sprint;
                                break;
                            case "biomeBlendRadius":
                                line = "biomeBlendRadius:"+biomeBlend;
                                break;
                            case "entityDistanceScaling":
                                line = "entityDistanceScaling:"+distanceScaling;
                                break;
                            case "mouseSensitivity":
                                line = "mouseSensitivity:"+mouseSens;
                                break;
                        }

                    } catch (Exception e) {}
                //System.out.println(line);
                // write string
                out.println(line);
    }} catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void delete() {
        if(selectedProfile ==null) {
            return;
        } else {
            //System.out.println("selectedProfile: "+selectedProfile);

            String newProfiles ="";

            try {
                File toDelete = new File(selectedProfile);
                toDelete.delete();
                File profiles = new File("profiles.txt");
                Scanner fromProfiles = new Scanner(profiles);
                //System.out.println("Deleting profile: "+selectedProfile);

                while (fromProfiles.hasNextLine()) {

                    String data = fromProfiles.nextLine().toString();
                    //System.out.println("data: "+data.strip()+"  selectedProfile: "+selectedProfile.strip());
                    if(!data.equals(selectedProfile)) {
                        //System.out.println(":(");
                        newProfiles += data + "\n";

                }
                //System.out.println("newProfiles:\n"+newProfiles);
                fromProfiles.close();
                FileWriter changeProfiles = new FileWriter("profiles.txt");
                changeProfiles.write(newProfiles);
                changeProfiles.close();
                listView.getItems().remove(selectedProfile);
                listView.refresh();
            }} catch (Exception e) {
                //System.out.println(":(");
                e.printStackTrace();
            }

        }

        if(listView.getItems().size()<1) {
            listView.getItems().add("no Profiles...");
            listView.refresh();
        }


    }
    @FXML
    public void apply() throws GameNotInstalledException {
        /*FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        try {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft"));
        } catch (Exception e) {
            System.out.println("OS is not Windows or Minecraft not installed.");
        }
        //fileChooser.setTitle("options");
        fileChooser.setInitialFileName("options");
        fileChooser.showSaveDialog(MinecraftSettingsEditor.getPrimaryStage());*/
        if (selectedProfile.equals("no Profiles...")) {
            return;
        }
        File applyTo = new File(System.getProperty("user.home")+"/AppData/Roaming/.minecraft/options.txt");
        File applyFrom = new File(selectedProfile);
        String content = "";
        try {
            Scanner readSelectedProfile = new Scanner(applyFrom);
            while (readSelectedProfile.hasNextLine()) {
                String line = readSelectedProfile.nextLine();
                content+= line + "\n";
            }
            readSelectedProfile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileWriter apply = new FileWriter(applyTo);
            apply.write(content);
            apply.close();
        } catch (IOException e) {
            //System.out.println("OS is not Windows or Minecraft not installed.");
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("GameNotInstalledException");
            alert.setContentText("OS is not Windows or Minecraft not installed.");
            throw new GameNotInstalledException("OS is not Windows or Minecraft not installed.");

        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        items = FXCollections.observableArrayList();

        items.removeAll();
        String whenEmpty = "no Profiles...";
        items.add(whenEmpty);
        listView.getItems().addAll(items);

        //System.out.println("items: "+items);
        try {
         File profiles = new File("profiles.txt");
         Scanner fromProfiles = new Scanner(profiles);
         while (fromProfiles.hasNextLine()) {

             String data = fromProfiles.nextLine();
             //System.out.println("Profile: "+data+" "+items.size());
             listView.getItems().add(data);
             items.add(data);
             listView.refresh();
             listView.getItems().remove("no Profiles...");
         }
         //System.out.println("items: "+items);
         fromProfiles.close();
         } catch (Exception e) {
         //System.out.println(":(");
         e.printStackTrace();
         }
        //set listener for selection
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String oldVal, String newVal) -> {

                    if (newVal != null) {
                        selectedProfile = newVal;

                        applyBtn.setDisable(false);
                        applyMenuItem.setDisable(false);
                        //System.out.println(selectedProfile);
                    } else {
                        //System.out.println(":(");
                        applyBtn.setDisable(false);
                        applyMenuItem.setDisable(false);
                    }
                    //System.out.println("selectedProfile: "+selectedProfile);
                });
    }
    public void addProfile(String path) {
        String oldProfileList = "";
        try {
            File profiles = new File("profiles.txt");
            Scanner fromProfiles = new Scanner(profiles);
            while (fromProfiles.hasNextLine()) {

                String data = fromProfiles.nextLine();
                oldProfileList += data+"\n";
            }
            fromProfiles.close();
        } catch (Exception e) {
            System.out.println(":(");
            e.printStackTrace();
        }
        oldProfileList += path+"\n";
        try {
            FileWriter addProfileToFile = new FileWriter("profiles.txt");
            addProfileToFile.write(oldProfileList);
            addProfileToFile.close();
            //System.out.println("Successfully wrote to \"profiles.txt\".");
        } catch (IOException e) {
            System.out.println(":(");
            e.printStackTrace();
        }
        try {
            File profiles = new File("profiles.txt");
            Scanner fromProfiles = new Scanner(profiles);
            listView.getItems().removeAll(listView.getItems());
            //items.removeAll(items);
            while (fromProfiles.hasNextLine()) {

                String data = fromProfiles.nextLine();
                /*if(listView.getItems().contains(data)) {
                    System.out.println("Profile "+data+" already in listView.");
                }*/
                //System.out.println("Profile: "+data+" "+items.size());
                listView.getItems().add(data);
                items.add(data);
                listView.refresh();
                listView.getItems().remove("no Profiles...");
            }
            //System.out.println("items: "+items);
            fromProfiles.close();
        } catch (Exception e) {
            System.out.println(":(");
            e.printStackTrace();
        }
    }

}

