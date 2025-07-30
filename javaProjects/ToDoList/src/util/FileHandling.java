package util;
import java.io.*;
import java.util.ArrayList;

public class FileHandling {
    public boolean initiate(){
        try(FileReader fr = new FileReader(".\\src\\data\\events.txt")){
            return true;
        }catch (FileNotFoundException e){
            try(FileWriter fw = new FileWriter(".\\src\\data\\events.txt")){
                fw.write("");
                return true;
            }catch (IOException _){
                System.out.println("unexpected Behaviour. Some directories are missing.");
                return false;
            }
        }catch (IOException e){
            System.out.println("something went Wrong.");
            return false;
        }
    }


    public boolean storeInData(ArrayList<Event> events){

        if(events == null){
            return false;
        }



        try(FileWriter fw = new FileWriter(".\\src\\data\\events.txt")){

            for(Event e: events){

                int index = e.getIndex();
                Date date = e.getDate();
                boolean status = e.isStatus();
                String task = e.getTask();



                StringBuilder sb = new StringBuilder();
                sb.append(index).append(",").append(date).append(",");
                if(status){
                    sb.append("Completed,").append(task).append("\n");
                }else{
                    sb.append("Pending,").append(task).append("\n");
                }

                fw.append(sb.toString());
            }

        }catch (IOException _){
            return false;
        }
        return true;
    }

    public ArrayList<Event> getData(){
        ArrayList<Event> result = new ArrayList<>();

        try(BufferedReader fb = new BufferedReader(new FileReader(".\\src\\data\\events.txt"))){
            String line = null;
            while((line = fb.readLine()) != null){
                String[] data = line.split(",");
                int index = Integer.parseInt(data[0]);
                Date date = getDate(data[1]);
                boolean status = switch (data[2]){
                    case "Completed" -> true;
                    case "Pending" -> false;
                    default -> throw new IllegalStateException("Unexpected value: " + data[2]);
                };

                Event e = new Event(index,data[3],date,status);

                result.add(e);
            }


        } catch (IOException e){
            return null;
        }
        return result;
    }

    public Date getDate(String date){
        if(date.length() == 9){
            return  Date.generateDate(Integer.parseInt(date.substring(0,2)),
                    Integer.parseInt(date.substring(3,4)),
                    Integer.parseInt(date.substring(5,9)));
        }
        return  Date.generateDate(Integer.parseInt(date.substring(0,2)),
                Integer.parseInt(date.substring(3,5)),
                Integer.parseInt(date.substring(6,10)));

    }
    public boolean clearData(){
        try(FileWriter fw = new FileWriter(".\\src\\data\\events.txt")){
            fw.write("");

        }catch (IOException _){
            return false;
        }
        return true;
    }




}

