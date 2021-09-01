import java.io.*;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;



public class ChineseRadicalLookup extends Application {
    private String oldString = "";
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        final Map<Character, List<String>> skritter = fileToDict("radical_dict.csv");
        final Map<Character, List<String>> heisig = fileToDict("nannan_heisig.txt");
        // find radicals in clipboard character
        // search radicals in heisig list
        // start loop when Win-Z is pressed
        // empty clipboard
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        Timeline repeatTask = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String newString;
                if (clipboard.hasString() && !(newString = clipboard.getString()).equals(oldString)) {
                    oldString = newString;
                    char clip = newString.charAt(0);
                    if (!skritter.containsKey(clip)) {
                        return;
                    }
                    String output = "";
                    String output2 = "";                    
                    for (String line : skritter.get(clip)) {
                        output += line + "\n";
                        output2 += heisig.get(line.charAt(2)) + "\n";
                    }
                    output += "\n" + output2;

                    Label label = new Label(output);
                    
                    StackPane root = new StackPane();
                    root.getChildren().add(label);

                    Scene scene = new Scene(root, 300, 250);

                    primaryStage.setTitle("Hello World!");
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            }
        }));
        repeatTask.setCycleCount(Timeline.INDEFINITE);
        repeatTask.play();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                t.consume();
            }
        });
    }
 public static void main(String[] args) {
        launch(args);
    }

    public static Map<Character, List<String>> fileToDict(final String file) throws IOException {
        final Map<Character, List<String>> dict = new Hashtable<>();
        BufferedReader in = null;
        try {
            File fileDir = new File(file);

            in = new BufferedReader(
               new InputStreamReader(
                          new FileInputStream(fileDir), "UTF8"));
//            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                final char key = line.charAt(0);
                if (!dict.containsKey(key)) {
                    dict.put(key, new ArrayList<String>());
                }
                dict.get(key).add(line);
            }
        } finally {
            if (in != null) {
                in.close();                            
            }
        }
        return dict;        
    }    
}
