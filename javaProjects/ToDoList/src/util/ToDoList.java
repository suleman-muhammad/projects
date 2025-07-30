package util;

import java.util.*;

public class ToDoList {
    FileHandling dataHandler = new FileHandling();
    public ArrayList<Event> events;
    public  Scanner sc = new Scanner(System.in);
    public boolean startCheck = false;


    public void mainMenu(){



        if(!dataHandler.initiate()){
            System.out.println("unexpected Behaviour.");
            return;
        }

        if(!startCheck){
            startCheck = true;
            events = dataHandler.getData();
        }

        String[] options = {"View Events","Add New Event","Exit"};
        int choice = chooseFrom(options);

        switch(choice){
            case 1 :
                viewEvent();
                break;
            case 2:
                addEvent();
                break;
            case 3:
                exit();
        }







    }



    public void viewEvent(){


        System.out.println("\nHere is the List of the Events: ");
        System.out.println();
        System.out.println("\tIndex\tDate\tStatus : \t\tTask\n");
        System.out.println("_____________________________________________________________________________________");
        System.out.println();
        if(events.isEmpty()){
            System.out.println("No Event to Show.");
        }else{
            for(Event e : events){
                System.out.println(e);
            }
        }

        System.out.println();
        System.out.println("_____________________________________________________________________________________");
        System.out.println();


        if(events.isEmpty()){
            goBack();
            mainMenu();
        }
        else{
            String[] options = {"Edit Single Event.","Select Multiple.",
                    "Filtering options","Go Back."};
            int choice = chooseFrom(options);

            switch(choice){
                case 1:
                    indexingMenu();
                    break;
                case 2:
                    editMultipleEvents();
                    break;
                case 3:
                    filteringMenu();
                    break;
                case 4:
                    mainMenu();
                    break;
            }
        }

    }

    public void indexingMenu(){
        System.out.println();
        System.out.println("Enter the index of the Event or 0 to go Back: ");
        int choice = -1;
        do{
            String response = sc.nextLine();

            try{
                choice = Integer.parseInt(response);


            }catch (Exception e){
                notANumberWarning();
                continue;
            }

            if(choice > events.size() || choice < 0){
                invalidNumberWarning();
            }
        }while(choice > events.size() || choice < 0);

        switch (choice){
            case 0:
                viewEvent();
                break;
            default:
                editSingleEvent(choice);
        }
    }


    public void viewEvent(boolean status){

        System.out.println("\nHere is the List of the Events Filtered By Status: ");
        System.out.println();
        System.out.println("\tIndex\tDate\tStatus : \t\tTask\n");
        System.out.println("_____________________________________________________________________________________");
        System.out.println();
        if(events.isEmpty()){
            System.out.println("No Event to Show.");
        }else{
            boolean check = false;
            for(Event e : events){
                if(e.isStatus() == status){
                    check = true;
                    System.out.println(e);
                }
            }

            if(!check){
                System.out.println("No Event to Show.");
            }
        }

        System.out.println();
        System.out.println("_____________________________________________________________________________________");

        goBack();

        filterByStatus();
    }



    public void addEvent(){
        System.out.println();
        System.out.println("_____________________________________________________________________________________");
        System.out.println();


        Date date = getDate();
        String task = getTask();

        int index = events.size() + 1;

        Event newEvent = new Event(index,task,date,false);

        events.add(newEvent);

        System.out.println("Event Added Successfully.");

        String[] options = {"Add new Event","GO to Main menu"};

        int choice = chooseFrom(options);


        switch(choice) {
            case 1:
                addEvent();
                break;
            case 2:
                mainMenu();
                break;
        }

    }

