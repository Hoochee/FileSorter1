package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        String targetName = "астат";
        String mainPath = "C:\\222";
        File dir = new File("C:\\222");
        try(DirectoryStream<Path> files = Files.newDirectoryStream(Path.of(mainPath))) {
            for(Path path : files){
               // File file = new File(String.valueOf(path));

                if(Files.isRegularFile(path) && path.getFileName().toString().toLowerCase().contains(targetName)){
                    String[] dd = path.getFileName().toString().split("\\D");
                    String stringDate = dd[0];
                    System.out.println(stringDate);
                    Date date =dateParser(stringDate);
                   System.out.println(date);
                   Path newDir = Path.of("C:\\222\\sorted");
                   if(Files.exists(newDir)) {
                       Files.copy(path, newDir.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                   } else {
                       Files.createDirectory(newDir);
                       Files.copy(path,newDir.resolve(path.getFileName()),StandardCopyOption.REPLACE_EXISTING);
                   }
                }
            }
        }


    }
    public static Date dateParser(String stringDateForParsing) {
        SimpleDateFormat format = new SimpleDateFormat();
        Calendar calendar = new GregorianCalendar();
        String st =Integer.toString(calendar.get(Calendar.YEAR)).substring(2);
        if(stringDateForParsing.length()>6){
            format=new SimpleDateFormat("ddMMyyyy");
        } else {
            if(stringDateForParsing.endsWith(st)){
                format=new SimpleDateFormat("ddMMyy");
            }
            if(stringDateForParsing.startsWith(st)){
                format=new SimpleDateFormat("yyMMdd");
            }
        }
        Date parsedDate = null;
        try {
            parsedDate = format.parse(stringDateForParsing);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }
}
