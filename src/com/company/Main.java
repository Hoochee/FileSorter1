package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
	// write your code here


        fileHandler(getPreparedFilesNames());

    }
    public static void fileHandler(List<String> fileNamesForCopy) throws IOException, ParseException {
        for (String fileName : fileNamesForCopy){

            String[] temp = fileName.split("\\\\");
            int indexOfLastElement = temp.length-1;
            String[] arrayForDateParsing = temp[indexOfLastElement].split("\\D");
            Date date = dateParser(arrayForDateParsing[0]);
            if(date.getTime()>parsedDateList().get(0).getTime() && date.getTime()<parsedDateList().get(1).getTime()){
                Path newDir = getLocationForCopy();
                if(!Files.exists(newDir)){
                    Files.createDirectory(newDir);
                }
                Path newDir1;
                for(String string : getSearchList()){
                    if(fileName.toLowerCase().contains(string)){
                        newDir1=Path.of(getLocationForCopy()+"\\\\"+string);
                        if (!Files.exists(newDir1)) {
                            Files.createDirectory(newDir1);
                        }
                        Files.copy(Path.of(fileName),newDir1.resolve(Path.of(temp[indexOfLastElement]+" -" + temp[3]+".pdf")),StandardCopyOption.REPLACE_EXISTING);// сделать приписку с филиалом
                    }
                }
            }
        }
    }
    /*Подготавливаем список файлов для обработки */
    public static List<String> getPreparedFilesNames() throws IOException {
        List<String> preparedNameList = new ArrayList<>();
        for(String fileName : allFiles()){
            for(String searchFileName : getSearchList() ){
                if(fileName.toLowerCase().contains(searchFileName.toLowerCase())){
                    preparedNameList.add(fileName);
                }
            }
        }
        return preparedNameList;
    }
    /* Получаем список всех файлов  */
    public static List<String> allFiles() throws IOException {
        List<String> allFiles;
        try (Stream<Path> walk = Files.walk(getFilesLocationPath())) {
            allFiles = walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());
        }
        return allFiles;
    }
    /* Метод считывает список из двух элементов. Первый элемент - дата с, второй элемент - дата до*/
    public static List<Date> parsedDateList() throws IOException, ParseException {
        Path dateConfigPath = Path.of("D:\\Projects\\dateConfig.txt");
        List<String> parsedList = new ArrayList<>();
        parsedList=Files.readAllLines(dateConfigPath);
        List<Date> parsedDateList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        parsedDateList.add(format.parse(parsedList.get(0)));
        parsedDateList.add(format.parse(parsedList.get(1)));
        return parsedDateList;
    }
                                                                    /*метод парсит список для поиска из файла*/
    public static List<String> getSearchList() throws IOException {
        Path searchListpath = Path.of("D:\\Projects\\SearchingList.txt");
        return Files.readAllLines(searchListpath);
    }
                                                                /* метод считывает папку поиска из файла*/
    public static Path getFilesLocationPath() throws IOException {
        Path filesLocationPath = Path.of("D:\\Projects\\filesLocation.txt");
        return Path.of(Files.readString(filesLocationPath));
    }
                                                            /*метод считывает папку куда копировать отсортированные файлы*/
    public static Path getLocationForCopy() throws IOException {
        Path locationForCopy = Path.of("D:\\Projects\\copydirectory.txt");
        return Path.of(Files.readString(locationForCopy));
    }
    /*Парсер даты из строки*/
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