    public void editMultipleEvents(){
        System.out.println();
        System.out.println("Enter the indices of the Events you wanna edit.         (Must be separated by spaces.) ");

        String response = sc.nextLine();

        String[] elements = response.split(" ");

        ArrayList<Integer> indicesToEdit = new ArrayList<>();
        ArrayList<String> invalidIndices = new ArrayList<>();
        for( String elem : elements){
            try{
                int index = Integer.parseInt(elem);
                indicesToEdit.add(index);
            }catch (Exception e){
                invalidIndices.add(elem);
            }
        }

        String[] options = {"Delete","Mark Complete","Mark Pending"};

        int choice = chooseFrom(options);

        ArrayList<Integer> invalidIndices2 = new ArrayList<>();

        invalidIndices2 = switch (choice) {
            case 1 -> deleteMultiple(indicesToEdit);
            case 2 -> markComplete(indicesToEdit);
            case 3 -> markPending(indicesToEdit);
            default -> invalidIndices2;
        };

        int counter = invalidIndices2.removeLast();
        System.out.println();
        System.out.println("_____________________________________________________________");
        System.out.println();
        System.out.println("Result is Given By: ");
        System.out.println();

        switch(choice){
            case 1:
                System.out.println("Deleted " + counter + " Events.");
                break;
            case 2:
                System.out.println("Completed " + counter + " Events.");
                break;
            case 3:
                System.out.println("Marked " + counter + " Events Pending.");
                break;
        }

        if(!(invalidIndices.isEmpty() && invalidIndices2.isEmpty())){
            System.out.println("These are Invalid Indices: ");
            for(String elem : invalidIndices){
                System.out.print(elem + " ");
            }
            for(int index : invalidIndices2){
                System.out.print(index + " ");
            }
        }
        System.out.println();
        System.out.println("_____________________________________________________________");
        System.out.println();

        goBack();

        viewEvent();


    }

    public ArrayList<Integer> deleteMultiple(ArrayList<Integer> indices){
        int counter = 0;
        ArrayList<Event> events2 = new ArrayList<>(events);
        for(Event e : events){
            int index = e.getIndex();
            if(indices.contains(index)){
                events2.remove(e);
                indices.remove(Integer.valueOf(index));
                counter ++;
            }
        }
        events.clear();
        int ind = 1;
        for(Event e : events2){
            e.setIndex(ind++);
            events.add(e);
        }


        indices.addLast(counter);
        return indices;
    }

    public ArrayList<Integer> markComplete(ArrayList<Integer> indices){
        int counter = 0;
        for(Event e : events){
            int index = e.getIndex();
            if(indices.contains(index)){
                e.setStatus(true);
                indices.remove(Integer.valueOf(index));
                counter ++;
            }
        }

        indices.addLast(counter);
        return indices;
    }

    public ArrayList<Integer> markPending(ArrayList<Integer> indices){
        int counter = 0;

        for(Event e : events){
            int index = e.getIndex();
            if(indices.contains(index)){
                e.setStatus(false);
                indices.remove(Integer.valueOf(index));
                counter ++;
            }
        }

        indices.addLast(counter);
        return indices;
    }


    public void editSingleEvent(int index){

        String[] options = {"Mark Complete","Mark Pending","Edit Event","Delete","Go Back"};
        int choice = chooseFrom(options);

        switch(choice){
            case 1:
                markComplete(new ArrayList<>(List.of(index)));
                System.out.println("Completed One Event.");
                break;
            case 2:
                markPending(new ArrayList<>(List.of(index)));
                System.out.println("Marked One event Pending");
            case 3:
                editEvent(index);
                System.out.println("Updated One Event");
                break;
            case 4:
                deleteMultiple(new ArrayList<>(List.of(index)));
                System.out.println("Deleted one Event.");
                break;
            case 5:
                viewEvent();
                return;
        }

        goBack();

        viewEvent();
    }

    public void editEvent(int index){
        Event eventToEdit = null;
        for(Event e : events){
            if(e.getIndex() == index){
                eventToEdit = e;
                break;
            }
        }

        if(eventToEdit == null){
            System.out.println();
            System.out.println("No Event Found with such index.");
            System.out.println();
            return;
        }


        System.out.println("\nCurrent Event: \n");
        System.out.println(eventToEdit);

        String[] options = {"Update Date","Update Task"};

        int choice = chooseFrom(options);

        switch(choice){
            case 1:
                Date date = getDate();
                eventToEdit.setDate(date);
                return;
            case 2:
                String task = getTask();
                eventToEdit.setTask(task);
                return;
        }


    }


    public void filteringMenu(){
        System.out.println();
        System.out.println("_________________________________________");
        System.out.println();

        String[] options = {"Filter by Status.","Filter by Date","Go Back"};
        int choice = chooseFrom(options);

        switch (choice){
            case 1:
                filterByStatus();
                break;
            case 2:
                filterByDate();
                break;
            case 3:
                viewEvent();
                break;
        }
    }






