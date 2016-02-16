package chat;

import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class History {

    private ArrayList<Message> history = new ArrayList<>();
    final static String LOG_FILE = "logfile.txt";
    final static String STRING_PARSE = "//-----------------------------------------------*****-----------------------------------------------\\\\\n";

    History() throws IOException {
        FileWriter writerLog = new FileWriter(LOG_FILE, true);
        writerLog.write(STRING_PARSE + "Start \"History.java\"\n");
        System.out.println("What you want to do?\n" +
                "0.Load history from the file.\n" +
                "1.Save the history.\n" +
                "2.Add a message.\n" +
                "3.Show the history.\n" +
                "4.Delete message.\n" +
                "5.Find message by author.\n" +
                "6.Find message by word.\n" +
                "7.FInd by regular expression.\n" +
                "8.Find by time period.\n" +
                "9.Exit.");
        chooser(writerLog);
    }

    public void chooser(FileWriter writerLog) throws IOException{
        String choose = "";
        Scanner in = new Scanner(System.in);
        while (!choose.equals("9")) {
            choose=in.next();
            switch (choose) {
                case "0": {
                    loadFromFile(writerLog);
                    break;
                }
                case "1": {
                    saveToFile(writerLog);
                    break;
                }
                case "2": {
                    addMessage(writerLog);
                    break;
                }
                case "3": {
                    showHistory(writerLog);
                    break;
                }
                case "4": {
                    deleteMes(writerLog);
                    break;
                }
                case "5": {
                    findAuthor(writerLog);
                    break;
                }
                case "6": {
                    findWord(writerLog);
                    break;
                }
                case "7": {
                    findRegularExpressions(writerLog);
                    break;
                }
                case "8": {
                    findByTimePeriod(writerLog);
                    break;
                }
                case "9": {
                    writerLog.write(STRING_PARSE + "End of program.\n");
                    writerLog.close();
                    System.out.println("End of program.");
                    break;
                }
                default: {
                    writerLog.write(STRING_PARSE + "Wrong input!(chooser)\n");
                    System.out.println("Wrong input! Number from 0 to 9!");
                    break;
                }
            }
        }
    }

    public void loadFromFile(FileWriter writerLog) throws IOException{
        writerLog.write(STRING_PARSE + "Load history from file.\n" );
        history.clear();
        try {
            String jData = Files.readAllLines(Paths.get("history.json")).toString();
            JsonReader jRead = Json.createReader(new StringReader(jData));
            JsonArray jArray = jRead.readArray();
            if (jArray.size() == 0) {
                System.out.println("Your history is empty.");
            }
            JsonArray arr = jArray.getJsonArray(0);
            jRead.close();
            for (int i = 0; i < arr.size(); i++) {
                JsonObject tmp = arr.getJsonObject(i);
                Date tmpTime = new Date(tmp.getJsonNumber("time").longValue());
                Message tempMes = new Message(tmp.getString("id"),
                                              tmp.getString("author"),
                                              tmp.getString("message"),
                                              tmpTime);
                history.add(tempMes);
            }
            System.out.println("Successfully done.");
            writerLog.write(STRING_PARSE + "Successfully done.\n");
        } catch (NoSuchFileException e) {
            System.out.println("No such file " + e.getMessage());
            writerLog.write(STRING_PARSE + "No such file " + e.getMessage() + "!\n");
        }
    }

    public void addMessage(FileWriter writerLog) throws IOException{
        Scanner in = new Scanner(System.in);
        writerLog.write(STRING_PARSE + "Add a message.\n");
        System.out.println("Input your name:");
        String name = in.nextLine();
        System.out.println("Input your message:");
        String mes = in.nextLine();
        Date tmpDate = new Date();
        Message temp = new Message("id-" + ((history.size() + 1)), name, mes, tmpDate);
        history.add(temp);
        System.out.println("Successfully added.");
        writerLog.write(STRING_PARSE + "Successfully added 1 message.\n");
    }

    public void saveToFile(FileWriter writerLog) throws IOException{
        writerLog.write(STRING_PARSE + "Save messages in file.\n");
        if (!history.isEmpty()) {
            FileWriter jOut = new FileWriter("history.json");
            JsonWriter jWrite = Json.createWriter(jOut);
            JsonArrayBuilder wrightArray = Json.createArrayBuilder();
            for (Message mes : history) {
                wrightArray.add(Json.createObjectBuilder()
                        .add("id", mes.getId())
                        .add("author", mes.getAuthor())
                        .add("message", mes.getMessage())
                        .add("time", mes.getTime().getTime()).build());
            }
            JsonArray arr = wrightArray.build();
            jWrite.writeArray(arr);
            jOut.close();
            jWrite.close();
            System.out.println("Successfully done.");
            writerLog.write(STRING_PARSE + "Successfully done.\n");
        }
        else {
            System.out.println("History is empty!");
            writerLog.write(STRING_PARSE + "History is empty!\n");
        }
    }

    public void deleteMes(FileWriter writerLog) throws IOException{
        Scanner in = new Scanner(System.in);
        writerLog.write(STRING_PARSE + "Delete message.\n");
        System.out.println("Input id:");
        String delId = in.next();
        boolean userWithDelId = false;
        int count = 0;
        for (int i=0;i<history.size();i++) {
            if (history.get(i).getId().equals(delId)) {
                history.remove(i);
                userWithDelId = true;
                count++;
            }
        }
        if (userWithDelId)
            System.out.println("Successfully done");
        else
            System.out.println("There is no such message!");
        writerLog.write(STRING_PARSE + "Deleted " + count + " message(s).\n");
    }

    public void findAuthor(FileWriter writerLog) throws IOException{
        Scanner in = new Scanner(System.in);
        writerLog.write(STRING_PARSE + "Find message by author.\n");
        System.out.println("Input author:");
        String auth = in.nextLine();
        boolean findAuth = false;
        int count = 0;
        for (Message mes : history) {
            if (mes.getAuthor().equals(auth)) {
                System.out.println(mes.toString());
                findAuth = true;
                count++;
            }
        }
        if (findAuth)
            System.out.println("Successfully done.");
        else
            System.out.println("No message from this author!");
        writerLog.write(STRING_PARSE + count + " message(s) found.\n");
    }

    public void findByTimePeriod(FileWriter writerLog) throws IOException{
        Scanner in = new Scanner(System.in);
        writerLog.write(STRING_PARSE + "Find message by time period.\n");
        boolean findInPeriod = false;
        int count = 0;
        try {
            Date stDat;
            Date enDat;
            System.out.println("Input start time in format: MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat start = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            stDat = start.parse(in.nextLine());
            System.out.println("Input end time in format: MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat end = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            enDat = end.parse(in.nextLine());
            for (Message mes : history) {
                if ( (mes.getTime().after(stDat)) && (mes.getTime().before(enDat)) ) {
                    System.out.println(mes.toString());
                    findInPeriod = true;
                    count++;
                }
            }
            if (findInPeriod)
                System.out.println("Successfully done.");
            else
                System.out.println("No message of this period!");
            writerLog.write(STRING_PARSE + count + " message(s) found.\n");
        }
        catch (ParseException e) {
            System.out.println("Unparseable date " + e.getMessage());
            writerLog.write(STRING_PARSE + "Unparseable date " + e.getMessage() + "!\n");
        }
    }

    public void findWord(FileWriter writerLog) throws IOException{
        Scanner in = new Scanner(System.in);
        writerLog.write(STRING_PARSE + "Find message by word.\n");
        System.out.println("Input word:");
        String word = in.next();
        boolean findWord = false;
        int count = 0;
        for (Message mes : history) {
            if (mes.getMessage().contains(word)) {
                System.out.println(mes.toString());
                findWord = true;
                count++;
            }
        }
        if (findWord)
            System.out.println("Successfully done.");
        else
            System.out.println("No message with this word!");
        writerLog.write(STRING_PARSE + count + " message(s) found.\n");
    }

    public void showHistory(FileWriter writerLog) throws IOException{
        writerLog.write(STRING_PARSE + "Show history.\n");
        if (!history.isEmpty())
            for (Message mes : history)
                System.out.println(mes.toString());
        else
            System.out.println("History is empty!");
    }

    public void findRegularExpressions(FileWriter writerLog) throws IOException{
        Scanner in = new Scanner(System.in);
        writerLog.write(STRING_PARSE + "Find message by regular expression.\n");
        boolean findReg = false;
        System.out.println("Input regular expression:");
        String regExpr = in.nextLine();
        int count = 0;
        Pattern pat = Pattern.compile(regExpr);
        for (Message mes : history) {
            Matcher matcher = pat.matcher(mes.getMessage());
            if (matcher.find()) {
                findReg = true;
                count++;
                System.out.println(mes.toString());
            }
        }
        if (!findReg)
            System.out.println("There are no messages with this regular expression!");
        writerLog.write(STRING_PARSE + count + " message(s) found.\n");
    }
}