    public void exit(){
        System.out.println();
        System.out.println("....................................");
        System.out.println();
        System.out.println("Thanks for Using To Do List.");
        System.out.println(" Have a Nice day. see you. ");
        System.out.println();
        System.out.println("....................................");

        dataHandler.clearData();
        dataHandler.storeInData(events);
    }


    public void filterByStatus(){
        String[] options = {"Completed","Pending","GO Back"};
        int choice = chooseFrom(options);

        switch(choice){
            case 1:
                viewEvent(true);
                break;
            case 2:
                viewEvent(false);
                break;
            case 3:
                filteringMenu();
                break;
        }


    }
    public void filterByDate(){
        Date date = getDate();

        System.out.println("\nHere is the List of the Events Filtered by Specific Date: ");
        System.out.println();
        System.out.println("\tIndex\tDate\tStatus : \t\tTask\n");
        System.out.println("_____________________________________________________________________________________");
        System.out.println();
        if(events.isEmpty()){
            System.out.println("No Event to Show.");
        }else{
            boolean check = false;
            for(Event e : events){
                if(e.getDate().equals(date)){
                    check = true;
                    System.out.println(e);
                }
            }

            if(!check){
                System.out.println("No Event to Show.");
            }
        }

        System.out.println();
        System.out.println("_____________________________________________________________________________________");
        System.out.println();


        goBack();

        filteringMenu();
    }



    public Date getDate(){
        String date;
        Date finalDate = null;
        do{
            System.out.println("Please Enter the Date for the task. \t(format: dd-mm-year),Kindly place no Space after date\n" );

            date = sc.nextLine();

            if(date.length() != 10){
                invalidDateWarning();
                continue;
            }

            try{
                finalDate = Date.generateDate(Integer.parseInt(date.substring(0,2)),
                        Integer.parseInt(date.substring(3,5)),
                        Integer.parseInt(date.substring(6,10)));
            }catch(Exception e){
                invalidDateWarning();
                continue;
            }

            if(finalDate == null){
                invalidDateWarning();
            }
        }while(finalDate == null);

        return finalDate;
    }

    public String getTask(){
        String task;
        do{
            System.out.println("Enter the Task: ");
            System.out.println();

            task = sc.nextLine();

            if(task.isEmpty()){
                System.out.println();
                System.out.println("............................................");
                System.out.println();
                System.out.println("Warning: Invalid Task. Try Again.");
                System.out.println();
                System.out.println("............................................");
                System.out.println();
            }
        }while(task.isEmpty());

        return task;
    }



    public void notANumberWarning(){
        System.out.println("-------------------------------------");
        System.out.println();
        System.out.println("Warning: Expected a number.");
        System.out.println("try Again.");
        System.out.println();
        System.out.println("-------------------------------------");
    }

    public void invalidNumberWarning(){
        System.out.println("-------------------------------------");
        System.out.println();
        System.out.println("Warning: Invalid Choice.");
        System.out.println("try Again.");
        System.out.println();
        System.out.println("-------------------------------------");
    }

    public void invalidDateWarning(){
        System.out.println();
        System.out.println("............................................");
        System.out.println();
        System.out.println("Warning: Invalid Date. Try Again.");
        System.out.println();
        System.out.println("............................................");
        System.out.println();
    }


    public int chooseFrom(String[] options){
        int choice = -1;
        do{
            System.out.println();
            for(int i = 0; i<options.length; i++){
                System.out.println((i+1) + "." + options[i]);
            }
            System.out.println();
            System.out.println("Choose one of above Option: ");

            String response = sc.nextLine();

            try{
                choice = Integer.parseInt(response);


            }catch (Exception e){
                notANumberWarning();
                continue;
            }

            if(choice > options.length || choice < 1){
                invalidNumberWarning();
            }
        }while(choice > options.length || choice < 1);
        return choice;
    }



    public void goBack(){
        int choice = -1;
        do{
            System.out.println();
            System.out.println("1.Go Back");
            System.out.println();

            String response = sc.nextLine();

            try{
                choice = Integer.parseInt(response);


            }catch (Exception e){

                notANumberWarning();
                continue;

            }

            if(choice  != 1){
                invalidNumberWarning();
            }
        }while(choice != 1);
    }



}



